package com.lefuyun.healthcondition;

import java.util.List;

import com.lefuyun.bean.ShowViewBean;
import com.lefuyun.bean.SignDataBean;
import com.lefuyun.util.LogUtil;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SignDataViewPagerAdapter extends PagerAdapter {
	private List<View> mListViews;
	List<ShowViewBean>showViewBeans;
	public SignDataViewPagerAdapter(List<View> mListViews,List<ShowViewBean>showViewBeans) {
		this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
	    this.showViewBeans=showViewBeans;
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if(mListViews.get(position).getParent()==null){
			container.addView(mListViews.get(position));
		}else{
			((ViewGroup)mListViews.get(position).getParent()).removeView(mListViews.get(position));
			container.addView(mListViews.get(position));
		}
		return mListViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mListViews.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}