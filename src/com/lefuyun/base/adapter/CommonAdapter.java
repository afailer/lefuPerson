package com.lefuyun.base.adapter;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {
	
	// 上下文
	private Context mContext;
	// 数据源
	private List<T> mDatas;
	// 布局文件ID
	private int mLayoutId;
	
	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		super();
		this.mContext = context;
		this.mDatas = datas;
		this.mLayoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mLayoutId, position);
		convert(holder, getItem(position),position);
		return holder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder, T t,int position);
	
	public void addData(List<T> datas){
		mDatas.addAll(datas);
		this.notifyDataSetChanged();
	}
	public void addSingleData(T data){
		mDatas.add(data);
		this.notifyDataSetChanged();
	}
	public void clearData(){
		mDatas.clear();
		this.notifyDataSetChanged();
	}
	public void setData(List<T> datas){
		mDatas.clear();
		mDatas.addAll(datas);
		this.notifyDataSetChanged();
	}
	public void removeData(T t){
		mDatas.remove(t);
		this.notifyDataSetChanged();
	}
}
