package com.dunglv.calendar.fragment;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.WeekTime;
import com.dunglv.calendar.dao.WeekTimeDao;
import com.dunglv.calendar.dao.WeekTimeDao.Properties;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public class AddRotaNextFragment extends BaseFragment implements
		OnClickListener {
	private static final String TAG = "AddRotaNextFragment";
	private static final int LENGTH = 7;
	private static final String TIME_ZERO = "0000000000000";
	private int weekCount;
	private int currentWeek = 1;
	private MySharedPreferences sharedPreferences;
	Button btnNext;
	Button btnCopyNext;
	Button btnMakeAll;
	TextView tvWeekNumber;
	private boolean isNextPress;
	private static final int[] startIdArray = { R.id.startDate_btn1,
			R.id.startDate_btn2, R.id.startDate_btn3, R.id.startDate_btn4,
			R.id.startDate_btn5, R.id.startDate_btn6, R.id.startDate_btn7 };

	private static final int[] endIdArray = { R.id.endDate_btn1,
			R.id.endDate_btn2, R.id.endDate_btn3, R.id.endDate_btn4,
			R.id.endDate_btn5, R.id.endDate_btn6, R.id.endDate_btn7 };

	private static final int[] hourIdArray = { R.id.hour_edittext,
			R.id.hour_edittext2, R.id.hour_edittext3, R.id.hour_edittext4,
			R.id.hour_edittext5, R.id.hour_edittext6, R.id.hour_edittext7 };
	private Button[] startBtn = new Button[LENGTH];
	private Button[] endBtn = new Button[LENGTH];
	private EditText[] hourEditText = new EditText[LENGTH];

	private long[] startTime = new long[LENGTH];
	private long[] endTime = new long[LENGTH];
	private int[] hourWorking = new int[LENGTH];
	private String[] timeArray = new String[LENGTH];

	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private SQLiteDatabase db;
	private WeekTimeDao weekTimeDao;
	private List<WeekTime> listWeekTimes;
	private WeekTime weekTime;
	Button saveBtn;
	RelativeLayout btnLayout;
	private long weekTimeId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_rota_next, container,
				false);
		initDao();
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

		// Action pick start time
		for (int i = 0; i < LENGTH; i++) {
			final int index = i;
			startBtn[index] = (Button) v.findViewById(startIdArray[index]);
			startBtn[index].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showTimePicker(startBtn[index], true, index);
				}
			});
		}
		// Action pick end time
		for (int i = 0; i < LENGTH; i++) {
			final int index = i;
			endBtn[index] = (Button) v.findViewById(endIdArray[index]);
			endBtn[index].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showTimePicker(endBtn[index], false, index);
				}
			});
		}
		// Init editText and get data
		for (int i = 0; i < LENGTH; i++) {
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
		timeArray[0] = weekTime.getMonday();
		timeArray[1] = weekTime.getTuesday();
		timeArray[2] = weekTime.getWednesday();
		timeArray[3] = weekTime.getThursday();
		timeArray[4] = weekTime.getFriday();
		timeArray[5] = weekTime.getSaturday();
		timeArray[6] = weekTime.getSunday();
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

	public void initDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"weekTime-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
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
		weekTime.setMonday(timeArray[0]);
		weekTime.setTuesday(timeArray[1]);
		weekTime.setWednesday(timeArray[2]);
		weekTime.setThursday(timeArray[3]);
		weekTime.setFriday(timeArray[4]);
		weekTime.setSaturday(timeArray[5]);
		weekTime.setSunday(timeArray[6]);
		return weekTime;
	}
}
