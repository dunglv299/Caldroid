package com.dunglv.calendar.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.util.Utils;

public class RotaDayAdapter extends BaseAdapter {
	private List<Rota> listRota;
	private List<String> listDetailDay;
	private LayoutInflater mInflator;
	private Context context;

	public RotaDayAdapter(Activity a, List<Rota> listRota,
			List<String> listDetailDay) {
		mInflator = a.getLayoutInflater();
		this.listRota = listRota;
		this.listDetailDay = listDetailDay;
		this.context = a;
	}

	@Override
	public int getCount() {
		return listRota.size();
	}

	@Override
	public Object getItem(int i) {
		return listRota.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void refresh(List<Rota> listRota) {
		this.listRota = listRota;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		// General ListView optimization code.
		if (view == null) {
			view = mInflator.inflate(R.layout.single_rotaday_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.mRectangle = view.findViewById(R.id.rectangleView);
			viewHolder.mRotaName = (TextView) view.findViewById(R.id.rota_name);
			viewHolder.mStartTime = (TextView) view
					.findViewById(R.id.startTime_tv);
			viewHolder.mEndTime = (TextView) view.findViewById(R.id.endTime_tv);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// Bind that data efficiently!
		String detail = "";
		if (listDetailDay.size() > 0) {
			detail = listDetailDay.get(position % listDetailDay.size());
		}
		long startTime = 0;
		long endTime = 0;
		long rotaId = 0;
		if (detail != null && !detail.isEmpty()) {
			startTime = Long.valueOf(detail.substring(0, 13));
			endTime = Long.valueOf(detail.substring(13, 26));
			rotaId = Long.valueOf(detail.substring(26, 27));
		}
		Rota rota = listRota.get(position);
		if (rotaId == rota.getId()) {
			if (rota.getName().isEmpty()) {
				viewHolder.mRotaName.setText("Untitled");
			} else {
				viewHolder.mRotaName.setText(rota.getName());
			}

			setTimeText(viewHolder.mStartTime, startTime, true);
			setTimeText(viewHolder.mEndTime, endTime, false);
			GradientDrawable bgShape = (GradientDrawable) viewHolder.mRectangle
					.getBackground();
			bgShape.setColor(Color.parseColor(rota.getColor()));
		} else {
			viewHolder.mRotaName.setVisibility(View.GONE);
			viewHolder.mStartTime.setVisibility(View.GONE);
			viewHolder.mEndTime.setVisibility(View.GONE);
			viewHolder.mRectangle.setVisibility(View.GONE);
		}
		return view;
	}

	static class ViewHolder {
		View mRectangle;
		TextView mRotaName;
		TextView mStartTime;
		TextView mEndTime;
	}

	private void setTimeText(TextView tv, long time, boolean haicham) {
		if (time == 0) {
			tv.setText("");
		} else {
			if (haicham) {
				tv.setText(Utils.convertLongToTime(time) + " - ");
				return;
			}
			tv.setText(Utils.convertLongToTime(time));

		}
	}

}
