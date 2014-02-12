package com.dunglv.calendar.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public class AddRotaNextFragment extends BaseFragment implements
		OnClickListener {
	private static final String TAG = "AddRotaNextFragment";
	private int weekCount;
	private int currentWeek = 1;
	private MySharedPreferences sharedPreferences;
	Button btnNext;
	Button btnCopyNext;
	Button btnMakeAll;
	TextView tvWeekNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_add_rota_next, container,
				false);
		initView(v);
		Log.e(TAG, "onCreateView");
		return v;
	}

	private void initView(View v) {
		btnNext = (Button) v.findViewById(R.id.next_btn);
		btnCopyNext = (Button) v.findViewById(R.id.copy_to_next);
		btnMakeAll = (Button) v.findViewById(R.id.make_all_week_btn);
		btnNext.setOnClickListener(this);
		btnCopyNext.setOnClickListener(this);
		btnMakeAll.setOnClickListener(this);
		tvWeekNumber = (TextView) v.findViewById(R.id.week_number);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		sharedPreferences = new MySharedPreferences(getActivity());
		weekCount = sharedPreferences.getInt(Utils.WEEK_REPEAT);
		currentWeek = sharedPreferences.getInt(Utils.CURRENT_WEEK);
		if (currentWeek == weekCount) {
			btnNext.setText("Done");
		}
		Log.e("" + currentWeek, "" + weekCount);
		tvWeekNumber.setText("WEEK " + currentWeek);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_btn:
			currentWeek++;
			sharedPreferences.putInt(Utils.CURRENT_WEEK, currentWeek);
			if (currentWeek > weekCount) {
				getActivity().finish();
				return;
			}
			replaceFragment(R.id.frame_add_next, new AddRotaNextFragment(),
					true);
			break;
		case R.id.copy_to_next:

			break;
		case R.id.make_all_week_btn:

			break;
		default:
			break;
		}
	}

}
