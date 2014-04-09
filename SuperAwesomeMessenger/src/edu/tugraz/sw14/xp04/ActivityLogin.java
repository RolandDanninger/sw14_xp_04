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
import edu.tugraz.sw14.xp04.helpers.MToast;
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
			MToast.errorLogin(context, true);
			return;
		}
		if (password == null || password.isEmpty()) {
			MToast.errorLogin(context, true);
			return;
		}

		doLogin(email, password);

	}

	private void handlerBtnRegister() {

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
			return null;

		}

		@Override
		protected void onPostExecute(LoginResponse result) {
			super.onPostExecute(result);
			if (dialog != null)
				dialog.dismiss();

		}

	}
}
