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
import com.dunglv.calendar.dao.WeekTime;
import com.dunglv.calendar.dao.WeekTimeDao;
import com.dunglv.calendar.dao.WeekTimeDao.Properties;
import com.dunglv.calendar.entity.RotaDay;
import com.dunglv.calendar.util.Utils;

public class CalendarViewFragment extends CaldroidFragment {
	private SimpleDateFormat formatter;
	View calendarTv;
	public RotaDao rotaDao;
	public WeekTimeDao weekTimeDao;
	private List<Rota> listRota;
	private List<RotaDay> listRotaDay;
	private List<Rota> listRotaShow;
	private List<String> listDetailDay;
	ListView mListView;
	private RotaDayAdapter rotaDayAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpCaldroidFragment();
		initRotaDao();
		initWeekTimeDao();
		listRota = new ArrayList<Rota>();
		listRota = rotaDao.loadAll();
		listRotaDay = new ArrayList<RotaDay>();
		for (Rota rota : listRota) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(rota.getDateStarted());
			int repeatDay;
			int time = 0;
			if (!rota.getTimeRepeat().isEmpty()
					&& !rota.getTimeRepeat().equals("0")) {
				time = Integer.parseInt(rota.getTimeRepeat());
			} else {
				time = 1;
			}
			repeatDay = rota.getWeekReapeat() * 7 * time;
			for (int i = 0; i < repeatDay; i++) {
				RotaDay rotaDay = new RotaDay();
				rotaDay.setDay(calendar.get(Calendar.DAY_OF_MONTH));
				rotaDay.setMonth(calendar.get(Calendar.MONTH) + 1);
				rotaDay.setYear(calendar.get(Calendar.YEAR));
				rotaDay.setDateTime(calendar.getTime());
				rotaDay.setColor(rota.getColor());
				rotaDay.setRota(rota);
				rotaDay.setTimeRepeat(time);
				int weekId = (int) (i / 7) + 1;
				if (weekId % time == 0) {
					weekId = time;
				} else {
					weekId = weekId % time;
				}
				rotaDay.setWeekId(weekId);
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

	public void initRotaDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"rota-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
	}

	public void initWeekTimeDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"weekTime-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		weekTimeDao = daoSession.getWeekTimeDao();
	}

	private void setUpCaldroidFragment() {
		formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
		setCaldroidListener(listener);
		startDayOfWeek = Calendar.MONDAY;
	}

	// Setup listener
	final CaldroidListener listener = new CaldroidListener() {

		@Override
		public void onSelectDate(Date date, View view) {
			Log.e(TAG, formatter.format(date));
			listRotaShow = new ArrayList<Rota>();
			listDetailDay = new ArrayList<String>();
			for (RotaDay rotaDay : listRotaDay) {
				if (Utils.isSameDay(rotaDay.getDateTime(), date)) {
					// Add rota to listRotaShow
					Rota rota = rotaDay.getRota();
					listRotaShow.add(rota);

					// Add detail time to list
					String detailTime = getDetailTime(date, rota.getId(),
							rotaDay.getWeekId());
					listDetailDay.add(detailTime);
				}
			}
			rotaDayAdapter = new RotaDayAdapter(getActivity(), listRotaShow,
					listDetailDay);
			mListView.setAdapter(rotaDayAdapter);
			setSelectedDates(date, date);
			refreshView();
		}

		@Override
		public void onChangeMonth(int month, int year) {
			String text = "month: " + month + " year: " + year;
			Log.e(TAG, text);
		}

		@Override
		public void onLongClickDate(Date date, View view) {
		}

		@Override
		public void onCaldroidViewCreated() {
			if (getLeftArrowButton() != null) {
				Log.e(TAG, "Caldroid view is created");
			}
		}
	};

	private String getDetailTime(Date date, long rotaId, int weekId) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		String detailTime = null;
		WeekTime weekTime = getWeekTimeDao(rotaId, weekId);
		if (weekTime != null) {
			if (Calendar.MONDAY == dayOfWeek) {
				detailTime = weekTime.getMonday();
			} else if (Calendar.TUESDAY == dayOfWeek) {
				detailTime = weekTime.getTuesday();
			} else if (Calendar.WEDNESDAY == dayOfWeek) {
				detailTime = weekTime.getWednesday();
			} else if (Calendar.THURSDAY == dayOfWeek) {
				detailTime = weekTime.getThursday();
			} else if (Calendar.FRIDAY == dayOfWeek) {
				detailTime = weekTime.getFriday();
			} else if (Calendar.SATURDAY == dayOfWeek) {
				detailTime = weekTime.getSaturday();
			} else if (Calendar.SUNDAY == dayOfWeek) {
				detailTime = weekTime.getSunday();
			}
		}
		return detailTime;
	}

	private WeekTime getWeekTimeDao(long rotaId, int weekId) {
		List<WeekTime> listWeekTime = weekTimeDao
				.queryBuilder()
				.where(Properties.WeekId.eq(weekId),
						Properties.RotaId.eq(rotaId)).list();
		if (listWeekTime != null && listWeekTime.size() > 0) {
			return listWeekTime.get(0);
		}
		return null;
	}
}
