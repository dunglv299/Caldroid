package com.dunglv.calendar.fragment;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

@SuppressLint("ValidFragment")
public class MyTimePickerFragment extends DialogFragment {
	private Fragment mFragment;

	public MyTimePickerFragment(Fragment callback) {
		mFragment = callback;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new TimePickerDialog(getActivity(),
				(OnTimeSetListener) mFragment, Calendar.getInstance().get(
						Calendar.HOUR), Calendar.getInstance().get(
						Calendar.MINUTE), false);
	}
}