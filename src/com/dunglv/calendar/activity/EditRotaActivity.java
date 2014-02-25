package com.dunglv.calendar.activity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.DayTime;
import com.dunglv.calendar.dao.DayTimeDao.Properties;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.util.Utils;

public class EditRotaActivity extends RotaActivity implements OnClickListener {
	private long rotaId;
	private Rota rota;
	private long previousDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rotaId = getIntent().getExtras().getLong(Utils.ROTA_ID);
		rota = rotaDao.queryBuilder().where(Properties.Id.eq(rotaId)).list()
				.get(0);
		mNameEd.setText(rota.getName());
		mWeekEd.setText(rota.getWeekReapeat() + "");
		mRepeatTimeEd.setText(rota.getTimeRepeat());

		// For color
		int indexColor = Arrays.asList(colorsArray).indexOf(rota.getColor());
		colorSpinner.setSelection(indexColor);

		// For reminder
		int indexReminder = Arrays.asList(reminderArray).indexOf(
				rota.getReminderTime());
		if (indexReminder == -1) {
			mCheckBoxReminder.setChecked(false);
		} else {
			mCheckBoxReminder.setChecked(true);
			reminderSpinner.setSelection(indexReminder);
		}
		// Start date btn
		startDate = rota.getDateStarted();
		previousDay = startDate;
		startDateBtn.setText(Utils.convertLongToTime("dd/MM/yyyy", startDate));

		// Start day of week

		// Google sync
		// isGoogleSync = rota.getIsGoogleSync();
		mCheckBoxGoogle.setChecked(rota.getIsGoogleSync());
	}

	@Override
	public void initView() {
		super.initView();
		setTitle("Edit Rota");
	}

	@Override
	public Rota getRota() {
		rota = super.getRota();
		rota.setId(rotaId);
		rota.setDateStarted(startDate);
		return rota;
	}

	@Override
	public void onContinue() {
		changeDate();
		if (getRota().getWeekReapeat() == 0) {
			Utils.showAlert(this, getString(R.string.alert_week));
			return;
		}
		super.onContinue();
		rotaDao.update(getRota());
		sharedPreferences.putLong(Utils.ROTA_ID, rotaId);
	}

	@Override
	public void onSave() {
		super.onSave();
		rotaDao.update(getRota());
		finish();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.delete_btn:
			rotaDao.delete(getRota());
			deleteDayTimeDao(rotaId);
			finish();
			break;
		default:
			break;
		}
	}

	public void deleteDayTimeDao(long rotaId) {
		initDayTimeDao();
		List<DayTime> listDelete = dayTimeDao.queryBuilder()
				.where(Properties.RotaId.eq(rotaId)).list();
		dayTimeDao.deleteInTx(listDelete);
	}

	public void changeDate() {
		// Get change Time
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTimeInMillis(previousDay);
		cal2.setTimeInMillis(startDate);
		previousDay = startDate;
		int plusDay = cal2.get(Calendar.DAY_OF_YEAR)
				- cal1.get(Calendar.DAY_OF_YEAR);
		initDayTimeDao();
		List<DayTime> listChange = dayTimeDao
				.queryBuilder()
				.where(Properties.RotaId.eq(rotaId), Properties.StartTime.gt(0))
				.list();
		for (DayTime dayTime : listChange) {
			Calendar cal = Calendar.getInstance();
			// Stat time
			cal.setTimeInMillis(dayTime.getStartTime());
			cal.add(Calendar.DAY_OF_MONTH, plusDay);
			dayTime.setStartTime(cal.getTimeInMillis());

			// End time
			cal.setTimeInMillis(dayTime.getEndTime());
			cal.add(Calendar.DAY_OF_MONTH, plusDay);
			dayTime.setEndTime(cal.getTimeInMillis());
			dayTimeDao.update(dayTime);
		}
	}
}
