package edu.tugraz.sw14.xp04.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.tugraz.sw14.xp04.ActivityMsg;
import edu.tugraz.sw14.xp04.ActivitySendTestMessage;
import edu.tugraz.sw14.xp04.R;
import edu.tugraz.sw14.xp04.database.Database;
import edu.tugraz.sw14.xp04.helpers.DateTime;
import edu.tugraz.sw14.xp04.msg.Msg;

public class GcmIntentService extends IntentService {

	public static final String TAG = "GcmIntentService";

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
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
				sendNotification(extras);
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void storeMessageInDatabase(Bundle extras) {
		String sender = extras.getString("sender");
		String msg = extras.getString("message");
		long timeStamp = Long.valueOf(extras.getString("timestamp"));

		if (sender == null || sender == "")
			Log.d(TAG, "sender email was empty or null");

		Msg toStore = new Msg(sender, msg, timeStamp, false, false);
		Log.d(TAG, "time in ms: " + timeStamp);
		Log.d(TAG, "time sent: " + DateTime.dateFromStamp(timeStamp) + " "
				+ DateTime.timeFromStamp(timeStamp));

		Database db = new Database(this);
		boolean result = db.insertMsg(toStore.toContentValues());
		if (result == false)
			Log.d(TAG, "failed to store message");

	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle extras) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		String sender = extras.getString("sender");
		String msg = extras.getString("message");
		long timeStamp = Long.valueOf(extras.getString("timestamp"));

		if (sender == null || sender == "")
			Log.d(TAG, "sender email was empty or null");

		Database db = new Database(this);
		int id = db.getContactId(sender);
		Log.d(TAG, "sender id is: " + id);

		Intent intent = new Intent(this, ActivityMsg.class);
		intent.putExtra(ActivityMsg.EXTRA_EMAIL, sender);

		PendingIntent contentIntent = PendingIntent.getActivity(this, id,
				intent, 0);
		Notification.Builder mBuilder = new Notification.Builder(this)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.logo_sam)
				.setWhen(timeStamp)
				.setContentTitle(sender)
				// .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.logo_sam)).setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(id, mBuilder.build());

	}
}