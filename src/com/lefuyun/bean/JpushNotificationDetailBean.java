package com.lefuyun.bean;

import java.io.Serializable;

public class JpushNotificationDetailBean implements Serializable{
	private int id;
	private int type;// 1养老新闻2养生课堂
	private int agency_id;
	private int create_by;
	private long create_dt;
	private String theme;
	private long update_dt;
	private String loginIMEI;
	private long loginTime;
	private String mailbox;
	private String mobile;
	private String name;
	private String phone;
	private int user_id;
	private String user_name;
	private String picture; // 新闻标题图片
	private String headline; // 内容提要
	private int read_number; // 阅读次数
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public int getRead_number() {
		return read_number;
	}

	public void setRead_number(int read_number) {
		this.read_number = read_number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(int agency_id) {
		this.agency_id = agency_id;
	}

	public int getCreate_by() {
		return create_by;
	}

	public void setCreate_by(int create_by) {
		this.create_by = create_by;
	}

	public long getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public long getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(long update_dt) {
		this.update_dt = update_dt;
	}

	public String getLoginIMEI() {
		return loginIMEI;
	}

	public void setLoginIMEI(String loginIMEI) {
		this.loginIMEI = loginIMEI;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	@Override
	public String toString() {
		return "JpushNotificationDetailBean [id=" + id + ", type=" + type
				+ ", agency_id=" + agency_id + ", create_by=" + create_by
				+ ", create_dt=" + create_dt + ", theme=" + theme
				+ ", update_dt=" + update_dt + ", loginIMEI=" + loginIMEI
				+ ", loginTime=" + loginTime + ", mailbox=" + mailbox
				+ ", mobile=" + mobile + ", name=" + name + ", phone=" + phone
				+ ", user_id=" + user_id + ", user_name=" + user_name
				+ ", picture=" + picture + ", headline=" + headline
				+ ", read_number=" + read_number + "]";
	}

}
