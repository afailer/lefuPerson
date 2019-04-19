package com.lefuyun.bean;

import android.graphics.drawable.Drawable;

/**
 * @author chenshichao
 * @date   2016-5-10
 * @description 显示的图片和文字
 */
public class ShowViewBean {
	private Drawable drawable;
	private String name;
    private int signType;
	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSignType() {
		return signType;
	}

	public void setSignType(int signType) {
		this.signType = signType;
	}

}
