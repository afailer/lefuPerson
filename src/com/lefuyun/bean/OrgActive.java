package com.lefuyun.bean;

import java.io.Serializable;

public class OrgActive implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int agencyId = 0;
	String content = "";
	String creatDt = "";
	String createTimeView = "";//创建时间
	long holdTime = 0;
	String holdTimeView = "";//举办时间
	private String images;//封面图
	int id = 0;
	int pageNo = 0;
	int pageSize = 0;
	String pic = "";
	String reserved = "";
	int startRow = 0;
	String theme = "";
	String agency_name = "";
	private int read_number;//阅读次数
	private int praise_number;//点赞次数
	public int getRead_number() {
		return read_number;
	}

	public void setRead_number(int read_number) {
		this.read_number = read_number;
	}

	public int getPraise_number() {
		return praise_number;
	}

	public void setPraise_number(int praise_number) {
		this.praise_number = praise_number;
	}

	public String getAgency_name() {
		return agency_name;
	}

	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatDt() {
		return creatDt;
	}

	public void setCreatDt(String creatDt) {
		this.creatDt = creatDt;
	}

	public String getCreateTimeView() {
		return createTimeView;
	}

	public void setCreateTimeView(String createTimeView) {
		this.createTimeView = createTimeView;
	}

	public long getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(long holdTime) {
		this.holdTime = holdTime;
	}

	public String getHoldTimeView() {
		return holdTimeView;
	}

	public void setHoldTimeView(String holdTimeView) {
		this.holdTimeView = holdTimeView;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "OrgActive [agencyId=" + agencyId + ", content=" + content
				+ ", creatDt=" + creatDt + ", createTimeView=" + createTimeView
				+ ", holdTime=" + holdTime + ", holdTimeView=" + holdTimeView
				+ ", images=" + images + ", id=" + id + ", pageNo=" + pageNo
				+ ", pageSize=" + pageSize + ", pic=" + pic + ", reserved="
				+ reserved + ", startRow=" + startRow + ", theme=" + theme
				+ ", agency_name=" + agency_name + ", read_number="
				+ read_number + ", praise_number=" + praise_number + "]";
	}
	
}
