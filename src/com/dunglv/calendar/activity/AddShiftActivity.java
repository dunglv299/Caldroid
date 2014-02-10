package com.dunglv.calendar.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.AddShiftAdapter;
import com.dunglv.calendar.entity.ItemAddShift;

public class AddShiftActivity extends Activity {
	AddShiftAdapter adapter;
	ListView mListView;
	private ArrayList<ItemAddShift> listItemAdd;
	String[] weekDays = { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_shift);
		listItemAdd = new ArrayList<ItemAddShift>();
		for (int i = 0; i < weekDays.length; i++) {
			ItemAddShift itemAddShift = new ItemAddShift();
			itemAddShift.setWeekDay(weekDays[i]);
			listItemAdd.add(itemAddShift);
		}
		mListView = (ListView) findViewById(R.id.list_weekday);
		adapter = new AddShiftAdapter(this, listItemAdd);
		mListView.setAdapter(adapter);
	}
}
