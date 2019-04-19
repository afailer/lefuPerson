package com.lefuyun.util;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.lefuyun.bean.ColorBean;
import com.lefuyun.bean.ContentTotal;
import com.lefuyun.bean.Line;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.SignConfigBean;
import com.lefuyun.util.share.ConstantUtil;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class ConfigUtils {
	
	 /**
	 *  配置的字符串
	 */
	public static String contentjson;
	public static SignConfigBean tempconfigBean;
	public static SignConfigBean pressureconfigBean;
	public static SignConfigBean sugarconfigBean;
	public static SignConfigBean pulseconfigBean;
	public static SignConfigBean defecationconfigBean;
	public static SignConfigBean breathconfigBean;
	public static final String a = "。";
	
	 // 非法特征值
	 public static final int SIGNS_DATA_ILLEGAL = 1;
	 // 正常特征值
	 public static final int SIGNS_DATA_NORMAL = 2;
	 // 合法但不正常特征值
	 public static final int SIGNS_DATA_ABNORMAL = 3;
	 static DecimalFormat df;
	 private static DecimalFormat getDf(int num){
	    		String s;
		    	if (num==1) {
		    		s = "#.0";
		    	} else if (num==2) {
		    		s = "#.00";
		    	} else {
		    		s="#";
		    	}
		    	df = new DecimalFormat(s);
	    	return df;
	    }
	 public static String getString(int num,float f){
		 return getDf(num).format(f);
	 }
	 //获取体温配置信息
	 public  static SignConfigBean getTemperatureConfig(Context context){
		 if(contentjson==null||"".equals(contentjson)){
			  contentjson= getContentStr(context);
		 }
		 if(tempconfigBean==null){
			   JSONObject jsonObject = null;
			   tempconfigBean=new SignConfigBean();
			   List<ColorBean>colorBeans=new ArrayList<ColorBean>();
			   List<Line>lines=new ArrayList<Line>();
			   if(!"".equals(contentjson)){
					try {
						jsonObject = new JSONObject(contentjson);
						JSONObject jsonObject_temp=jsonObject.getJSONObject("temperature");
						tempconfigBean=(SignConfigBean) JsonUtil.jsonToBean(jsonObject.optString("temperature"),SignConfigBean.class);
						Type typenews = new TypeToken<List<ColorBean>>() {}.getType();
						colorBeans=(List<ColorBean>) JsonUtil.jsonToList(jsonObject_temp.optString("color"), typenews);
						Type type_line = new TypeToken<List<Line>>() {}.getType();
						lines=(List<Line>) JsonUtil.jsonToList(jsonObject_temp.optString("line"), type_line);
						tempconfigBean.setColor(colorBeans);
						tempconfigBean.setLine(lines);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		 }
		return tempconfigBean;
	 }
	 //获取血压配置信息
	 public  static SignConfigBean getPressureConfig(Context context){
		 if(contentjson==null||"".equals(contentjson)){
			  contentjson= getContentStr(context);
		 }
		 if(pressureconfigBean==null){
			 JSONObject jsonObject = null;
			 pressureconfigBean=new SignConfigBean();
			 List<ColorBean>colorBeans=new ArrayList<ColorBean>();
			 List<Line>lines=new ArrayList<Line>();
		     if(!"".equals(contentjson)){
			   try {
				jsonObject = new JSONObject(contentjson);
				JSONObject jsonObject_pressure=jsonObject.getJSONObject("bloodPressure");
				pressureconfigBean=(SignConfigBean) JsonUtil.jsonToBean(jsonObject.optString("bloodPressure"),SignConfigBean.class);
				Type typenews = new TypeToken<List<ColorBean>>() {}.getType();
				colorBeans=(List<ColorBean>) JsonUtil.jsonToList(jsonObject_pressure.optString("color"), typenews);
				Type type_line = new TypeToken<List<Line>>() {}.getType();
				lines=(List<Line>) JsonUtil.jsonToList(jsonObject_pressure.optString("line"), type_line);
				pressureconfigBean.setColor(colorBeans);
				pressureconfigBean.setLine(lines);
			 } catch (JSONException e) {
				e.printStackTrace();
			}
	   }
		 }
		return pressureconfigBean;
	 }
	 //获取血糖配置信息
	 public  static SignConfigBean getSugarConfig(Context context){
		 if(contentjson==null||"".equals(contentjson)){
			  contentjson= getContentStr(context);
		 }
		 if(sugarconfigBean==null){
			 JSONObject jsonObject = null;
			 sugarconfigBean=new SignConfigBean();
			 List<ColorBean>colorBeans=new ArrayList<ColorBean>();
			 List<Line>lines=new ArrayList<Line>();
			 if(!"".equals(contentjson)){
				 try {
					jsonObject = new JSONObject(contentjson);
					JSONObject jsonObject_sugar=jsonObject.getJSONObject("bloodSugar");
					sugarconfigBean.setInputMin(jsonObject_sugar.getDouble("inputMin"));
					sugarconfigBean.setInputMax(jsonObject_sugar.getDouble("inputMax"));
					sugarconfigBean.setConfirmMin(jsonObject_sugar.getDouble("confirmMin"));
					sugarconfigBean.setConfirmMax(jsonObject_sugar.getDouble("confirmMax"));
					sugarconfigBean.setyMin(jsonObject_sugar.getDouble("yMin"));
					sugarconfigBean.setyMax(jsonObject_sugar.getDouble("yMax"));
					sugarconfigBean.setAccur(jsonObject_sugar.getDouble("accur"));
					Type typenews = new TypeToken<List<ColorBean>>() {}.getType();
					colorBeans=(List<ColorBean>) JsonUtil.jsonToList(jsonObject_sugar.optString("color"), typenews);
					Type type_line = new TypeToken<List<Line>>() {}.getType();
					lines=(List<Line>) JsonUtil.jsonToList(jsonObject_sugar.optString("line"), type_line);
					sugarconfigBean.setColor(colorBeans);
					sugarconfigBean.setLine(lines);
				 } catch (JSONException e) {
					e.printStackTrace();
				 }
		     }
		 }
		return sugarconfigBean;
	 }
	 //获取心率配置信息
	 public  static SignConfigBean getPulseConfig(Context context){
		 if(contentjson==null||"".equals(contentjson)){
			  contentjson= getContentStr(context);
		 }
		 if(pulseconfigBean==null){
			 JSONObject jsonObject = null;
			  pulseconfigBean=new SignConfigBean();
			 List<ColorBean>colorBeans=new ArrayList<ColorBean>();
			 List<Line>lines=new ArrayList<Line>();
		     if(!"".equals(contentjson)){
			     try {
				jsonObject = new JSONObject(contentjson);
				JSONObject jsonObject_pulse=jsonObject.getJSONObject("pulse");
				pulseconfigBean=(SignConfigBean) JsonUtil.jsonToBean(jsonObject.optString("pulse"),SignConfigBean.class);
				Type typenews = new TypeToken<List<ColorBean>>() {}.getType();
				colorBeans=(List<ColorBean>) JsonUtil.jsonToList(jsonObject_pulse.optString("color"), typenews);
				Type type_Line = new TypeToken<List<Line>>() {}.getType();
				lines=(List<Line>) JsonUtil.jsonToList(jsonObject_pulse.optString("line"), type_Line);
				pulseconfigBean.setColor(colorBeans);
				pulseconfigBean.setLine(lines);
			} catch (JSONException e) {
				e.printStackTrace();
			}
	     }
		 }
		return pulseconfigBean;
	 }
	 //获取排便配置
	 public  static SignConfigBean getdefecationConfig(Context context){
		    if(contentjson==null||"".equals(contentjson)){
			  contentjson= getContentStr(context);
		    }
		    if(defecationconfigBean==null){
				JSONObject jsonObject = null;
				defecationconfigBean=new SignConfigBean();
				List<ColorBean>colorBeans=new ArrayList<ColorBean>();
				List<Line>lines=new ArrayList<Line>();
				   if(!"".equals(contentjson)){
				     try {
					jsonObject = new JSONObject(contentjson);
					JSONObject jsonObject_pulse=jsonObject.getJSONObject("defecation");
					defecationconfigBean=(SignConfigBean) JsonUtil.jsonToBean(jsonObject.optString("defecation"),SignConfigBean.class);
					Type typenews = new TypeToken<List<ColorBean>>() {}.getType();
					colorBeans=(List<ColorBean>) JsonUtil.jsonToList(jsonObject_pulse.optString("color"), typenews);
					Type type_Line = new TypeToken<List<Line>>() {}.getType();
					lines=(List<Line>) JsonUtil.jsonToList(jsonObject_pulse.optString("line"), type_Line);
					defecationconfigBean.setColor(colorBeans);
					defecationconfigBean.setLine(lines);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		      }
		    }
			return defecationconfigBean;
		 }
	 //获取呼吸配置
	 public  static SignConfigBean getbreathingConfig(Context context){
		    if(contentjson==null||"".equals(contentjson)){
			  contentjson= getContentStr(context);
		    }
		    if(breathconfigBean==null){
				JSONObject jsonObject = null;
				breathconfigBean=new SignConfigBean();
				List<ColorBean>colorBeans=new ArrayList<ColorBean>();
				List<Line>lines=new ArrayList<Line>();
			    if(!"".equals(contentjson)){
			     try {
					jsonObject = new JSONObject(contentjson);
					JSONObject jsonObject_pulse=jsonObject.getJSONObject("breathing");
					breathconfigBean=(SignConfigBean) JsonUtil.jsonToBean(jsonObject.optString("breathing"),SignConfigBean.class);
					Type typenews = new TypeToken<List<ColorBean>>() {}.getType();
					colorBeans=(List<ColorBean>) JsonUtil.jsonToList(jsonObject_pulse.optString("color"), typenews);
					Type type_Line = new TypeToken<List<Line>>() {}.getType();
					lines=(List<Line>) JsonUtil.jsonToList(jsonObject_pulse.optString("line"), type_Line);
					breathconfigBean.setColor(colorBeans);
					breathconfigBean.setLine(lines);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		      }
		    }
			return breathconfigBean;
		 }
	 //获取体征数据列表
	 public  static List<SignsData> getSigndataConfig(Context context){
		    if(contentjson==null||"".equals(contentjson)){
			  contentjson= getContentStr(context);
		    }
			JSONObject jsonObject = null;
			List<SignsData>signsData_list=new ArrayList<SignsData>();
			   if(!"".equals(contentjson)){
			     try {
				jsonObject = new JSONObject(contentjson);
				Type typenews = new TypeToken<List<SignsData>>() {}.getType();
				signsData_list=(List<SignsData>) JsonUtil.jsonToList(jsonObject.optString("signsData"), typenews);
			  } catch (JSONException e) {
				e.printStackTrace();
			 }
		   }
			return signsData_list;
	  }
	   //获取配置的json串
	 /**
	 * @param context
	 * @param type 
	 * @return
	 */
	public static String getContentStr(Context context){
		 String config_json_data=(String) SPUtils.get(context, SPUtils.CONFIG_JSON_DATA, "");
		 String con="";	
		 if(!"".equals(config_json_data)){
				    ContentTotal content=(ContentTotal) JsonUtil.jsonToBean(config_json_data, ContentTotal.class);
				    con=content.getContent();//  content={  "approvalStatus": 0 }
                    return con;
		 }else{
             con=context.getResources().getString(com.lefuyun.R.string.json_config);
			 return con;
		 }
	 }
		//体温显示不同的颜色
		public static void showDifferentColor_temp(Context context,Double temp,TextView tv_content,TextView tv_date){
			   SignConfigBean tempconfig=ConfigUtils.getTemperatureConfig(context);
		       if(tempconfig!=null&&tempconfig.getColor()!=null){
		    	   for(int i=0;i<tempconfig.getColor().size();i++){
			    	   if(temp>=tempconfig.getColor().get(i).getLow()&&temp<=tempconfig.getColor().get(i).getHigh()){
			    		   if(tv_date != null) {
			    			   tv_date.setTextColor(Color.parseColor(tempconfig.getColor().get(i).getFontColor()));
			    		   }
			    		  tv_content.setTextColor(Color.parseColor(tempconfig.getColor().get(i).getFontColor()));
			    	     break;
			    	   }else{
			    		   if(i!=tempconfig.getColor().size()-1){
			    			   continue;
			    		   }else{
			    			   if(tv_date != null) {
			    				   tv_date.setTextColor(Color.GRAY);
			    			   }
			    			   tv_content.setTextColor(Color.GRAY);
			    		   }
			    	   }
			       }
		       }
		}
		//血压显示不同的颜色
		public static String showDifferentColor_pressure(Context context,int high_pressure,int low_pressure,TextView tv_content,TextView tv_date){
			   SignConfigBean pressureconfig=ConfigUtils.getPressureConfig(context);
		       ColorBean color_low = null,color_high = null;
			   if(pressureconfig!=null&&pressureconfig.getColor()!=null){
		    	   for(int i=0;i<pressureconfig.getColor().size();i++){
			    	   if(low_pressure>=pressureconfig.getColor().get(i).getLow()&&low_pressure<=pressureconfig.getColor().get(i).getHigh()){
			    		   color_low=pressureconfig.getColor().get(i);
			    		   break;
			    	   }else{
			    		   if(i!=pressureconfig.getColor().size()-1){
			    			   continue;
			    		   }
			    	   }
			       }
		    	   for(int i=0;i<pressureconfig.getColor().size();i++){
			    	   if(high_pressure>=pressureconfig.getColor().get(i).getLow()&&high_pressure<=pressureconfig.getColor().get(i).getHigh()){
			    		   color_high=pressureconfig.getColor().get(i);
			    		   break;
			    	   }else{
			    		   if(i!=pressureconfig.getColor().size()-1){
			    			   continue;
			    		   }
			    	   }
			       }
		    	   if(color_high!=null&&color_low!=null){
		    		   if(color_high.getShowLevel()>=color_low.getShowLevel()){
		    			   tv_content.setTextColor(Color.parseColor(color_high.getFontColor()));
		    			   return color_high.getFontColor();
		    		   }else if(color_high.getShowLevel()<color_low.getShowLevel()){
			    		   tv_content.setTextColor(Color.parseColor(color_low.getFontColor()));
			    		   return  color_low.getFontColor();
			    	   }else{
			    		   tv_content.setTextColor(Color.GRAY);
			    		   return "";
			    	   }
		    	   }
		       }
			   return "";
		}
		//血糖显示不同的颜色
		public static void showDifferentColor_sugar(Context context,double sugar,TextView tv_content,TextView tv_date){
			   SignConfigBean sugarconfig=ConfigUtils.getSugarConfig(context);
		       LogUtil.i("tag", "sugar:"+sugar);
			   if(sugarconfig!=null&&sugarconfig.getColor()!=null){
		    	   for(int i=0;i<sugarconfig.getColor().size();i++){
		    		   LogUtil.i("tag", sugarconfig.getColor().get(i).getLow()+","+sugarconfig.getColor().get(i).getHigh());
		    		   if(sugar>=sugarconfig.getColor().get(i).getLow()&&sugar<=sugarconfig.getColor().get(i).getHigh()){
			    		 LogUtil.i("tag", "zoule");
		    			  tv_content.setTextColor(Color.parseColor(sugarconfig.getColor().get(i).getFontColor()));
			    		 if(tv_date != null) {
			    			 tv_date.setTextColor(Color.parseColor(sugarconfig.getColor().get(i).getFontColor()));
			    		 }
			    		 break;
			    	   }else{
			    		   if(i!=sugarconfig.getColor().size()-1){
			    			   continue;
			    		   }else{
			    			   tv_content.setTextColor(Color.GRAY);
			    			   if(tv_date != null) {
			    				   tv_date.setTextColor(Color.GRAY);
			    			   }
			    		   }
			    	   }
			       }
		       }
		}
		public static SignConfigBean getConfigBean(Context context,int type,OldPeople oldPeople){
			SignConfigBean config = null;
			if(oldPeople.getConfig()==null||"".equals(oldPeople.getConfig())){
				LogUtil.i("tag", "通用配置");
				switch (type) {
				case ConstantUtil.BLOODSUGAR:
					config=ConfigUtils.getSugarConfig(context);
					break;
				case ConstantUtil.BLOODPRESSURE:
					config=ConfigUtils.getPressureConfig(context);
					break;
				case ConstantUtil.PULSE:
					config=ConfigUtils.getPulseConfig(context);
					break;
				case ConstantUtil.TEMPERATURE:
					config=ConfigUtils.getTemperatureConfig(context);
					break;
				case ConstantUtil.DEFECATION:
					config=ConfigUtils.getdefecationConfig(context);
					break;
				case ConstantUtil.BREATHING:
					config=ConfigUtils.getbreathingConfig(context);
					break;
			}
			}else{
				LogUtil.i("tag", "自己配置");
				switch (type) {
				case ConstantUtil.BLOODSUGAR:
					config=ConfigForSpeialOldUtils.getSugarConfig(context,oldPeople.getConfig());
					break;
				case ConstantUtil.BLOODPRESSURE:
					config=ConfigForSpeialOldUtils.getPressureConfig(context,oldPeople.getConfig());
					break;
				case ConstantUtil.PULSE:
					config=ConfigForSpeialOldUtils.getPulseConfig(context,oldPeople.getConfig());
					break;
				case ConstantUtil.TEMPERATURE:
					config=ConfigForSpeialOldUtils.getTemperatureConfig(context,oldPeople.getConfig());
					break;
				case ConstantUtil.DEFECATION:
					config=ConfigForSpeialOldUtils.getdefecationConfig(context,oldPeople.getConfig());
					break;
				case ConstantUtil.BREATHING:
					config=ConfigForSpeialOldUtils.getbreathingConfig(context,oldPeople.getConfig());
					break;
			}
			}
			if(config==null){
				config=new SignConfigBean();
			}
			return config;
		}
		//获取配置文件的信息
		public static ContentTotal getConfig(Context context){
				String config_json_data=(String) SPUtils.get(context, SPUtils.CONFIG_JSON_DATA, "");
				if(!"".equals(config_json_data)){
					try {
						JSONObject	jsonObject = new JSONObject(config_json_data);
						ContentTotal content=(ContentTotal) JsonUtil.jsonToBean(config_json_data, ContentTotal.class);
						String con=content.getContent();//  content={  "approvalStatus": 0 }
					    int approvalStatus=jsonObject.getInt("approvalStatus");
					    content.setApprovalStatus(approvalStatus);
					    LogUtil.i("tag", "11"+content.toString());
	                    return content;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{//json数据为空
	              	ContentTotal content_null=new ContentTotal();
	              	content_null.setTag("0");
	              	content_null.setApprovalStatus(1);//审核通过
					return content_null;		
				}
	          	ContentTotal content_null2=new ContentTotal();
	        	content_null2.setTag("0");
	          	content_null2.setApprovalStatus(1);//审核通过
			    return content_null2;
		}
		//心率显示不同的颜色
		public static void showDifferentColor_pulse(Context context,int pulse,TextView tv_content,TextView tv_date){
			   SignConfigBean pulseconfig=ConfigUtils.getPulseConfig(context);
		       if(pulseconfig!=null&&pulseconfig.getColor()!=null){
		    	   for(int i=0;i<pulseconfig.getColor().size();i++){
			    	   if(pulse>=pulseconfig.getColor().get(i).getLow()&&pulse<=pulseconfig.getColor().get(i).getHigh()){
			    		   tv_content.setTextColor(Color.parseColor(pulseconfig.getColor().get(i).getFontColor()));
			    		   if(tv_date != null) {
			    			   tv_date.setTextColor(Color.parseColor(pulseconfig.getColor().get(i).getFontColor()));
			    		   }
			    		   break;
			    	   }else{
			    		   if(i!=pulseconfig.getColor().size()-1){
			    			   continue;
			    		   }else{
			    			   tv_content.setTextColor(Color.GRAY);
			    			   if(tv_date != null) {
			    				   tv_date.setTextColor(Color.GRAY);
			    			   }
			    		   }
			    	   }
			       }
		       }
		}
		//排便显示不同的颜色
		public static void showDifferentColor_defecation(Context context,int pulse,TextView tv_content,TextView tv_date){
			   SignConfigBean defecationconfig=ConfigUtils.getdefecationConfig(context);
		       if(defecationconfig!=null&&defecationconfig.getColor()!=null){
		    	   for(int i=0;i<defecationconfig.getColor().size();i++){
			    	   if(pulse>=defecationconfig.getColor().get(i).getLow()&&pulse<=defecationconfig.getColor().get(i).getHigh()){
			    		   tv_content.setTextColor(Color.parseColor(defecationconfig.getColor().get(i).getFontColor()));
			    		   if(tv_date != null) {
			    			   tv_date.setTextColor(Color.parseColor(defecationconfig.getColor().get(i).getFontColor()));
			    		   }
			    		   break;
			    	   }else{
			    		   if(i!=defecationconfig.getColor().size()-1){
			    			   continue;
			    		   }else{
			    			   tv_content.setTextColor(Color.GRAY);
			    			   if(tv_date != null) {
			    				   tv_date.setTextColor(Color.GRAY);
			    			   }
			    		   }
			    	   }
			       }
		       }
		}
		//呼吸显示不同的颜色
		public static void showDifferentColor_breath(Context context,int pulse,TextView tv_content,TextView tv_date){
			 SignConfigBean breathconfig=ConfigUtils.getbreathingConfig(context);
		     if(breathconfig!=null&&breathconfig.getColor()!=null){
		    	 for(int i=0;i<breathconfig.getColor().size();i++){
			    	   if(pulse>=breathconfig.getColor().get(i).getLow()&&pulse<=breathconfig.getColor().get(i).getHigh()){
			    		 tv_content.setTextColor(Color.parseColor(breathconfig.getColor().get(i).getFontColor()));
			    		 if(tv_date != null) {
			    			 tv_date.setTextColor(Color.parseColor(breathconfig.getColor().get(i).getFontColor()));
			    		 }
			    		 break;
			    	   }else{
			    		   if(i!=breathconfig.getColor().size()-1){
			    			   continue;
			    		   }else{
			    			   tv_content.setTextColor(Color.GRAY);
			    			   if(tv_date != null) {
			    				   tv_date.setTextColor(Color.GRAY);
			    			   }
			    		   }
			    	   }
			       }
		     }  
		}
		//饮水显示不同的颜色
		public static void showDifferentColor_drinking(Context context,int pulse,TextView tv_content,TextView tv_date){
				 SignConfigBean breathconfig=ConfigUtils.getbreathingConfig(context);
			     if(breathconfig!=null&&breathconfig.getColor()!=null){
			    	 for(int i=0;i<breathconfig.getColor().size();i++){
				    	   if(pulse>=breathconfig.getColor().get(i).getLow()&&pulse<=breathconfig.getColor().get(i).getHigh()){
				    		 tv_content.setTextColor(Color.parseColor(breathconfig.getColor().get(i).getFontColor()));
				    		 if(tv_date != null) {
				    			 tv_date.setTextColor(Color.parseColor(breathconfig.getColor().get(i).getFontColor()));
				    		 }
				    		 break;
				    	   }else{
				    		   if(i!=breathconfig.getColor().size()-1){
				    			   continue;
				    		   }else{
				    			   tv_content.setTextColor(Color.GRAY);
				    			   if(tv_date != null) {
				    				   tv_date.setTextColor(Color.GRAY);
				    			   }
				    		   }
				    	   }
				       }
			     }  
		 }
		//睡眠显示不同的颜色
		public static void showDifferentColor_sleep(Context context,int pulse,TextView tv_content,TextView tv_date){
//				 SignConfigBean breathconfig=ConfigUtils.getbreathingConfig(context);
//			     if(breathconfig!=null&&breathconfig.getColor()!=null){
//			    	 for(int i=0;i<breathconfig.getColor().size();i++){
//				    	   if(pulse>=breathconfig.getColor().get(i).getLow()&&pulse<=breathconfig.getColor().get(i).getHigh()){
//				    		 tv_content.setTextColor(Color.parseColor(breathconfig.getColor().get(i).getFontColor()));
//				    		 if(tv_date != null) {
//				    			 tv_date.setTextColor(Color.parseColor(breathconfig.getColor().get(i).getFontColor()));
//				    		 }
//				    		 break;
//				    	   }else{
//				    		   if(i!=breathconfig.getColor().size()-1){
//				    			   continue;
//				    		   }else{
//				    			   tv_content.setTextColor(Color.GRAY);
//				    			   if(tv_date != null) {
//				    				   tv_date.setTextColor(Color.GRAY);
//				    			   }
//				    		   }
//				    	   }
//				       }
//			     }  
		 }
		/**
		 * 校验体征数据的合法性
		 * @param context
		 * @param data 需要校验的数据
		 * @param type 校验数据的类型
		 * @return 合法时返回true,否则返回false
		 */
		public static int checkSignsData(Context context, String data, int type) {
			int flag = SIGNS_DATA_ILLEGAL;
			switch (type) {
			case ConstantUtil.TEMPERATURE :
				// 体温
				// 校验体温的合法性
				flag = checkTemperature(context, data, true);
				break;
			case ConstantUtil.BLOODPRESSURE :
				// 血压
				// 校验血压的合法性
				flag = checkBloodPressure(context, data, true);
				break;
			case ConstantUtil.BLOODSUGAR :
				// 血糖
				// 校验血糖的合法性
				flag = checkBloodSugar(context, data, true);
				break;
			case ConstantUtil.PULSE :
				// 心率
				// 校验心率的合法性
				flag = checkPulse(context, data, true);
				break;
			case ConstantUtil.DEFECATION :
				// 排便
				// 校验排便的合法性
				flag = checkDefecation(context, data, true);
				break;
			case ConstantUtil.BREATHING :
				// 呼吸
				// 校验呼吸的合法性
				flag = checkBreathing(context, data, true);
				break;
			case ConstantUtil.DRINKWATER :
				flag=SIGNS_DATA_NORMAL;
				// 喝水
				break;
			case ConstantUtil.SLEEPING:
				flag=SIGNS_DATA_NORMAL;
				break;
			}
			return flag;
		}
		
		/**
		 * 校验体温的合法性
		 * @param context
		 * @param data 需要校验的数据
		 * @return
		 */
		public static int checkTemperature(Context context, String data, boolean isToast) {
			int state = SIGNS_DATA_ILLEGAL;
			// 获取体温的配置信息
			SignConfigBean signConfigBean = getTemperatureConfig(context);
			// 获取体温的最大允许值
			double inputMax = signConfigBean.getInputMax();
			// 获取体温的最小允许值
			double inputMin = signConfigBean.getInputMin();
			// 获取确认体温的最大值
			double confirmMax = signConfigBean.getConfirmMax();
			// 获取确认体温的最大值
			double confirmMin = signConfigBean.getConfirmMin();
			LogUtil.i("TAG", "确认数据的范围:" + confirmMin + "~" + confirmMax);
			LogUtil.i("TAG", "最值数据的范围:" + inputMin + "~" + inputMax);
			double temperature = 0;
			try {
				// 将字符串转换成Double类型,如果字符不合法则会抛出异常
				temperature = Double.valueOf(data);
				if(temperature <= inputMax && temperature >= inputMin) {
					// temperature符合要求
					if(temperature > confirmMax || temperature < confirmMin) {
						// 数据不正常
						state = SIGNS_DATA_ABNORMAL;
					}else {
						// 数据正常
						state = SIGNS_DATA_NORMAL;
					}
				}else {
					if(isToast) {
						ToastUtils.show(context, "体温的输入范围是: " + inputMin + "~" + inputMax);
					}
				}
			} catch(Exception e) {
				// 不合法时返回false
				return state;
			}
			return state;
		}
		
		/**
		 * 校验血压的合法性
		 * @param context
		 * @param data 需要校验的数据
		 * @param isDialog 是否谈toast
		 * @return
		 */
		public static int checkBloodPressure(Context context, String data, boolean isToast) {
			int state = SIGNS_DATA_ILLEGAL;
			// 先检验data的合法性
			String regex = "^(\\d)+[/,.，。、\\-](\\d)+$";
			if(!data.matches(regex)) {
				if(isToast) {
					ToastUtils.show(context, "输入格式不对,如129/78");
				}
				return state;
			}
			// 切割字符串
			String[] datas = getBloodPressureArray(data);
			if(datas.length != 2) {
				if(isToast) {
					ToastUtils.show(context, "输入格式不对,如129/78");
				}
				return state;
			}else {
				if(Integer.valueOf(datas[0])>Integer.valueOf(datas[1])) {
					if(isToast) {
						ToastUtils.show(context, "收缩压必须大于舒张压");
					}
					return state;
				}
			}
			// 获取血压的配置信息
			SignConfigBean signConfigBean = getPressureConfig(context);
			// 获取血压的最大允许值
			int inputMax = (int) signConfigBean.getInputMax();
			// 获取血压的最小允许值
			int inputMin = (int) signConfigBean.getInputMin();
			// 获取确认心率的最大值
			int confirmMax = (int) signConfigBean.getConfirmMax();
			// 获取确认心率的最大值
			int confirmMin = (int) signConfigBean.getConfirmMin();
			double diff=ConfigUtils.getPressureConfig(context).getDiff();
			LogUtil.i("TAG", "确认数据的范围:" + confirmMin + "~" + confirmMax);
			LogUtil.i("TAG", "最值数据的范围:" + inputMin + "~" + inputMax);
			try {
				if(Integer.valueOf(datas[1]) <= inputMax && Integer.valueOf(datas[0]) >= inputMin) {
					// bloodPressure符合要求
					if(Integer.valueOf(datas[1]) -Integer.valueOf(datas[0])>=diff){
						if(Integer.valueOf(datas[1]) > confirmMax || Integer.valueOf(datas[0]) < confirmMin) {
							// 数据不正常
							state = SIGNS_DATA_ABNORMAL;
						}else {
							// 数据正常
							state = SIGNS_DATA_NORMAL;
						}
					}else{
						state = SIGNS_DATA_ILLEGAL;
						if(isToast) {
							ToastUtils.show(context, "压差必须大于"+getString(0,Float.parseFloat(diff+"")));
						}
					}
					
				}
				else {
					if(isToast) {
						ToastUtils.show(context, "血压的输入范围是: " + inputMin + "~" + inputMax);
					}
				}
			} catch(Exception e) {
				// 不合法时返回
				return state;
			}
			return state;
		}
		
		/**
		 * 校验血糖的合法性
		 * @param context
		 * @param data 需要校验的数据
		 * @return
		 */
		public static int checkBloodSugar(Context context, String data, boolean isToast) {
			int state = SIGNS_DATA_ILLEGAL;
			// 获取血糖的配置信息
			SignConfigBean signConfigBean = getSugarConfig(context);
			// 获取血糖的最大允许值
			double inputMax = signConfigBean.getInputMax();
			// 获取血糖的最小允许值
			double inputMin = signConfigBean.getInputMin();
			// 获取确认血糖的最大值
			double confirmMax = signConfigBean.getConfirmMax();
			// 获取确认血糖的最大值
			double confirmMin = signConfigBean.getConfirmMin();
			LogUtil.i("TAG", "确认数据的范围:" + confirmMin + "~" + confirmMax);
			LogUtil.i("TAG", "最值数据的范围:" + inputMin + "~" + inputMax);
			double bloodSugar = 0;
			try {
				// 将字符串转换成Float类型,如果字符不合法则会抛出异常
				bloodSugar = Double.valueOf(data);
				LogUtil.i("TAG", "血糖要求的取值范围是 ---> " + inputMin + " ------ " + inputMax);
				LogUtil.i("TAG", "当前校验的血糖值是 == " + bloodSugar);
				if(bloodSugar <= inputMax && bloodSugar >= inputMin) {
					// bloodSugar符合要求
					if(bloodSugar > confirmMax || bloodSugar < confirmMin) {
						// 数据不正常
						state = SIGNS_DATA_ABNORMAL;
					}else {
						// 数据正常
						state = SIGNS_DATA_NORMAL;
					}
				}else {
					if(isToast) {
						ToastUtils.show(context, "血糖的输入范围是: " + inputMin + "~" + inputMax);
					}
				}
			} catch(Exception e) {
				// 不合法时返回
				return state;
			}
			return state;
		}
		
		/**
		 * 校验心率的合法性
		 * @param context
		 * @param data 需要校验的数据
		 * @return
		 */
		public static int checkPulse(Context context, String data, boolean isToast) {
			int state = SIGNS_DATA_ILLEGAL;
			// 获取心率的配置信息
			SignConfigBean signConfigBean = getPulseConfig(context);
			// 获取心率的最大允许值
			int inputMax = (int) signConfigBean.getInputMax();
			// 获取心率的最小允许值
			int inputMin = (int) signConfigBean.getInputMin();
			// 获取确认心率的最大值
			int confirmMax = (int) signConfigBean.getConfirmMax();
			// 获取确认心率的最大值
			int confirmMin = (int) signConfigBean.getConfirmMin();
			LogUtil.i("TAG", "确认数据的范围:" + confirmMin + "~" + confirmMax);
			LogUtil.i("TAG", "最值数据的范围:" + inputMin + "~" + inputMax);
			int pulse = 0;
			try {
				// 将字符串转换成Integer类型,如果字符不合法则会抛出异常
				pulse = Integer.valueOf(data);
				if(pulse <= inputMax && pulse >= inputMin) {
					// Pulse值符合要求
					if(pulse > confirmMax || pulse < confirmMin) {
						// 数据不正常
						state = SIGNS_DATA_ABNORMAL;
					}else {
						// 数据正常
						state = SIGNS_DATA_NORMAL;
					}
				}else {
					if(isToast) {
						ToastUtils.show(context, "心率的输入范围是: " + inputMin + "~" + inputMax);
					}
				}
			} catch(Exception e) {
				// 不合法时返回
				return state;
			}
			return state;
		}
		
		/**
		 * 校验排便的合法性
		 * @param context
		 * @param data 需要校验的数据
		 * @return
		 */
		public static int checkDefecation(Context context, String data, boolean isToast) {
			int state = SIGNS_DATA_ILLEGAL;
			// 获取排便的配置信息
			SignConfigBean signConfigBean = getdefecationConfig(context);
			// 获取排便的最大允许值
			int inputMax = (int) signConfigBean.getInputMax();
			// 获取排便的最小允许值
			int inputMin = (int) signConfigBean.getInputMin();
			// 获取确认排便的最大值
			int confirmMax = (int) signConfigBean.getConfirmMax();
			// 获取确认排便的最大值
			int confirmMin = (int) signConfigBean.getConfirmMin();
			LogUtil.i("TAG", "确认数据的范围:" + confirmMin + "~" + confirmMax);
			LogUtil.i("TAG", "最值数据的范围:" + inputMin + "~" + inputMax);
			int defecation = 0;
			try {
				// 将字符串转换成Integer类型,如果字符不合法则会抛出异常
				defecation = Integer.valueOf(data);
				if(defecation <= inputMax && defecation >= inputMin) {
					// defecation值符合要求
					if(defecation > confirmMax || defecation < confirmMin) {
						// 数据不正常
						state = SIGNS_DATA_ABNORMAL;
					}else {
						// 数据正常
						state = SIGNS_DATA_NORMAL;
					}
				}else {
					if(isToast) {
						ToastUtils.show(context, "排便的输入范围是: " + inputMin + "~" + inputMax);
					}
				}
			} catch(Exception e) {
				// 不合法时返回
				return state;
			}
			return state;
		}
		
		/**
		 * 校验呼吸的合法性
		 * @param context
		 * @param data 需要校验的数据
		 * @return
		 */
		public static int checkBreathing(Context context, String data, boolean isToast) {
			int state = SIGNS_DATA_ILLEGAL;
			// 获取呼吸的配置信息
			SignConfigBean signConfigBean = getbreathingConfig(context);
			// 获取呼吸的最大允许值
			int inputMax = (int) signConfigBean.getInputMax();
			// 获取呼吸的最小允许值
			int inputMin = (int) signConfigBean.getInputMin();
			// 获取确认体温的最大值
			int confirmMax = (int) signConfigBean.getConfirmMax();
			// 获取确认体温的最大值
			int confirmMin = (int) signConfigBean.getConfirmMin();
			LogUtil.i("TAG", "确认数据的范围:" + confirmMin + "~" + confirmMax);
			LogUtil.i("TAG", "最值数据的范围:" + inputMin + "~" + inputMax);
			int breathing = 0;
			try {
				// 将字符串转换成Integer类型,如果字符不合法则会抛出异常
				breathing = Integer.valueOf(data);
				if(breathing <= inputMax && breathing >= inputMin) {
					// breathing值符合要求
					if(breathing > confirmMax || breathing < confirmMin) {
						// 数据不正常
						state = SIGNS_DATA_ABNORMAL;
					}else {
						// 数据正常
						state = SIGNS_DATA_NORMAL;
					}
				}else {
					if(isToast) {
						ToastUtils.show(context, "呼吸的输入范围是: " + inputMin + "~" + inputMax);
					}
				}
			} catch(Exception e) {
				// 不合法时返回
				return state;
			}
			return state;
		}
		
		/**
		 * 切割血压字符串,此字符串必须符合123/79格式,中间的分割符可以为/ , . ， 。 、 \ -中的任意一种
		 * @param str 需要切割的字符串
		 * @return 返回结果中0下标是低血压,1下标是高血压
		 */
		public static String[] getBloodPressureArray(String str) {
			String[] string = str.split("[/,.，。、\\-]");
			String[] request = new String[2];
			request[0] = string[1];
			request[1] = string[0];
			return request;
		}
		
		/** 获取配置文件中要格式化数据的位数 */
		public static int getAccur(Context context,int type) {
			SignConfigBean signConfigBean = null;
			int num = 0;
			switch (type) {
			case ConstantUtil.TEMPERATURE:
				signConfigBean = ConfigUtils.getTemperatureConfig(context);
				break;
			case ConstantUtil.BLOODPRESSURE:
				signConfigBean = ConfigUtils.getPressureConfig(context);
				break;
			case ConstantUtil.BLOODSUGAR:
				signConfigBean = ConfigUtils.getSugarConfig(context);
				break;
			case ConstantUtil.PULSE:
				signConfigBean = ConfigUtils.getPulseConfig(context);
				break;
			case ConstantUtil.DEFECATION:
				signConfigBean = ConfigUtils.getdefecationConfig(context);
				break;
			case ConstantUtil.BREATHING:
				signConfigBean = ConfigUtils.getbreathingConfig(context);
				break;
			case ConstantUtil.DRINKWATER:
//				signConfigBean = ConfigUtils.getTemperatureConfig(context);
				break;

			}
			if(signConfigBean != null) {
				num = (int) signConfigBean.getAccur();
			}
			return num;
		}
		
		/** 获取指定数据的格式化数据 */
		public static String numberFormat(Context context, int type, double d) {
			// 获取要格式化数据小数位的位数
			int num = getAccur(context,type);
			String str = String.format("%." + num + "f", d);
			return str;
		}
		/** 获取指定数据的格式化数据 */
		public static String numberFormat(Context context, int type, String string) {
			// 强字符串类型转换成double类型
			double d = Double.parseDouble(string);
			return numberFormat(context, type, d);
		}
		
//		public static void controlaccur(Context mContext,EditText dataValue,int type){
//			// 获取当前数值允许小数点后输入的数字个数
//			int mAccur = ConfigUtils.getAccur(mContext,type);
//			// 判断当前体征数据的类型
//			// 默认的输入类型为double数字类型
////			if(healthData.getTitle() == ConstantUtil.BLOODPRESSURE) {
////				// 血压
////				// 血压要包含特殊字符,故文本输入类型为text
////				dataValue.setInputType(InputType.TYPE_CLASS_TEXT);		
////			}else if(mAccur == 0) {
////				// 小数点后不能有数据则不能添加小数点
////				dataValue.setInputType(InputType.TYPE_CLASS_NUMBER);
////				setPricePoint(dataValue, mAccur);
////			}else {
//				// 其他情况进行校验
//				setPricePoint(dataValue, mAccur);
//			//}
//		}
		/** editText输入框小数点后只能为1位,还有数字前面不可以0 */
		public static void controlaccur(Context mContext,final EditText editText,int type) {
			final int accur = ConfigUtils.getAccur(mContext,type);
			editText.addTextChangedListener(new TextWatcher() {
	 
	            @Override
	            public void onTextChanged(CharSequence s, int start, int before,
	                    int count) {
	                if (s.toString().contains(".")) {
	                	// 如果字符串中有小数点则进行校验
	                    if (s.length() - 1 - s.toString().indexOf(".") > accur) {
	                    	// 小数点后的数字个数大于1,则将小数点后除第一位都删除
	                        s = s.toString().subSequence(0,
	                                s.toString().indexOf(".") + accur + 1);
	                        editText.setText(s);
	                        // 设置光标的位置,使其一直保持在最右边
	                        editText.setSelection(s.length());
	                    }
	                }
	                if (s.toString().trim().substring(0).equals(".")) {
	                	// 以小数点为开始,在小数点的前面加上一个0
	                    s = "0" + s;
	                    editText.setText(s);
	                    editText.setSelection(2);
	                }
	 
	                if (s.toString().startsWith("0")
	                        && s.toString().trim().length() > 1) {
	                	// 开始位置上的数是0,且还有其他数字
	                    if (!s.toString().substring(1, 2).equals(".")) {
	                    	// 第二位上的字符不为小数点,则无法继续输入
	                        editText.setText(s.subSequence(0, 1));
	                        editText.setSelection(1);
	                        return;
	                    }
	                }
	            }
	 
	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {
	 
	            }
	 
	            @Override
	            public void afterTextChanged(Editable s) {
	                 
	            }
	        });
	    }
}
