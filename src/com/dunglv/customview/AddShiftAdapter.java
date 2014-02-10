package com.dunglv.customview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.entity.ItemAddShift;

public class AddShiftAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ItemAddShift> listItems;

	public AddShiftAdapter(Context context, ArrayList<ItemAddShift> listItems) {
		this.context = context;
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		if (v == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			v = mInflater.inflate(R.layout.single_weekday, null);
		}
		
		CheckBox mCheckbox = (CheckBox) v.findViewById(R.id.checkbox);
		TextView mTextWeekday = (TextView) v.findViewById(R.id.weekday);
		Button mStartDateBtn = (Button) v.findViewById(R.id.startDate_btn);
		Button mEndDateBtn = (Button) v.findViewById(R.id.endDate_btn);
		
		ItemAddShift item = new ItemAddShift();
		item = listItems.get(position);
		mCheckbox.setChecked(item.isEnable());
		mTextWeekday.setText(item.getWeekDay());
		
		return v;
	}
}
