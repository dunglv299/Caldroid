package com.dunglv.calendar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.dunglv.calendar.R;

public class NewRotaFragment extends BaseFragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_new_rota, container, false);
		Button newRotaBtn = (Button) v.findViewById(R.id.continue_btn);
		newRotaBtn.setOnClickListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newRota_btn:

			break;

		default:
			break;
		}
	}
}
