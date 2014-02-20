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

public class RotaDayAdapter extends BaseAdapter {
	private List<Rota> listRota;
	private LayoutInflater mInflator;
	private Context context;

	public RotaDayAdapter(Activity a, List<Rota> listRota) {
		mInflator = a.getLayoutInflater();
		this.listRota = listRota;
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
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// Bind that data efficiently!
		Rota rota = listRota.get(position);
		if (rota.getName().isEmpty()) {
			viewHolder.mRotaName.setText("Untitled");
		} else {
			viewHolder.mRotaName.setText(rota.getName());
		}

		GradientDrawable bgShape = (GradientDrawable) viewHolder.mRectangle
				.getBackground();
		bgShape.setColor(Color.parseColor(rota.getColor()));

		return view;
	}

	static class ViewHolder {
		View mRectangle;
		TextView mRotaName;
	}

}
