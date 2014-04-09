package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.tugraz.sw14.xp04.gcm.GCM;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.helpers.UserInfo;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class ActivityRegistration extends Activity implements OnClickListener {
	
	private Context context;
	
	private EditText txtId = null;
	private EditText txtPassword = null;
	private EditText txtPasswordRepeat = null;
	private TextView lblError = null;
	private Button btnRegister = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		context = this;
		
		txtId = (EditText) findViewById(R.id.a_registration_txt_id);
		txtPassword = (EditText) findViewById(R.id.a_registration_txt_password);
		txtPasswordRepeat = (EditText) findViewById(R.id.a_registration_txt_reenter_password);
		lblError = (TextView) findViewById(R.id.a_registration_lbl_error);
		btnRegister = (Button) findViewById(R.id.a_registration_btn_register);
		btnRegister.setOnClickListener(this);
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
	
	private void handlerBtnRegister(){
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

		doRegister(id, password);
		Toast.makeText(this, R.string.a_registration_success,
				Toast.LENGTH_SHORT).show();
	}
	
	private void doRegister(String email, String password) {
		new RegisterTask(email, password).execute((Void[]) null);
	}

	private class RegisterTask extends AsyncTask<Void, Void, RegistrationResponse> {

		private ProgressDialog dialog;
		private final String email;
		private final String password;

		public RegisterTask(String email, String password) {
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
		protected RegistrationResponse doInBackground(Void... params) {
			RegistrationResponse response = null;
			RegistrationRequest request = new RegistrationRequest();
			UserInfo info = GCM.loadIdPair(context);
			if(info == null) return null;
			String gmcId = info.getGcmRegId();
			if(gmcId == null) return null;
			request.setId(email);
			request.setPassword(password);
			
			ServerConnection connection = new ServerConnection(ServerConnection.SERVER_URL);
			if(connection != null){
				response = connection.register(request);
			}
			return response;

		}

		@Override
		protected void onPostExecute(RegistrationResponse response) {
			super.onPostExecute(response);
			if (dialog != null) dialog.dismiss();
			if(response == null) MToast.error(context, true);
			else {
				if(response.isError()) MToast.errorLoginEmail(context, true);
				else {
					MToast.errorLoginEmail(context, true);
					MApp.goToActivity((Activity)context, ActivityMain.class, true);
				}
			}
		}
	}
}
