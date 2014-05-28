package edu.tugraz.sw14.xp04.server;

import android.os.AsyncTask;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class SendMessageTask extends AsyncTask<SendMessageRequest, Void, SendMessageResponse> {
  
  private final ServerConnection connection;
  private SendMessageTaskListener listener;

  public interface SendMessageTaskListener {

    void onPreExecute();

    void onPostExecute(SendMessageResponse response);
  }
  
  public SendMessageTask(ServerConnection connection, SendMessageTaskListener listener) {
    this.connection = connection;
    this.listener = listener;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    listener.onPreExecute();
  }

  @Override
  protected SendMessageResponse doInBackground(SendMessageRequest... params) {
    
    SendMessageResponse response = null;

    if (connection != null) {
      try {
        response = connection.sendMessage(params[0]);
        return response;
      } catch (ServerConnectionException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return null;

  }

  @Override
  protected void onPostExecute(SendMessageResponse response) {
    super.onPostExecute(response);
    listener.onPostExecute(response);
  }
}
