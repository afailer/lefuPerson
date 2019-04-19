package com.lefuyun.bean;

public class SignDataBean {
	private String old_people_name;

	private long agency_id;

	private long blood_pressure_id;

	private String val1;

	private long inspect_dt;

	private String inspect_user_name;

	private int approval_status;

	private long entry_dt;

	private String agency_name;

	private long old_people_id;

	private String elderly_name;

	private String reserved;

	private String val2;

	private String entry_user_name;

	private int scode;

	private String entry_username;
	private int defecation_times;// 排便次数
	private int breathing_times;// 呼吸次数
	private int water_amount;// 饮水
	private String sleep_quality;// 睡眠质量
	private int meal_amount;// 饮食量 1偏少2正常3偏少
	private int meal_type;// 饮食类型1早2中3晚
    private int hasdata;//是否有数据 0有数据1无数据
    
	public int getHasdata() {
		return hasdata;
	}

	public void setHasdata(int hasdata) {
		this.hasdata = hasdata;
	}

	public int getDefecation_times() {
		return defecation_times;
	}

	public void setDefecation_times(int defecation_times) {
		this.defecation_times = defecation_times;
	}

	public int getBreathing_times() {
		return breathing_times;
	}

	public void setBreathing_times(int breathing_times) {
		this.breathing_times = breathing_times;
	}

	public int getWater_amount() {
		return water_amount;
	}

	public void setWater_amount(int water_amount) {
		this.water_amount = water_amount;
	}

	public String getSleep_quality() {
		return sleep_quality;
	}

	public void setSleep_quality(String sleep_quality) {
		this.sleep_quality = sleep_quality;
	}

	public int getMeal_amount() {
		return meal_amount;
	}

	public void setMeal_amount(int meal_amount) {
		this.meal_amount = meal_amount;
	}

	public int getMeal_type() {
		return meal_type;
	}

	public void setMeal_type(int meal_type) {
		this.meal_type = meal_type;
	}

	public String getOld_people_name() {
		return old_people_name;
	}

	public void setOld_people_name(String old_people_name) {
		this.old_people_name = old_people_name;
	}

	public long getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}

	public long getBlood_pressure_id() {
		return blood_pressure_id;
	}

	public void setBlood_pressure_id(long blood_pressure_id) {
		this.blood_pressure_id = blood_pressure_id;
	}

	public String getVal1() {
		return val1;
	}

	public void setVal1(String val1) {
		this.val1 = val1;
	}

	public long getInspect_dt() {
		return inspect_dt;
	}

	public void setInspect_dt(long inspect_dt) {
		this.inspect_dt = inspect_dt;
	}

	public String getInspect_user_name() {
		return inspect_user_name;
	}

	public void setInspect_user_name(String inspect_user_name) {
		this.inspect_user_name = inspect_user_name;
	}

	public int getApproval_status() {
		return approval_status;
	}

	public void setApproval_status(int approval_status) {
		this.approval_status = approval_status;
	}

	public long getEntry_dt() {
		return entry_dt;
	}

	public void setEntry_dt(long entry_dt) {
		this.entry_dt = entry_dt;
	}

	public String getAgency_name() {
		return agency_name;
	}

	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}

	public long getOld_people_id() {
		return old_people_id;
	}

	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}

	public String getElderly_name() {
		return elderly_name;
	}

	public void setElderly_name(String elderly_name) {
		this.elderly_name = elderly_name;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getVal2() {
		return val2;
	}

	public void setVal2(String val2) {
		this.val2 = val2;
	}

	public String getEntry_user_name() {
		return entry_user_name;
	}

	public void setEntry_user_name(String entry_user_name) {
		this.entry_user_name = entry_user_name;
	}

	public int getScode() {
		return scode;
	}

	public void setScode(int scode) {
		this.scode = scode;
	}

	public String getEntry_username() {
		return entry_username;
	}

	public void setEntry_username(String entry_username) {
		this.entry_username = entry_username;
	}

}
