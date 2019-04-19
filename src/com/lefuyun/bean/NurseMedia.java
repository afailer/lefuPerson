package com.lefuyun.bean;

import java.io.Serializable;

public class NurseMedia implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long nursing_dt; //护理时间
	private String media; //路径集合
	private String reserved; //备注信息
	private long daily_id;//id
	
	public long getDaily_id() {
		return daily_id;
	}
	public void setDaily_id(long daily_id) {
		this.daily_id = daily_id;
	}
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
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
}
