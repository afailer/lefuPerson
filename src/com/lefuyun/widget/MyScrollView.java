package com.lefuyun.widget;

import com.lefuyun.interf.ScrollViewListener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	
	private ScrollViewListener mScrollViewListener;

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyScrollView(Context context) {
		super(context);
	}
	
	public void setScrollViewListener(ScrollViewListener listener) {
		mScrollViewListener = listener;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(mScrollViewListener != null) {
			mScrollViewListener.onScrollChanged(l, t, oldl, oldt);
		}
	}
	
}
