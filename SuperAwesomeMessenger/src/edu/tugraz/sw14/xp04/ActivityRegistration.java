package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class ActivityRegistration extends Activity {

	private EditText txtId = null;
	private EditText txtPassword = null;
	private EditText txtPasswordRepeat = null;
	private Button btnRegister = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		txtId = (EditText) findViewById(R.id.a_registration_txt_id);
		txtPassword = (EditText) findViewById(R.id.a_registration_txt_password);
		txtPasswordRepeat = (EditText) findViewById(R.id.a_registration_txt_reenter_password);
		btnRegister = (Button) findViewById(R.id.a_registration_btn_register);
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

}
