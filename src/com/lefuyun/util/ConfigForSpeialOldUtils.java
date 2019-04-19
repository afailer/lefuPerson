package com.lefuyun.util;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.lefuyun.R;
import com.lefuyun.bean.ColorBean;
import com.lefuyun.bean.ConfigForSpecialOldBean;
import com.lefuyun.bean.Line;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.SignConfigBean;

public class ConfigForSpeialOldUtils {
	public static SignConfigBean tempconfigBean;
	public static SignConfigBean pressureconfigBean;
	public static SignConfigBean sugarconfigBean;
	public static SignConfigBean pulseconfigBean;
	public static SignConfigBean defecationconfigBean;
	public static SignConfigBean breathconfigBean;
	public static List<ConfigForSpecialOldBean>configForSpecialOldBeans;
	public static List<OldPeople>oldPeoples_all;
	public static List<ConfigForSpecialOldBean> getHaveConfigOld(Context context){
		String oldjson=(String) SPUtils.get(context, SPUtils.OLDPEOPLE_JSON, "");
		  Type type=new TypeToken<List<OldPeople>>(){}.getType();
		if(oldjson==null||"".equals(oldjson)){
			return new ArrayList<ConfigForSpecialOldBean>();
		}
		if(oldPeoples_all==null){
			oldPeoples_all=(List<OldPeople>) JsonUtil.jsonToList(oldjson, type);
		}
		List<OldPeople>oldPeoples_special=new ArrayList<OldPeople>();
		//得到有配置的老人对象
		for(int i=0;i<oldPeoples_all.size();i++){
			if(oldPeoples_all.get(i).getConfig()!=null&&!oldPeoples_all.get(i).getConfig().equals("")){
				oldPeoples_special.add(oldPeoples_all.get(i));
			}
		}
		configForSpecialOldBeans=new ArrayList<ConfigForSpecialOldBean>();
		for(int i=0;i<oldPeoples_special.size();i++){
			ConfigForSpecialOldBean configForSpecialOldBean=new ConfigForSpecialOldBean();
			ArrayList<SignConfigBean>signConfigBeans=new ArrayList<SignConfigBean>();
			SignConfigBean signConfigBean_temp=getTemperatureConfig(context,oldPeoples_special.get(i).getConfig());
			SignConfigBean signConfigBean_pressure=getPressureConfig(context, oldPeoples_special.get(i).getConfig());
			SignConfigBean signConfigBean_sugar=getSugarConfig(context, oldPeoples_special.get(i).getConfig());
		    SignConfigBean signConfigBean_pulse=getPulseConfig(context,  oldPeoples_special.get(i).getConfig());
		    SignConfigBean signConfigBean_defecation=getdefecationConfig(context, oldPeoples_special.get(i).getConfig());
		    SignConfigBean signConfigBean_breath=getbreathingConfig(context,oldPeoples_special.get(i).getConfig());
		    signConfigBeans.add(signConfigBean_temp);
		    signConfigBeans.add(signConfigBean_pressure);
		    signConfigBeans.add(signConfigBean_sugar);
		    signConfigBeans.add(signConfigBean_pulse);
		    signConfigBeans.add(signConfigBean_defecation);
		    signConfigBeans.add(signConfigBean_breath);
		    configForSpecialOldBean.setOld_people_id(oldPeoples_special.get(i).getId());
		    configForSpecialOldBean.setSignConfigBeans(signConfigBeans);
		    configForSpecialOldBeans.add(configForSpecialOldBean);
		}
		if(configForSpecialOldBeans==null){
			configForSpecialOldBeans=new ArrayList<ConfigForSpecialOldBean>();
		}
		return configForSpecialOldBeans;
	}
	 //获取体温配置信息
	 public  static SignConfigBean getTemperatureConfig(Context context,String contentjson){
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
		return tempconfigBean;
	 }
	 //获取血压配置信息
	 public  static SignConfigBean getPressureConfig(Context context,String contentjson){
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
		return pressureconfigBean;
	 }
	 //获取血糖配置信息
	 public  static SignConfigBean getSugarConfig(Context context,String contentjson){
//		 if(sugarconfigBean==null){
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
//		 }
		return sugarconfigBean;
	 }
	 //获取心率配置信息
	 public  static SignConfigBean getPulseConfig(Context context,String contentjson){
//		 if(pulseconfigBean==null){
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
//		 }
		return pulseconfigBean;
	 }
	 //获取排便配置
	 public  static SignConfigBean getdefecationConfig(Context context,String contentjson){
//		    if(defecationconfigBean==null){
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
//		    }
			return defecationconfigBean;
		 }
	 //获取呼吸配置
	 public  static SignConfigBean getbreathingConfig(Context context,String contentjson){
//		    if(breathconfigBean==null){
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
//		    }
			return breathconfigBean;
		 }
		//体温显示不同的颜色
		public static void showDifferentColor_temp(Context context,Double temp,TextView tv_content,TextView tv_date,OldPeople oldPeople){
			if(configForSpecialOldBeans==null){
				configForSpecialOldBeans=getHaveConfigOld(context);
			}
			for(int j=0;j<configForSpecialOldBeans.size();j++){
				if(oldPeople.getId()==configForSpecialOldBeans.get(j).getOld_people_id()){
					SignConfigBean tempconfig=configForSpecialOldBeans.get(j).getSignConfigBeans().get(0);
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
					    				   tv_date.setTextColor( Color.GRAY);
					    			   }
					    			   tv_content.setTextColor( Color.GRAY);
					    		   }
					    	   }
					       }
				       }
				}
			}
		}
		//血压显示不同的颜色
		public static String showDifferentColor_pressure(Context context,int high_pressure,int low_pressure,TextView tv_content,TextView tv_date,OldPeople oldPeople){
			if(configForSpecialOldBeans==null){
				configForSpecialOldBeans=getHaveConfigOld(context);
			}
			for(int j=0;j<configForSpecialOldBeans.size();j++){
				if(oldPeople.getId()==configForSpecialOldBeans.get(j).getOld_people_id()){
					   SignConfigBean pressureconfig=configForSpecialOldBeans.get(j).getSignConfigBeans().get(1);
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
					    		   tv_content.setTextColor( Color.GRAY);
					    		   return "";
					    	   }
				    	   }
				       }
				}
		     }
			   return "";
		}
		//血糖显示不同的颜色
		public static void showDifferentColor_sugar(Context context,double sugar,TextView tv_content,TextView tv_date,OldPeople oldPeople){
			if(configForSpecialOldBeans==null){
				configForSpecialOldBeans=getHaveConfigOld(context);
			}
			for(int j=0;j<configForSpecialOldBeans.size();j++){
				if(oldPeople.getId()==configForSpecialOldBeans.get(j).getOld_people_id()){
					SignConfigBean sugarconfig=configForSpecialOldBeans.get(j).getSignConfigBeans().get(2);
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
					    			   tv_content.setTextColor( Color.GRAY);
					    			   if(tv_date != null) {
					    				   tv_date.setTextColor( Color.GRAY);
					    			   }
					    		   }
					    	   }
					       }
				       }
				  }  
		     }
		}
		//心率显示不同的颜色
		public static void showDifferentColor_pulse(Context context,int pulse,TextView tv_content,TextView tv_date,OldPeople oldPeople){
			if(configForSpecialOldBeans==null){
				configForSpecialOldBeans=getHaveConfigOld(context);
			}
			for(int j=0;j<configForSpecialOldBeans.size();j++){
				if(oldPeople.getId()==configForSpecialOldBeans.get(j).getOld_people_id()){
					SignConfigBean pulseconfig=configForSpecialOldBeans.get(j).getSignConfigBeans().get(3);
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
					    			   tv_content.setTextColor( Color.GRAY);
					    			   if(tv_date != null) {
					    				   tv_date.setTextColor( Color.GRAY);
					    			   }
					    		   }
					    	   }
					       }
				       }
					} 
				}
			
		}
		//排便显示不同的颜色
		public static void showDifferentColor_defecation(Context context,int pulse,TextView tv_content,TextView tv_date,OldPeople oldPeople){
			if(configForSpecialOldBeans==null){
				configForSpecialOldBeans=getHaveConfigOld(context);
			}
			for(int j=0;j<configForSpecialOldBeans.size();j++){
				if(oldPeople.getId()==configForSpecialOldBeans.get(j).getOld_people_id()){
					SignConfigBean defecationconfig=configForSpecialOldBeans.get(j).getSignConfigBeans().get(4);
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
					    			   tv_content.setTextColor( Color.GRAY);
					    			   if(tv_date != null) {
					    				   tv_date.setTextColor( Color.GRAY);
					    			   }
					    		   }
					    	   }
					       }
				       }
				}
			}
		}
		//呼吸显示不同的颜色
		public static void showDifferentColor_breath(Context context,int pulse,TextView tv_content,TextView tv_date,OldPeople oldPeople){
			if(configForSpecialOldBeans==null){
				configForSpecialOldBeans=getHaveConfigOld(context);
			}
			for(int j=0;j<configForSpecialOldBeans.size();j++){
				if(oldPeople.getId()==configForSpecialOldBeans.get(j).getOld_people_id()){
					SignConfigBean breathconfig=configForSpecialOldBeans.get(j).getSignConfigBeans().get(5);
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
					    			   tv_content.setTextColor( Color.GRAY);
					    			   if(tv_date != null) {
					    				   tv_date.setTextColor( Color.GRAY);
					    			   }
					    		   }
					    	   }
					       }
				     }  
				}
			}
		}
		//饮水显示不同的颜色
		public static void showDifferentColor_drinking(Context context,int pulse,TextView tv_content,TextView tv_date,String contentjson){
				 SignConfigBean breathconfig=ConfigForSpeialOldUtils.getbreathingConfig(context,contentjson);
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
				    			   tv_content.setTextColor( Color.GRAY);
				    			   if(tv_date != null) {
				    				   tv_date.setTextColor( Color.GRAY);
				    			   }
				    		   }
				    	   }
				       }
			     }  
		 }
}
