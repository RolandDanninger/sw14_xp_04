package edu.tugraz.sw14.xp04;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivitySendTestMessage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_test_message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_send_test_message, menu);
		return true;
	}

}
