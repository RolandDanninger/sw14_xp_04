package edu.tugraz.sw14.xp04.controllers;

import android.util.Log;
import edu.tugraz.sw14.xp04.ActivityRegistration;
import edu.tugraz.sw14.xp04.helpers.Encryption;
import edu.tugraz.sw14.xp04.helpers.EncryptionDES;
import edu.tugraz.sw14.xp04.server.RegistrationTask;
import edu.tugraz.sw14.xp04.server.RegistrationTask.RegistrationTaskListener;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class RegistrationController {

	private final ActivityRegistration activity;
	private Encryption encryptor;
	private final ServerConnection connection;

	private final RegistrationTaskListener registrationTaskListener = new RegistrationTaskListener() {
		@Override
		public void onPostExecute(RegistrationResponse response) {
			onRegistrationTaskFinished(response);
		}
	};

	public RegistrationController(ActivityRegistration activity,
			ServerConnection connection) {
		this.activity = activity;
		this.connection = connection;
	}

	public void onRegistrationTaskFinished(RegistrationResponse response) {
		activity.onRegistrationTaskFinished(response);
	}

	public void startRegistrationTask(String email, String password) {
		RegistrationRequest request = new RegistrationRequest();
		encryptor = new EncryptionDES();

		request.setId(encryptor.encrypt(email));
		request.setPassword(encryptor.encrypt(password));
		request.setName(encryptor.encrypt(email));

		Log.d("registration", "encoded mail: " + encryptor.encrypt(email));
		Log.d("registration",
				"encoded password: " + encryptor.encrypt(password));

		RegistrationTask task = new RegistrationTask(connection,
				registrationTaskListener);
		task.execute(request);
	}
}
