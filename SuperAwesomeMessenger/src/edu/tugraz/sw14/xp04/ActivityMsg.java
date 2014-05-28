package edu.tugraz.sw14.xp04;

import java.util.ArrayList;

import edu.tugraz.sw14.xp04.adapters.ChatOverviewAdapter;
import edu.tugraz.sw14.xp04.adapters.MsgAdapter;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.UIHelper;
import edu.tugraz.sw14.xp04.msg.Msg;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.os.Build;

public class ActivityMsg extends Activity {

	public static final String EXTRA_EMAIL = "extra_email";

	private ListView listView;
	private ArrayList<Msg> list;
	private MsgAdapter adapter;
	
	private ImageButton btnSend;
	
	private boolean msgs_loaded = false;

    public boolean msgsLoaded(){
        return this.msgs_loaded;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);

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
		String email = bundle.getString(EXTRA_EMAIL);
		if (email == null) {
			exitOnError();
			return;
		}

		Database db = new Database(this);
		String name = db.getContactName(email);
		name = name != null ? name : email;
		
		UIHelper.setActionBarIco(this, R.drawable.ico_w_person);
		UIHelper.setActionBarTitle(this, name);
		
		this.btnSend = (ImageButton) findViewById(R.id.msg_btn_send);
		if(this.btnSend != null){
			this.btnSend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		this.list = new ArrayList<Msg>();
		this.listView = (ListView) findViewById(R.id.msg_list);
        this.adapter = new MsgAdapter(this, R.layout.item_msg, list);
        this.listView.setAdapter(this.adapter);
        loadMsgs(0);

	}
	
	private void loadMsgs(int limit){
		
		Msg test1 = new Msg("a@b.com",
				"das ist eine testnachricht",
				System.currentTimeMillis(),
				false, false);
		Msg test2 = new Msg("a@b.com",
				"Hallo",
				System.currentTimeMillis(),
				false, false);
		Msg test3 = new Msg("a@b.com",
				"das ist eine testnachricht und diese ist sehr sehr sehr sehr sehr sehr sehr sehr sehr sehr sherhseh sehr sehr sehr sehr sehr laaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaang",
				System.currentTimeMillis(),
				false, false);
		Msg test4 = new Msg("a@b.com",
				"das ist meine antwort",
				System.currentTimeMillis(),
				true, false);
		Msg test5 = new Msg("a@b.com",
				"jippiiii",
				System.currentTimeMillis(),
				false, false);

		list.add(test1);
		list.add(test2);
		list.add(test3);
		list.add(test4);
		list.add(test5);
		
		msgs_loaded = true;
		adapter.notifyDataSetChanged();
		if(limit <= 0){
			scrollMyListViewToBottom();
		}
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

	private void exitOnError() {
		MToast.error(this, true);
		MApp.finishActivity(this);
	}

}
