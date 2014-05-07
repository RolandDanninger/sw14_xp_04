package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.UserInfo;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.ServerConnectionException;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

public class ActivityLogin extends Activity {

	private Context context;
	private EditText etEmail;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnRegister;

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

	private void handlerBtnRegister() {
		MApp.goToActivity(this, ActivityRegistration.class, true);
	}

	private void doLogin(String email, String password) {
		new LoginTask(email, password).execute((Void[]) null);
	}

	private class LoginTask extends AsyncTask<Void, Void, LoginResponse> {

		private ProgressDialog dialog;
		private final String email;
		private final String password;

		public LoginTask(String email, String password) {
			this.email = email;
			this.password = password;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.show();
			dialog.setContentView(new ProgressBar(context));
		}

		@Override
		protected LoginResponse doInBackground(Void... params) {
			LoginResponse response = null;
			LoginRequest request = new LoginRequest();
			UserInfo info = GCM.loadIdPair(context);
			if (info == null)
				return null;
			String gmcId = info.getGcmRegId();
			if (gmcId == null)
				return null;
			request.setGcmId(gmcId);
			request.setId(email);
			request.setPassword(password);

			MApp app = MApp.getApp(context);
			ServerConnection connection = app.getServerConnection();
			if (connection != null) {
				try {
					response = connection.login(request);
					return response;
				} catch (ServerConnectionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;

		}

		@Override
		protected void onPostExecute(LoginResponse response) {
			super.onPostExecute(response);
			if (dialog != null)
				dialog.dismiss();
			if (response == null)
				MToast.error(context, true);
			else {
				if (response.isError())
					MToast.errorLogin(context, true);
				else {
					MApp.goToActivity((Activity) context,
							ActivitySendTestMessage.class, true);
				}
			}
		}

	}
}
