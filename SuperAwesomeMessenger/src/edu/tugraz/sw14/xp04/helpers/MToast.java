package edu.tugraz.sw14.xp04.helpers;

import edu.tugraz.sw14.xp04.R;
import android.content.Context;
import android.widget.Toast;

public class MToast {

	public static void error(Context context, boolean isShort) {
		Toast.makeText(context, context.getString(R.string.error),
				isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
	}
	public static void errorLogin(Context context, boolean isShort) {
		Toast.makeText(context, context.getString(R.string.a_login_error_login),
				isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
	}
	public static void errorLoginEmail(Context context, boolean isShort) {
		Toast.makeText(context, context.getString(R.string.a_login_error_login_email),
				isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
	}
	public static void errorLoginPassword(Context context, boolean isShort) {
		Toast.makeText(context, context.getString(R.string.a_login_error_login_password),
				isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
	}
}
