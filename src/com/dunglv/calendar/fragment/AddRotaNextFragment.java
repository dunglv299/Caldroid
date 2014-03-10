package com.dunglv.calendar.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dunglv.calendar.R;
import com.dunglv.calendar.activity.AddRotaNextActivity;
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

	private static final int[] deleteIdArray = { R.id.delete1, R.id.delete2,
			R.id.delete3, R.id.delete4, R.id.delete5, R.id.delete6,
			R.id.delete7 };
	private TextView[] weekDayTv = new TextView[LENGTH];
	private Button[] startBtn = new Button[LENGTH];
	private Button[] endBtn = new Button[LENGTH];
	private EditText[] hourEditText = new EditText[LENGTH];
	private ImageView[] deleteBtn = new ImageView[LENGTH];
	private boolean[] isGoogleSyncs = new boolean[LENGTH];

	private long[] startTime = new long[LENGTH];
	private long[] endTime = new long[LENGTH];
	private double[] hourWorking = new double[LENGTH];

	private DayTimeDao dayTimeDao;
	private RotaDao rotaDao;
	private List<DayTime> listDayTimes;
	Button saveBtn;
	RelativeLayout btnLayout;
	private Rota rota;
	int calendarId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initRotaDao();
		initDayTimeDao();
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
		sharedPreferences = new MySharedPreferences(getActivity());
		weekCount = sharedPreferences.getInt(Utils.WEEK_REPEAT);
		currentWeek = sharedPreferences.getInt(Utils.CURRENT_WEEK);
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
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(rota.getDateStarted());
		cal.add(Calendar.DAY_OF_MONTH, (currentWeek - 1) * 7);
		for (int i = 0; i < LENGTH; i++) {
			final int index = i;
			// Init weekDay TextView
			weekDayTv[index] = (TextView) v.findViewById(weekDayIdArray[index]);
			weekDayTv[i].setText(new SimpleDateFormat("EEE").format(
					cal.getTime()).toUpperCase());
			cal.add(Calendar.DAY_OF_MONTH, 1);

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

			// Init delete btn
			deleteBtn[i] = (ImageView) v.findViewById(deleteIdArray[i]);
			deleteBtn[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					clearTimeText(index);
				}
			});
		}
	}

	private void initData() {
		listDayTimes = getListDayTimeDao();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(rota.getDateStarted());
		cal.add(Calendar.DAY_OF_MONTH, (currentWeek - 1) * 7);
		if (listDayTimes.size() >= currentWeek * 7) {
			// Init when exists data
			for (int i = 0; i < LENGTH; i++) {
				weekDayTv[i].setText(new SimpleDateFormat("EEE").format(
						cal.getTime()).toUpperCase());
				cal.add(Calendar.DAY_OF_MONTH, 1);

				DayTime dayTime = listDayTimes.get(i + (currentWeek - 1) * 7);
				startTime[i] = dayTime.getStartTime();
				endTime[i] = dayTime.getEndTime();
				setTextButton(startBtn[i], startTime[i]);
				setTextButton(endBtn[i], endTime[i]);
				// Init hour working
				double hourWorking = dayTime.getHourWorking();
				if (hourWorking == 0) {
					hourEditText[i].setText("");
				} else {
					hourEditText[i].setText(hourWorking + "");
				}
				// Init google sync
				if (dayTime.getIsSyncGoogle() == null
						|| !dayTime.getIsSyncGoogle()) {
					isGoogleSyncs[i] = false;
				} else {
					isGoogleSyncs[i] = true;
				}

			}
		}
	}

	private void setTextButton(Button button, long time) {
		if (time == 0) {
			button.setText("");
		} else {
			button.setText(Utils.convertLongToTime(time));
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
		if (startTime[index] > 0) {
			c.setTimeInMillis(isStartTime ? startTime[index] : endTime[index]);
		}
		final TimePickerDialog dateDialog = new TimePickerDialog(getActivity(),
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						Calendar mCalendar = Calendar.getInstance();
						mCalendar.setTimeInMillis(rota.getDateStarted());
						mCalendar.add(Calendar.DAY_OF_MONTH, index
								+ (currentWeek - 1) * 7);
						mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						mCalendar.set(Calendar.MINUTE, minute);
						button.setText(DateFormat.format("h:mm a", mCalendar));
						if (isStartTime) {
							startTime[index] = mCalendar.getTimeInMillis();
							if (endTime[index] == 0) {
								endTime[index] = mCalendar.getTimeInMillis() + 10000l;
								setTextButton(endBtn[index], endTime[index]);
							}
							if (startTime[index] > endTime[index]) {
								setTextButton(endBtn[index], endTime[index]);
							}
						} else {
							endTime[index] = mCalendar.getTimeInMillis();
							if (startTime[index] == 0) {
								startTime[index] = mCalendar.getTimeInMillis() - 10000l;
								setTextButton(startBtn[index], startTime[index]);
							}
						}
					}
				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		dateDialog.show();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated");
		isNextPress = false;

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
			// onRemind();
			break;
		case R.id.copy_to_next:
			copyToNext();
			break;
		case R.id.make_all_week_btn:
			new MakeAllWeekAsyntask().execute();
			break;
		case R.id.save_btn:
			// Intent returnIntent = new Intent();
			// getActivity().setResult(Activity.RESULT_OK, returnIntent);
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
		listDayTimes = getListDayTimeDao();
		for (int i = 0; i < LENGTH; i++) {
			DayTime dayTime = collectData(i);
			dayTime.setStartTime(startTime[i]);
			dayTime.setEndTime(endTime[i]);
			dayTimeDao.insertOrReplace(dayTime);
		}
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
		listDayTimes = getListDayTimeDao();
		if (currentWeek <= weekCount) {
			for (int i = 0; i < LENGTH; i++) {
				DayTime dayTime = collectData(i);
				// Plus day
				if (startTime[i] > 0) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(startTime[i]);
					cal.add(Calendar.DAY_OF_MONTH, LENGTH);
					dayTime.setStartTime(cal.getTimeInMillis());
					cal.setTimeInMillis(endTime[i]);
					cal.add(Calendar.DAY_OF_MONTH, LENGTH);
					dayTime.setEndTime(cal.getTimeInMillis());
					dayTime.setRotaId(rota.getId());
				} else {
					dayTime.setStartTime(0l);
					dayTime.setEndTime(0l);

				}
				dayTimeDao.insertOrReplace(dayTime);
			}
		}
	}

	public int getPlusDay() {
		return (currentWeek - 1) * 7;
	}

	private List<DayTime> getListDayTimeDao() {
		List<DayTime> listDayTimes = dayTimeDao
				.queryBuilder()
				.where(Properties.RotaId
						.eq(((AddRotaNextActivity) getActivity()).getRotaId()))
				.list();
		return listDayTimes;
	}

	/**
	 * make all week action
	 */
	private void onMakeAllWeek() {
		List<DayTime> listDelete = dayTimeDao
				.queryBuilder()
				.where(Properties.DayId.between((currentWeek - 1) * 7,
						weekCount * 7),
						Properties.RotaId
								.eq(((AddRotaNextActivity) getActivity())
										.getRotaId())).list();
		dayTimeDao.deleteInTx(listDelete);
		for (int i = (currentWeek - 1) * 7; i < weekCount * 7; i++) {
			DayTime dayTime = collectData(i % 7);
			if (startTime[i % 7] > 0) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(startTime[i % 7]);
				cal.add(Calendar.DAY_OF_MONTH, 7 * ((i / 7) - currentWeek + 1));
				dayTime.setStartTime(cal.getTimeInMillis());
				cal.setTimeInMillis(endTime[i % 7]);
				cal.add(Calendar.DAY_OF_MONTH, 7 * ((i / 7) - currentWeek + 1));
				dayTime.setEndTime(cal.getTimeInMillis());
				dayTime.setRotaId(rota.getId());

			} else {
				dayTime.setStartTime(0l);
				dayTime.setEndTime(0l);

			}
			dayTime.setId(null);
			dayTime.setDayId(i);
			dayTimeDao.insert(dayTime);
		}
		isNextPress = true;
		for (int i = currentWeek; i <= weekCount; i++) {
			if (i == weekCount) {
				sharedPreferences.putInt(Utils.CURRENT_WEEK, i);
			}
			if (i > currentWeek) {
				replaceFragment(R.id.frame_add_next, new AddRotaNextFragment(),
						true);
			}
		}
	}

	private DayTime collectData(int index) {
		hourWorking[index] = Utils.convertStringToDouble(hourEditText[index]
				.getText().toString());
		DayTime dayTime = new DayTime();
		dayTime.setDayId(index + getPlusDay());
		dayTime.setHourWorking(hourWorking[index]);
		dayTime.setRotaId(rota.getId());
		dayTime.setIsSyncGoogle(isGoogleSyncs[index]);
		if (listDayTimes.size() < currentWeek * 7) {
			dayTime.setId(null);
		} else {
			dayTime.setId(listDayTimes.get(index + getPlusDay()).getId());
		}
		return dayTime;
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
	}

	public void initDayTimeDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"dayTime-db", null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		dayTimeDao = daoSession.getDayTimeDao();
	}

	private void clearTimeText(int index) {
		startTime[index] = 0;
		endTime[index] = 0;
		setTextButton(startBtn[index], startTime[index]);
		setTextButton(endBtn[index], endTime[index]);
	}
}
