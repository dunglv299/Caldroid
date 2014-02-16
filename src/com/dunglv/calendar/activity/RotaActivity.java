package com.dunglv.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public abstract class RotaActivity extends FragmentActivity implements
		OnClickListener {
	private static final String TAG = "RotaActivity";
	public static final int REQUES_CODE_FINISH = 100;
	private String colorRota;
	public String[] colorArray;
	public Spinner colorSpinner;
	public EditText mNameEd;
	public EditText mWeekEd;
	public EditText mRepeatTimeEd;
	public DaoMaster daoMaster;
	public DaoSession daoSession;
	private SQLiteDatabase db;
	public RotaDao rotaDao;
	public MySharedPreferences sharedPreferences;
	public Button deleteBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rota);
		initRotaDao();
		initView();
		init();
	}

	public void initView() {
		colorSpinner = (Spinner) findViewById(R.id.color_spinner);
		Button continueBtn = (Button) findViewById(R.id.continue_btn);
		continueBtn.setOnClickListener(this);
		Button saveBtn = (Button) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		deleteBtn = (Button) findViewById(R.id.delete_btn);
		deleteBtn.setOnClickListener(this);
		mNameEd = (EditText) findViewById(R.id.rota_name);
		mWeekEd = (EditText) findViewById(R.id.week_number);
		mRepeatTimeEd = (EditText) findViewById(R.id.time_number);
	}

	public void init() {
		colorArray = getResources().getStringArray(R.array.color_spinner);
		setCustomColorSpinner();
		sharedPreferences = new MySharedPreferences(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_btn:
			onContinue();
			break;
		case R.id.save_btn:
			onSave();
			break;
		default:
			break;
		}
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

	public void initRotaDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "rota-db",
				null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUES_CODE_FINISH) {
			if (resultCode == RESULT_OK) {
				Log.e(TAG, "dunglv");
				finish();
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}// onActivityResult

	/**
	 * Collect data for sava data to DB
	 */
	public Rota getRota() {
		Rota rota = new Rota();
		rota.setColor(colorRota);
		rota.setName(mNameEd.getText().toString());
		rota.setWeekReapeat(Utils.convertStringToInt(mWeekEd.getText()
				.toString()));
		rota.setTimeRepeat(mRepeatTimeEd.getText().toString());
		return rota;
	}

	public void onContinue() {
		Intent intent = new Intent(this, AddRotaNextActivity.class);
		// Send data to Next Activity
		sharedPreferences.putInt(Utils.WEEK_REPEAT, getRota().getWeekReapeat());
		startActivityForResult(intent, REQUES_CODE_FINISH);
	}

	abstract void onSave();

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sharedPreferences.putInt(Utils.CURRENT_WEEK, 0);
		sharedPreferences.putInt(Utils.ROTA_ID, 0);

	}
}
