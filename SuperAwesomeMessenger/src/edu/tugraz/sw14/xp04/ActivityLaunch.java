package edu.tugraz.sw14.xp04;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.ShPref;
import edu.tugraz.sw14.xp04.helpers.UserInfo;
import edu.tugraz.sw14.xp04.server.LoginTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.LoginTask.LoginTaskListener;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

public class ActivityLaunch extends Activity {

	private Context context;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	String regid;

	private ProgressDialog dialog;

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

	private void goToTarget() {
		String email = ShPref.getShPrefString(context, "logininfo_email");
		String password = ShPref.getShPrefString(context, "logininfo_password");
		if ((email == null || email.isEmpty())
				|| (password == null || password.isEmpty()))
			MApp.goToActivity(this, ActivityLogin.class, true);
		else
			doAutoLogin(email, password);
	}

	private void doAutoLogin(String email, String password) {
		LoginRequest request = new LoginRequest();
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
		request.setId(email);
		request.setPassword(password);

		MApp app = MApp.getApp(context);
		ServerConnection connection = app.getServerConnection();

		LoginTask loginTask = new LoginTask(connection, loginTaskListener);
		loginTask.execute(request);
	}

	private LoginTaskListener loginTaskListener = new LoginTaskListener() {

		@Override
		public void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.show();
			dialog.setContentView(new ProgressBar(context));
		}

		@Override
		public void onPostExecute(LoginResponse response) {
			if (dialog != null)
				dialog.dismiss();
			if (response == null) {
				MToast.error(context, true);
				goToError();
			} else {
				if (response.isError()) {
					MToast.errorLogin(context, true);
					goToError();
				} else {
					MApp.goToActivity((Activity) context, ActivityMain.class,
							true);
				}
			}
		}
	};

	private void goToError() {
		MApp.goToActivity(this, ActivityLogin.class, true);
	}
}
