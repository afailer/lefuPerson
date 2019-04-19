package com.lefuyun.bean;

import java.io.Serializable;

public class Portrait implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id; // 
	private String fileUrl; // 用户头像地址

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

}
