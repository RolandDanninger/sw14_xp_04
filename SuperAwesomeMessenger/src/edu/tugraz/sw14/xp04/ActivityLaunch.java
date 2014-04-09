package edu.tugraz.sw14.xp04;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.gcm.IdPair;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.ShPref;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
			IdPair pair = GCM.loadIdPair(context);
			if (pair == null)
				regid = null;
			else
				regid = pair.getGcmRegId();
			if (regid == null)
				registerInBackground();
			else {
				Log.d("gcm", regid);
				MApp.goToActivity(this, ActivityMain.class, true);
			}
		} else {
			Log.i("checkPlayService", "No valid google play apk found.");
			// TODO
			// MApp.goToActivity(this, ActivityNoPlayService.class, true);
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
				// finish();
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

				// You should send the registration ID to your server over HTTP,
				// so it can use GCM/HTTP or CCS to send messages to your app.
				// The request to your server should be authenticated if your
				// app
				// is using accounts.
				// sendRegistrationIdToBackend();

				// For this demo: we don't need to send it because the device
				// will send upstream messages to a server that echo back the
				// message using the 'from' address in the message.

				// Persist the regID - no need to register again.
				storeRegistrationId(context, regid);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				// If there is an error, don't just keep trying to register.
				// Require the user to click a button again, or perform
				// exponential back-off.
			}
			return msg;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d("gcm", result);
		}

	}

	private void storeRegistrationId(Context context, String regid) {
		IdPair pair = GCM.loadIdPair(context);
		if (pair == null) {
			pair = new IdPair(regid);
		}

		pair.setDeviceServerId(regid);
		GCM.storeIdPair(context, pair);
	}
}
