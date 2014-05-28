package edu.tugraz.sw14.xp04;

import edu.tugraz.sw14.xp04.server.RegistrationTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.RegistrationTask.RegistrationTaskListener;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class RegistrationController{
	
	private ActivityRegistration activity;
	private ServerConnection connection;
	
	private final RegistrationTaskListener registrationTaskListener = new RegistrationTaskListener() {
		@Override
		public void onPostExecute(RegistrationResponse response) {
			onRegistrationTaskFinished(response);
		}
	};
	
	public RegistrationController(ActivityRegistration activity, ServerConnection connection)
	{
		this.activity = activity;
		this.connection = connection;
	}
	
	public void onRegistrationTaskFinished(RegistrationResponse response)
	{
		activity.onRegistrationTaskFinished(response);
	}
	
	public void startRegistrationTask(String email, String password)
	{
		RegistrationRequest request = new RegistrationRequest();

		request.setId(email);
		request.setPassword(password);
		request.setName(email);

		RegistrationTask task = new RegistrationTask(connection,
				registrationTaskListener);
		task.execute(request);
	}
}
