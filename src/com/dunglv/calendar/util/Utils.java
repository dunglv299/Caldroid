package com.dunglv.calendar.util;

public class Utils {
	public static final String WEEK_REPEAT = "week";
	public static final String CURRENT_WEEK = "current_week";

	public static int convertStringToInt(String s) {
		if (s != null && !s.equals("")) {
			return Integer.parseInt(s);
		}
		return 0;
	}
}
