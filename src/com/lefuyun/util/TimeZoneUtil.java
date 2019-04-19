package com.lefuyun.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 地域事件管理工具类
 */
public class TimeZoneUtil {

	/**
	 * 判断用户的设备时区是否为东八区（中国） 2014年7月31日
	 * @return
	 */
	public static boolean isInEasternEightZones() {
		boolean defaultVaule = true;
		if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
			defaultVaule = true;
		else
			defaultVaule = false;
		return defaultVaule;
	}

	/**
	 * 根据不同时区，转换时间 2014年7月31日
	 * @param time
	 * @return
	 */
	public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
		Date finalDate = null;
		if (date != null) {
			int timeOffset = oldZone.getOffset(date.getTime())
					- newZone.getOffset(date.getTime());
			finalDate = new Date(date.getTime() - timeOffset);
		}
		return finalDate;
	}
	
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日"),sdfDay=new SimpleDateFormat("MM.dd"), sdfNormal=new SimpleDateFormat("yyyy/MM/dd hh时mm分"),msgSdf=new SimpleDateFormat("yyyy-MM-dd"),monthSdf=new SimpleDateFormat("yyyyMM"),month=new SimpleDateFormat("yyyy年MM月"),normalSdf=new SimpleDateFormat("yyyy年MM月dd日");
	public static String getTimeCompute(long time){
		//return StringUtils.friendly_time(time);
		long oneHour=3600000;
		int hour=(int) ((new Date().getTime()-time)/oneHour);
		if(hour==0){
			return "半小时前";
		}else if(hour>0&&hour<24){
			return hour+"小时前";
		}else if(hour>24&&hour<48){
			return "昨天";
		}else if(hour>48&&hour<72){
			return "前天";
		}else{
			return sdf.format(new Date(time));
		}
	}
	/*public static long getMonthTime(long time){
		String string = msgSdf.format(new Date(time));
		Date d=new Date();
		try {
			d=msgSdf.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d.getTime();
	}*/
	public static int getMonthNum(long time){
		String string = monthSdf.format(new Date(time));
		return Integer.parseInt(string);
	}
	public static String getWeekOfDate(long date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(date));
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
		w = 0;

		return weekDays[w];
	}
	@SuppressWarnings("deprecation")
	public static String getTimeNormal(long time){
		return sdfNormal.format(new Date(time));
	}
}
