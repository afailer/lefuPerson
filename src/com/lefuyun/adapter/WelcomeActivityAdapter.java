package com.lefuyun.adapter;

import java.util.List;

import com.lefuyun.base.BaseActivity;
import com.lefuyun.ui.SwitchActivity;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.WindowUtil;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 *
 */
public class WelcomeActivityAdapter extends PagerAdapter {
	
	private List<ImageView> mList;
	
	private BaseActivity mActivity;
	
	public WelcomeActivityAdapter(BaseActivity activity, List<ImageView> list) {
		mActivity = activity;
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
		ImageView imageView = mList.get(position);
		if(position == 3) {
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String versionName = WindowUtil.getVersionName(mActivity);
					SPUtils.put(mActivity, "isFirst", versionName);
					Intent intent = new Intent(mActivity, SwitchActivity.class);
					mActivity.startActivity(intent);
					mActivity.finish();
				}
			});
		}
		container.addView(imageView);
		return mList.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
