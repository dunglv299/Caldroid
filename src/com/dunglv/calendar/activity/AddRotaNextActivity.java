package com.dunglv.calendar.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.dunglv.calendar.R;
import com.dunglv.calendar.fragment.AddRotaNextFragment;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public class AddRotaNextActivity extends FragmentActivity {
	private static final String TAG = "AddRotaNextActivity";
	private MySharedPreferences sharedPreferences;
	private long rotaId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rota_next);
		setTitle("Add Rota Continue");
		sharedPreferences = new MySharedPreferences(this);
		sharedPreferences.putInt(Utils.CURRENT_WEEK, 1);
		rotaId = sharedPreferences.getLong(Utils.ROTA_ID);
		replaceFragment(new AddRotaNextFragment(), false);
	}

	public void replaceFragment(Fragment f, boolean addBackStack) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.frame_add_next, f);
		if (addBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sharedPreferences.putInt(Utils.CURRENT_WEEK, 0);
		sharedPreferences.putInt(Utils.WEEK_REPEAT, 1);
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	/**
	 * @return the rotaId
	 */
	public long getRotaId() {
		return rotaId;
	}

	/**
	 * @param rotaId
	 *            the rotaId to set
	 */
	public void setRotaId(long rotaId) {
		this.rotaId = rotaId;
	}

}
