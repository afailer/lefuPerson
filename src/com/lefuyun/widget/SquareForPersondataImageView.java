package com.lefuyun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


public class SquareForPersondataImageView extends ImageView {

	public SquareForPersondataImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SquareForPersondataImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareForPersondataImageView(Context context) {
		super(context);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int width = getMeasuredWidth();
		LayoutParams params = getLayoutParams();
		params.height = (int) (width/0.88);
		setLayoutParams(params);
	}

	
}
