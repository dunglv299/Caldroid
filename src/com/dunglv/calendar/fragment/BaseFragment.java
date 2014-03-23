package com.dunglv.calendar.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class BaseFragment extends Fragment {
	public void replaceFragment(int layout, Fragment f, boolean addBackStack) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(layout, f);
		if (addBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
}
