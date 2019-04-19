package com.lefuyun.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_city")
public class City implements Serializable{

	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	private long _id;
	@DatabaseField(columnName = "id")
	private long id;
	@DatabaseField(columnName = "pid") 
	private long pid;
	@DatabaseField(columnName = "region_name")
    private String region_name;
    private String sortLetters; // 显示数据拼音的首字母
    
    public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	@Override
	public String toString() {
		return "City [id=" + id + ", pid=" + pid + ", region_name="
				+ region_name + ", sortLetters=" + sortLetters + "]";
	}
}
