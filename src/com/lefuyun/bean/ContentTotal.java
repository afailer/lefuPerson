package com.lefuyun.bean;

public class ContentTotal {
  private String tag;
  private String creat_time;
  private String update_time;
  private String agency_id;
  private String version;
  private String id;
  private String  content;
  private int approvalStatus;

public int getApprovalStatus() {
	return approvalStatus;
}
public void setApprovalStatus(int approvalStatus) {
	this.approvalStatus = approvalStatus;
}
public String getTag() {
	return tag;
}
public void setTag(String tag) {
	this.tag = tag;
}
public String getCreat_time() {
	return creat_time;
}
public void setCreat_time(String creat_time) {
	this.creat_time = creat_time;
}
public String getUpdate_time() {
	return update_time;
}
public void setUpdate_time(String update_time) {
	this.update_time = update_time;
}
public String getAgency_id() {
	return agency_id;
}
public void setAgency_id(String agency_id) {
	this.agency_id = agency_id;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
@Override
public String toString() {
	return "Content [tag=" + tag + ", creat_time=" + creat_time
			+ ", update_time=" + update_time + ", agency_id=" + agency_id
			+ ", version=" + version + ", id=" + id + ", content=" + content
			+ "]";
}
  
}
