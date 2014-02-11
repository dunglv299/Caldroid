package com.dunglv.calendar.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.activity.AddRotaNextActivity;

public class AddRotaFragment extends BaseFragment implements OnClickListener {
	String[] colorArray;
	Spinner colorSpinner;
	private static final String TAG = "AddRotaFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_rota, container, false);
		colorArray = getResources().getStringArray(R.array.color_spinner);
		colorSpinner = (Spinner) v.findViewById(R.id.color_spinner);
		Button newRotaBtn = (Button) v.findViewById(R.id.continue_btn);
		newRotaBtn.setOnClickListener(this);
		setCustomColorSpinner();
		Log.e(TAG, "onCreateView");
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_btn:
			Intent intent = new Intent(getActivity(), AddRotaNextActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void setCustomColorSpinner() {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,
				colorArray) {

			@Override
			public boolean areAllItemsEnabled() {
				return false;
			}

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				View v = convertView;
				if (v == null) {
					Context mContext = this.getContext();
					LayoutInflater vi = (LayoutInflater) mContext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.single_spinner_dropdown_item, null);
				}

				TextView tv = (TextView) v.findViewById(R.id.text_spinner);
				tv.setText(colorArray[position]);
				tv.setTextColor(Color.parseColor(colorArray[position]));
				return v;
			}

		};
		colorSpinner.setAdapter(spinnerAdapter);
		colorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				TextView selectedText = (TextView) parentView.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.parseColor(colorSpinner
							.getSelectedItem().toString()));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
	}
}
