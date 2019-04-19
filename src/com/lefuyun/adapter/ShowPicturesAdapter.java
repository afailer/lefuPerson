package com.lefuyun.adapter;

import java.util.List;

import com.lefuyun.api.remote.ImageLoader;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
/**
 *
 */
public class ShowPicturesAdapter extends PagerAdapter {
	
	private Context mContext;
	private List<String> mList;
	
	public ShowPicturesAdapter(Context context, List<String> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView  = new ImageView(mContext);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		ImageLoader.loadImg(mList.get(position), imageView);
		container.addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
