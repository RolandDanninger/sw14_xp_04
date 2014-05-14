package edu.tugraz.sw14.xp04.derivates;

import android.app.Activity;
import android.os.Bundle;
import edu.tugraz.sw14.xp04.ActivityLogin;
import edu.tugraz.sw14.xp04.ActivitySendTestMessage;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.helpers.MToast;

public class ActivityLoginDerivate extends ActivityLogin {
  
  public ActivityLoginDerivate() {
    super();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void doLogin(String email, String password) {
    
    // QND
    if(!email.equals("success@user.sw"))
      MToast.errorLogin(this, true);
    else
      MApp.goToActivity((Activity) this,
          ActivitySendTestMessage.class, true);
  }
  
  

}
