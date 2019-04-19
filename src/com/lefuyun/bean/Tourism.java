package com.lefuyun.bean;

import java.io.Serializable;

public class Tourism implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id; // 产品ID
	private long company_id; // 公司ID
	private String name; // 产品名称
	private String beseem_age; // 适合年龄
	private int type; // 状态: 1起售, 2停售
	private int price; // 价格
	private String phone; // 联系电话
	private String introduction_img; // 介绍图(多张图以";"分割)
	private String content; // 产品介绍正文
	private long start_time; // 有效期开始时间
	private long finish_time; // 有效期结束时间
	private long departure_time; // 活动或产品的开始时间
	private long inspecter_id; // 录入人id
	private String inspecter; // 录入人姓名
	private long create_time; // 本条消息的创建时间
	private long update_time; // 本条消息的更新时间
	private String remark; // 备注
	private int status; // 本条数据的状态
	private String company_name; // 产品所属公司名称
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCompany_id() {
		return company_id;
	}
	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeseem_age() {
		return beseem_age;
	}
	public void setBeseem_age(String beseem_age) {
		this.beseem_age = beseem_age;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIntroduction_img() {
		return introduction_img;
	}
	public void setIntroduction_img(String introduction_img) {
		this.introduction_img = introduction_img;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getStart_time() {
		return start_time;
	}
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	public long getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(long finish_time) {
		this.finish_time = finish_time;
	}
	public long getDeparture_time() {
		return departure_time;
	}
	public void setDeparture_time(long departure_time) {
		this.departure_time = departure_time;
	}
	public long getInspecter_id() {
		return inspecter_id;
	}
	public void setInspecter_id(long inspecter_id) {
		this.inspecter_id = inspecter_id;
	}
	public String getInspecter() {
		return inspecter;
	}
	public void setInspecter(String inspecter) {
		this.inspecter = inspecter;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public long getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	@Override
	public String toString() {
		return "Tourism [id=" + id + ", company_id=" + company_id + ", name="
				+ name + ", beseem_age=" + beseem_age + ", type=" + type
				+ ", price=" + price + ", phone=" + phone
				+ ", introduction_img=" + introduction_img + ", content="
				+ content + ", start_time=" + start_time + ", finish_time="
				+ finish_time + ", departure_time=" + departure_time
				+ ", inspecter_id=" + inspecter_id + ", inspecter=" + inspecter
				+ ", create_time=" + create_time + ", update_time="
				+ update_time + ", remark=" + remark + ", status=" + status
				+ ", company_name=" + company_name + "]";
	}
}
