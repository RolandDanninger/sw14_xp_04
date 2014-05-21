package edu.tugraz.sw14.xp04.helpers;

import java.util.Calendar;

import android.text.format.DateFormat;

public class DateTime {

	public static String dateFromStamp(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		String date = DateFormat.format("dd.MM.yyyy", cal).toString();
		return date;
	}

	public static String timeFromStamp(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		String date = DateFormat.format("hh:mm", cal).toString();
		return date;
	}

	public static boolean isToday(long time) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(time);
		String date_time = DateFormat.format("dd.MM.yyyy", cal1).toString();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		String date_now = DateFormat.format("dd.MM.yyyy", cal).toString();

		return date_time.equals(date_now);
	}
}
