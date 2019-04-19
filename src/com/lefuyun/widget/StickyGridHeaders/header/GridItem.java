package com.lefuyun.widget.StickyGridHeaders.header;

import java.io.Serializable;

import com.lefuyun.bean.NurseMedia;

public class GridItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;
	private String time;
	private int section;
	private NurseMedia nurseMedia;
	public GridItem(String path, String time,NurseMedia media) {
		super();
		this.path = path;
		this.time = time;
		this.nurseMedia=media;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public NurseMedia getNurseMedia() {
		return nurseMedia;
	}

	public void setNurseMedia(NurseMedia nurseMedia) {
		this.nurseMedia = nurseMedia;
	}

}
