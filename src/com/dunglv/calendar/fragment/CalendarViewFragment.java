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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.caldroid.caldroidcustom.CaldroidFragment;
import com.caldroid.caldroidcustom.CaldroidGridAdapter;
import com.caldroid.caldroidcustom.CaldroidListener;
import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.CaldroidSampleCustomAdapter;
import com.dunglv.calendar.adapter.RotaDayAdapter;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;
import com.dunglv.calendar.entity.RotaDay;
import com.dunglv.calendar.util.Utils;

public class CalendarViewFragment extends CaldroidFragment {
	private CaldroidFragment caldroidFragment;
	private SimpleDateFormat formatter;
	View calendarTv;
	public DaoMaster daoMaster;
	public DaoSession daoSession;
	private SQLiteDatabase db;
	public RotaDao rotaDao;
	private List<Rota> listRota;
	private List<RotaDay> listRotaDay;
	private List<Rota> listRotaShow;
	ListView mListView;
	private RotaDayAdapter rotaDayAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initRotaDao();
		initData();
		listRota = new ArrayList<Rota>();
		listRota = rotaDao.loadAll();
		listRotaDay = new ArrayList<RotaDay>();
		for (Rota rota : listRota) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(rota.getDateStarted());
			int repeatDay;
			int time = 0;
			if (!rota.getTimeRepeat().isEmpty()) {
				time = Integer.parseInt(rota.getTimeRepeat());
			}
			if (time == 0) {
				repeatDay = rota.getWeekReapeat() * 7;
			} else {
				repeatDay = rota.getWeekReapeat() * 7 * time;
			}
			for (int i = 0; i < repeatDay; i++) {
				RotaDay rotaDay = new RotaDay();
				rotaDay.setDay(calendar.get(Calendar.DAY_OF_MONTH));
				rotaDay.setMonth(calendar.get(Calendar.MONTH) + 1);
				rotaDay.setYear(calendar.get(Calendar.YEAR));
				rotaDay.setDateTime(calendar.getTime());
				rotaDay.setColor(rota.getColor());
				rotaDay.setRota(rota);
				listRotaDay.add(rotaDay);
				calendar.add(Calendar.DATE, 1);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		setUpCaldroidFragment();
		mListView = (ListView) v.findViewById(R.id.listRotaDate);
		return v;
	}

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		CaldroidSampleCustomAdapter adapter = new CaldroidSampleCustomAdapter(
				getActivity(), month, year, getCaldroidData(), extraData,
				listRotaDay);
		return adapter;
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
		formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
		setCaldroidListener(listener);
	}

	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@Override
		public void onSelectDate(Date date, View view) {
			Log.e(TAG, formatter.format(date));
			listRotaShow = new ArrayList<Rota>();
			for (RotaDay rotaDay : listRotaDay) {
				if (Utils.isSameDay(rotaDay.getDateTime(), date)) {
					listRotaShow.add(rotaDay.getRota());
				}
			}
			setSelectedDates(date, date);
			refreshView();
			rotaDayAdapter = new RotaDayAdapter(getActivity(), listRotaShow);
			mListView.setAdapter(rotaDayAdapter);

		}

		@Override
		public void onChangeMonth(int month, int year) {
			String text = "month: " + month + " year: " + year;
			Log.e(TAG, text);
		}

		@Override
		public void onLongClickDate(Date date, View view) {
			// Toast.makeText(getActivity(),
			// "Long click " + formatter.format(date), Toast.LENGTH_SHORT)
			// .show();
		}

		@Override
		public void onCaldroidViewCreated() {
			if (getLeftArrowButton() != null) {
				Log.e(TAG, "Caldroid view is created");
			}
		}
	};
}
