package edu.tugraz.sw14.xp04.helpers;

import android.app.ActionBar;
import android.app.Activity;

public class UIHelper {

	public static void setActionBarIco(Activity activity, int res){
        ActionBar actionBar = activity.getActionBar();
        if(actionBar != null){
            actionBar.setIcon(res);
        }
    }
	public static void setActionBarTitle(Activity activity, String title){
        ActionBar actionBar = activity.getActionBar();
        if(actionBar != null){
            actionBar.setTitle(title);
        }
    }
	
}
