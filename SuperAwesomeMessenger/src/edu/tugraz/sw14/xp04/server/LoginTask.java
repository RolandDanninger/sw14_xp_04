package edu.tugraz.sw14.xp04.server;

import android.os.AsyncTask;
import android.util.Log;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

  private final ServerConnection connection;
  private LoginTaskListener listener;

  public interface LoginTaskListener {
    void onPreExecute();
    void onPostExecute(LoginResponse response);
  }

  public LoginTask(ServerConnection connection, LoginTaskListener listener) {
    this.connection = connection;
    this.listener = listener;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    this.listener.onPreExecute();
  }

  @Override
  protected LoginResponse doInBackground(LoginRequest... params) {
    LoginResponse response = null;

    if (connection != null) {
      try {
        response = connection.login(params[0]);
        return response;
      } catch (ServerConnectionException e) {
        Log.e("LoginTask", e.getMessage(), e);
      }
    }
    else
      Log.e("LoginTask", "connection is null");
    return null;
  }

  @Override
  protected void onPostExecute(LoginResponse response) {
    super.onPostExecute(response);
    this.listener.onPostExecute(response);
  }

}