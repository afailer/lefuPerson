package com.lefuyun.bean;

public class MediaBean {
	public MediaBean(int mediaType, String mediaPath) {
		super();
		this.mediaType = mediaType;
		this.mediaPath = mediaPath;
	}
	private int mediaType;
	private String mediaPath;
	public int getMediaType() {
		return mediaType;
	}
	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	public String getMediaPath() {
		return mediaPath;
	}
	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}
	
}	
