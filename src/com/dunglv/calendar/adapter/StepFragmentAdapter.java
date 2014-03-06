package com.dunglv.calendar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dunglv.calendar.R;
import com.dunglv.calendar.fragment.StepFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class StepFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	private int mCount = 3;

	public StepFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return StepFragment.newInstance(position);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public int getIconResId(int index) {
		return R.drawable.perm_group_calendar;
	}

}
