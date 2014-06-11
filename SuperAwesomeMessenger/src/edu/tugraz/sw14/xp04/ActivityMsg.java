package edu.tugraz.sw14.xp04;

import java.util.ArrayList;

import edu.tugraz.sw14.xp04.adapters.ChatOverviewAdapter;
import edu.tugraz.sw14.xp04.adapters.MsgAdapter;
import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.Encryption;
import edu.tugraz.sw14.xp04.helpers.EncryptionDES;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.ShPref;
import edu.tugraz.sw14.xp04.helpers.UIHelper;
import edu.tugraz.sw14.xp04.helpers.UserInfo;
import edu.tugraz.sw14.xp04.msg.Msg;
import edu.tugraz.sw14.xp04.server.AddContactTask;
import edu.tugraz.sw14.xp04.server.LoginTask;
import edu.tugraz.sw14.xp04.server.SendMessageTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.AddContactTask.AddContactTaskListener;
import edu.tugraz.sw14.xp04.server.LoginTask.LoginTaskListener;
import edu.tugraz.sw14.xp04.server.SendMessageTask.SendMessageTaskListener;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;
import edu.tugraz.sw14.xp04.stubs.ContactStub;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.os.Build;

public class ActivityMsg extends Activity {

	public static final String EXTRA_EMAIL = "extra_email";
	public static final int DEFAULT_LIMIT = 250;

	public static final String KEY_CURRENT_SENDER = "key_current_sender";

	private Context context;

	private String sender;

	private EditText etMsg;

	private ListView listView;
	private ArrayList<Msg> list;
	private MsgAdapter adapter;

	private ImageButton btnSend;

	private ProgressDialog dialog;
	private ProgressDialog dialogAdd;

	private Menu menu;

	private boolean contactAdded = false;

	Encryption encryptor;

	private boolean msgs_loaded = false;

	public boolean msgsLoaded() {
		return this.msgs_loaded;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);

		this.context = this;

		encryptor = new EncryptionDES();

		init();
	}

	private void init() {
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
		db.setAsRead(sender);
		NotificationManager nmgr = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nmgr.cancel(db.getContactId(sender));
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

	@Override
	public void onBackPressed() {
		MApp.finishActivity(this);
	}

	private final SendMessageTaskListener sendMessageTaskListener = new SendMessageTaskListener() {

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
					String id = encryptor.decrypt(response.getId());
					String content = encryptor.decrypt(response.getContent());
					long timestamp = response.getTimestamp();
					Msg responseMsg = new Msg(id, content, timestamp, true,
							true);
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

		request.setReceiverId(encryptor.encrypt(id));
		request.setMessage(encryptor.encrypt(msg));

		SendMessageTask sendMessageTask = new SendMessageTask(connection,
				sendMessageTaskListener);
		sendMessageTask.execute(request);
	}

	private void loadMsgs(int limit) {

		this.list.clear();
		Database db = new Database(this);
		int limit_tmp = limit > 0 ? limit : DEFAULT_LIMIT;
		this.list.addAll(db.getMsgsBySender(sender, limit_tmp));

		adapter.notifyDataSetChanged();
		msgs_loaded = true;
		if (limit <= 0) {
			scrollMyListViewToBottom();
		}
	}

	private void addMsg(Msg msg) {
		this.list.add(msg);
		adapter.notifyDataSetChanged();
		scrollMyListViewToBottom();
	}

	private void scrollMyListViewToBottom() {
		listView.setSelection(adapter.getCount() - 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_msg, menu);
		this.menu = menu;
		Database db = new Database(context);
		menu.getItem(0).setVisible(!db.contactAlreadyExists(sender));
		MApp app = MApp.getApp(context);
		if (app.isLoggedIn()) {
			MenuItem item = menu.findItem(R.id.action_msg_state);
			if (item != null)
				item.setIcon(R.drawable.ico_state_ready);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_add_contact:
			doAddContact(sender);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private final AddContactTaskListener addContactTaskListener = new AddContactTaskListener() {

		@Override
		public void onPreExecute() {
			dialogAdd = new ProgressDialog(context);
			dialogAdd.setCancelable(false);
			dialogAdd.show();
			dialogAdd.setContentView(new ProgressBar(context));
		}

		@Override
		public void onPostExecute(AddContactResponse response) {
			if (dialogAdd != null)
				dialogAdd.dismiss();
			if (response == null) {
				MToast.error(context, true);
			} else {

				Log.d("AddContactResponse", "AddContactResponse is: \n"
						+ response.toString());
				if (response.isError())
					MToast.errorAddContact(context, true);
				else {
					ContactStub contact_stub = response.getContact();
					contact_stub.setEmail(encryptor.decrypt(contact_stub
							.getEmail()));
					contact_stub.setName(encryptor.decrypt(contact_stub
							.getName()));

					if (contact_stub != null) {
						Contact contact = new Contact(contact_stub);
						Database db = new Database(context);
						if (db.insertContact(contact.toContentValues())) {
							contactAdded = true;
							if (menu != null)
								menu.getItem(0).setVisible(false);
						} else
							MToast.error(context, true);
					} else
						MToast.errorAddContact(context, true);
				}
			}
		}
	};

	protected void doAddContact(String id) {

		Database db = new Database(context);
		if (db.contactAlreadyExists(id)) {
			MToast.errorUserAlreadyExists(context, true);
			return;
		}

		AddContactRequest request = new AddContactRequest();
		request.setId(encryptor.encrypt(id));

		MApp app = MApp.getApp(context);
		ServerConnection connection = app.getServerConnection();

		AddContactTask addContactTask = new AddContactTask(connection,
				addContactTaskListener);
		addContactTask.execute(request);
	}

	private void doAutoLogin() {
		String email = ShPref.getShPrefString(context, "logininfo_email");
		String password = ShPref.getShPrefString(context, "logininfo_password");
		if ((email != null && !email.isEmpty())
				&& (password != null && !password.isEmpty())) {

			LoginRequest request = new LoginRequest();
			Encryption encryptor = new EncryptionDES();
			UserInfo info = GCM.loadIdPair(context);
			if (info == null) {
				Log.e("ActivityLogin", "UserInfo is null");
				return;
			}
			String gmcId = info.getGcmRegId();
			if (gmcId == null) {
				Log.e("ActivityLogin", "gmcId is null");
				return;
			}
			request.setGcmId(gmcId);
			request.setId(encryptor.encrypt(email));
			request.setPassword(encryptor.encrypt(password));

			MApp app = MApp.getApp(context);
			ServerConnection connection = app.getServerConnection();

			LoginTask loginTask = new LoginTask(connection, loginTaskListener);
			loginTask.execute(request);
		}
	}

	private final LoginTaskListener loginTaskListener = new LoginTaskListener() {

		@Override
		public void onPreExecute() {

		}

		@Override
		public void onPostExecute(LoginResponse response) {
			if (response != null) {
				if (!response.isError()) {
					MApp app = MApp.getApp(context);
					app.setLoggedIn();
					if (menu != null) {
						MenuItem item = menu.findItem(R.id.action_msg_state);
						if (item != null)
							item.setIcon(R.drawable.ico_state_ready);
					}
				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		init();
		ShPref.setShPrefString(this, KEY_CURRENT_SENDER, sender);
		MApp app = MApp.getApp(this);
		if (app.isLoggedIn()) {
			if (menu != null) {
				MenuItem item = menu.findItem(R.id.action_msg_state);
				if (item != null)
					item.setIcon(R.drawable.ico_state_ready);
			}
		} else
			doAutoLogin();

	}

	@Override
	protected void onPause() {
		super.onPause();
		ShPref.setShPrefString(this, KEY_CURRENT_SENDER, null);
	}

	private void exitOnError() {
		MToast.error(this, true);
		MApp.finishActivity(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.setIntent(intent);
		// Log.d("test", intent.getExtras().getString(ActivityMsg.EXTRA_EMAIL));
	}

}
