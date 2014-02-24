package com.dunglv.calendar.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dunglv.calendar.R;
import com.dunglv.calendar.activity.AddRotaNextActivity;
import com.dunglv.calendar.adapter.AlarmReceiver;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;
import com.dunglv.calendar.dao.WeekTime;
import com.dunglv.calendar.dao.WeekTimeDao;
import com.dunglv.calendar.dao.WeekTimeDao.Properties;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public class AddRotaNextFragment extends BaseFragment implements
		OnClickListener {
	private static final String TAG = "AddRotaNextFragment";
	private static final int LENGTH = 7;
	public static final String TIME_ZERO = "0000000000000";
	private int weekCount;
	private int currentWeek = 1;
	private MySharedPreferences sharedPreferences;
	Button btnNext;
	Button btnCopyNext;
	Button btnMakeAll;
	TextView tvWeekNumber;
	private boolean isNextPress;
	private static final int[] weekDayIdArray = { R.id.weekday1, R.id.weekday2,
			R.id.weekday3, R.id.weekday4, R.id.weekday5, R.id.weekday6,
			R.id.weekday7 };
	private static final int[] startIdArray = { R.id.startDate_btn1,
			R.id.startDate_btn2, R.id.startDate_btn3, R.id.startDate_btn4,
			R.id.startDate_btn5, R.id.startDate_btn6, R.id.startDate_btn7 };

	private static final int[] endIdArray = { R.id.endDate_btn1,
			R.id.endDate_btn2, R.id.endDate_btn3, R.id.endDate_btn4,
			R.id.endDate_btn5, R.id.endDate_btn6, R.id.endDate_btn7 };

	private static final int[] hourIdArray = { R.id.hour_edittext,
			R.id.hour_edittext2, R.id.hour_edittext3, R.id.hour_edittext4,
			R.id.hour_edittext5, R.id.hour_edittext6, R.id.hour_edittext7 };
	private TextView[] weekDayTv = new TextView[LENGTH];
	private Button[] startBtn = new Button[LENGTH];
	private Button[] endBtn = new Button[LENGTH];
	private EditText[] hourEditText = new EditText[LENGTH];

	private long[] startTime = new long[LENGTH];
	private long[] endTime = new long[LENGTH];
	private int[] hourWorking = new int[LENGTH];
	private String[] timeArray = new String[LENGTH];

	private WeekTimeDao weekTimeDao;
	private RotaDao rotaDao;
	private List<WeekTime> listWeekTimes;
	private WeekTime weekTime;
	Button saveBtn;
	RelativeLayout btnLayout;
	private long weekTimeId;
	private int startDayOfWeek;
	private List<String> listDayOfWeek;
	private Rota rota;
	private Uri eventsUri;
	private Cursor cursor;
	int calendarId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initRotaDao();
		initWeekTimeDao();
		listDayOfWeek = getListWeekDay(startDayOfWeek);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_rota_next, container,
				false);
		initView(v);
		Log.e(TAG, "onCreateView");
		return v;
	}

	private void initView(View v) {
		btnNext = (Button) v.findViewById(R.id.next_btn);
		btnCopyNext = (Button) v.findViewById(R.id.copy_to_next);
		btnMakeAll = (Button) v.findViewById(R.id.make_all_week_btn);
		saveBtn = (Button) v.findViewById(R.id.save_btn);
		btnLayout = (RelativeLayout) v.findViewById(R.id.button_layout);
		btnNext.setOnClickListener(this);
		btnCopyNext.setOnClickListener(this);
		btnMakeAll.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		tvWeekNumber = (TextView) v.findViewById(R.id.week_number);

		for (int i = 0; i < LENGTH; i++) {
			final int index = i;
			// Init weekDay TextView
			weekDayTv[index] = (TextView) v.findViewById(weekDayIdArray[index]);
			weekDayTv[i].setText(listDayOfWeek.get(i));

			// Action pick start time
			startBtn[index] = (Button) v.findViewById(startIdArray[index]);
			startBtn[index].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showTimePicker(startBtn[index], true, index);
				}
			});
			// Action pick end time
			endBtn[index] = (Button) v.findViewById(endIdArray[index]);
			endBtn[index].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showTimePicker(endBtn[index], false, index);
				}
			});
			// Init editText and get data
			hourEditText[i] = (EditText) v.findViewById(hourIdArray[i]);
		}
	}

	private void initData() {
		listWeekTimes = getListWeekTimeDao();
		if (listWeekTimes.size() == 0) {
			return;
		}
		// Init when exists data
		weekTime = listWeekTimes.get(0);
		timeArray[listDayOfWeek.indexOf("MON")] = weekTime.getMonday();
		timeArray[listDayOfWeek.indexOf("TUE")] = weekTime.getTuesday();
		timeArray[listDayOfWeek.indexOf("WED")] = weekTime.getWednesday();
		timeArray[listDayOfWeek.indexOf("THU")] = weekTime.getThursday();
		timeArray[listDayOfWeek.indexOf("FRI")] = weekTime.getFriday();
		timeArray[listDayOfWeek.indexOf("SAT")] = weekTime.getSaturday();
		timeArray[listDayOfWeek.indexOf("SUN")] = weekTime.getSunday();
		weekTimeId = weekTime.getId();
		for (int i = 0; i < LENGTH; i++) {
			String s1 = timeArray[i].substring(0, 13);
			String s2 = timeArray[i].substring(13, 26);
			startTime[i] = Long.parseLong(s1);
			endTime[i] = Long.parseLong(s2);
			setTextButton(startBtn[i], s1);
			setTextButton(endBtn[i], s2);
			String s3 = timeArray[i].substring(26);
			if (s3.equals("0")) {
				hourEditText[i].setText("");
			} else {
				hourEditText[i].setText(s3);
			}

		}
	}

	private void setTextButton(Button button, String s) {
		if (s.equals(TIME_ZERO)) {
			button.setText("");
		} else {
			long time = Long.valueOf(s);
			button.setText(Utils.convertStringToTime(s));
		}
	}

	/**
	 * Show time picker dialog
	 * 
	 * @param button
	 * @param index
	 * @param isStartTime
	 */
	private void showTimePicker(final Button button, final boolean isStartTime,
			final int index) {
		Calendar c = Calendar.getInstance();
		final TimePickerDialog dateDialog = new TimePickerDialog(getActivity(),
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						Calendar mCalendar = Calendar.getInstance();
						mCalendar.add(Calendar.DAY_OF_MONTH, index
								+ (currentWeek - 1) * 7);
						mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						mCalendar.set(Calendar.MINUTE, minute);
						button.setText(DateFormat.format("h:mm a", mCalendar));
						if (isStartTime) {
							startTime[index] = mCalendar.getTimeInMillis();
						} else {
							endTime[index] = mCalendar.getTimeInMillis();
						}
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		dateDialog.show();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");
		sharedPreferences = new MySharedPreferences(getActivity());
		isNextPress = false;
		weekCount = sharedPreferences.getInt(Utils.WEEK_REPEAT);
		currentWeek = sharedPreferences.getInt(Utils.CURRENT_WEEK);
		if (currentWeek == weekCount) {
			btnLayout.setVisibility(View.GONE);
			saveBtn.setVisibility(View.VISIBLE);
		}
		tvWeekNumber.setText("WEEK " + currentWeek);
		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			onNextPress();
			onRemind();
			if (rota.getIsGoogleSync()) {
				for (int i = 0; i < LENGTH; i++) {
					addEvent(startTime[i], endTime[i]);
				}
			}
			break;
		case R.id.copy_to_next:
			copyToNext();
			break;
		case R.id.make_all_week_btn:
			new MakeAllWeekAsyntask().execute();
			break;
		case R.id.save_btn:
			Intent returnIntent = new Intent();
			getActivity().setResult(Activity.RESULT_OK, returnIntent);
			onNextPress();
			break;
		default:
			break;
		}
	}

	/**
	 * back action
	 */
	private void onBackPress() {
		if (!isNextPress) {
			currentWeek--;
			sharedPreferences.putInt(Utils.CURRENT_WEEK, currentWeek);
		}
	}

	/**
	 * Press next action
	 */
	private void onNextPress() {
		Log.e("currentWeek: " + currentWeek, "" + weekCount);

		// Get weektime ID for replace
		listWeekTimes = getListWeekTimeDao();
		if (listWeekTimes.size() > 0) {
			weekTime = listWeekTimes.get(0);
			weekTimeId = weekTime.getId();// End get weekTime id
		}
		// Collect data
		weekTime = collectData();
		// Week time id = 0 mean record don't exits DB and insert new record
		// If record is exists. Replaced
		weekTime.setId(weekTimeId);
		if (weekTimeId == 0) {
			weekTime.setId(null);
		}
		weekTimeDao.insertOrReplace(weekTime);
		currentWeek++;
		sharedPreferences.putInt(Utils.CURRENT_WEEK, currentWeek);
		if (currentWeek > weekCount) {
			getActivity().finish();
			return;
		}
		replaceFragment(R.id.frame_add_next, new AddRotaNextFragment(), true);
		isNextPress = true;
	}

	/**
	 * Copy to next action
	 */
	private void copyToNext() {
		onNextPress();
		List<WeekTime> listNext = getListWeekTimeDao();
		if (currentWeek <= weekCount) {
			weekTime = collectData();
			if (listNext.size() > 0) {
				WeekTime nextWeekTime = listNext.get(0);
				weekTime.setId(nextWeekTime.getId());
			}
			weekTime.setWeekId(currentWeek);
			weekTimeDao.insertOrReplace(weekTime);
		}
	}

	private List<WeekTime> getListWeekTimeDao() {
		List<WeekTime> listWeekTimes = weekTimeDao
				.queryBuilder()
				.where(Properties.WeekId.eq(currentWeek),
						Properties.RotaId
								.eq(((AddRotaNextActivity) getActivity())
										.getRotaId())).list();
		return listWeekTimes;
	}

	/**
	 * make all week action
	 */
	private void onMakeAllWeek() {
		List<WeekTime> listDelete = weekTimeDao
				.queryBuilder()
				.where(Properties.WeekId.between(currentWeek, weekCount),
						Properties.RotaId
								.eq(((AddRotaNextActivity) getActivity())
										.getRotaId())).list();
		weekTimeDao.deleteInTx(listDelete);
		for (int i = currentWeek; i <= weekCount; i++) {
			weekTime = collectData();
			weekTime.setWeekId(i);
			weekTimeDao.insert(weekTime);
			isNextPress = true;

			if (i == weekCount) {
				sharedPreferences.putInt(Utils.CURRENT_WEEK, i);
			}
			if (i > currentWeek) {
				replaceFragment(R.id.frame_add_next, new AddRotaNextFragment(),
						true);
			}
		}
	}

	private class MakeAllWeekAsyntask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;

		public MakeAllWeekAsyntask() {
			dialog = new ProgressDialog(getActivity());
		}

		protected void onPreExecute() {
			this.dialog.setMessage("Please wait...");
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			onMakeAllWeek();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		onBackPress();
	}

	public void initRotaDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"rota-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
		rota = rotaDao
				.queryBuilder()
				.where(com.dunglv.calendar.dao.RotaDao.Properties.Id
						.eq(((AddRotaNextActivity) getActivity()).getRotaId()))
				.list().get(0);
		startDayOfWeek = rota.getStartDayOfWeek();
	}

	public void initWeekTimeDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"weekTime-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		weekTimeDao = daoSession.getWeekTimeDao();
	}

	/**
	 * Parse time to save to DB
	 */
	private void parseTime() {

		for (int i = 0; i < LENGTH; i++) {
			hourWorking[i] = Utils.convertStringToInt(hourEditText[i].getText()
					.toString());
			if (startTime[i] == 0 && endTime[i] == 0) {
				timeArray[i] = TIME_ZERO + TIME_ZERO;
			} else if (startTime[i] == 0) {
				timeArray[i] = TIME_ZERO + String.valueOf(endTime[i]);
			} else if (endTime[i] == 0) {
				timeArray[i] = String.valueOf(startTime[i]) + TIME_ZERO;
			} else {
				timeArray[i] = String.valueOf(startTime[i])
						+ String.valueOf(endTime[i]);
			}
			timeArray[i] += String.valueOf(hourWorking[i]);

		}
	}

	private WeekTime collectData() {
		parseTime();
		weekTime = new WeekTime();
		weekTime.setWeekId(currentWeek);
		weekTime.setRotaId(((AddRotaNextActivity) getActivity()).getRotaId());
		// TODO thay bang index of monday
		weekTime.setMonday(timeArray[listDayOfWeek.indexOf("MON")]);
		weekTime.setTuesday(timeArray[listDayOfWeek.indexOf("TUE")]);
		weekTime.setWednesday(timeArray[listDayOfWeek.indexOf("WED")]);
		weekTime.setThursday(timeArray[listDayOfWeek.indexOf("THU")]);
		weekTime.setFriday(timeArray[listDayOfWeek.indexOf("FRI")]);
		weekTime.setSaturday(timeArray[listDayOfWeek.indexOf("SAT")]);
		weekTime.setSunday(timeArray[listDayOfWeek.indexOf("SUN")]);
		return weekTime;
	}

	private long plusDay(long time, int index) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.add(Calendar.DAY_OF_MONTH, index + (currentWeek - 1) * 7);
		return cal.getTimeInMillis();
	}

	public void onRemind() {
		// Notification
		if (rota.getReminderTime() > 0) {
			for (int i = 0; i < LENGTH; i++) {
				if (startTime[i] != 0 && weekTime != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(startTime[i]);
					cal.add(Calendar.MINUTE, -1 * rota.getReminderTime());
					alarm(cal.getTimeInMillis(), (int) startTime[i]);
				}
			}
		}
	}

	private List<String> getListWeekDay(int startDayOfWeek) {
		List<String> list = new ArrayList<String>();
		SimpleDateFormat fmt = new SimpleDateFormat("EEE", Locale.getDefault());
		// 17 Feb 2013 is Sunday
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 2, 17);
		cal.add(Calendar.DAY_OF_WEEK, startDayOfWeek - Calendar.SUNDAY);
		for (int i = 0; i < LENGTH; i++) {
			Date date = cal.getTime();
			list.add(fmt.format(date).toUpperCase());
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
		return list;
	}

	private void alarm(long time, int id) {
		Log.e("startAlarm", "startAlarm");
		Intent intent = new Intent(getActivity(), AlarmReceiver.class);
		intent.putExtra("alarm_message", rota.getName());
		// In reality, you would want to have a static variable for the request
		// code instead of 192837
		PendingIntent sender = PendingIntent.getBroadcast(getActivity(), id,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) getActivity().getSystemService(
				Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, time, sender);

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void addEvent(long startTime, long endTime) {
		if (android.os.Build.VERSION.SDK_INT <= 7) {
			eventsUri = Uri.parse("content://calendar/events");
			cursor = getActivity().getContentResolver().query(
					Uri.parse("content://calendar/calendars"),
					new String[] { "_id", "displayName" }, null, null, null);

		}

		else if (android.os.Build.VERSION.SDK_INT <= 14) {
			eventsUri = Uri.parse("content://com.android.calendar/events");
			cursor = getActivity().getContentResolver().query(
					Uri.parse("content://com.android.calendar/calendars"),
					new String[] { "_id", "displayName" }, null, null, null);

		}

		else {
			eventsUri = Uri.parse("content://com.android.calendar/events");
			cursor = getActivity().getContentResolver().query(
					Uri.parse("content://com.android.calendar/calendars"),
					new String[] { "_id", "calendar_displayName" }, null, null,
					null);

		}

		if (cursor.moveToFirst()) {
			do {
				int calId = cursor.getInt(0);
				String calName = cursor.getString(1);
				if (calName.contains("@gmail.com")) {
					calendarId = calId;
					break;
				}
				// do what ever you want here
			} while (cursor.moveToNext());
		}
		TimeZone timeZone = TimeZone.getDefault();
		ContentValues event = new ContentValues();
		event.put(CalendarContract.Events.CALENDAR_ID, calendarId);
		event.put(CalendarContract.Events.TITLE, rota.getName());
		event.put(CalendarContract.Events.DESCRIPTION, "");
		event.put(CalendarContract.Events.EVENT_LOCATION, "");
		event.put(CalendarContract.Events.DTSTART, startTime);
		event.put(CalendarContract.Events.DTEND, endTime);
		event.put(CalendarContract.Events.STATUS, 1);
		event.put(CalendarContract.Events.HAS_ALARM, 1);
		event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		// To Insert
		getActivity().getContentResolver().insert(eventsUri, event);

	}
}
