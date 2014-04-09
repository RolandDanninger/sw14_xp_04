package edu.tugraz.sw14.xp04.gcm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;
import android.util.Log;

public final class GCM {

	public static final String TAG = "gcm";

	public static final String PROJECT_NUMBER = "40743753914";
	private static String fileName = "sfaf";

	public static void storeIdPair(Context context, IdPair idPair) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);

			ObjectOutputStream os = new ObjectOutputStream(fos);

			os.writeObject(idPair);
			os.close();
		} catch (Exception e) {
			Log.d(TAG, "store idPaire failed");
		}
	}

	public static IdPair loadIdPair(Context context) {

		IdPair result = null;
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			result = (IdPair) is.readObject();
			is.close();
		} catch (Exception e) {
			Log.d(TAG, "load idPaire failed");

		}
		return result;
	}
}
