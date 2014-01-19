package com.dunglv.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.caldroid.caldroidcustom.CaldroidFragment;
import com.caldroid.caldroidcustom.CaldroidListener;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends FragmentActivity {
	private static final String TAG = "CaldroidSampleActivity";
	private CaldroidFragment caldroidFragment;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();
		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
					CaldroidFragment.SUNDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}
		setUpCaldroidFragment();
		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();
		// initData();

	}

	private void setUpCaldroidFragment() {
		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Log.e(TAG, formatter.format(date));

			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Log.e(TAG, text);
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				Toast.makeText(getApplicationContext(),
						"Long click " + formatter.format(date),
						Toast.LENGTH_SHORT).show();
				Log.e(TAG, "Long click");
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					Log.e(TAG, "Caldroid view is created");
				}
			}

		};
		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
	}
}
