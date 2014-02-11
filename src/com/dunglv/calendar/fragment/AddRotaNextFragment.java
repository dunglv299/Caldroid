package com.dunglv.calendar.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.WeekdayAdapter;
import com.dunglv.calendar.entity.ItemAddShift;

public class AddRotaNextFragment extends BaseFragment {
	WeekdayAdapter adapter;
	ListView mListView;
	private ArrayList<ItemAddShift> listItemAdd;
	String[] weekDays = { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_add_rota_next, container,
				false);
		init(v);
		return v;
	}

	public void init(View v) {
		listItemAdd = new ArrayList<ItemAddShift>();
		for (int i = 0; i < weekDays.length; i++) {
			ItemAddShift itemAddShift = new ItemAddShift();
			itemAddShift.setWeekDay(weekDays[i]);
			listItemAdd.add(itemAddShift);
		}
		mListView = (ListView) v.findViewById(R.id.list_weekday);
		adapter = new WeekdayAdapter(getActivity(), listItemAdd);
		mListView.setAdapter(adapter);

	}
}
