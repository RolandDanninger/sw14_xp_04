package edu.tugraz.sw14.xp04.client;

import java.util.Set;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	public static final String TAG = "GcmBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// DEbug
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
		/*
		 * // Explicitly specify that GcmIntentService will handle the intent.
		 * ComponentName comp = new ComponentName(context.getPackageName(),
		 * GcmIntentService.class.getName()); // Start the service, keeping the
		 * device awake while it is launching. startWakefulService(context,
		 * (intent.setComponent(comp))); setResultCode(Activity.RESULT_OK);
		 */
	}
}
