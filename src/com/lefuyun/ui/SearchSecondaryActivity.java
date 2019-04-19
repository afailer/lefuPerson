package com.lefuyun.ui;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lefuyun.R;
import com.lefuyun.adapter.SearchSecondaryAdapter;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.City;
import com.lefuyun.db.CityDao;
import com.lefuyun.util.LogUtil;

public class SearchSecondaryActivity extends BaseActivity implements OnItemClickListener{
	
	private ListView mListView;
	
	private SearchSecondaryAdapter mAdapter;
	
	private City mCity;
	
	private String mCityName;
	
	private CityDao mCityDao;

	private List<City> mList;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_search_secondary;
	}

	@Override
	protected void initView() {
		mCityDao = new CityDao(getApplicationContext());
		mListView = (ListView) findViewById(R.id.lv_search_secondary_activity);
		
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mCity = (City) intent.getSerializableExtra("city");
		mCityName = mCity.getRegion_name();
		setActionBarTitle(mCityName);
		mList = mCityDao.findCitysById(mCity.getId());
		mCity.setRegion_name("全部");
		mList.add(0, mCity);
		mAdapter = new SearchSecondaryAdapter(this, mList, R.layout.item_activity_search_secondary);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	public void onClick(View v) {}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		City city = mList.get(position);
		if(position == 0) {
			city.setRegion_name(mCityName);
		}
		Intent intent = new Intent();
		LogUtil.e("searchSecondCity", city.getRegion_name()+" "+mCityName);
		intent.putExtra("city", city);
		setResult(1000, intent);
		finish();
	}
	
}
