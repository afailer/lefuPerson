package com.lefuyun.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.News;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.WindowUtil;

public class NewsDetailsFragmentAdapter extends CommonAdapter<News>{
	
	private float ratio = 0.66f;
	
	private String format = "yyyy-MM-dd HH:mm";
	private int mHeight;

	public NewsDetailsFragmentAdapter(Context context, List<News> datas,
			int layoutId) {
		super(context, datas, layoutId);
		// 获取屏幕的宽度
		int width = WindowUtil.getWindowWidth((BaseActivity) context);
		// 根据比例获取高度
		mHeight = (int) (width * ratio);
		
	}

	@SuppressLint("NewApi")
	@Override
	public void convert(ViewHolder holder, News t,int position) {
		// 动态设置条目的高度
		LinearLayout view = holder.getView(R.id.relative_news_details_fragment);
		LayoutParams params = view.getLayoutParams();
		params.height = mHeight;
		view.setLayoutParams(params);
		holder.setImageByUrl(R.id.img_item_news_details_fragment, 
				t.getPicture())
		.setText(R.id.title_item_news_details_fragment, t.getTheme())
		.setText(R.id.type_item_news_details_fragment, t.getType() == 1 ? "养老" : "养生")
		.setText(R.id.date_item_news_details_fragment, StringUtils.getFormatData(t.getCreate_dt(), format));
		holder.getView(R.id.type_item_news_details_fragment).setBackgroundResource(
				t.getType() == 1 ? R.drawable.shape_news_type_one : R.drawable.shape_news_type_two);
	}

}
