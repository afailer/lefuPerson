package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.lefuyun.R;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.OldPeople;
/**
 *
 */
public class ConcernElderActivityAdapter extends CommonAdapter<OldPeople> {
	
	public ConcernElderActivityAdapter(Context context, List<OldPeople> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, OldPeople t,int position) {
		if("0".equals(t.getStatus())) {
			// 审核中
			holder.getView(R.id.background_item_concern_elder).setVisibility(View.VISIBLE);
			holder.getView(R.id.note_item_concern_elder).setVisibility(View.VISIBLE);
		}else {
			holder.getView(R.id.background_item_concern_elder).setVisibility(View.GONE);
			holder.getView(R.id.note_item_concern_elder).setVisibility(View.GONE);
		}
		holder.setText(R.id.name_item_concern_elder, t.getElderly_name())
		.setText(R.id.age_item_concern_elder, t.getAge() + "岁")
		.setImageByUrl(R.id.img_item_concern_elder, t.getIcon());
	}



}
