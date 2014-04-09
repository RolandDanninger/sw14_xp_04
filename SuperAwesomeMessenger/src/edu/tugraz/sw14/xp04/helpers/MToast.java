package edu.tugraz.sw14.xp04.helpers;

import edu.tugraz.sw14.xp04.R;
import android.content.Context;
import android.widget.Toast;

public class MToast {

	public static void errorLogin(Context context, boolean isShort) {
		Toast.makeText(context, context.getString(R.string.error_login),
				isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
	}

}
