package edu.tugraz.sw14.xp04;

import java.util.ArrayList;

import edu.tugraz.sw14.xp04.adapters.ChatOverviewAdapter;
import edu.tugraz.sw14.xp04.adapters.MsgAdapter;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.UIHelper;
import edu.tugraz.sw14.xp04.msg.Msg;
import edu.tugraz.sw14.xp04.server.SendMessageTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.SendMessageTask.SendMessageTaskListener;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.os.Build;

public class ActivityMsg extends Activity {

	public static final String EXTRA_EMAIL = "extra_email";
	public static final int DEFAULT_LIMIT = 50;

	private Context context;

	private String sender;

	private EditText etMsg;

	private ListView listView;
	private ArrayList<Msg> list;
	private MsgAdapter adapter;

	private ImageButton btnSend;

	private ProgressDialog dialog;

	private boolean msgs_loaded = false;

	public boolean msgsLoaded() {
		return this.msgs_loaded;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);

		this.context = this;

		Intent intent = getIntent();
		if (intent == null) {
			exitOnError();
			return;
		}
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			exitOnError();
			return;
		}
		sender = bundle.getString(EXTRA_EMAIL);
		if (sender == null) {
			exitOnError();
			return;
		}

		Database db = new Database(this);
		String name = db.getContactName(sender);
		name = name != null ? name : sender;

		UIHelper.setActionBarIco(this, R.drawable.ico_w_person);
		UIHelper.setActionBarTitle(this, name);

		this.etMsg = (EditText) findViewById(R.id.msg_et_msg);
		this.btnSend = (ImageButton) findViewById(R.id.msg_btn_send);
		if (this.btnSend != null) {
			this.btnSend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (etMsg == null) {
						MToast.error(context, true);
						return;
					}
					Editable eaMsg = etMsg.getText();
					if (eaMsg == null) {
						MToast.error(context, true);
						return;
					}
					String msg = eaMsg.toString();
					if (msg == null) {
						MToast.error(context, true);
						return;
					}
					if (msg.isEmpty()) {
						MToast.error(context, true);
						return;
					}
					sendMsg(sender, msg);
				}
			});
		}

		this.list = new ArrayList<Msg>();
		this.listView = (ListView) findViewById(R.id.msg_list);
		this.adapter = new MsgAdapter(this, R.layout.item_msg, list);
		this.listView.setAdapter(this.adapter);
		loadMsgs(0);

	}

	private SendMessageTaskListener sendMessageTaskListener = new SendMessageTaskListener() {

		@Override
		public void onPreExecute() {

			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.show();
			dialog.setContentView(new ProgressBar(context));
		}

		@Override
		public void onPostExecute(SendMessageResponse response) {

			if (dialog != null)
				dialog.dismiss();
			if (response == null)
				MToast.error(context, true);
			else {
				if (response.isError())
					MToast.errorSendMessage(context, true);
				else {
					if (etMsg != null) {
						etMsg.setText("");
					}
					String id = response.getId();
					String content = response.getContent();
					long timestamp = response.getTimestamp();
					Msg responseMsg = new Msg(id, content, timestamp, true, true);
					if (responseMsg != null) {
						Database db = new Database(context);
						db.insertMsg(responseMsg.toContentValues());
						addMsg(responseMsg);
					} else
						MToast.error(context, true);
				}
			}
		}
	};

	private void sendMsg(String id, String msg) {

		MApp app = MApp.getApp(context);
		ServerConnection connection = app.getServerConnection();

		SendMessageRequest request = new SendMessageRequest();
		request.setReceiverId(id);
		request.setMessage(msg);

		SendMessageTask sendMessageTask = new SendMessageTask(connection,
				sendMessageTaskListener);
		sendMessageTask.execute(request);
	}

	private void loadMsgs(int limit) {

		this.list.clear();
		Database db = new Database(this);
		limit = limit > 0 ? limit : DEFAULT_LIMIT;
		this.list.addAll(db.getMsgsBySender(sender, limit));

		adapter.notifyDataSetChanged();
		msgs_loaded = true;
		if (limit <= 0) {
			scrollMyListViewToBottom();
		}
	}
	
	private void addMsg(Msg msg){
		this.list.add(msg);
		adapter.notifyDataSetChanged();
		scrollMyListViewToBottom();
	}

	private void scrollMyListViewToBottom() {
		listView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				listView.setSelection(adapter.getCount() - 1);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_msg, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		loadMsgs(0);		
	}

	private void exitOnError() {
		MToast.error(this, true);
		MApp.finishActivity(this);
	}

}
