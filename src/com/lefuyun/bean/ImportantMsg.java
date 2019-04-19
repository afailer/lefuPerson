package com.lefuyun.bean;

import java.io.Serializable;

public class ImportantMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;//通知ID
	private long agency_id;//机构ID
	private int type;//通知类型
	private String theme="";//标题
	private String picture;//封面图URL
	private String headline;//内容提要
	private String content; //通知内容
	private int read_number; //阅读次数
	private int status;  //0：未发布，1：已发布
	private long create_by;  //创建人ID
	private long create_dt;  //创建时间
	private long update_dt;  //修改时间
	private int pageNo; //当前页号
	private int startRow; //起始行
	private String oldPeopleIds;//老人的id
	private String old_name;
	
	public String getOld_name() {
		return old_name;
	}
	public void setOld_name(String old_name) {
		this.old_name = old_name;
	}
	public String getOldPeopleIds() {
		return oldPeopleIds;
	}
	public void setOldPeopleIds(String oldPeopleIds) {
		this.oldPeopleIds = oldPeopleIds;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(long agency_id) {
		this.agency_id = agency_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRead_number() {
		return read_number;
	}
	public void setRead_number(int read_number) {
		this.read_number = read_number;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreate_by() {
		return create_by;
	}
	public void setCreate_by(long create_by) {
		this.create_by = create_by;
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
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	@Override
	public String toString() {
		return "ImportantMsg [id=" + id + ", agency_id=" + agency_id
				+ ", type=" + type + ", theme=" + theme + ", picture="
				+ picture + ", headline=" + headline + ", content=" + content
				+ ", read_number=" + read_number + ", status=" + status
				+ ", create_by=" + create_by + ", create_dt=" + create_dt
				+ ", update_dt=" + update_dt + ", pageNo=" + pageNo
				+ ", startRow=" + startRow + "]";
	}
	
	
	
}
