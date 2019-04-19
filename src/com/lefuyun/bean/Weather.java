package com.lefuyun.bean;

import java.io.Serializable;

public class Weather implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tem; // 最低气温和最高气温
	private String img; // 天气图片
	private int code; //
	private String txt; // 天气状况;比如 多云
	private String city; // 老人所在的城市
	
	public String getTem() {
		return tem;
	}
	public void setTem(String tem) {
		this.tem = tem;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "Weather [tem=" + tem + ", img=" + img + ", code=" + code
				+ ", txt=" + txt + ", city=" + city + "]";
	}
}
