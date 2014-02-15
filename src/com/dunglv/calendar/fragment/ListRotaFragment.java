package com.dunglv.calendar.fragment;

import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.dunglv.calendar.R;
import com.dunglv.calendar.activity.AddRotaActivity;
import com.dunglv.calendar.activity.EditRotaActivity;
import com.dunglv.calendar.adapter.ListRotaAdapter;
import com.dunglv.calendar.dao.DaoMaster;
import com.dunglv.calendar.dao.DaoMaster.DevOpenHelper;
import com.dunglv.calendar.dao.DaoSession;
import com.dunglv.calendar.dao.Rota;
import com.dunglv.calendar.dao.RotaDao;
import com.dunglv.calendar.dao.RotaDao.Properties;
import com.dunglv.calendar.util.Utils;

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
		adapter = new ListRotaAdapter(getActivity(), listRota);
		mListView.setAdapter(adapter);
		mListView.setDividerHeight(0);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int position, long rowId) {
				Rota rota = (Rota) (mListView.getItemAtPosition(position));
				Bundle bundle = new Bundle();
				bundle.putLong(Utils.ROTA_ID, rota.getId());
				Intent intent = new Intent(getActivity(),
						EditRotaActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
		Log.e("onResume", "onResume");
	}

	public void initDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(),
				"rota-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		rotaDao = daoSession.getRotaDao();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newRota_btn:
			Intent i = new Intent(getActivity(), AddRotaActivity.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

	/**
	 * Refresh listview
	 */
	private void refresh() {
		listRota = rotaDao.queryBuilder().orderDesc(Properties.DateStarted)
				.list();
		adapter.refresh(listRota);
	}
}
