package com.lefuyun.bean;

public class NurseItem {
	long nursing_dt; //护理时间
	
	long nurs_items_id; //护理内容id
	
	long old_people_id; //老人id
	
	String entry_staff_name; //护理人员姓名
	
	String media; //媒体
	
	String elderly_name; //老人名字
	
	String items; //聊天，吃饭
	
	String reserved; //备注
	
	public long getNursing_dt() {
		return nursing_dt;
	}
	public void setNursing_dt(long nursing_dt) {
		this.nursing_dt = nursing_dt;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getElderly_name() {
		return elderly_name;
	}
	public void setElderly_name(String elderly_name) {
		this.elderly_name = elderly_name;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public long getNurs_items_id() {
		return nurs_items_id;
	}
	public void setNurs_items_id(long nurs_items_id) {
		this.nurs_items_id = nurs_items_id;
	}
	public long getOld_people_id() {
		return old_people_id;
	}
	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}
	public String getEntry_staff_name() {
		return entry_staff_name;
	}
	public void setEntry_staff_name(String entry_staff_name) {
		this.entry_staff_name = entry_staff_name;
	}
	
}
