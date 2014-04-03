package com.dunglv.calendar.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.caldroid.caldroidcustom.CaldroidFragment;
import com.caldroid.caldroidcustom.CaldroidGridAdapter;
import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.DayOff;
import com.dunglv.calendar.entity.RotaDay;
import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
    private List<String> listColors;
    private float scale;
    private List<RotaDay> listRotaDay;
    private List<DayOff> listDayOff;
    private List<Long> listDateOff = new ArrayList<Long>();

    public CaldroidSampleCustomAdapter(Context context, int month, int year,
                                       HashMap<String, Object> caldroidData,
                                       HashMap<String, Object> extraData, List<RotaDay> listRotaDay, List<DayOff> listDayOff) {
        super(context, month, year, caldroidData, extraData);
        this.listRotaDay = listRotaDay;
        this.listDayOff = listDayOff;
        for (DayOff dayOff : listDayOff) {
            listDateOff.add(dayOff.getDayOfTime());
        }
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

        TextView dayTextView = (TextView) cellView.findViewById(R.id.tv1);
        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            dayTextView.setTextColor(resources
                    .getColor(R.color.previous_month));
        }

        boolean shouldResetSelectedView = false;

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            if (CaldroidFragment.selectedBackgroundDrawable != -1) {
                cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
            } else {
                cellView.setBackgroundResource(R.drawable.blue_border);
            }

            dayTextView.setTextColor(CaldroidFragment.selectedTextColor);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                dayTextView.setTextColor(resources
                        .getColor(R.color.today_color));
                cellView.setBackgroundColor(resources
                        .getColor(R.color.today_background));
            } else {
                cellView.setBackgroundResource(R.drawable.cell_bg);
            }
        }
        dayTextView.setText("" + dateTime.getDay());
        LinearLayout linearLayout = (LinearLayout) cellView
                .findViewById(R.id.circular_image_view);
        linearLayout.removeAllViews();
        int numberOfRota = getNumberOfRota(dateTime);
        int index = listDateOff.indexOf(dateTime.getMilliseconds(TimeZone.getDefault()));
        // Check sick day
        if (index != -1) {
            if (listDayOff.get(index).getOffType() == 1) {
                linearLayout.setBackgroundColor(Color.GRAY);
            } else if (listDayOff.get(index).getOffType() == 2) {
                linearLayout.setBackgroundColor(Color.RED);
            }

        } else {
            if (numberOfRota > 0) {
                for (int i = 0; i < numberOfRota; i++) {
                    // Restricted 4 round
                    if (i == 4) {
                        break;
                    }
                    MyCircleView circleView = new MyCircleView(context,
                            Color.parseColor(listColors.get(i)));
                    int circleSize = (int) context.getResources().getDimension(R.dimen.circle_size);
                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(
                            circleSize, circleSize);
                    vp.leftMargin = circleSize / 4;
                    vp.rightMargin = circleSize / 4;
                    circleView.setLayoutParams(vp);
                    linearLayout.addView(circleView);
                }
            }
        }

        return cellView;
    }

    private int getNumberOfRota(DateTime dateTime) {
        int index = 0;
        listColors = new ArrayList<String>();
        for (RotaDay rotaDay : listRotaDay) {
            if (dateTime.getDay() == rotaDay.getDay()
                    && dateTime.getMonth() == rotaDay.getMonth()
                    && dateTime.getYear() == rotaDay.getYear()
                    && rotaDay.getDayTime().getStartTime() > 0) {
                listColors.add(rotaDay.getColor());
                index++;
            }
        }
        return index;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        listDateOff = new ArrayList<Long>();
        for (DayOff dayOff : listDayOff) {
            listDateOff.add(dayOff.getDayOfTime());
        }
    }
}
