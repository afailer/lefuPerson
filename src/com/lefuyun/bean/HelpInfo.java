package com.lefuyun.bean;

import java.io.Serializable;

public class HelpInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public HelpInfo() {
		super();
	}
	private int mId;
	
	private long create_dt;
	
	private int item_id;
	
	private int agency_id;
	
	private String remark;
	
	private String nursing_content;
	private int icon;
	
	

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public long getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(long create_dt) {
		this.create_dt = create_dt;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public int getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(int agency_id) {
		this.agency_id = agency_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNursing_content() {
		return nursing_content;
	}

	public void setNursing_content(String nursing_content) {
		this.nursing_content = nursing_content;
	}
	

	

	
	
	
}
