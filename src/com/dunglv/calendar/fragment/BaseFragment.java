package com.dunglv.calendar.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.dunglv.calendar.R;

public class BaseFragment extends Fragment {
	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.addRota_frame, fragment);
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}

		// Commit the transaction
		transaction.commit();
	}
}
