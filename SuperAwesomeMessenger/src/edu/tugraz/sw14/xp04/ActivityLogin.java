package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class ActivityLogin extends Activity {

	private Context context;
	private EditText etEmail;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnRegister;
	private ProgressDialog dialog;

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
			if (response == null)
				MToast.error(context, true);
			else {
				if (response.isError())
					MToast.errorLogin(context, true);
				else {
					saveLoginInfo();
					MApp.goToActivity((Activity) context, ActivityMain.class,
							true);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		context = this;
		etEmail = (EditText) findViewById(R.id.a_login_txt_email);
		etPassword = (EditText) findViewById(R.id.a_login_txt_password);
		btnLogin = (Button) findViewById(R.id.a_login_btn_login);
		btnRegister = (Button) findViewById(R.id.a_login_btn_register);

		if (btnLogin != null) {
			btnLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					handlerBtnLogin();

				}
			});
		}

		if (btnRegister != null) {
			btnRegister.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					handlerBtnRegister();

				}
			});
		}

		String email = ShPref.getShPrefString(context, "logininfo_email");
		String password = ShPref.getShPrefString(context, "logininfo_password");
		if (email != null && !email.isEmpty()) {
			if (etEmail != null)
				etEmail.setText(email);
		}
		if (password != null && !password.isEmpty()) {
			if (etPassword != null)
				etPassword.setText(password);
		}

	}

	@Override
	public void onBackPressed() {
		MApp.quitApp(this);
	}

	private void handlerBtnLogin() {
		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString();

		if (email == null || email.isEmpty()) {
			MToast.errorLoginEmail(context, true);
			return;
		}
		if (password == null || password.isEmpty()) {
			MToast.errorLoginPassword(context, true);
			return;
		}

		doLogin(email, password);

	}

	private void saveLoginInfo() {
		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString();

		if (email == null || email.isEmpty()) {
			MToast.errorLoginEmail(context, true);
			return;
		}
		if (password == null || password.isEmpty()) {
			MToast.errorLoginPassword(context, true);
			return;
		}
		ShPref.setShPrefString(context, "logininfo_email", email);
		ShPref.setShPrefString(context, "logininfo_password", password);
	}

	private void handlerBtnRegister() {
		MApp.goToActivity(this, ActivityRegistration.class, true);
	}

	protected void doLogin(String email, String password) {
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
}
