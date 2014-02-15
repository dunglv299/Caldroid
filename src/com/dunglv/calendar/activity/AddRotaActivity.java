package com.dunglv.calendar.activity;

import android.view.View.OnClickListener;

import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.util.Utils;

public class AddRotaActivity extends RotaActivity implements OnClickListener {
	private Rota rota;

	@Override
	public void initView() {
		super.initView();
		setTitle("Edit Rota");
		mWeekEd.setText("5");
	}

	@Override
	public Rota getRota() {
		rota = super.getRota();
		rota.setDateStarted(System.currentTimeMillis());
		return rota;
	}

	@Override
	public void onContinue() {
		super.onContinue();
		long rotaId = rotaDao.insertOrReplace(getRota());
		sharedPreferences.putLong(Utils.ROTA_ID, rotaId);
	}

	@Override
	void onSave() {
		rotaDao.insertOrReplace(getRota());
		finish();
	}
}
