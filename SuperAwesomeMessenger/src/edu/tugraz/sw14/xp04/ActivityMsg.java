package edu.tugraz.sw14.xp04;

import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.UIHelper;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class ActivityMsg extends Activity {
	
	public static final String EXTRA_EMAIL = "extra_email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);

		Intent intent = getIntent();
		if(intent == null) {
			exitOnError();
			return;
		}
		Bundle bundle = intent.getExtras();
		if(bundle == null) {
			exitOnError();
			return;
		}
		String email = bundle.getString(EXTRA_EMAIL);
		if(email == null){
			exitOnError();
			return;
		}
		
		UIHelper.setActionBarTitle(this, email);
		
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
	
	
	private void exitOnError(){
		MToast.error(this, true);
		MApp.finishActivity(this);
	}

}
