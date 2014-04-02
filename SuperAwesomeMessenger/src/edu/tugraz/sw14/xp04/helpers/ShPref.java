package edu.tugraz.sw14.xp04.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class ShPref {

	public static final int ERROR_INT = -1;
	public static final long ERROR_LONG = -1;
	public static final float ERROR_FLOAT = -1;

	public static final String REG_ID = "regid";

	// Methods
	public static boolean removeShPref(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.edit().remove(key).commit();
	}

	public static boolean setShPrefString(Context context, String key,
			String value) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.edit().putString(key, value).commit();
	}

	public static boolean setShPrefInt(Context context, String key, int value) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.edit().putInt(key, value).commit();
	}

	public static boolean setShPrefLong(Context context, String key, long value) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.edit().putLong(key, value).commit();
	}

	public static boolean setShPrefFloat(Context context, String key,
			float value) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.edit().putFloat(key, value).commit();
	}

	public static boolean setShPrefBoolean(Context context, String key,
			boolean value) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.edit().putBoolean(key, value).commit();
	}

	public static String getShPrefString(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return null;
		return prefs.getString(key, null);
	}

	public static int getShPrefInt(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return ERROR_INT;
		return prefs.getInt(key, ERROR_INT);
	}

	public static long getShPrefLong(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return ERROR_LONG;
		return prefs.getLong(key, ERROR_LONG);
	}

	public static float getShPrefFloat(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return ERROR_FLOAT;
		return prefs.getFloat(key, ERROR_FLOAT);
	}

	public static boolean getShPrefBoolean(Context context, String key) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.getBoolean(key, false);
	}

	public static boolean getShPrefBoolean(Context context, String key,
			boolean default_result) {
		SharedPreferences prefs = context.getSharedPreferences(
				context.getPackageName(), Context.MODE_PRIVATE);
		if (prefs == null)
			return false;
		return prefs.getBoolean(key, default_result);
	}

}
