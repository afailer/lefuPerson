package com.lefuyun.bean;

import java.io.Serializable;

public class MyEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type;
	private int position;
	
	public MyEvent(int type, int position) {
		super();
		this.type = type;
		this.position = position;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "MyEvent [type=" + type + ", position=" + position + "]";
	}
	
	
}
