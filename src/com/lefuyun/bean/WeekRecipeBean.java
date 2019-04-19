package com.lefuyun.bean;

import java.util.List;

public class WeekRecipeBean {
	private long createTime;
	private List<WeekRecipe> dayRecipeBeans;
	private int id;
	private long firstDayOfWeek;
	private long lastDayOfWeek;
	private int weekno;
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public List<WeekRecipe> getDayRecipeBeans() {
		return dayRecipeBeans;
	}
	public void setDayRecipeBeans(List<WeekRecipe> dayRecipeBeans) {
		this.dayRecipeBeans = dayRecipeBeans;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getFirstDayOfWeek() {
		return firstDayOfWeek;
	}
	public void setFirstDayOfWeek(long firstDayOfWeek) {
		this.firstDayOfWeek = firstDayOfWeek;
	}
	public long getLastDayOfWeek() {
		return lastDayOfWeek;
	}
	public void setLastDayOfWeek(long lastDayOfWeek) {
		this.lastDayOfWeek = lastDayOfWeek;
	}
	public int getWeekno() {
		return weekno;
	}
	public void setWeekno(int weekno) {
		this.weekno = weekno;
	}
	
}
