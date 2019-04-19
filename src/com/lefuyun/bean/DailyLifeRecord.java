package com.lefuyun.bean;

public class DailyLifeRecord {
	private long id; //实体ID
	
	private String sleep_quality;//睡眠质量
	
	private long inspect_dt;
	
	private String media="";
	
	private int meal_amount;//饮食量 1==偏少 2==正常 3==偏多
	
	private int meal_type;//就餐类型 1==早 2==午 3==晚
	
	private int type;//实体类型   1==DailyLifeRecord(日常生活) 2==MealRecordBean(吃饭) 3==SleepRecordBean(睡眠)
	
	private int pageNo;

    private String reserved;//备注
	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSleep_quality() {
		return sleep_quality;
	}

	public void setSleep_quality(String sleep_quality) {
		this.sleep_quality = sleep_quality;
	}

	public long getInspect_dt() {
		return inspect_dt;
	}

	public void setInspect_dt(long inspect_dt) {
		this.inspect_dt = inspect_dt;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "DailyLifeRecord [id=" + id + ", sleep_quality=" + sleep_quality
				+ ", inspect_dt=" + inspect_dt + ", media=" + media
				+ ", meal_amount=" + meal_amount + ", meal_type=" + meal_type
				+ ", type=" + type + ", pageNo=" + pageNo + ", reserved="
				+ reserved + "]";
	}
	
}
