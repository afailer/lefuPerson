package com.lefuyun.bean;

import java.io.Serializable;

/**
 * 老人评估bean类
 *
 */
public class OldPeopleAssess implements Serializable{
	
	protected static final long serialVersionUID = 1L;
	
	private long id; // 老人评估条目IDassessID
	private long agency_id; // 老人所属机构ID
	private long old_people_id; // 老人ID
	private int score; // 老人当前评分
	private String type; // 评估
	private int signData; // 健康状况
	private int dailyLife; // 日常生活
	private int dailyNurs; // 护理服务
	private int drinkMeal; // 饮食情况
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}
	public long getOld_people_id() {
		return old_people_id;
	}
	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
		return "OldPeopleAssess [id=" + id + ", agency_id=" + agency_id
				+ ", old_people_id=" + old_people_id + ", score=" + score
				+ ", type=" + type + "]";
	}
	
}
