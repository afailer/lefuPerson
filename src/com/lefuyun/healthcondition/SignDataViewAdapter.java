package com.lefuyun.healthcondition;

import java.util.ArrayList;
import java.util.List;

import com.lefuyun.R;
import com.lefuyun.bean.ShowViewBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author chenshichao
 * @date   2016-5-10
 * @description  健康状况中下部的体征数据的适配器
 */
public class SignDataViewAdapter extends BaseAdapter{
    List<ShowViewBean>showViewBeans;
    Context context;
    public SignDataViewAdapter( Context context,List<ShowViewBean>showViewBeans){
    	this.context=context;
    	this.showViewBeans=showViewBeans;
    }
	@Override
	public int getCount() {
		return showViewBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return showViewBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.signdata_showview_item, null);
		}
		ImageView image_icon=ViewHolder.get(convertView,R.id.image_icon);
		TextView tv_signdata_text=ViewHolder.get(convertView, R.id.tv_signdata_text);
		image_icon.setImageDrawable(showViewBeans.get(position).getDrawable());
		tv_signdata_text.setText(showViewBeans.get(position).getName());
		return convertView;
	}

}
