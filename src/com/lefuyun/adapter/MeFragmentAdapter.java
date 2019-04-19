package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.lefuyun.R;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.PersonalPreference;

public class MeFragmentAdapter extends CommonAdapter<PersonalPreference> {

	public MeFragmentAdapter(Context context, List<PersonalPreference> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, PersonalPreference t,int position) {
		holder.setText(R.id.title_item_me_fragment, t.getTitle())
		.setText(R.id.version_item_me_fragment, t.getMessage());
		if(TextUtils.isEmpty(t.getTitle().trim())) {
			holder.getView(R.id.img_item_me_fragment).setVisibility(View.GONE);
			holder.setViewBackGroundWithColor(R.id.line_item_me_fragment, Color.WHITE);
			holder.setViewBackGroundWithRes(R.id.view_me_fragment, 0);
		}else {
			holder.getView(R.id.img_item_me_fragment).setVisibility(View.VISIBLE);
			holder.setViewBackGroundWithColor(R.id.line_item_me_fragment, Color.parseColor("#c8c7cc"));
			holder.setViewBackGroundWithRes(R.id.view_me_fragment, R.drawable.selector_item_listview_click);
		}
	}
}
