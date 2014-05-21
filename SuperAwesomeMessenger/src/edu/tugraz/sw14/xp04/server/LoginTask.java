package edu.tugraz.sw14.xp04.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {

  private final Context context;
  private LoginTaskListener listener;

  public interface LoginTaskListener {
    void onPreExecute();

    void onPostExecute(LoginResponse response);
  }

  public LoginTask(Context context, LoginTaskListener listener) {
    this.context = context;
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

    MApp app = MApp.getApp(context);
    ServerConnection connection = app.getServerConnection();
    if (connection != null) {
      try {
        response = connection.login(params[0]);
        return response;
      } catch (ServerConnectionException e) {
        Log.e("LoginTask", e.getMessage(), e);
      }
    }
    return null;
  }

  @Override
  protected void onPostExecute(LoginResponse response) {
    super.onPostExecute(response);
    this.listener.onPostExecute(response);
  }

}