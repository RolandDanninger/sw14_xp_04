package edu.tugraz.sw14.xp04.server;

import android.os.AsyncTask;
import android.util.Log;
import edu.tugraz.sw14.xp04.stubs.AddContactRequest;
import edu.tugraz.sw14.xp04.stubs.AddContactResponse;

public class AddContactTask extends AsyncTask<AddContactRequest, Void, AddContactResponse> {

  private final ServerConnection connection;
  private AddContactTaskListener listener;

  public interface AddContactTaskListener {
    void onPreExecute();

    void onPostExecute(AddContactResponse response);
  }

  public AddContactTask(ServerConnection connection, AddContactTaskListener listener) {
    this.connection = connection;
    this.listener = listener;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    this.listener.onPreExecute();
  }

  @Override
  protected AddContactResponse doInBackground(AddContactRequest... params) {
    AddContactResponse response = null;

    if (connection != null) {
      try {
        response = connection.AddContact(params[0]);
        return response;
      } catch (ServerConnectionException e) {
        Log.e("AddContactTask", e.getMessage(), e);
      }
    }
    else
      Log.e("AddContactTask", "connection is null");
    return null;
  }

  @Override
  protected void onPostExecute(AddContactResponse response) {
    super.onPostExecute(response);
    this.listener.onPostExecute(response);
  }

}