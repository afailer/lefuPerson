package com.lefuyun.healthcondition;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author chenshichao
 * @date   2016-5-20
 * @description 垂直滚动的数据的适配器
 */
public class SigndataVerticalAdapter extends PagerAdapter{
    List<View>view_list;
    public SigndataVerticalAdapter(List<View>view_list){
    	this.view_list=view_list;
    }
	@Override 
	public int getCount() {
		return view_list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
    @Override
	public void destroyItem(ViewGroup container, int position, Object object) {
     	container.removeView(view_list.get(position));
	}
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	if(view_list.get(position).getParent()==null){
			container.addView(view_list.get(position));
		}else{
			((ViewGroup)view_list.get(position).getParent()).removeView(view_list.get(position));
			container.addView(view_list.get(position));
		}
		return view_list.get(position);
    }
  
 

}
