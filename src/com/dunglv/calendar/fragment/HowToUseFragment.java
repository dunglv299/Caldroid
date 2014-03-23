package com.dunglv.calendar.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.StepFragmentAdapter;
import com.viewpagerindicator.IconPageIndicator;

public class HowToUseFragment extends BaseFragment {
	StepFragmentAdapter mAdapter;
	ViewPager mPager;
	IconPageIndicator mIndicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_howtouse, container, false);
		mAdapter = new StepFragmentAdapter(getChildFragmentManager());
		mPager = (ViewPager) v.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (IconPageIndicator) v.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		return v;
	}
}
