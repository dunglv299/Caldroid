package com.dunglv.calendar.activity;

import android.view.View;
import android.view.View.OnClickListener;

import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.util.Utils;

public class AddRotaActivity extends RotaActivity implements OnClickListener {
	private Rota rota;
	private long rotaId;

	@Override
	public void initView() {
		super.initView();
		setTitle("Add Rota");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.delete_btn:
			if (getRota().getId() != null) {
				rotaDao.delete(getRota());
			}
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public Rota getRota() {
		rota = super.getRota();
		if (rotaId != 0) {
			rota.setId(rotaId);
		}
		return rota;
	}

	@Override
	public void onContinue() {
		if (getRota().getWeekReapeat() == 0) {
			Utils.showAlert(this, getString(R.string.alert_week));
			return;
		}
		super.onContinue();
		rotaId = rotaDao.insertOrReplace(getRota());
		sharedPreferences.putLong(Utils.ROTA_ID, rotaId);
	}

	@Override
	public void onSave() {
		super.onSave();
		rotaDao.insertOrReplace(getRota());
		finish();
	}
}
