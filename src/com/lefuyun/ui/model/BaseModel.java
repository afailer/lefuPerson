package com.lefuyun.ui.model;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseModel<T> implements OnClickListener{
	
	/**
	 * 初始化view控件
	 * @return
	 */
	
	public abstract View getView();
	/**
	 * 初始化必要的数据
	 * @param t
	 */
	public abstract void initData(T t);
	
}
