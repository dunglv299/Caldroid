package com.dunglv.calendar.activity;

import android.view.View.OnClickListener;

import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.util.Utils;

public class AddRotaActivity extends RotaActivity implements OnClickListener {
	private Rota rota;

	@Override
	public void initView() {
		super.initView();
		setTitle("Add Rota");
	}

	@Override
	public Rota getRota() {
		rota = super.getRota();
		rota.setDateStarted(System.currentTimeMillis());
		return rota;
	}

	@Override
	public void onContinue() {
		if (getRota().getWeekReapeat() == 0) {
			Utils.showAlert(this, getString(R.string.alert_week));
			return;
		}
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
