package com.lefuyun.bean;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private long user_id;   // 用户ID
	private String user_name; // 用户名称
	private String name; // 
	private String password; // 用户密码
	private String icon; // 用户头像url
	private int gender; // 
	private long birthday_dt; // 用户生日
	private int birthday_type;
	private int document_type; // 
	private long create_dt; // 用户创建日期
	private long update_dt; // 用户信息最后修改日期
	private String mailbox; // 用户邮箱
	private String mailCode; // 
	private String mobile; // 用户手机号码
	private int mobileCode;
	private long agency_id; // 用户所属机构ID
	private String accountAddress; // 
	private String address; // 
	private String agencys; // 
	private String document_number; // 
	private String phone; // 
	private boolean rememberMe; // 
    private long staff_id; // 
    private String typeList; // 
    
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public long getBirthday_dt() {
		return birthday_dt;
	}
	public void setBirthday_dt(long birthday_dt) {
		this.birthday_dt = birthday_dt;
	}
	public int getBirthday_type() {
		return birthday_type;
	}
	public void setBirthday_type(int birthday_type) {
		this.birthday_type = birthday_type;
	}
	public int getDocument_type() {
		return document_type;
	}
	public void setDocument_type(int document_type) {
		this.document_type = document_type;
	}
	public long getCreate_dt() {
		return create_dt;
	}
	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}
	public long getUpdate_dt() {
		return update_dt;
	}
	public void setUpdate_dt(long update_dt) {
		this.update_dt = update_dt;
	}
	public String getMailbox() {
		return mailbox;
	}
	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}
	public String getMailCode() {
		return mailCode;
	}
	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(int mobileCode) {
		this.mobileCode = mobileCode;
	}
	public long getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}
	public String getAccountAddress() {
		return accountAddress;
	}
	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAgencys() {
		return agencys;
	}
	public void setAgencys(String agencys) {
		this.agencys = agencys;
	}
	public String getDocument_number() {
		return document_number;
	}
	public void setDocument_number(String document_number) {
		this.document_number = document_number;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	public long getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(long staff_id) {
		this.staff_id = staff_id;
	}
	public String getTypeList() {
		return typeList;
	}
	public void setTypeList(String typeList) {
		this.typeList = typeList;
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_name=" + user_name
				+ ", name=" + name + ", password=" + password + ", icon="
				+ icon + ", gender=" + gender + ", birthday_dt=" + birthday_dt
				+ ", birthday_type=" + birthday_type + ", document_type="
				+ document_type + ", create_dt=" + create_dt + ", update_dt="
				+ update_dt + ", mailbox=" + mailbox + ", mailCode=" + mailCode
				+ ", mobile=" + mobile + ", mobileCode=" + mobileCode
				+ ", agency_id=" + agency_id + ", accountAddress="
				+ accountAddress + ", address=" + address + ", agencys="
				+ agencys + ", document_number=" + document_number + ", phone="
				+ phone + ", rememberMe=" + rememberMe + ", staff_id="
				+ staff_id + ", typeList=" + typeList + "]";
	}
    
}
