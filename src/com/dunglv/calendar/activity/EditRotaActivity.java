package com.dunglv.calendar.activity;

import java.util.Arrays;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.WeekTime;
import com.dunglv.calendar.dao.WeekTimeDao;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.WeekTimeDao.Properties;
import com.dunglv.calendar.util.Utils;

public class EditRotaActivity extends RotaActivity implements OnClickListener {
	private long rotaId;
	private Rota rota;
	private long dateStarted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rotaId = getIntent().getExtras().getLong(Utils.ROTA_ID);
		rota = rotaDao.queryBuilder().where(Properties.Id.eq(rotaId)).list()
				.get(0);
		mNameEd.setText(rota.getName());
		mWeekEd.setText(rota.getWeekReapeat() + "");
		mRepeatTimeEd.setText(rota.getTimeRepeat());
		int index = Arrays.asList(colorArray).indexOf(rota.getColor());
		colorSpinner.setSelection(index);
		dateStarted = rota.getDateStarted();
	}

	@Override
	public void initView() {
		super.initView();
		setTitle("Edit Rota");
		deleteBtn.setVisibility(View.VISIBLE);
	}

	@Override
	public Rota getRota() {
		rota = super.getRota();
		rota.setId(rotaId);
		rota.setDateStarted(dateStarted);
		return rota;
	}

	@Override
	public void onContinue() {
		if (getRota().getWeekReapeat() == 0) {
			Utils.showAlert(this, getString(R.string.alert_week));
			return;
		}
		super.onContinue();
		rotaDao.update(getRota());
		sharedPreferences.putLong(Utils.ROTA_ID, rotaId);
	}

	@Override
	void onSave() {
		rotaDao.update(getRota());
		finish();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.delete_btn:
			rotaDao.delete(getRota());
			deleteWeekTimeDao(rotaId);
			finish();
			break;

		default:
			break;
		}
	}

	public void deleteWeekTimeDao(long rotaId) {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weekTime-db",
				null);
		SQLiteDatabase db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		DaoSession daoSession = daoMaster.newSession();
		WeekTimeDao weekTimeDao = daoSession.getWeekTimeDao();

		List<WeekTime> listDelete = weekTimeDao.queryBuilder()
				.where(Properties.RotaId.eq(rotaId)).list();
		weekTimeDao.deleteInTx(listDelete);
	}
}