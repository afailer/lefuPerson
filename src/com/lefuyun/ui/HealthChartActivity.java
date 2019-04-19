package com.lefuyun.ui;

import android.view.View;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;

public class HealthChartActivity extends BaseActivity {

	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_health_chart;
	}

	@Override
	protected void initView() {
		setActionBarTitle("重要通知");
	}

	@Override
	protected void initData() {
		
	}
	@Override
	protected boolean hasActionBar() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected boolean hasBackButton() {
		// TODO Auto-generated method stub
		return true;
	}
}
