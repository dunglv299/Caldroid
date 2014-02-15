package com.dunglv.calendar.activity;

import java.util.Arrays;

import android.os.Bundle;
import android.view.View.OnClickListener;

import com.dunglv.calendar.dao.Rota;
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
		mRepeatTimeEd.setText(rota.getTimeRepeat() + "");
		int index = Arrays.asList(colorArray).indexOf(rota.getColor());
		colorSpinner.setSelection(index);
		dateStarted = rota.getDateStarted();
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
		rota.setDateStarted(dateStarted);
		return rota;
	}

	@Override
	public void onContinue() {
		super.onContinue();
		rotaDao.update(getRota());
	}

	@Override
	void onSave() {
		rotaDao.update(getRota());
		finish();
	}
}
