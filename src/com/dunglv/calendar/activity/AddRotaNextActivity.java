package com.dunglv.calendar.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.WeekdayAdapter;
import com.dunglv.calendar.entity.ItemAddShift;

public class AddRotaNextActivity extends Activity {
	WeekdayAdapter adapter;
	ListView mListView;
	private ArrayList<ItemAddShift> listItemAdd;
	String[] weekDays = { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_rota_next);
		setTitle("Add Rota Continue");
	}
}
