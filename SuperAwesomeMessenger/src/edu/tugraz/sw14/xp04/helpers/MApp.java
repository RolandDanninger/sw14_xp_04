package edu.tugraz.sw14.xp04.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

public class MApp extends Application {

	public static void goToActivity(final Activity activity, Class target,
			boolean doFinish) {
		if (activity != null && target != null) {
			Intent intent = new Intent(activity, target);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			if (doFinish)
				activity.finish();
		}
	}

	public static void goToActivity(final Activity activity, Intent target,
			boolean doFinish) {
		if (activity != null && target != null) {
			target.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(target);
			activity.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			if (doFinish)
				activity.finish();
		}
	}

}
