package com.lefuyun.bean;

import java.io.Serializable;

import com.lefuyun.base.BaseActivity;

public class PersonalPreference implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title; // 条目标题
	private String message; // 描述
	private Class<? extends BaseActivity> clzz; // 要跳转Activity的字节码
	private boolean skip; // 是否能够跳转
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Class<? extends BaseActivity> getClzz() {
		return clzz;
	}
	public void setClzz(Class<? extends BaseActivity> clzz) {
		this.clzz = clzz;
	}
	public boolean isSkip() {
		return skip;
	}
	public void setSkip(boolean skip) {
		this.skip = skip;
	}
}
