package edu.tugraz.sw14.xp04.server;

import android.os.AsyncTask;
import android.util.Log;
import edu.tugraz.sw14.xp04.stubs.RegistrationRequest;
import edu.tugraz.sw14.xp04.stubs.RegistrationResponse;

public class RegistrationTask extends
    AsyncTask<RegistrationRequest, Void, RegistrationResponse> {

  private final ServerConnection connection;
  private RegistrationTaskListener listener;

  public interface RegistrationTaskListener {
    void onPreExecute();

    void onPostExecute(RegistrationResponse response);
  }

  public RegistrationTask(ServerConnection connection,
      RegistrationTaskListener listener) {
    this.connection = connection;
    this.listener = listener;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    this.listener.onPreExecute();
  }

  @Override
  protected RegistrationResponse doInBackground(RegistrationRequest... params) {
    RegistrationResponse response = null;

    if (connection != null) {
      try {
        response = connection.register(params[0]);
        return response;
      } catch (ServerConnectionException e) {
        Log.e("RegistrationTask", e.getMessage(), e);
      }
    } else
      Log.e("RegistrationTask", "connection is null");
    return null;
  }

  @Override
  protected void onPostExecute(RegistrationResponse response) {
    super.onPostExecute(response);
    this.listener.onPostExecute(response);
  }

}