package com.lefuyun.bean;

import android.os.Bundle;


public final class ViewPageInfo {
	
    public final Class<?> clss; // 类字节码
    public final String title; // 页面名称
    public final Bundle args; // 传递信息的值

    public ViewPageInfo(String _title, Class<?> _class, Bundle _args) {
    	title = _title;
        clss = _class;
        args = _args;
    }

}
