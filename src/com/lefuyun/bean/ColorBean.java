package com.lefuyun.bean;

public class ColorBean {
	private int type;//体征数据类型
	private String signdataName;//体征数据名称
    private double low;
    private double high;
    private int showLevel;
    private String fontColor;
    
    public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSigndataName() {
    	return signdataName;
    }
    public void setSigndataName(String signdataName) {
    	this.signdataName = signdataName;
    }
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public int getShowLevel() {
		return showLevel;
	}
	public void setShowLevel(int showLevel) {
		this.showLevel = showLevel;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
    
}
