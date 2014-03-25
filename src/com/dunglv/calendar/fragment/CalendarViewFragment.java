package com.dunglv.calendar.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.caldroid.caldroidcustom.CaldroidFragment;
import com.caldroid.caldroidcustom.CaldroidGridAdapter;
import com.caldroid.caldroidcustom.CaldroidListener;
import com.dunglv.calendar.R;
import com.dunglv.calendar.activity.AddRotaActivity;
import com.dunglv.calendar.adapter.CaldroidSampleCustomAdapter;
import com.dunglv.calendar.adapter.RotaDayAdapter;
import com.dunglv.calendar.dao.*;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DayTimeDao.Properties;
import com.dunglv.calendar.entity.RotaDay;
import com.dunglv.calendar.util.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarViewFragment extends CaldroidFragment {
    public static final int REQUES_ADD_ROTA = 1;
    private SimpleDateFormat formatter;
    View calendarTv;
    public RotaDao rotaDao;
    public DayTimeDao dayTimeDao;
    private List<Rota> listRota;
    private List<RotaDay> listRotaDay;
    private List<Rota> listRotaShow;
    private List<String> listDetailDay;
    private List<DayTime> listDayTime;
    ListView mListView;
    private RotaDayAdapter rotaDayAdapter;
    private CaldroidSampleCustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpCaldroidFragment();
        initRotaDao();
        initDayTimeDao();
        fillDataToList();
    }

    public void fillDataToList() {
        listRota = new ArrayList<Rota>();
        listRota = rotaDao.loadAll();
        listRotaDay = new ArrayList<RotaDay>();
        for (Rota rota : listRota) {
            listDayTime = getListDayTimeDao(rota.getId());
            if (listDayTime.size() > 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(rota.getDateStarted());
                int time = 0;
                if (!rota.getTimeRepeat().isEmpty()
                        && !rota.getTimeRepeat().equals("0")) {
                    time = Integer.parseInt(rota.getTimeRepeat());
                } else {
                    time = 1;
                }
                for (int j = 0; j < time; j++) {
                    // for (int i = 0; i < listDayTime.size(); i++) {
                    for (int i = 0; i < 7 * rota.getWeekReapeat(); i++) {
                        RotaDay rotaDay = new RotaDay();
                        rotaDay.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                        rotaDay.setMonth(calendar.get(Calendar.MONTH) + 1);
                        rotaDay.setYear(calendar.get(Calendar.YEAR));
                        rotaDay.setDateTime(calendar.getTime());
                        rotaDay.setColor(rota.getColor());
                        rotaDay.setRota(rota);
                        rotaDay.setTimeRepeat(time);
                        DayTime dayTime = listDayTime.get(i
                                % listDayTime.size());
                        // Just only add data when daytime has data
                        if (dayTime.getStartTime() > 0) {
                            rotaDay.setDayTime(dayTime);
                            listRotaDay.add(rotaDay);
                        }
                        calendar.add(Calendar.DATE, 1);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mListView = (ListView) v.findViewById(R.id.listRotaDate);
        return v;
    }

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        adapter = new CaldroidSampleCustomAdapter(
                getActivity(), month, year, getCaldroidData(), extraData,
                listRotaDay);
        return adapter;
    }

    public void initRotaDao() {
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
                "rota-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        rotaDao = daoSession.getRotaDao();
    }

    public void initDayTimeDao() {
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
                "dayTime-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        dayTimeDao = daoSession.getDayTimeDao();
    }

    private List<DayTime> getListDayTimeDao(long id) {
        List<DayTime> listDayTime = dayTimeDao.queryBuilder()
                .where(Properties.RotaId.eq(id)).list();
        return listDayTime;

    }

    private void setUpCaldroidFragment() {
        formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        setCaldroidListener(listener);
        startDayOfWeek = Calendar.MONDAY;
    }

    // Setup listener
    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            Log.e(TAG, formatter.format(date));
            listRotaShow = new ArrayList<Rota>();
            listDetailDay = new ArrayList<String>();
            for (RotaDay rotaDay : listRotaDay) {
                if (Utils.isSameDay(rotaDay.getDateTime(), date)) {
                    // Add rota to listRotaShow
                    Rota rota = rotaDay.getRota();
                    listRotaShow.add(rota);

                    // Add detail time to list
                    listDayTime = getListDayTimeDao(rota.getId());

                    // Find day in list Day time
                    int time = rotaDay.getTimeRepeat();
                    for (int i = 0; i < time; i++) {
                        for (DayTime dayTime : listDayTime) {
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(dayTime.getStartTime());
                            c.add(Calendar.DAY_OF_MONTH, listDayTime.size() * i);
                            if (Utils.isSameDay(date, c.getTime())) {
                                String detailTime = dayTime.getStartTime() + ""
                                        + dayTime.getEndTime()
                                        + dayTime.getRotaId();
                                listDetailDay.add(detailTime);
                            }
                        }
                    }
                }
            }
            rotaDayAdapter = new RotaDayAdapter(getActivity(), listRotaShow,
                    listDetailDay);
            mListView.setAdapter(rotaDayAdapter);
            setSelectedDates(date, date);
            refreshView();
        }

        @Override
        public void onChangeMonth(int month, int year) {
            String text = "month: " + month + " year: " + year;
            Log.e(TAG, text);
        }

        @Override
        public void onLongClickDate(final Date date, View view) {
            final CharSequence[] items = {"Add Shift", "Mark as sick", "Mark as annual leave"};

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select options for this date");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            addShift(date);
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        public void onCaldroidViewCreated() {
            if (getLeftArrowButton() != null) {
                Log.e(TAG, "Caldroid view is created");
            }
        }
    };

    /**
     * Add shift by long click
     *
     * @param date
     */
    public void addShift(Date date) {
        Intent intent = new Intent(getActivity(), AddRotaActivity.class);
        intent.putExtra(Utils.START_DATE, date.getTime());
        getActivity().startActivityForResult(intent, REQUES_ADD_ROTA);
    }

    public void markAsSick() {

    }
}
