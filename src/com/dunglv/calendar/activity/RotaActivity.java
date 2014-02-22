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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;
import com.dunglv.calendar.util.MySharedPreferences;
import com.dunglv.calendar.util.Utils;

public abstract class RotaActivity extends FragmentActivity implements
		OnClickListener, OnCheckedChangeListener {
	private static final String TAG = "RotaActivity";
	public static final int REQUES_CODE_FINISH = 100;
	private String colorRota;
	public Spinner colorSpinner;
	public Spinner reminderSpinner;
	public EditText mNameEd;
	public EditText mWeekEd;
	public EditText mRepeatTimeEd;
	public DaoMaster daoMaster;
	public DaoSession daoSession;
	private SQLiteDatabase db;
	public RotaDao rotaDao;
	public MySharedPreferences sharedPreferences;
	public Button deleteBtn;
	public CheckBox mCheckBoxReminder;
	public static int[] reminderArray = { 1, 5, 10, 15, 20, 25, 30, 45, 60,
			120, 180, 12 * 60, 24 * 60 };
	public static String[] colorsArray = { "#f6cd6c", "#5b93cb", "#a864a8",
			"#7ab977", "#a67c52", "#f7941d", "#44d5ce", "#fa565c", "#8393ca",
			"#7d7d7d" };
	public int remindTime;

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
		mCheckBoxReminder = (CheckBox) findViewById(R.id.checkbox);
		mCheckBoxReminder.setOnCheckedChangeListener(this);
		reminderSpinner = (Spinner) findViewById(R.id.reminder_spinner);
	}

	public void init() {
		setCustomColorSpinner();
		reminderSpinnerListener();
		sharedPreferences = new MySharedPreferences(this);
		setSpinnerEnabled(reminderSpinner, false);
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.checkbox:
			setSpinnerEnabled(reminderSpinner, isChecked);
			break;

		default:
			break;
		}
	}

	private void setSpinnerEnabled(Spinner spinner, boolean enabled) {
		spinner.setEnabled(enabled);
		spinner.setAlpha(enabled ? 1.0f : 0.4f);
	}

	public void setCustomColorSpinner() {
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, new String[] {
						"", "", "", "", "", "", "", "", "", "" }) {

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

				View colorView = v.findViewById(R.id.text_spinner);
				colorView.setBackgroundColor(Color
						.parseColor(colorsArray[position]));
				return v;
			}

		};
		colorSpinner.setAdapter(spinnerAdapter);
		colorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				colorRota = colorsArray[position];
				colorSpinner.setBackgroundColor(Color.parseColor(colorRota));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});
	}

	private void reminderSpinnerListener() {
		reminderSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				remindTime = reminderArray[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

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
		rota.setReminderTime(mCheckBoxReminder.isChecked() ? remindTime : 0);
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
