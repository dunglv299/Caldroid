package com.dunglv.calendar.adapter;

import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caldroid.caldroidcustom.CaldroidGridAdapter;
import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.Rota;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
	private List<Rota> listRota;
	private List<String> listColors;
	private float scale;

	public CaldroidSampleCustomAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData, List<Rota> listRota) {
		super(context, month, year, caldroidData, extraData);
		this.listRota = listRota;
		scale = context.getResources().getDisplayMetrics().density;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_cell_calendar, null);
		}

		TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
		// View colorView = cellView.findViewById(R.id.circular_image_view);

		tv1.setTextColor(Color.BLACK);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			tv1.setTextColor(resources.getColor(R.color.caldroid_darker_gray));
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(R.drawable.red_border);
			} else {
				cellView.setBackgroundResource(R.drawable.cell_bg);
			}
		}
		// Custom color
		tv1.setText("" + dateTime.getDay());
		// a holder to place your text view wherever you want
		LinearLayout linearLayout = (LinearLayout) cellView
				.findViewById(R.id.circular_image_view);
		linearLayout.removeAllViews();
		int numberOfRota = getNumberOfRota(dateTime);
		if (numberOfRota > 0) {
			for (int i = 0; i < numberOfRota; i++) {
				if (i == 4) {
					break;
				}
				MyCircleView circleView = new MyCircleView(context,
						Color.parseColor(listColors.get(i)));
				int pixels = (int) (10 * scale + 0.5f);
				LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(
						pixels, pixels);
				vp.leftMargin = (int) (1 * scale + 0.5f);
				vp.rightMargin = (int) (1 * scale + 0.5f);
				// imageView
				// .setBackgroundColor(Color.parseColor(listColors.get(i)));
				circleView.setLayoutParams(vp);
				linearLayout.addView(circleView);
			}
		}

		return cellView;
	}

	private int getNumberOfRota(DateTime dateTime) {
		int index = 0;
		listColors = new ArrayList<String>();
		for (Rota rota : listRota) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(rota.getDateStarted());
			if (dateTime.getDay() == calendar.get(Calendar.DAY_OF_MONTH)
					&& dateTime.getMonth() - 1 == calendar.get(Calendar.MONTH)) {
				listColors.add(rota.getColor());
				index++;
			}
		}
		return index;
	}
}
