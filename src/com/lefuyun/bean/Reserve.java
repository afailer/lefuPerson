package com.lefuyun.bean;

import java.io.Serializable;
/**
 * 床位预约bean类
 */
public class Reserve implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String contactPerson; // 联系人
	private String mobile; // 联系号码
	private String mailbox; // 邮箱
	private String age; // 老人年龄
	private String selfCareAbility; // 老人自理能力
	private long orderbed; // 预约床位,默认机构id
	private long agencyId; // 机构id
	
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMailbox() {
		return mailbox;
	}
	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSelfCareAbility() {
		return selfCareAbility;
	}
	public void setSelfCareAbility(String selfCareAbility) {
		this.selfCareAbility = selfCareAbility;
	}
	public long getOrderbed() {
		return orderbed;
	}
	public void setOrderbed(long orderbed) {
		this.orderbed = orderbed;
	}
	public long getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}
	@Override
	public String toString() {
		return "Reserve [contactPerson=" + contactPerson + ", mobile=" + mobile
				+ ", mailbox=" + mailbox + ", age=" + age
				+ ", selfCareAbility=" + selfCareAbility + ", orderbed="
				+ orderbed + ", agencyId=" + agencyId + "]";
	}
}
