package com.lefuyun.bean;

public class BodyData {
	private Long agency_id=(long) 0;
	private String agency_name="";
	private Long blood_pressure_id=(long) 0;
	private String entry_dt="";
	private String entry_staff_name="";//录入人
	private Long inspect_dt=(long) 0;//测量日期
	private String inspect_staff_name="";//测量人
	private int old_people_id=0;
	public String val1="";
	private String val2="";
	public Long getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(Long agency_id) {
		this.agency_id = agency_id;
	}
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
	public Long getBlood_pressure_id() {
		return blood_pressure_id;
	}
	public void setBlood_pressure_id(Long blood_pressure_id) {
		this.blood_pressure_id = blood_pressure_id;
	}
	public String getEntry_dt() {
		return entry_dt;
	}
	public void setEntry_dt(String entry_dt) {
		this.entry_dt = entry_dt;
	}
	public String getEntry_staff_name() {
		return entry_staff_name;
	}
	public void setEntry_staff_name(String entry_staff_name) {
		this.entry_staff_name = entry_staff_name;
	}
	public Long getInspect_dt() {
		return inspect_dt;
	}
	public void setInspect_dt(Long inspect_dt) {
		this.inspect_dt = inspect_dt;
	}
	public String getInspect_staff_name() {
		return inspect_staff_name;
	}
	public void setInspect_staff_name(String inspect_staff_name) {
		this.inspect_staff_name = inspect_staff_name;
	}
	public int getOld_people_id() {
		return old_people_id;
	}
	public void setOld_people_id(int old_people_id) {
		this.old_people_id = old_people_id;
	}
	
	public String getVal1() {
		return val1;
	}
	public void setVal1(String val1) {
		this.val1 = val1;
	}
	public String getVal2() {
		if(null==val2){
			return "";
		}else{
			return val2;
		}
	}
	public void setVal2(String val2) {
		this.val2 = val2;
	}
	
	
	
}
