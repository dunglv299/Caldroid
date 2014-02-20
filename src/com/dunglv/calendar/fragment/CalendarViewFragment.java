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
	// View calendarTv;
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
		setUpCaldroidFragment();
		initRotaDao();
		listRota = new ArrayList<Rota>();
		listRota = rotaDao.loadAll();
		listRotaDay = new ArrayList<RotaDay>();
		for (Rota rota : listRota) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(rota.getDateStarted());
			int repeatDay = rota.getWeekReapeat() * 7;
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
		this.setCaldroidListener(listener);
	}

	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onSelectDate(Date date, View view) {
			Log.e(TAG, formatter.format(date));
			listRotaShow = new ArrayList<Rota>();
			for (RotaDay rotaDay : listRotaDay) {
				if (Utils.isSameDay(rotaDay.getDateTime(), date)) {
					listRotaShow.add(rotaDay.getRota());
				}
			}
			rotaDayAdapter = new RotaDayAdapter(getActivity(), listRotaShow);
			mListView.setAdapter(rotaDayAdapter);
			// if (calendarTv != null) {
			// calendarTv.setBackgroundResource(R.color.caldroid_white);
			// }
			// calendarTv = (View) view.findViewById(R.id.calendar_tv);
			// calendarTv.setBackgroundResource(R.drawable.today_bg);
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
