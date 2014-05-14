package edu.tugraz.sw14.xp04;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;


public class ActivityQuit extends Activity {
	
	private static final int QUIT_APP_OFFSET = 2000;

    private Handler qHandler = new Handler();
    private Runnable qRunn = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit);


        View close = this.findViewById(R.id.a_quit_frl_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitNow();
            }
        });
        qHandler.postDelayed(qRunn, QUIT_APP_OFFSET);
    }

    @Override
    public void onBackPressed() {
        quitNow();
    }

    private void quitNow(){
        qHandler.removeCallbacks(qRunn);
        qHandler = null;
        finish();
    }
}
