package com.lefuyun.bean;

import java.io.Serializable;

public class Assess implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int signData; // 健康状况
	private int dailyLife; // 日常生活
	private int dailyNurs; // 护理服务
	private int drinkMeal; // 饮食情况
	public int getSignData() {
		return signData;
	}
	public void setSignData(int signData) {
		this.signData = signData;
	}
	public int getDailyLife() {
		return dailyLife;
	}
	public void setDailyLife(int dailyLife) {
		this.dailyLife = dailyLife;
	}
	public int getDailyNurs() {
		return dailyNurs;
	}
	public void setDailyNurs(int dailyNurs) {
		this.dailyNurs = dailyNurs;
	}
	public int getDrinkMeal() {
		return drinkMeal;
	}
	public void setDrinkMeal(int drinkMeal) {
		this.drinkMeal = drinkMeal;
	}
	@Override
	public String toString() {
		return "Assess [signData=" + signData + ", dailyLife=" + dailyLife
				+ ", dailyNurs=" + dailyNurs + ", drinkMeal=" + drinkMeal + "]";
	}
}
