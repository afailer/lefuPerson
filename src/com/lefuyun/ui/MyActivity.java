package com.lefuyun.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.widget.CircleImageView;

public class MyActivity extends BaseActivity {
	// 当前绑定老人的姓名,所属机构
	private TextView mElderName, mElderOrganization;
	// 老人年龄
	private TextView mElderAge;
	// 老人头像
	private CircleImageView mElderImg;
	
	// 生成邀请码以及生成二维码按钮
	private TextView mBuildInviteBtn, mBuildZxingBtn;
	
	// 当前页面用户选择的老人
	private OldPeople mCurrentOldPeople;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_my;
	}

	@Override
	protected void initView() {
		mElderName = (TextView) findViewById(R.id.name_elder_my_activity);
		mElderOrganization = (TextView) findViewById(R.id.organization_elder_my_activity);
		mElderAge = (TextView) findViewById(R.id.age_elder_my_activity);
		mElderImg = (CircleImageView) findViewById(R.id.img_elder_my_activity);
		
		mBuildInviteBtn = (TextView) findViewById(R.id.build_invite_code_my_activity);
		mBuildZxingBtn = (TextView) findViewById(R.id.build_zxing_code_my_activity);
		mBuildInviteBtn.setOnClickListener(this);
		mBuildZxingBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		setActionBarTitle("分享");
		Intent intent = getIntent();
		mCurrentOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
		// 设置老人头像
		ImageLoader.loadImg(mCurrentOldPeople.getIcon(), mElderImg);
		// 设置老人姓名
		mElderName.setText(mCurrentOldPeople.getElderly_name());
		// 设置老人所属机构名称
		mElderOrganization.setText(mCurrentOldPeople.getAgency_name());
		// 设置老人年龄
		mElderAge.setText("年龄:  " + mCurrentOldPeople.getAge() + "岁");
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.build_invite_code_my_activity:
			// 跳转到生成邀请码页面
			intent = new Intent(this, SuccessBuildInviteCodeActivity.class);
			intent.putExtra("oldPeople", mCurrentOldPeople);
			startActivity(intent);
			break;
		case R.id.build_zxing_code_my_activity:
			// 跳转到生成二维码页面
			intent = new Intent(this, SuccessBuildZxingCodeActivity.class);
			intent.putExtra("oldPeople", mCurrentOldPeople);
			startActivity(intent);
			break;

		default:
			break;
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

}
