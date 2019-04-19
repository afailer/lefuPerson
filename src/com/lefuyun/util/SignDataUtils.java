package com.lefuyun.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.ShowViewBean;
import com.lefuyun.bean.SignDataBean;
import com.lefuyun.healthcondition.SignDataViewPagerAdapter;
import com.lefuyun.ui.HealthConditionActivity;
import com.lefuyun.ui.HealthConditionIndexActivity;
import com.lefuyun.ui.SignDataRecordActivity;
import com.lefuyun.util.share.ConstantUtil;
import com.lefuyun.widget.SquareForPersondataImageView;
import com.lefuyun.widget.SquareImageView;

@SuppressLint("NewApi")
public class SignDataUtils {
	public static String SHOWVIEW="showview";
	public static String SHOWVIEWBEAN="showviewbean";
	public static String SHOWCHILD_CHILD_VIEW="childview";
	  static List<ShowViewBean>showViewBeans=new ArrayList<ShowViewBean>();
	 /**
     * 初始化下方体征数据的列表
     */
	public static Map<String, Object> initBottomViewPager(Context context){
		     final BaseActivity activity=(BaseActivity) context;
		     Map<String, Object>map=new HashMap<String, Object>();
			List<View>view_childlist=new ArrayList<View>();
			List<LinearLayout>view_child_child_list=new ArrayList<LinearLayout>();
	        int pos;
	        Drawable drawable;
			for(int i=0;i<12;i++){
				ShowViewBean showViewBean=new ShowViewBean();
				switch(i){
				case 0:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_temp);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("体温");
					 showViewBean.setSignType(ConstantUtil.TEMPERATURE);
					 showViewBeans.add(showViewBean);
					 break;
				case 1:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_bloodpressure);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("血压");
					 showViewBean.setSignType(ConstantUtil.BLOODPRESSURE);
					 showViewBeans.add(showViewBean);
					 break;
				case 2:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_sugar);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("血糖");
					 showViewBean.setSignType(ConstantUtil.BLOODSUGAR);
					 showViewBeans.add(showViewBean);
					 break;
				case 3:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_pulse);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("心率");
					 showViewBean.setSignType(ConstantUtil.PULSE);
					 showViewBeans.add(showViewBean);
					 break;
				case 4:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_defecation);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("排便");
					 showViewBean.setSignType(ConstantUtil.DEFECATION);
					 showViewBeans.add(showViewBean);
					 break;
				case 5:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_breath);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("呼吸");
					 showViewBean.setSignType(ConstantUtil.BREATHING);
					 showViewBeans.add(showViewBean);
					 break;
				case 6:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_drink);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("饮水");
					 showViewBean.setSignType(ConstantUtil.DRINKWATER);
					 showViewBeans.add(showViewBean);
					 break;
				 case 7:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_sleep);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("睡眠");
					 showViewBean.setSignType(ConstantUtil.SLEEPING);
					 showViewBeans.add(showViewBean);
					 break;
				 case 8:
					 drawable=context.getResources().getDrawable(R.drawable.signdata_meal);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("饮食");
					 showViewBean.setSignType(ConstantUtil.MEAL);
					 showViewBeans.add(showViewBean);
				   break;
				 case 9:
					 drawable=context.getResources().getDrawable(R.color.white);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("");
					 showViewBean.setSignType(ConstantUtil.MEAL);
					 showViewBeans.add(showViewBean);
				   break;
				 case 10:
					 drawable=context.getResources().getDrawable(R.color.white);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("");
					 showViewBean.setSignType(ConstantUtil.MEAL);
					 showViewBeans.add(showViewBean);
				   break;
				 case 11:
					 drawable=context.getResources().getDrawable(R.color.white);
					 showViewBean.setDrawable(drawable);
					 showViewBean.setName("");
					 showViewBean.setSignType(ConstantUtil.MEAL);
					 showViewBeans.add(showViewBean);
				   break;
				}
			}
	        //每一个展示的方框
			for(int i=0;i<12;i++){
				 LinearLayout view_child_child=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.signdata_showview_item, null);
				 SquareForPersondataImageView image_icon=(SquareForPersondataImageView) view_child_child.findViewById(R.id.image_icon);
				TextView tv_signdata_text=(TextView) view_child_child.findViewById(R.id.tv_signdata_text);
				image_icon.setBackground(showViewBeans.get(i).getDrawable());
				tv_signdata_text.setText(showViewBeans.get(i).getName());
			    view_child_child_list.add(view_child_child);
			}
			//初始化下方的viewpager的数据源
			int m=0;
			int viewpager_child_num = 0;
			int viewpager_num_zheng=view_child_child_list.size()/4;
			int viewpager_num_yu=view_child_child_list.size()%4;
	        //获取viewpager有几个滑动的view
			if(viewpager_num_yu!=0){
				viewpager_child_num=viewpager_num_zheng+1;
			}else{
				viewpager_child_num=viewpager_num_zheng;
			}
			
			for(int j=1;j<=viewpager_child_num;j++){
				//viewpager有几个子view就创建几个子view
				LinearLayout view_child1=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.viewpager_child_item, null);
				LinearLayout liner_viewpager_child=(LinearLayout) view_child1.findViewById(R.id.liner_viewpager_child);
			    if(m+4>=view_child_child_list.size()){
			    	for(int k=m;k<view_child_child_list.size();k++){
			    		LinearLayout.LayoutParams  layoutParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			    		view_child_child_list.get(k).setLayoutParams(layoutParams);
				    	liner_viewpager_child.addView(view_child_child_list.get(k));
				    }
				    view_childlist.add(liner_viewpager_child);
			    }else{
			    	for(int k=m;k<m+4;k++){
			    		//每一个子view都添加4个子view。
			    		LinearLayout.LayoutParams  layoutParams=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			    		view_child_child_list.get(k).setLayoutParams(layoutParams);
			    		liner_viewpager_child.addView(view_child_child_list.get(k));
				    }
				    m=m+4;
				    view_childlist.add(liner_viewpager_child);
			    }
			}
			map.put(SHOWCHILD_CHILD_VIEW,view_child_child_list );
			map.put(SHOWVIEW, view_childlist);
			map.put(SHOWVIEWBEAN,showViewBeans );
			return map;
	}
	/**
	 * 获取老人的状况
	 * @param oldPeople
	 * @return
	 */
	public static String getOldStatusStr(OldPeople oldPeople){
		String str="";
		int score = oldPeople.getScore();
		if(score >= 95) {
			str="优秀";
		}else if(score >= 80) {
			str="良好";
		}else if(score >= 60) {
			str="一般";
		}else {
			str="较差";
		}
		return str;
	}
	
	/**
	 * 根据key获取体征数据字符串
	 * @param type
	 * @return
	 */
	public static String getSigndataStr(int type){
		switch(type){
		   case ConstantUtil.TEMPERATURE:
			   return "体温数据";
		   case ConstantUtil.BLOODPRESSURE:
			   return "血压数据";
		   case ConstantUtil.BLOODSUGAR:
			   return "血糖数据";
		   case ConstantUtil.PULSE:
			   return "心率数据";
		   case ConstantUtil.DEFECATION:
			   return "排便数据";
		   case ConstantUtil.BREATHING:
			   return "呼吸数据";
		   case ConstantUtil.DRINKWATER:
			   return "饮水数据";
		   case ConstantUtil.SLEEPING:
			   return "睡眠数据";
		   case ConstantUtil.MEAL:
			   return "饮食数据";
		   default:
			   return "";
		}
	}
	/**
	 * 根据key获取体征数据的单位
	 * @param type
	 * @return
	 */
	public static String getSignDataUnit(int type){
		switch(type){
		   case ConstantUtil.TEMPERATURE:
			   return "°C";
		   case ConstantUtil.BLOODPRESSURE:
			   return "mmHg";
		   case ConstantUtil.BLOODSUGAR:
			   return "mmol/L";
		   case ConstantUtil.PULSE:
			   return "次/分";
		   case ConstantUtil.DEFECATION:
			   return "次/天";
		   case ConstantUtil.BREATHING:
			   return "次/分";
		   case ConstantUtil.DRINKWATER:
			   return "ml";
		   case ConstantUtil.SLEEPING:
			   return "";
		   case ConstantUtil.MEAL:
			   return "";
		   default:
			   return "";
		}
	}
	/**
	 * 设置显示的体征数据的值 包括单位
	 * 
	 * @param map
	 * @param typePos
	 * @return
	 */
	public static void setShowSignDataStr(Context context,SignDataBean signDataBean,int typePos,TextView tv_value,TextView tv_unit,OldPeople oldPeople) {
		    if(signDataBean==null){
		    	tv_value.setText("");
		    	tv_unit.setText("");
		    	return ;
		    }
		    if(signDataBean.getHasdata()==1){
		    	tv_value.setText("暂无数据");
		    	tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
		    	tv_unit.setText("");
		    	return;
		    }
		    String showstr = "";
			if (typePos == ConstantUtil.BLOODPRESSURE) {
				showstr =signDataBean.getVal1() + "/"+ signDataBean.getVal2();
				showdifferentcolor(context, showstr, typePos, tv_value, oldPeople);
				tv_unit.setText(SignDataUtils.getSignDataUnit(typePos));
			} else if (typePos == ConstantUtil.DEFECATION) {
				showstr = signDataBean.getDefecation_times() + "";
				showdifferentcolor(context, showstr, typePos, tv_value, oldPeople);
				tv_unit.setText(SignDataUtils.getSignDataUnit(typePos));
			} else if (typePos == ConstantUtil.BREATHING) {
				showstr = signDataBean.getBreathing_times() + "";
				showdifferentcolor(context, showstr, typePos, tv_value, oldPeople);
				tv_unit.setText(SignDataUtils.getSignDataUnit(typePos));
			} else if (typePos == ConstantUtil.DRINKWATER) {
				showstr = signDataBean.getWater_amount() + "";
				showdifferentcolor(context, showstr, typePos, tv_value, oldPeople);
				tv_unit.setText(SignDataUtils.getSignDataUnit(typePos));
			} else if (typePos == ConstantUtil.SLEEPING) {
				showstr = signDataBean.getSleep_quality();
				showdifferentcolor(context, showstr, typePos, tv_value, oldPeople);
				tv_unit.setText(SignDataUtils.getSignDataUnit(typePos));
			} else if (typePos == ConstantUtil.MEAL) {
				showstr = getEatShowstr(signDataBean.getMeal_type(),
						signDataBean.getMeal_amount());
				showdifferentcolor(context, showstr, typePos, tv_value, oldPeople);
				tv_unit.setText(SignDataUtils.getSignDataUnit(typePos));
			} else {
				showstr = signDataBean.getVal1();
				showdifferentcolor(context, showstr, typePos, tv_value, oldPeople);
				tv_unit.setText(SignDataUtils.getSignDataUnit(typePos));
			}
	}
	public static void showdifferentcolor(Context context,String value,int signtype,TextView tv_value,OldPeople oldPeople){
		if(value==null||"".equals(value)){
			 tv_value.setText("");
			 return ;
		}
		//没有缓存配置的json的时候用默认的
		String config_json_data=ConfigUtils.getContentStr(context);
		String old_json_data=(String) SPUtils.get(context, SPUtils.OLDPEOPLE_JSON, "");
		if("".equals(config_json_data)||"".equals(old_json_data)||config_json_data==null||old_json_data==null){
			switch(signtype){
			 case ConstantUtil.TEMPERATURE:
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.TEMPERATURE, value));
				 break;
			 case ConstantUtil.BLOODPRESSURE:
				 String pressure[]=value.split("/");
				 if(!"null/null".equals(value)&&pressure[0]!=null&&!"".equals(pressure[0])&&pressure[1]!=null&&!"".equals(pressure[1])){
					 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BLOODPRESSURE, Double.parseDouble(pressure[0]))+"/"+ConfigUtils.numberFormat(context, ConstantUtil.BLOODPRESSURE, Double.parseDouble(pressure[1])));
				 }
				 break;
			 case ConstantUtil.BLOODSUGAR:
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BLOODSUGAR, value));
				 break;
			 case ConstantUtil.PULSE:
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.PULSE, value));
				 break;
			 case ConstantUtil.DEFECATION:
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.DEFECATION, value));
				 break;
			 case ConstantUtil.BREATHING:
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BREATHING, value));
				 break;
			 case ConstantUtil.DRINKWATER:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			 case ConstantUtil.SLEEPING:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			 case ConstantUtil.MEAL:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			}
		    return ;
		}
		//度默认配置
		if(oldPeople.getConfig()==null||"".equals(oldPeople.getConfig())){
			LogUtil.i("value", value);
			switch(signtype){
			 case ConstantUtil.TEMPERATURE:
				 ConfigUtils.showDifferentColor_temp(context, Double.parseDouble(value), tv_value, null);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.TEMPERATURE, value));
				break;
			 case ConstantUtil.BLOODPRESSURE:
				 String pressure[]=value.split("/");
				 if(!"null/null".equals(value)&&pressure[0]!=null&&!"".equals(pressure[0])&&pressure[1]!=null&&!"".equals(pressure[1])){
					 ConfigUtils.showDifferentColor_pressure(context, Integer.parseInt(pressure[0]), Integer.parseInt(pressure[1]), tv_value,null);
					 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BLOODPRESSURE, Double.parseDouble(pressure[0]))+"/"+ConfigUtils.numberFormat(context, ConstantUtil.BLOODPRESSURE, Double.parseDouble(pressure[1])));
				 }
				 break;
			 case ConstantUtil.BLOODSUGAR:
				 ConfigUtils.showDifferentColor_sugar(context,Double.parseDouble(value), tv_value, null);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BLOODSUGAR, value));
				 break;
			 case ConstantUtil.PULSE:
				 ConfigUtils.showDifferentColor_pulse(context, Integer.parseInt(value), tv_value, null);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.PULSE, value));
				 break;
			 case ConstantUtil.DEFECATION:
				 ConfigUtils.showDifferentColor_defecation(context, Integer.parseInt(value), tv_value, null);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.DEFECATION, value));
				 break;
			 case ConstantUtil.BREATHING:
				 ConfigUtils.showDifferentColor_breath(context, Integer.parseInt(value), tv_value, null);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BREATHING, value));
				 break;
			 case ConstantUtil.DRINKWATER:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			 case ConstantUtil.SLEEPING:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			 case ConstantUtil.MEAL:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			}
		}else{//读自己的配置
			LogUtil.i("value", value);
			switch(signtype){
			 case ConstantUtil.TEMPERATURE:
				 ConfigForSpeialOldUtils.showDifferentColor_temp(context, Double.parseDouble(value), tv_value, null,oldPeople);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.TEMPERATURE, value));
				break;
			 case ConstantUtil.BLOODPRESSURE:
				 String pressure[]=value.split("/");
				 if(!"null/null".equals(value)&&pressure[0]!=null&&!"".equals(pressure[0])&&pressure[1]!=null&&!"".equals(pressure[1])){
					 ConfigUtils.showDifferentColor_pressure(context, Integer.parseInt(pressure[0]), Integer.parseInt(pressure[1]), tv_value,null);
					 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BLOODPRESSURE, Double.parseDouble(pressure[0]))+"/"+ConfigUtils.numberFormat(context, ConstantUtil.BLOODPRESSURE, Double.parseDouble(pressure[1])));
				 }
				 break;
			 case ConstantUtil.BLOODSUGAR:
				 ConfigForSpeialOldUtils.showDifferentColor_sugar(context,Double.parseDouble(value), tv_value, null,oldPeople);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BLOODSUGAR, value));
				 break;
			 case ConstantUtil.PULSE:
				 ConfigForSpeialOldUtils.showDifferentColor_pulse(context, Integer.parseInt(value), tv_value, null,oldPeople);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.PULSE,value));
				 break;
			 case ConstantUtil.DEFECATION:
				 ConfigForSpeialOldUtils.showDifferentColor_defecation(context, Integer.parseInt(value), tv_value, null,oldPeople);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.DEFECATION,value));
				 break;
			 case ConstantUtil.BREATHING:
				 ConfigForSpeialOldUtils.showDifferentColor_breath(context, Integer.parseInt(value), tv_value, null,oldPeople);
				 tv_value.setText(ConfigUtils.numberFormat(context, ConstantUtil.BREATHING, value));
				 break;
			 case ConstantUtil.DRINKWATER:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			 case ConstantUtil.SLEEPING:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			 case ConstantUtil.MEAL:
				 tv_value.setText(value);
				 tv_value.setTextColor(context.getResources().getColor(R.color.main_text));
				 break;
			}
		}
	}
	/**
	 * 获取要显示的饮食的字符串
	 * 
	 * @param meal_type
	 * @param meal_amount
	 * @return
	 */
	public static String getEatShowstr(int meal_type, int meal_amount) {
		String data_str = "";
		switch (meal_type) {
		case 1:
			data_str = "早餐";
			break;
		case 2:
			data_str = "午餐";
			break;
		case 3:
			data_str = "晚餐";
			break;
		}
		switch (meal_amount) {
		case 1:
			data_str = data_str + "偏少";
			break;
		case 2:
			data_str = data_str + "正常";
			break;
		case 3:
			data_str = data_str + "偏多";
			break;
		}
		return data_str;
	}
}
