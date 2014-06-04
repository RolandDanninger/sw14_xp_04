package edu.tugraz.sw14.xp04.controllers;

import android.util.Log;
import android.view.View;
import edu.tugraz.sw14.xp04.ActivityMain;
import edu.tugraz.sw14.xp04.ActivityRegistration;
import edu.tugraz.sw14.xp04.contacts.Contact;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.navigation.NavigationDrawerFragment;
import edu.tugraz.sw14.xp04.server.AddContactTask.AddContactTaskListener;
import edu.tugraz.sw14.xp04.server.AddContactTask;
import edu.tugraz.sw14.xp04.server.RegistrationTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.RegistrationTask.RegistrationTaskListener;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;
import edu.tugraz.sw14.xp04.stubs.ContactStub;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class NavigationController{
	
	public enum State {
		SUCCESS,
		FAILED,
		ERROR
	}
	
	private NavigationDrawerFragment fragment;
	private ServerConnection connection;
	private Database db;
	
	private final AddContactTaskListener addContactTaskListener = new AddContactTaskListener() {
		
		@Override
		public void onPreExecute() {
			onAddContactTaskStarted();
			
		}
		
		@Override
		public void onPostExecute(AddContactResponse response) {
			onAddContactTaskFinished(response);
		}
	};
	
	public NavigationController(NavigationDrawerFragment fragment, ServerConnection connection, Database db)
	{
		this.fragment = fragment;
		this.connection = connection;
		this.db = db;
	}
	
	public void onAddContactTaskStarted()
	{
		fragment.onAddContactTaskStarted();
	}
	public void onAddContactTaskFailed()
	{
		fragment.onAddContactTaskFailed();
	}
	public void onAddContactTaskFinished(AddContactResponse response)
	{
		State state = State.ERROR;
		if (response == null) {
			state = State.ERROR;
		} else {
			Log.d("AddContactResponse",
					"AddContactResponse is: \n" + response.toString());
			if (response.isError())
				state = State.FAILED;
			else {
				ContactStub contact_stub = response.getContact();
				if (contact_stub != null) {
					Contact contact = new Contact(contact_stub);
					if (db.insertContact(contact.toContentValues())) {
						state = State.SUCCESS;

					} else
						state = State.ERROR;
				} else
					state = State.FAILED;
			}
		}
		fragment.onAddContactTaskFinished(state);
	}
	
	
	public void startAddContactTask(String id)
	{
		if(db.contactAlreadyExists(id)){
			onAddContactTaskFailed();
			return;
		}
		
		AddContactRequest request = new AddContactRequest();
		request.setId(id);

		AddContactTask addContactTask = new AddContactTask(connection,
				addContactTaskListener);
		addContactTask.execute(request);
	}
}
