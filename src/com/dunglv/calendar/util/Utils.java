package com.dunglv.calendar.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

	public static String convertStringToTime(String s) {
		long time = Long.parseLong(s);
		Date dateObj = new Date(time);
		SimpleDateFormat sdfAM = new SimpleDateFormat("h:mm a", Locale.US);
		return sdfAM.format(dateObj);
	}
}
