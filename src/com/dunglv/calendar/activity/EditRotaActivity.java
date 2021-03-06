package com.dunglv.calendar.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.dunglv.calendar.R;
import com.dunglv.calendar.dao.DayTime;
import com.dunglv.calendar.dao.DayTimeDao.Properties;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.util.Utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditRotaActivity extends RotaActivity implements OnClickListener {
    private long rotaId;
    private Rota rota;
    private long previousDay;
    private String calendarUri;
    private String[] arrayUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rotaId = getIntent().getExtras().getLong(Utils.ROTA_ID);
        rota = rotaDao.queryBuilder().where(Properties.Id.eq(rotaId)).list()
                .get(0);
        mNameEd.setText(rota.getName());
        mWeekEd.setText(rota.getWeekReapeat() + "");
        mRepeatTimeEd.setText(rota.getTimeRepeat());

        // For color
        int indexColor = Arrays.asList(colorsArray).indexOf(rota.getColor());
        colorSpinner.setSelection(indexColor);

        // Google sync
        mCheckBoxGoogle.setChecked(rota.getIsGoogleSync());
        calendarUri = rota.getCalendarUri();
        calendar.setTimeInMillis(rota.getDateStarted());

        // For reminder
        int indexReminder = Arrays.asList(reminderArray).indexOf(
                rota.getReminderTime());
        if (indexReminder == -1) {
        } else {
            reminderSpinner.setSelection(indexReminder);
        }
        // Start date btn
        startDate = rota.getDateStarted();
        previousDay = startDate;
        startDateBtn.setText(Utils.convertLongToTime("dd/MM/yyyy", startDate));

    }

    @Override
    public void initView() {
        super.initView();
        setTitle("Edit Rota");
    }

    @Override
    public Rota getRota() {
        rota = super.getRota();
        rota.setId(rotaId);
        rota.setDateStarted(startDate);
        if (!calendarUri.isEmpty()) {
            rota.setCalendarUri(calendarUri);
        }
        return rota;
    }

    @Override
    public void onContinue() {
        changeDate();
        if (getRota().getWeekReapeat() == 0) {
            Utils.showAlert(this, getString(R.string.alert_week));
            return;
        }
        super.onContinue();
        rotaDao.update(getRota());
        sharedPreferences.putLong(Utils.ROTA_ID, rotaId);
    }

    @Override
    public void onSave() {
        super.onSave();
        changeDate();
        rotaDao.update(getRota());
        deleteDayChangeWeek(getRota().getId());
        finish();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.delete_btn:
                rotaDao.delete(getRota());
                deleteDayTimeDao(rotaId);
                finish();
                break;
            default:
                break;
        }
    }

    public void deleteDayTimeDao(long rotaId) {
        initDayTimeDao();
        List<DayTime> listDelete = dayTimeDao.queryBuilder()
                .where(Properties.RotaId.eq(rotaId)).list();
        cancelRemind(listDelete);
        // Delete event
        arrayUri = calendarUri.split(",");
        for (int i = 0; i < arrayUri.length; i++) {
            deleteEvent(arrayUri[i]);
        }
        dayTimeDao.deleteInTx(listDelete);
    }

    public void deleteDayChangeWeek(long rotaId) {
        List<DayTime> listDelete = dayTimeDao
                .queryBuilder()
                .where(Properties.RotaId.eq(rotaId),
                        Properties.DayId.ge(getRota().getWeekReapeat() * 7))
                .list();
        dayTimeDao.deleteInTx(listDelete);
    }

    public void changeDate() {
        // Get change Time
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(previousDay);
        cal2.setTimeInMillis(startDate);
        previousDay = startDate;
        int plusDay = cal2.get(Calendar.DAY_OF_YEAR)
                - cal1.get(Calendar.DAY_OF_YEAR);
        initDayTimeDao();
        List<DayTime> listChange = dayTimeDao
                .queryBuilder()
                .where(Properties.RotaId.eq(rotaId), Properties.StartTime.gt(0))
                .list();
        for (DayTime dayTime : listChange) {
            Calendar cal = Calendar.getInstance();
            // Stat time
            cal.setTimeInMillis(dayTime.getStartTime());
            cal.add(Calendar.DAY_OF_YEAR, plusDay);
            dayTime.setStartTime(cal.getTimeInMillis());

            // End time
            cal.setTimeInMillis(dayTime.getEndTime());
            cal.add(Calendar.DAY_OF_YEAR, plusDay);
            dayTime.setEndTime(cal.getTimeInMillis());
            dayTimeDao.update(dayTime);
        }
    }

    public void cancelRemind(List<DayTime> listRemind) {
        // Notification
        for (DayTime dayTime : listRemind) {
            cancelNotification(dayTime.getId().intValue());
        }
    }

    public void cancelNotification(int notificationId) {
        if (Context.NOTIFICATION_SERVICE != null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getSystemService(ns);
            nMgr.cancel(notificationId);
        }
    }

    private void deleteEvent(String uri) {
        try {
            getContentResolver().delete(Uri.parse(uri), null, null);
            Log.e("delete uri", uri + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
