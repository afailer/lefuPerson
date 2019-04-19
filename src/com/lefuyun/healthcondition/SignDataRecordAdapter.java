package com.lefuyun.healthcondition;

import java.util.List;

import com.lefuyun.R;
import com.lefuyun.bean.ConfigForSpecialOldBean;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.SignDataBean;
import com.lefuyun.util.ConfigForSpeialOldUtils;
import com.lefuyun.util.ConfigUtils;
import com.lefuyun.util.DateUtils;
import com.lefuyun.util.SignDataUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.util.share.ConstantUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author chenshichao
 * @date   2016-5-11
 * @description 体征数据记录的适配器
 */
public class SignDataRecordAdapter extends BaseAdapter{
    List<SignDataBean>list;
    Context context;
    int signtype;
    OldPeople oldPeople;
    public SignDataRecordAdapter( Context context,List<SignDataBean>list,int signtype,OldPeople oldPeople){
    	this.context=context;
    	this.list=list;
    	this.signtype=signtype;
    	this.oldPeople=oldPeople;
    }
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.signdata_listview_item,null);
		}
		TextView tv_date=ViewHolder.get(convertView, R.id.tv_date);
		TextView tv_time=ViewHolder.get(convertView, R.id.tv_time);
		TextView tv_value=ViewHolder.get(convertView, R.id.tv_value);
		TextView tv_unit=ViewHolder.get(convertView, R.id.tv_unit);
		TextView tv_remark=ViewHolder.get(convertView, R.id.tv_remark);
		//2013-08-14 09:12:59
		String dtstr=DateUtils.getStringtimeFromLong(list.get(position).getInspect_dt());
		String dateSub=dtstr.substring(0,10);
		String timeSub=dtstr.substring(11,19);
		tv_date.setText(dateSub);
		tv_time.setText(timeSub);
		String unit=SignDataUtils.getSignDataUnit(signtype);
		tv_unit.setText(unit);
		tv_remark.setText(list.get(position).getReserved());
		SignDataUtils.setShowSignDataStr(context,list.get(position),signtype,tv_value,tv_unit,oldPeople);
		return convertView;
	}

}
