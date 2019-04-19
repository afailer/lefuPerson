package com.lefuyun.interf;

import android.view.View;

/**
 * ActionBar接口类
 * @author wenhui
 *
 */
public interface ActionBarControl {
	
	/**
	 *  右边内容的点击事件, ActionBar右侧内容的点击事件在此处理,
	 * @param type 当前触发点击事件的类型,TextView或者ImageView
	 * @param view 返回当前被点击的控件
	 * setOnClick
	 */
	public abstract void onRightViewClick(View view);

}
