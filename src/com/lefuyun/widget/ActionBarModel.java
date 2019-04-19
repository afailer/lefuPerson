package com.lefuyun.widget;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.interf.ActionBarControl;

public class ActionBarModel implements OnClickListener{
	
	private Activity mActivity;
	private ActionBarControl mActionBarControl;
	private View view;
	private TextView mTitleView, mTextRight;
	private TextView mBackView;
	private ImageView mRightImg;
	
	public ActionBarModel(Activity activity, LayoutInflater inflate , ViewGroup parent){
		mActivity = activity;
		view = inflate.inflate(R.layout.item_action_bar, parent, false);
		// 初始化控件
		mTitleView = (TextView) view.findViewById(R.id.title_action_bar);
		mBackView = (TextView) view.findViewById(R.id.back_action_bar);
		mRightImg = (ImageView) view.findViewById(R.id.image_action_bar);
		mTextRight = (TextView) view.findViewById(R.id.text_right_action_bar);
	}
	
	public View getView() {
		return view;
	}
	
	public void setTitle(String title) {
		mTitleView.setText(title);
	}
	
	public void setBackButton() {
		mBackView.setVisibility(View.VISIBLE);
		mBackView.setOnClickListener(this);
	}
	
	public void setRightImageView(int res) {
		if(res != 0) {
			mRightImg.setVisibility(View.VISIBLE);
			mRightImg.setImageResource(res);
			mRightImg.setOnClickListener(this);
			mTextRight.setVisibility(View.GONE);
		}else {
			mRightImg.setVisibility(View.GONE);
		}
	}
	
	public void setRightTextView(String txt) {
		if(!TextUtils.isEmpty(txt)) {
			mTextRight.setVisibility(View.VISIBLE);
			mTextRight.setText(txt);
			mTextRight.setOnClickListener(this);
		}else {
			mTextRight.setVisibility(View.GONE);
		}
	}
	public void setRightTextView(String txt,String color) {
		if(!TextUtils.isEmpty(txt)) {
			mTextRight.setVisibility(View.VISIBLE);
			mTextRight.setText(txt);
			mTextRight.setOnClickListener(this);
			mTextRight.setTextColor(Color.parseColor(color));
		}else {
			mTextRight.setVisibility(View.GONE);
		}
	}
	public void setActionBarControl(ActionBarControl abc) {
		if(abc == null) {
			throw new NullPointerException("the ActionBarControl is null");
		}
		mActionBarControl = abc;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_action_bar:
			mActivity.finish();
			break;
		case R.id.text_right_action_bar:
			if(mActionBarControl != null) {
				mActionBarControl.onRightViewClick(mTextRight);
			}
			break;
		case R.id.image_action_bar:
			if(mActionBarControl != null) {
				mActionBarControl.onRightViewClick(mRightImg);
			}
			break;
		default:
			break;
		}
		
	}
}
