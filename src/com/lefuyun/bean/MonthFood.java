package com.lefuyun.bean;

import java.util.ArrayList;
import java.util.List;

public class MonthFood {
	boolean isAdd=true;
	int monthNum;
	String inspect_dt;
	public String getInspect_dt() {
		return inspect_dt;
	}
	public void setInspect_dt(String inspect_dt) {
		this.inspect_dt = inspect_dt;
	}
	List<WeekFood> weekFoods=new ArrayList<WeekFood>();
	public boolean isAdd() {
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public void addWeekFood(WeekFood food){
		weekFoods.add(food);
	}
	public List<WeekFood> getWeekFoods() {
		return weekFoods;
	}
	public void setWeekFoods(List<WeekFood> weekFoods) {
		this.weekFoods = weekFoods;
	}
	public MonthFood(int monthNum,String inspect_dt) {
		super();
		this.monthNum = monthNum;
		this.inspect_dt=inspect_dt;
	}
	public int getMonthNum() {
		return monthNum;
	}
	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}
	
	
}
