package edu.tugraz.sw14.xp04.client;

import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GcmBroadcastReceiver extends BroadcastReceiver {
	public static final String TAG = "GcmBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onReceive called()");
		String action = intent.getAction();
		if (action != null)
			Log.d(TAG, "action is '" + action + "'");
		Set<String> categories = intent.getCategories();
		if (categories != null) {
			for (String cat : categories) {
				if (cat != null)
					Log.d(TAG, "- " + cat);
			}
		}
	}
}
