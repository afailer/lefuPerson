package com.lefuyun.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.widget.CircleImageView;

public class ObtainOutActivity extends BaseActivity {
	
	// 当前绑定老人的姓名,所属机构
	private TextView mElderName, mElderOrganization;
	// 老人年龄
	private TextView mElderAge;
	// 老人头像
	private CircleImageView mElderImg;
	// 老人信息
	private OldPeople mOldPeople;
	// 请假按钮和查看历史按钮
	private ImageView mObtainOutBtn, mHistoryBtn;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_obtain_out;
	}

	@Override
	protected void initView() {
		setActionBarTitle("外出请假");
		mElderImg = (CircleImageView) findViewById(R.id.img_elder_obtain_out_activity);
		mElderName = (TextView) findViewById(R.id.name_elder_obtain_out_activity);
		mElderOrganization = (TextView) findViewById(R.id.organization_elder_obtain_out_activity);
		mElderAge = (TextView) findViewById(R.id.age_elder_obtain_out_activity);
		mObtainOutBtn = (ImageView) findViewById(R.id.permission_elder_obtain_out_activity);
		mHistoryBtn = (ImageView) findViewById(R.id.history_elder_obtain_out_activity);
		mObtainOutBtn.setOnClickListener(this);
		mHistoryBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
		// 设置老人头像
		ImageLoader.loadImg(mOldPeople.getIcon(), mElderImg);
		// 设置老人姓名
		mElderName.setText(mOldPeople.getElderly_name());
		// 设置老人所属机构名称
		mElderOrganization.setText(mOldPeople.getAgency_name());
		// 设置老人年龄
		mElderAge.setText("年龄:  " + mOldPeople.getAge() + "岁");
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.permission_elder_obtain_out_activity:
			// 跳转到请假页面
			intent = new Intent(this, ObtainOutAddActivity.class);
			intent.putExtra("oldPeopleId", mOldPeople.getId());
			startActivity(intent);
			break;
		case R.id.history_elder_obtain_out_activity:
			// 跳转到请假历史页面
			intent = new Intent(this, ObtainOutHistoryActivity.class);
			intent.putExtra("oldPeople", mOldPeople);
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
