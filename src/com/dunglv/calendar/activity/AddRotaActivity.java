package com.dunglv.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public class AddRotaActivity extends FragmentActivity implements
		OnClickListener {
	private static final String TAG = "AddRotaActivity";
	String[] colorArray;
	Spinner colorSpinner;
	private Rota rota;
	private String colorRota;
	EditText mNameEd;
	EditText mWeekEd;
	EditText mRepeatTimeEd;
	private MySharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rota);
		initView();
		init();
	}

	private void initView() {
		colorSpinner = (Spinner) findViewById(R.id.color_spinner);
		Button newRotaBtn = (Button) findViewById(R.id.continue_btn);
		newRotaBtn.setOnClickListener(this);
		mNameEd = (EditText) findViewById(R.id.rota_name);
		mWeekEd = (EditText) findViewById(R.id.week_number);
		mWeekEd.setText("3");
		mRepeatTimeEd = (EditText) findViewById(R.id.time_number);
	}

	private void init() {
		colorArray = getResources().getStringArray(R.array.color_spinner);
		setTitle("Add Rota");
		setCustomColorSpinner();
		rota = new Rota();
		sharedPreferences = new MySharedPreferences(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_btn:
			Intent intent = new Intent(this, AddRotaNextActivity.class);
			// Send data to Next Activity
			collectData();
			sharedPreferences.putInt(Utils.WEEK_REPEAT, rota.getWeekReapeat());
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void collectData() {
		rota.setColor(colorRota);
		rota.setWeekReapeat(Utils.convertStringToInt(mWeekEd.getText()
				.toString()));
	}

	public void setCustomColorSpinner() {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, colorArray) {

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
					colorRota = selectedText.getText().toString();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
	}
}
