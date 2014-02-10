package com.dunglv.calendar.fragment;

import java.util.Calendar;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.adapter.ListRotaAdapter;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;

public class ListRotaFragment extends BaseFragment implements OnClickListener {
	private ListRotaAdapter adapter;
	private ListView mListView;
	private List<Rota> listRota;
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private RotaDao rotaDao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_rota_list, container, false);
		mListView = (ListView) v.findViewById(R.id.list_rota);
		Button newRotaBtn = (Button) v.findViewById(R.id.newRota_btn);
		newRotaBtn.setOnClickListener(this);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initDao();
		listRota = rotaDao.loadAll();
		Log.e("size", listRota.size() + "");
		adapter = new ListRotaAdapter(getActivity(), listRota);
		mListView.setAdapter(adapter);
		mListView.setDividerHeight(0);
		addNewRota();
	}

	public void initDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"rota-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
	}

	public void addNewRota() {
		Rota rota = new Rota();
		rota.setName("Rota Name 1");
		rota.setDateStarted(Calendar.getInstance().getTimeInMillis());
		rotaDao.insert(rota);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newRota_btn:
			replaceFragment(new NewRotaFragment(), true);
			break;

		default:
			break;
		}
	}

	/**
	 * Refresh listview
	 */
	private void refresh() {
		listRota = rotaDao.loadAll();
		adapter.refresh(listRota);
	}
}
