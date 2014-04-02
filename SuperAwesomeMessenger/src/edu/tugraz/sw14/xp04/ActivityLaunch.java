package edu.tugraz.sw14.xp04;

import edu.tugraz.sw14.xp04.helpers.MApp;
import android.app.Activity;
import android.os.Bundle;

public class ActivityLaunch extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		MApp.goToActivity(this, ActivityMain.class, true);

	}
}
