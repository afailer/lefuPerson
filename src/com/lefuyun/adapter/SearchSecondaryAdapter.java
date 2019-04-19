package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;

import com.lefuyun.R;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.City;

public class SearchSecondaryAdapter extends CommonAdapter<City> {

	public SearchSecondaryAdapter(Context context, List<City> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, City t,int position) {
		holder.setText(R.id.city_search_secondary_item, t.getRegion_name());
	}


}
