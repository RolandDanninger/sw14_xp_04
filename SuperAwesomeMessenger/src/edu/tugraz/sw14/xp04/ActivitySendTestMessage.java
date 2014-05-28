package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;
import edu.tugraz.sw14.xp04.server.SendMessageTask;
import edu.tugraz.sw14.xp04.server.SendMessageTask.SendMessageTaskListener;
import edu.tugraz.sw14.xp04.server.LoginTask;
import edu.tugraz.sw14.xp04.server.ServerConnection;
import edu.tugraz.sw14.xp04.server.ServerConnectionException;
import edu.tugraz.sw14.xp04.server.LoginTask.LoginTaskListener;
import edu.tugraz.sw14.xp04.stubs.LoginRequest;
import edu.tugraz.sw14.xp04.stubs.LoginResponse;
import edu.tugraz.sw14.xp04.stubs.SendMessageRequest;
import edu.tugraz.sw14.xp04.stubs.SendMessageResponse;

public class ActivitySendTestMessage extends Activity {

  private Context context;

  private EditText etId;
  private EditText etMsg;
  private Button btnSend;
  private ProgressDialog dialog;

  private SendMessageTaskListener sendMessageTaskListener = new SendMessageTaskListener() {

    @Override
    public void onPreExecute() {
      
      dialog = new ProgressDialog(context);
      dialog.setCancelable(false);
      dialog.show();
      dialog.setContentView(new ProgressBar(context));
    }

    @Override
    public void onPostExecute(SendMessageResponse response) {
      
      if (dialog != null)
        dialog.dismiss();
      if (response == null)
        MToast.error(context, true);
      else {
        if (response.isError())
          MToast.errorSendMessage(context, true);
        else {
          MApp.goToActivity((Activity) context, ActivitySendTestMessage.class,
              true);
        }
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_test_message);
    this.context = this;

    this.etId = (EditText) findViewById(R.id.a_sendtestmessage_txt_id);
    this.etMsg = (EditText) findViewById(R.id.a_sendtestmessage_txt_message);
    this.btnSend = (Button) findViewById(R.id.a_sendtestmessage_btn_send);

    if (this.btnSend != null)
      btnSend.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Editable eaId = etId.getText();
          String strId = eaId.toString();

          Editable eaMsg = etMsg.getText();
          String strMsg = eaMsg.toString();

          sendMsg(strId, strMsg);
        }
      });

  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void sendMsg(String id, String msg) {
    
    MApp app = MApp.getApp(context);
    ServerConnection connection = app.getServerConnection();
    
    SendMessageRequest request = new SendMessageRequest();
    request.setReceiverId(id);
    request.setMessage(msg);
    
    SendMessageTask sendMessageTask = new SendMessageTask(connection, sendMessageTaskListener);
    sendMessageTask.execute(request);
  }

}
