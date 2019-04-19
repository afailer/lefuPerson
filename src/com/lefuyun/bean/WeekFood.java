package com.lefuyun.bean;

import java.io.Serializable;

public class WeekFood implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int weekno;
	private long inspect_dt;
	private long firstDayOfWeek;
	private long lastDayOfWeek;
	
	public long getInspect_dt() {
		return inspect_dt;
	}
	public void setInspect_dt(long inspect_dt) {
		this.inspect_dt = inspect_dt;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getWeekno() {
		return weekno;
	}
	public void setWeekno(int weekno) {
		this.weekno = weekno;
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
	
}
