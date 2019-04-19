package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.Tourism;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.WindowUtil;

public class TourismFragmentAdapter extends CommonAdapter<Tourism> {
	
	private float ratio = 0.84f;
	private int mHeight;
	private String format = "yyyy年MM月";

	public TourismFragmentAdapter(Context context, List<Tourism> datas,
			int layoutId) {
		super(context, datas, layoutId);
		// 获取屏幕的宽度
		int width = WindowUtil.getWindowWidth((BaseActivity) context);
		// 根据比例获取高度
		mHeight = (int) (width * ratio);
	}

	@Override
	public void convert(ViewHolder holder, Tourism t, int position) {
		// 动态设置条目的高度
		LinearLayout view = holder.getView(R.id.relative_item_tourism_fragment);
		LayoutParams params = view.getLayoutParams();
		params.height = mHeight;
		view.setLayoutParams(params);
		String[] split = t.getIntroduction_img().split(";");
		if(split.length > 0) {
			holder.setImageByUrl(R.id.img_item_tourism_fragment, split[0]);
		}
		holder
		.setText(R.id.name_img_item_tourism_fragment, t.getName())
		.setText(R.id.org_img_item_tourism_fragment, t.getCompany_name())
		.setText(R.id.data_img_item_tourism_fragment, StringUtils.getFormatData(t.getStart_time(), format))
		.setText(R.id.price_item_tourism_fragment, t.getPrice() + "元/人起")
		.setText(R.id.state_item_tourism_fragment, t.getType() == 1 ? "+ 报名" : "已结束")
		.getView(R.id.state_item_tourism_fragment)
		.setBackgroundResource(t.getType() == 1 ? R.drawable.shape_tourism_type_one : R.drawable.shape_tourism_type_two);
		holder.getView(R.id.state_img_item_tourism_fragment)
		.setVisibility(t.getType() == 1 ? View.GONE : View.VISIBLE);
		
	}

}
