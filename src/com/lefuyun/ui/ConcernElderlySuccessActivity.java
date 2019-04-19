package com.lefuyun.ui;

import android.view.View;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;

public class ConcernElderlySuccessActivity extends BaseActivity {
	
	private TextView mButton;
	// 是否要跳转
	private boolean isSkip;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_concern_elder_success;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("添加关注");
		mButton = (TextView) findViewById(R.id.btn_concern_elder_activity_success);
		mButton.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_concern_elder_activity_success) {
			setResult(AppContext.BINDING_SUCCESS_SKIP);
			isSkip = true;
			finish();
		}
	}


	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	public void finish() {
		if(!isSkip) {
			setResult(AppContext.BINDING_SUCCESS);
		}
		super.finish();
	}

}
