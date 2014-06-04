package edu.tugraz.sw14.xp04.gcm;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.tugraz.sw14.xp04.ActivityLaunch;
import edu.tugraz.sw14.xp04.ActivityMain;
import edu.tugraz.sw14.xp04.ActivityMsg;
import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.helpers.DateTime;
import edu.tugraz.sw14.xp04.helpers.Encryption;
import edu.tugraz.sw14.xp04.helpers.EncryptionDES;
import edu.tugraz.sw14.xp04.helpers.EncryptionSimple;
import edu.tugraz.sw14.xp04.helpers.MApp;
import edu.tugraz.sw14.xp04.msg.Msg;

public class GcmIntentService extends IntentService {

	public static final String TAG = "GcmIntentService";
	private final Encryption encryptorz;

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
		encryptorz = new EncryptionDES();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "onHandleIntent() called");
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			/*
			 * if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
			 * .equals(messageType)) { sendNotification("Send error: " +
			 * extras.toString()); } else if
			 * (GoogleCloudMessaging.MESSAGE_TYPE_DELETED .equals(messageType))
			 * { sendNotification("Deleted messages on server: " +
			 * extras.toString()); // If it's a regular GCM message, do some
			 * work. } else
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// This loop represents the service doing some work.

				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

				storeMessageInDatabase(extras);
				// Post notification of received message.

				Log.i(TAG, "app on top is: " + getAppNameOnTop());
				String appOnTop = getAppNameOnTop();
				if (!appOnTop.contains("SuperAwesomeMessenger")) {
					sendNotification(extras);
				} else {
					if (appOnTop.contains("ActivityMsg")) {

					} else if (appOnTop.contains("ActivityMain")) {
						Intent i = new Intent(this, ActivityMain.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						getApplicationContext().startActivity(i);
					}

				}

				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	public String getAppNameOnTop() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
		PackageManager mPackageManager = getPackageManager();
		String packageName = mActivityManager.getRunningTasks(1).get(0).topActivity
				.getPackageName();
		ApplicationInfo mApplicationInfo;
		try {
			mApplicationInfo = mPackageManager.getApplicationInfo(packageName,
					0);
		} catch (NameNotFoundException e) {
			mApplicationInfo = null;
		}
		String appName = (String) (mApplicationInfo != null ? mPackageManager
				.getApplicationLabel(mApplicationInfo) : "(unknown)");

		String className = mActivityManager.getRunningTasks(1).get(0).topActivity
				.getClassName();
		return appName + "-" + className;
	}

	private void storeMessageInDatabase(Bundle extras) {
		String sender = encryptorz.decrypt(extras.getString("sender"));
		String msg = encryptorz.decrypt(extras.getString("message"));
		// String msg = extras.getString("message");

		long timeStamp = Long.valueOf(extras.getString("timestamp"));

		if (sender == null || sender == "") {
			Log.d(TAG, "sender email was empty or null");
		} else if (msg == null) {
			Log.d(TAG, "decode error, not storing message");
		} else {
			Msg toStore = new Msg(sender, msg, timeStamp, false, false);
			Log.d(TAG, "time in ms: " + timeStamp);
			Log.d(TAG, "time sent: " + DateTime.dateFromStamp(timeStamp) + " "
					+ DateTime.timeFromStamp(timeStamp));

			Database db = new Database(this);
			boolean result = db.insertMsg(toStore.toContentValues());
			if (result == false)
				Log.d(TAG, "failed to store message");
		}

	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle extras) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		String sender = encryptorz.decrypt(extras.getString("sender"));
		String msg = encryptorz.decrypt(extras.getString("message"));
		// String msg = extras.getString("message");

		long timeStamp = Long.valueOf(extras.getString("timestamp"));

		if (sender == null || sender == "")
			Log.d(TAG, "sender email was empty or null");

		Database db = new Database(this);
		int id = db.getContactId(sender);
		Log.d(TAG, "sender id is: " + id);

		Intent intent = new Intent(this, ActivityLaunch.class);
		intent.putExtra(ActivityMsg.EXTRA_EMAIL, sender);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, id,
				intent, 0);

		// PendingIntent contentIntent = PendingIntent.getActivity(this, id,
		// intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder mBuilder = new Notification.Builder(this)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.logo_sam)
				.setWhen(timeStamp)
				.setContentTitle(sender)
				.setVibrate(new long[] { 700, 700, 1000, 700, 700 })
				// .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.logo_sam)).setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		Notification note = mBuilder.build();
		note.defaults |= Notification.DEFAULT_SOUND;

		mNotificationManager.notify(id, note);

	}
}