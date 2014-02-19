package com.dunglv.calendar.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.caldroid.caldroidcustom.CaldroidFragment;
import com.caldroid.caldroidcustom.CaldroidGridAdapter;
import com.caldroid.caldroidcustom.CaldroidListener;
import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.CaldroidSampleCustomAdapter;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;

public class CalendarViewFragment extends CaldroidFragment {
	private CaldroidFragment caldroidFragment;
	private SimpleDateFormat formatter;
	View calendarTv;
	public DaoMaster daoMaster;
	public DaoSession daoSession;
	private SQLiteDatabase db;
	public RotaDao rotaDao;
	private List<Rota> listRota;
	CaldroidSampleCustomAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpCaldroidFragment();
		initRotaDao();
		listRota = new ArrayList<Rota>();
		listRota = rotaDao.loadAll();
	}

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		adapter = new CaldroidSampleCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData, listRota);
		return adapter;
	}

	/**
	 * Refresh calendarview
	 */
	public void refresh() {
		daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
		listRota = rotaDao.loadAll();
		adapter.refresh(listRota);
		refreshView();
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, 2);
//		Date fromDate = cal.getTime();
//		// To Date
//		cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, 3);
//		Date toDate = cal.getTime();
//		setSelectedDates(fromDate, toDate);
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

	public void initRotaDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"rota-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
	}

	private void setUpCaldroidFragment() {
		caldroidFragment = new CaldroidFragment();
		formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
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
