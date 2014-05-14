package edu.tugraz.sw14.xp04;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.UserInfo;

public class ActivityLaunch extends Activity {

	private Context context;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	String regid;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);

		this.context = this;

		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(context);
			UserInfo pair = GCM.loadIdPair(context);
			if (pair == null)
				regid = null;
			else {
				regid = pair.getGcmRegId();
			}
			if (regid == null)
				registerInBackground();
			else {
				Log.d("gcm", "registration id loaded: " + regid);
				goToTarget();
			}
		} else {
			Log.i("checkPlayService", "No valid google play apk found.");
			goToError();
		}

	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("checkPlayService", "This device is not supported.");
			}
			return false;
		}
		return true;
	}

	private void registerInBackground() {
		new GCMRegisterTask().execute((Void[]) null);
	}

	private class GCMRegisterTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}
				regid = gcm.register(GCM.PROJECT_NUMBER);
				msg = "Device registered, registration ID=" + regid;

				storeRegistrationId(context, regid);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
			}
			return msg;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				goToTarget();
				Log.d("gcm", result);
			} else {
				goToError();
			}
		}

	}

	private void storeRegistrationId(Context context, String regid) {
		UserInfo pair = GCM.loadIdPair(context);
		if (pair == null) {
			pair = new UserInfo(regid);
		}

		pair.setDeviceServerId(regid);
		GCM.storeIdPair(context, pair);
	}

	private boolean isLoggedIn() {
		return false;
	}

	private void goToTarget() {
		if (isLoggedIn())
			MApp.goToActivity(this, ActivityMain.class, true);
		else
			MApp.goToActivity(this, ActivityMain.class, true);
	}

	private void goToError() {
		// TODO
		// MApp.goToActivity(this, ActivityNoPlayService, true);
	}
}
