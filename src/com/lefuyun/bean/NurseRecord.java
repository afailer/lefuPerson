package com.lefuyun.bean;

import java.io.Serializable;

public class NurseRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long agency_id;//机构ID
	private long audioSum;//音频总数
	private long videoSum;//视频总数
	private long pictureSum;//图片总数
	private long caregiver_id;//护理人员id
	private String caregiver_name;//护理人员姓名
	private long daily_id; //日常生活ID
	private long praise_number;//点赞的次数
	private String elderly_name;//老人姓名
	private long old_people_id;//老人id
	private String items;//护理名称
	private String media;//媒体名称
	private int nurs_items_id;//护理内容id
	private long nursing_dt;//护理时间
	private String reserved="";//备注信息
	public long getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}
	public long getAudioSum() {
		return audioSum;
	}
	public void setAudioSum(long audioSum) {
		this.audioSum = audioSum;
	}
	public long getVideoSum() {
		return videoSum;
	}
	public void setVideoSum(long videoSum) {
		this.videoSum = videoSum;
	}
	public long getPictureSum() {
		return pictureSum;
	}
	public void setPictureSum(long pictureSum) {
		this.pictureSum = pictureSum;
	}
	public long getCaregiver_id() {
		return caregiver_id;
	}
	public void setCaregiver_id(long caregiver_id) {
		this.caregiver_id = caregiver_id;
	}
	public String getCaregiver_name() {
		return caregiver_name;
	}
	public void setCaregiver_name(String caregiver_name) {
		this.caregiver_name = caregiver_name;
	}
	public long getDaily_id() {
		return daily_id;
	}
	public void setDaily_id(long daily_id) {
		this.daily_id = daily_id;
	}
	public long getPraise_number() {
		return praise_number;
	}
	public void setPraise_number(long praise_number) {
		this.praise_number = praise_number;
	}
	public String getElderly_name() {
		return elderly_name;
	}
	public void setElderly_name(String elderly_name) {
		this.elderly_name = elderly_name;
	}
	public long getOld_people_id() {
		return old_people_id;
	}
	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public int getNurs_items_id() {
		return nurs_items_id;
	}
	public void setNurs_items_id(int nurs_items_id) {
		this.nurs_items_id = nurs_items_id;
	}
	public long getNursing_dt() {
		return nursing_dt;
	}
	public void setNursing_dt(long nursing_dt) {
		this.nursing_dt = nursing_dt;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
}
