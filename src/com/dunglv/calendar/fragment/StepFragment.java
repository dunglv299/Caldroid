package com.dunglv.calendar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunglv.calendar.R;

public class StepFragment extends BaseFragment {
	ImageView mImageView;
	TextView mTextView;
	TextView mDescriptionTextView;
	private int position;
	private int imageStep[] = { R.drawable.calendar_step1,
			R.drawable.calendar_step2, R.drawable.calendar_step3 };
	private String[] titleStep = { "1.Manage Shift", "2.Manage Weekdays",
			"3.Calendar View" };
	private String[] descriptions;

	public static StepFragment newInstance(int position) {
		StepFragment stepFragment = new StepFragment();
		stepFragment.position = position;
		return stepFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_step, container, false);
		mImageView = (ImageView) v.findViewById(R.id.mImageView);
		mImageView.setImageDrawable(getResources().getDrawable(
				imageStep[position]));
		mTextView = (TextView) v.findViewById(R.id.title_tv);
		mTextView.setText(titleStep[position]);

		descriptions = getResources().getStringArray(R.array.descriptions);
		mDescriptionTextView = (TextView) v.findViewById(R.id.description_tv);
		mDescriptionTextView.setText(descriptions[position]);
		return v;
	}
}
