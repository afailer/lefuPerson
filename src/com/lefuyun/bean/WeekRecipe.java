package com.lefuyun.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.Utils;

public class WeekRecipe {
	private String breakfast_snack="";//早餐加餐
	private String lunch_snack="";//午餐加餐
	private String dinner_snack="";//晚餐加餐
	private long weekrecipe_id;//周食谱的id
	private String dinner="";//晚餐
	private String dinner_pic="";//晚餐图片
	private String dinner_snack_pic="";//晚餐加餐图片
	private String day_of_week=""; //当前周的第几天
	private String breakfast="";//早餐
	private String breakfast_pic="";//早餐图片
	private String breakfast_snack_pic="";//早餐加餐图片
	private String lunch="";//午餐
	private String lunch_pic="";//午餐图片
	private String lunch_snack_pic="";//午餐加餐图片
	private long id;
	private long inspect_dt;
	private long updateTime;
	private long createTime;
	public pic hasPic(){
		if(!"".equals(getBreakfast_pic())||!"".equals(getBreakfast())){
			return new pic(getBreakfast(),"早餐",getBreakfast_pic());
		}
		if(!"".equals(getBreakfast_snack_pic())||!"".equals(getBreakfast_snack())){
			return new pic(getBreakfast_snack(),"早餐加餐",getBreakfast_snack_pic());
		}
		if(!"".equals(getLunch_pic())||!"".equals(getLunch())){
			return new pic(getLunch(),"午餐", getLunch_pic());
		}
		if(!"".equals(getLunch_snack_pic())||!"".equals(getLunch_snack())){
			return new pic(getLunch_snack(),"午餐加餐", getLunch_snack_pic());
		}
		if(!"".equals(getDinner_pic())||!"".equals(getDinner())){
			return new pic(getDinner(),"晚餐" ,getDinner_pic());
		}
		if(!"".equals(getDinner_snack_pic())||!"".equals(getDinner_snack())){
			return new pic(getDinner_snack(),"晚餐加餐",getDinner_snack_pic());
		}
		return null;
	}
	public String getPicUrl(){
		if(!"".equals(getBreakfast_pic())){
			return getBreakfast_pic();
		}
		if(!"".equals(getBreakfast_snack_pic())){
			return getBreakfast_snack_pic();
		}
		if(!"".equals(getLunch_pic())){
			return getLunch_pic();
		}
		if(!"".equals(getLunch_snack_pic())){
			return getLunch_snack_pic();
		}
		if(!"".equals(getDinner_pic())){
			return getDinner_pic();
		}
		if(!"".equals(getDinner_snack_pic())){
			return getDinner_snack_pic();
		}
		return "";
	}
	public List<pic> getPicUrlList(){
		List<pic> picList=new ArrayList<pic>();
		if(!"".equals(getBreakfast_pic())||!"".equals(getBreakfast())){
			picList.add(new pic(getBreakfast(),"早餐",getBreakfast_pic()));
		}
		if(!"".equals(getBreakfast_snack_pic())||!"".equals(getBreakfast_snack())){
			picList.add(new pic(getBreakfast_snack(),"早餐加餐",getBreakfast_snack_pic()));
		}
		if(!"".equals(getLunch_pic())||!"".equals(getLunch())){
			picList.add(new pic(getLunch(),"午餐",getLunch_pic()));
		}
		if(!"".equals(getLunch_snack_pic())||!"".equals(getLunch_snack())){
			picList.add(new pic(getLunch_snack(),"午餐加餐",getLunch_snack_pic()));
		}
		if(!"".equals(getDinner_pic())||!"".equals(getDinner())){
			picList.add(new pic(getDinner(),"晚餐",getDinner_pic()));
		}
		if(!"".equals(getDinner_snack_pic())||!"".equals(getDinner_snack())){
			picList.add(new pic(getDinner_snack(),"晚餐加餐", getDinner_snack_pic()));
		}
		return picList;
	}
	public String getBreakfast_snack() {
		return breakfast_snack;
	}
	public void setBreakfast_snack(String breakfast_snack) {
		this.breakfast_snack = breakfast_snack;
	}
	public String getLunch_snack() {
		return lunch_snack;
	}
	public void setLunch_snack(String lunch_snack) {
		this.lunch_snack = lunch_snack;
	}
	public String getDinner_snack() {
		return dinner_snack;
	}
	public void setDinner_snack(String dinner_snack) {
		this.dinner_snack = dinner_snack;
	}
	public long getWeekrecipe_id() {
		return weekrecipe_id;
	}
	public void setWeekrecipe_id(long weekrecipe_id) {
		this.weekrecipe_id = weekrecipe_id;
	}
	public String getDinner() {
		return dinner;
	}
	public void setDinner(String dinner) {
		this.dinner = dinner;
	}
	public String getDinner_pic() {
		return dinner_pic;
	}
	public void setDinner_pic(String dinner_pic) {
		this.dinner_pic = dinner_pic;
	}
	public String getDinner_snack_pic() {
		return dinner_snack_pic;
	}
	public void setDinner_snack_pic(String dinner_snack_pic) {
		this.dinner_snack_pic = dinner_snack_pic;
	}
	public String getDay_of_week() {
		return day_of_week;
	}
	public void setDay_of_week(String day_of_week) {
		this.day_of_week = day_of_week;
	}
	public String getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}
	public String getBreakfast_pic() {
		return breakfast_pic;
	}
	public void setBreakfast_pic(String breakfast_pic) {
		this.breakfast_pic = breakfast_pic;
	}
	public String getBreakfast_snack_pic() {
		return breakfast_snack_pic;
	}
	public void setBreakfast_snack_pic(String breakfast_snack_pic) {
		this.breakfast_snack_pic = breakfast_snack_pic;
	}
	public String getLunch() {
		return lunch;
	}
	public void setLunch(String lunch) {
		this.lunch = lunch;
	}
	public String getLunch_pic() {
		return lunch_pic;
	}
	public void setLunch_pic(String lunch_pic) {
		this.lunch_pic = lunch_pic;
	}
	public String getLunch_snack_pic() {
		return lunch_snack_pic;
	}
	public void setLunch_snack_pic(String lunch_snack_pic) {
		this.lunch_snack_pic = lunch_snack_pic;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public long getInspect_dt() {
		return inspect_dt;
	}
	public void setInspect_dt(long inspect_dt) {
		this.inspect_dt = inspect_dt;
	}

	public class pic{
		private String reserved;
		private String meatName;
		private String urlString;
		public String getReserved() {
			return reserved;
		}
		public void setReserved(String reserved) {
			this.reserved = reserved;
		}
		public String getMeatName() {
			return meatName;
		}
		public void setMeatName(String meatName) {
			this.meatName = meatName;
		}
		public String getUrlString() {
			return urlString;
		}
		public void setUrlString(String urlString) {
			this.urlString = urlString;
		}
		public pic(String reserved, String meatName, String urlString) {
			super();
			this.reserved = reserved;
			this.meatName = meatName;
			this.urlString = urlString;
		}
		
	}
	public String getTime(){
		return TimeZoneUtil.sdfNormal.format(new Date(updateTime));
	}
	@Override
	public String toString() {
		String s="";
		if(!"".equals(getBreakfast())){
			s+=("早餐:"+getBreakfast());
		}
		if(!"".equals(getBreakfast_snack())){
			s+=(" 早餐加餐:"+getBreakfast_snack());
		}
		if(!"".equals(getLunch())){
			s+=(" 午餐:"+getLunch());
		}
		if(!"".equals(getLunch_snack())){
			s+=(" 午餐加餐:"+getLunch_snack());
		}
		if("".equals(getDinner())){
			s+=(" 晚餐"+getDinner());
		}
		if("".equals(getDinner_snack())){
			s+=(" 晚餐加餐"+getDinner_snack());
		}
		return s;
	}
}
