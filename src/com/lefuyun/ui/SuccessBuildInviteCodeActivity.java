package com.lefuyun.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.WindowUtil;

public class SuccessBuildInviteCodeActivity extends BaseActivity {

	// 当前老人
	private OldPeople mCurrentOldPeople;
	
	private TextView mCodeView, mInviteBtn;
	
	private TextView mCopyView;

	private String number;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_success_build_invite_code;
	}

	@Override
	protected void initView() {
		setActionBarTitle("邀请码");
		mCodeView = (TextView) findViewById(R.id.code_success_build_invite_activity);
		mInviteBtn = (TextView) findViewById(R.id.btn_success_build_invite_activity);
		mInviteBtn.setOnClickListener(this);
		mCopyView = (TextView) findViewById(R.id.copy_success_build_invite_activity);
		mCopyView.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mCurrentOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
		number = StringUtils.randomBuildNumber(mCurrentOldPeople.getId());
		mCodeView.setText(number);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_success_build_invite_activity:
			// 前往邀请
 			String url = LefuApi.getAbsoluteApiUrl("lefuyun/socialPeopleCtr/toShareOldPeople");
 			url = url + "?oid=" + mCurrentOldPeople.getId() + "&type=2&invite=" + number;
 			String message = "输入邀请码" + number + ",关注您的家人" + mCurrentOldPeople.getElderly_name();
 			LefuApi.sharePage(this, " ", message, "", url, false);
			break;
		case R.id.copy_success_build_invite_activity:
			// 复制到剪切板中
			WindowUtil.copyToShearPlate(mCodeView.getText().toString());
			showToast("复制成功");
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
