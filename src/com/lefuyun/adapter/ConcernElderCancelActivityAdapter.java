package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;

import com.lefuyun.R;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.OldPeople;
/**
 *
 */
public class ConcernElderCancelActivityAdapter extends CommonAdapter<OldPeople> {
	
	public ConcernElderCancelActivityAdapter(Context context, List<OldPeople> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, final OldPeople t,int position) {
		holder.setText(R.id.name_item_concern_elder, t.getElderly_name())
		.setText(R.id.age_item_concern_elder, t.getAge() + "岁")
		.setCircleImageViewByUrl(R.id.img_item_concern_elder, t.getIcon(), t.isSelect());
	}

}
