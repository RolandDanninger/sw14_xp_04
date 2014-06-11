package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.tugraz.sw14.xp04.controllers.RegistrationController;
import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.UserInfo;
import edu.tugraz.sw14.xp04.server.LoginTask;
import edu.tugraz.sw14.xp04.server.RegistrationTask;
import edu.tugraz.sw14.xp04.server.LoginTask.LoginTaskListener;
import edu.tugraz.sw14.xp04.server.RegistrationTask.RegistrationTaskListener;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.ServerConnectionException;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class ActivityRegistration extends Activity implements OnClickListener {

	private Context context;

	private EditText txtId = null;
	private EditText txtPassword = null;
	private EditText txtPasswordRepeat = null;
	private TextView lblError = null;
	private Button btnRegister = null;
	private ProgressDialog dialog;

	private RegistrationController controller = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("ActivityRegistration", "ONCREATE");

		setContentView(R.layout.activity_registration);
		context = this;

		MApp app = MApp.getApp(context);
		ServerConnection connection = app.getServerConnection();

		this.setController(new RegistrationController(this, connection));

		txtId = (EditText) findViewById(R.id.a_registration_txt_id);
		txtPassword = (EditText) findViewById(R.id.a_registration_txt_password);
		txtPasswordRepeat = (EditText) findViewById(R.id.a_registration_txt_reenter_password);
		lblError = (TextView) findViewById(R.id.a_registration_lbl_error);
		btnRegister = (Button) findViewById(R.id.a_registration_btn_register);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		MApp.goToActivity(this, ActivityLogin.class, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_registration, menu);
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.a_registration_btn_register:
			handlerBtnRegister();
			break;
		default:
			break;
		}
	}

	private void handlerBtnRegister() {
		String id = txtId.getText().toString();
		String password = txtPassword.getText().toString();
		String passwordRepeat = txtPasswordRepeat.getText().toString();

		if (id.isEmpty()) {
			lblError.setText(R.string.a_registration_error_missing_id);
			lblError.setVisibility(View.VISIBLE);
			return;
		}
		if (password.isEmpty()) {
			lblError.setText(R.string.a_registration_error_missing_password);
			lblError.setVisibility(View.VISIBLE);
			return;
		}
		if (password.compareTo(passwordRepeat) != 0) {
			lblError.setText(R.string.a_registration_error_passwords_mismatch);
			lblError.setVisibility(View.VISIBLE);
			return;
		}

		boolean idValid = android.util.Patterns.EMAIL_ADDRESS.matcher(id)
				.matches();
		if (!idValid) {
			lblError.setText(R.string.a_registration_error_invalid_id);
			lblError.setVisibility(View.VISIBLE);
			return;
		}

		startRegistrationTask(id, password);
	}

	private void startRegistrationTask(String email, String password) {
		this.setLoading();
		controller.startRegistrationTask(email, password);
	}

	public void setLoading() {
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.show();
		dialog.setContentView(new ProgressBar(context));
	}

	public void onRegistrationTaskFinished(RegistrationResponse response) {
		if (dialog != null)
			dialog.dismiss();
		if (response == null)
			MToast.error(context, true);
		else {
			if (response.isError())
				registrationFailed();
			else {
				registrationSuccessfull();
			}
		}
	}

	private void registrationSuccessfull() {
		Toast.makeText(ActivityRegistration.this,
				R.string.a_registration_success, Toast.LENGTH_SHORT).show();
		MApp.goToActivity((Activity) context, ActivityLogin.class, true);
	}

	private void registrationFailed() {
		MToast.errorRegister(context, true);
	}

	public void setController(RegistrationController controller) {
		this.controller = controller;
	}
}
