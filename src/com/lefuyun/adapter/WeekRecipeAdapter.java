package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.base.adapter.CommonPagerAdapter;
import com.lefuyun.bean.WeekRecipe;
import com.lefuyun.bean.WeekRecipe.pic;
import com.lefuyun.ui.PhotoViewActivity;

public class WeekRecipeAdapter extends CommonPagerAdapter<WeekRecipe.pic>{
	public WeekRecipeAdapter(Context context, List<WeekRecipe.pic> datas, int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void processItem(View view, final pic t, int position) {
		ImageView img=(ImageView) view.findViewById(R.id.recipe_dialog_img);
		ImageLoader.loadImg(t.getUrlString(), img);
		TextView title=(TextView) view.findViewById(R.id.recipe_dialog_title);
		TextView content=(TextView) view.findViewById(R.id.recipe_dialog_content);
		title.setText(t.getMeatName());
		content.setText(t.getReserved());
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View position) {
				Intent intent=new Intent(mContext, PhotoViewActivity.class);
				intent.putExtra("path", t.getUrlString());
				mContext.startActivity(intent);
			}
		});
	}


}
