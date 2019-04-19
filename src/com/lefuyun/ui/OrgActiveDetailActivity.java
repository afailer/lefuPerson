package com.lefuyun.ui;

import java.io.Serializable;

import android.view.View;

import com.lefuyun.R;
import com.lefuyun.R.layout;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OrgActive;

public class OrgActiveDetailActivity extends BaseActivity {

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_org_active_detail;
	}

	@Override
	protected void initView() {
		OrgActive orgActive = (OrgActive) getIntent().getSerializableExtra("active");
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	
}
