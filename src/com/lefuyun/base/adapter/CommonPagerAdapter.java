package com.lefuyun.base.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class CommonPagerAdapter<T> extends PagerAdapter{
	
	// 上下文
		public Context mContext;
		// 数据源
		private List<T> mDatas;
		// 布局文件ID
		private int mLayoutId;
		
		public CommonPagerAdapter(Context context, List<T> datas, int layoutId) {
			super();
			this.mContext = context;
			this.mDatas = datas;
			this.mLayoutId = layoutId;
		}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}
	public T getData(int position){
		try{
		return mDatas.get(position);
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(mContext, mLayoutId, null);
		processItem(view, mDatas.get(position),position);
		container.addView(view);
		return view;
	}
	public void refreshData(List<T> datas){
		mDatas.clear();
		//mDatas.addAll(datas);
		this.notifyDataSetChanged();
	}
	public void deleteData(){
		mDatas.clear();
		this.notifyDataSetChanged();
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}
	public abstract void processItem(View view,T t,int position);
	
	
}
