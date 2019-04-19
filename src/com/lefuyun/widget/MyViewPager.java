package com.lefuyun.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private float mDownX;
	private float mDownY;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}
	
	@Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getX();
			mDownY = ev.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
		case MotionEvent.ACTION_CANCEL:
			if(Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
        return super.dispatchTouchEvent(ev);  
    }
}
