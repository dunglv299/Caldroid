package com.dunglv.calendar.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

public class Utils {
	public static final String WEEK_REPEAT = "week";
	public static final String CURRENT_WEEK = "current_week";
	public static final String ROTA_ID = "rotaId";

	public static int convertStringToInt(String s) {
		if (s != null && !s.equals("")) {
			return Integer.parseInt(s);
		}
		return 0;
	}

	public static double convertStringToDouble(String s) {
		if (s != null && !s.equals("")) {
			return Double.parseDouble(s);
		}
		return 0;
	}

	public static String convertLongToTime(long time) {
		Date dateObj = new Date(time);
		SimpleDateFormat sdfAM = new SimpleDateFormat("h:mm a", Locale.US);
		return sdfAM.format(dateObj);
	}

	public static void showAlert(Context context, String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(context);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d("alert", "Showing alert dialog: " + message);
		bld.create().show();
	}

	public static void checkEmptyEditText(EditText editText, int number) {
		if (number == 0) {
			editText.setText("");
		} else {
			editText.setText(number + "");
		}
	}

	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}

	public static String convertLongToTime(String template, long time) {
		return new SimpleDateFormat(template, Locale.US).format(new Date(time));
	}
}
