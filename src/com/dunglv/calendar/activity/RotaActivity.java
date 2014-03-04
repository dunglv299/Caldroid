package com.dunglv.calendar.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.AlarmReceiver;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.DayTime;
import com.dunglv.calendar.dao.DayTimeDao;
import com.dunglv.calendar.dao.DayTimeDao.Properties;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public abstract class RotaActivity extends FragmentActivity implements
		OnClickListener, OnCheckedChangeListener {
	public static final int REQUES_CODE_FINISH = 100;
	private String colorRota;
	public Spinner colorSpinner;
	public Spinner reminderSpinner;
	public EditText mNameEd;
	public EditText mWeekEd;
	public EditText mRepeatTimeEd;
	public DaoMaster daoMaster;
	public DaoSession daoSession;
	private SQLiteDatabase db;
	public RotaDao rotaDao;
	public MySharedPreferences sharedPreferences;
	public Button deleteBtn, startDateBtn;
	public CheckBox mCheckBoxReminder, mCheckBoxGoogle;
	public static Integer[] reminderArray = { 1, 5, 10, 15, 20, 25, 30, 45, 60,
			120, 180, 12 * 60, 24 * 60 };
	public static String[] colorsArray = { "#f6cd6c", "#5b93cb", "#a864a8",
			"#7ab977", "#a67c52", "#f7941d", "#44d5ce", "#fa565c", "#8393ca",
			"#7d7d7d" };
	public int remindTime;
	public long startDate;
	public boolean isGoogleSync;
	public DayTimeDao dayTimeDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rota);
		initRotaDao();
		initView();
		init();
	}

	public void initView() {
		colorSpinner = (Spinner) findViewById(R.id.color_spinner);
		Button continueBtn = (Button) findViewById(R.id.continue_btn);
		continueBtn.setOnClickListener(this);
		Button saveBtn = (Button) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		deleteBtn = (Button) findViewById(R.id.delete_btn);
		deleteBtn.setOnClickListener(this);
		mNameEd = (EditText) findViewById(R.id.rota_name);
		mWeekEd = (EditText) findViewById(R.id.week_number);
		mRepeatTimeEd = (EditText) findViewById(R.id.time_number);
		mCheckBoxReminder = (CheckBox) findViewById(R.id.checkbox);
		mCheckBoxGoogle = (CheckBox) findViewById(R.id.google_checkbox);
		mCheckBoxGoogle.setOnCheckedChangeListener(this);
		reminderSpinner = (Spinner) findViewById(R.id.reminder_spinner);
		startDateBtn = (Button) findViewById(R.id.start_date_btn);
		startDateBtn.setOnClickListener(this);
	}

	public void init() {
		setCustomColorSpinner();
		reminderSpinnerListener();
		sharedPreferences = new MySharedPreferences(this);
		setSpinnerEnabled(reminderSpinner, false);
		startDateBtn.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US)
				.format(new Date()));

		Calendar mCalendar = Calendar.getInstance();
		startDate = mCalendar.getTimeInMillis();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_btn:
			onContinue();
			break;
		case R.id.save_btn:
			onSave();
			break;
		case R.id.start_date_btn:
			showDatePicker(startDateBtn);
			break;
		default:
			break;
		}
	}

	/**
	 * Show time picker dialog
	 */
	private void showDatePicker(final Button button) {
		Calendar c = Calendar.getInstance();
		final DatePickerDialog dateDialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Calendar mCalendar = Calendar.getInstance();
						mCalendar.set(Calendar.YEAR, year);
						mCalendar.set(Calendar.MONTH, monthOfYear);
						mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						mCalendar.set(Calendar.HOUR_OF_DAY, 0);
						mCalendar.set(Calendar.MINUTE, 0);
						button.setText(DateFormat.format("dd/MM/yyyy",
								mCalendar));
						startDate = mCalendar.getTimeInMillis();
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		dateDialog.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.checkbox:
			setSpinnerEnabled(reminderSpinner, isChecked);
			break;
		case R.id.google_checkbox:
			isGoogleSync = isChecked;
			break;
		default:
			break;
		}
	}

	private void setSpinnerEnabled(Spinner spinner, boolean enabled) {
		spinner.setEnabled(enabled);
		spinner.setAlpha(enabled ? 1.0f : 0.4f);
	}

	public void setCustomColorSpinner() {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, new String[] {
						"", "", "", "", "", "", "", "", "", "" }) {

			@Override
			public boolean areAllItemsEnabled() {
				return false;
			}

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					Context mContext = this.getContext();
					LayoutInflater vi = (LayoutInflater) mContext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.single_spinner_dropdown_item, null);
				}

				View colorView = v.findViewById(R.id.text_spinner);
				colorView.setBackgroundColor(Color
						.parseColor(colorsArray[position]));
				return v;
			}

		};
		colorSpinner.setAdapter(spinnerAdapter);
		colorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				colorRota = colorsArray[position];
				colorSpinner.setBackgroundColor(Color.parseColor(colorRota));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});
	}

	private void reminderSpinnerListener() {
		reminderSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				remindTime = reminderArray[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void initRotaDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "rota-db",
				null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
	}

	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (requestCode == REQUES_CODE_FINISH) {
	// if (resultCode == RESULT_OK) {
	// Log.e(TAG, "dunglv");
	// finish();
	// }
	// if (resultCode == RESULT_CANCELED) {
	// // Write your code if there's no result
	// }
	// }
	// }// onActivityResult

	/**
	 * Collect data for sava data to DB
	 */
	public Rota getRota() {
		Rota rota = new Rota();
		rota.setColor(colorRota);
		rota.setName(mNameEd.getText().toString());
		rota.setWeekReapeat(Utils.convertStringToInt(mWeekEd.getText()
				.toString()));
		rota.setTimeRepeat(mRepeatTimeEd.getText().toString());
		rota.setReminderTime(mCheckBoxReminder.isChecked() ? remindTime : 0);
		rota.setDateStarted(startDate);
		rota.setIsGoogleSync(isGoogleSync);
		return rota;
	}

	public void onContinue() {
		Intent intent = new Intent(this, AddRotaNextActivity.class);
		// Send data to Next Activity
		sharedPreferences.putInt(Utils.WEEK_REPEAT, getRota().getWeekReapeat());
		startActivityForResult(intent, REQUES_CODE_FINISH);
	}

	public void onSave() {
		initDayTimeDao();
		Rota rota = getRota();
		if (rota.getId() != null) {
			List<DayTime> listData = dayTimeDao
					.queryBuilder()
					.where(Properties.RotaId.eq(rota.getId()),
							Properties.StartTime.gt(0)).list();
			if (rota.getReminderTime() > 0) {
				onRemind(rota, listData);
			}
			if (rota.getIsGoogleSync()) {
				onGoogleSync(rota, listData);
			}
		} else {
			// Utils.showAlert(this,
			// "Please select day by press \"Save & Continue\"");
			// return;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sharedPreferences.putInt(Utils.CURRENT_WEEK, 0);
		sharedPreferences.putInt(Utils.ROTA_ID, 0);
	}

	public void initDayTimeDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dayTime-db",
				null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		dayTimeDao = daoSession.getDayTimeDao();
	}

	private void onGoogleSync(Rota rota, List<DayTime> listData) {
		int time = 0;
		if (!rota.getTimeRepeat().isEmpty()
				&& !rota.getTimeRepeat().equals("0")) {
			time = Integer.parseInt(rota.getTimeRepeat());
		} else {
			time = 1;
		}
		for (DayTime dayTime : listData) {
			if (dayTime.getIsSyncGoogle() == null || !dayTime.getIsSyncGoogle()) {
				for (int i = 0; i < time; i++) {
					Calendar calendarStart = Calendar.getInstance();
					Calendar calendarEnd = Calendar.getInstance();
					calendarStart.setTimeInMillis(dayTime.getStartTime());
					calendarEnd.setTimeInMillis(dayTime.getEndTime());
					calendarStart.add(Calendar.DAY_OF_YEAR,
							7 * i * rota.getWeekReapeat());
					calendarEnd.add(Calendar.DAY_OF_YEAR,
							7 * i * rota.getWeekReapeat());
					addEvent(calendarStart.getTimeInMillis(),
							calendarEnd.getTimeInMillis(), rota);
					dayTime.setIsSyncGoogle(true);
					dayTimeDao.update(dayTime);
				}

			}
		}
	}

	public void onRemind(Rota rota, List<DayTime> listRemind) {
		// Notification
		for (DayTime dayTime : listRemind) {
			if (dayTime.getStartTime() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(dayTime.getStartTime());
				cal.add(Calendar.MINUTE, -1 * rota.getReminderTime());
				if (cal.getTimeInMillis() > System.currentTimeMillis()) {
					alarm(cal.getTimeInMillis(), dayTime.getId().intValue(),
							rota);
				}
			}
		}
	}

	private void alarm(long time, int id, Rota rota) {
		Log.e("startAlarm", "startAlarm");
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("alarm_message", rota.getName());
		// In reality, you would want to have a static variable for the request
		// code instead of 192837
		PendingIntent sender = PendingIntent.getBroadcast(this, id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, time, sender);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void addEvent(long startTime, long endTime, Rota rota) {
		Uri eventsUri;
		Cursor cursor;
		int calendarId = 0;

		if (android.os.Build.VERSION.SDK_INT <= 7) {
			eventsUri = Uri.parse("content://calendar/events");
			cursor = this.getContentResolver().query(
					Uri.parse("content://calendar/calendars"),
					new String[] { "_id", "displayName" }, null, null, null);

		}

		else if (android.os.Build.VERSION.SDK_INT <= 14) {
			eventsUri = Uri.parse("content://com.android.calendar/events");
			cursor = this.getContentResolver().query(
					Uri.parse("content://com.android.calendar/calendars"),
					new String[] { "_id", "displayName" }, null, null, null);

		}

		else {
			eventsUri = Uri.parse("content://com.android.calendar/events");
			cursor = this.getContentResolver().query(
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
		this.getContentResolver().insert(eventsUri, event);

	}
}
