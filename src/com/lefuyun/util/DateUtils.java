package com.lefuyun.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class DateUtils {
	/**
	 * 将长时间类型转换成 年-月-日 时:分:秒形式
	 * 
	 * @param l
	 * @return
	 */
	public static String getStringtimeFromLong(long time){
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateformat.format(new Date(time));
	}
	/**
	 * 将长时间类型转换成 年-月-日 形式
	 * 
	 * @param l
	 * @return
	 */
	public static String dateFormatYMD(long l) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(l);
		return dateformat.format(date);
	}
	/**
	 * 将长时间类型转换成 年-月-日 时:分:秒形式
	 * 
	 * @param l
	 * @return
	 */
	public static String dateFormatYMD_HMS(String l) {
		long lng = Long.valueOf(l);
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date(lng);
		return dateformat.format(date);
	}
	
    /**
     * 把时间字符串转成long型
     * @param strtime  2012-01-01 02:03:04
     * @return  返回long
     */
    public static long getLongtimeFromString(String strtime){
    	 Calendar c = Calendar.getInstance();  
         try {
				c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strtime));
				LogUtil.i("tag", "time2"+c.getTimeInMillis());
			} catch (ParseException e) {
				e.printStackTrace();
				LogUtil.i("tag", "时间转换错误");
			}  
         return c.getTimeInMillis();
    }
}
