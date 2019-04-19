package com.lefuyun.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;

public class ViewHolder {

	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}
	
	public static ViewHolder get(Context context, View convertView, 
			ViewGroup parent, int layoutId, int position) {
		if(convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			// holder已经复用了，但是他的position已经改变，故我们将这个位置记录
			holder.mPosition = position;
			return holder;
		}
	}
	/**
	 * 通过viewId获取控件
	 * @param viewId
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if(view == null) {
			view = mConvertView.findViewById(viewId);
			if(view == null) {
				throw new NullPointerException("viewId not find!");
			}
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	public View getConvertView() {
		return mConvertView;
	}
	
	public int getPosition() {
		return mPosition;
	}
	
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	public ViewHolder setBackgroundColor(Context context,int viewId,int position){
		TextView tv = getView(viewId);
		int pos=position%6;
		tv.setBackgroundDrawable(context.getResources().getDrawable(Utils.getIntDrawable(pos)));
		return this;
	}
	public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }
	
	public ViewHolder setViewBackGroundWithColor(int viewId, int color) {
		View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
	}
	public ViewHolder setViewBackGroundWithRes(int viewId, int resId) {
		View view = getView(viewId);
		view.setBackgroundResource(resId);
		return this;
	}
	public ViewHolder setImageByUrl(int viewId,String uri){
		ImageView img = getView(viewId);
		ImageLoader.loadImg(uri, img);
		return this;
	}
	public ViewHolder setCircleImageByResource(int viewId,int resId){
		CircleImageView img=getView(viewId);
		img.setImageResource(resId);
		return this;
	}
	public ViewHolder setImageByUrl(int viewId,String uri, 
			int width, int height){
		ImageView img = getView(viewId);
		ImageLoader.loadImg(uri, width, height, img);
		return this;
	}
	public ViewHolder goneView(int viewId){
		View view = getView(viewId);
		view.setVisibility(View.GONE);
		return this;
	}
	public ViewHolder visibleView(int viewId){
		View view = getView(viewId);
		view.setVisibility(View.VISIBLE);
		return this;
	}
	public ViewHolder inVisibleView(int viewId){
		View view = getView(viewId);
		view.setVisibility(View.INVISIBLE);
		return this;
	}
	public ViewHolder setCircleImageViewByUrl(int viewId,String uri, boolean isSelect){
		CircleImageView img = getView(viewId);
		ImageLoader.loadImg(uri, img);
		img.setmSelectOn(isSelect);
		return this;
	}
}
