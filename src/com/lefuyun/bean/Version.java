package com.lefuyun.bean;

import java.io.Serializable;

public class Version implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String version; // 最新版本号
	private String desc; // 更新版本描述
	private String appUrl; // 最新应用版本地址
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	@Override
	public String toString() {
		return "Version [version=" + version + ", desc=" + desc + ", appUrl="
				+ appUrl + "]";
	}
}
