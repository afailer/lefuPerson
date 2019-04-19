package com.lefuyun.bean;

import java.util.ArrayList;

public class ConfigForSpecialOldBean {
	private long old_people_id;// 老人的id
	private ArrayList<SignConfigBean> signConfigBeans;// 老人的配置集合

	public long getOld_people_id() {
		return old_people_id;
	}

	public void setOld_people_id(long old_people_id) {
		this.old_people_id = old_people_id;
	}

	public ArrayList<SignConfigBean> getSignConfigBeans() {
		return signConfigBeans;
	}

	public void setSignConfigBeans(ArrayList<SignConfigBean> signConfigBeans) {
		this.signConfigBeans = signConfigBeans;
	}

}
