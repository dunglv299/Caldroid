package com.dunglv.calendar.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.caldroid.caldroidcustom.CaldroidFragment;
import com.caldroid.caldroidcustom.CaldroidListener;
import com.dunglv.calendar.R;

public class CalendarViewFragment extends CaldroidFragment {
	private CaldroidFragment caldroidFragment;
	private SimpleDateFormat formatter;
	View calendarTv;

	public CalendarViewFragment() {
		setUpCaldroidFragment();
	}

	private void initData() {
		Calendar cal = Calendar.getInstance();
		// Min date is last 7 days
		cal.add(Calendar.DATE, -17);
		Date blueDate = cal.getTime();

		// Max date is next 7 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					blueDate);
			caldroidFragment.setBackgroundResourceForDate(R.color.green,
					greenDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}
	}

	private void setUpCaldroidFragment() {
		caldroidFragment = new CaldroidFragment();
		formatter = new SimpleDateFormat("dd MMM yyyy");
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
		args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
		args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY); // Tuesday
		caldroidFragment.setArguments(args);
		caldroidFragment.setCaldroidListener(listener);
	}

	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@Override
		public void onSelectDate(Date date, View view) {
			Log.e(TAG, formatter.format(date));
			if (calendarTv != null) {
				calendarTv.setBackgroundResource(R.color.caldroid_white);
			}
			calendarTv = (View) view.findViewById(R.id.calendar_tv);
			calendarTv.setBackgroundResource(R.drawable.today_bg);
		}

		@Override
		public void onChangeMonth(int month, int year) {
			String text = "month: " + month + " year: " + year;
			Log.e(TAG, text);
		}

		@Override
		public void onLongClickDate(Date date, View view) {
			Toast.makeText(getActivity(),
					"Long click " + formatter.format(date), Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onCaldroidViewCreated() {
			if (caldroidFragment.getLeftArrowButton() != null) {
				Log.e(TAG, "Caldroid view is created");
			}
		}
	};
}
