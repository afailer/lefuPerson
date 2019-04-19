package com.lefuyun.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseCheckPhoneActivity;
import com.lefuyun.bean.Organization;
import com.lefuyun.bean.Reserve;
import com.lefuyun.util.StringUtils;
import com.lefuyun.widget.dialog.ActionSheetDialog;
import com.lefuyun.widget.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.lefuyun.widget.dialog.ActionSheetDialog.SheetItemColor;

public class ReserveOrganization extends BaseCheckPhoneActivity {
	
	private Organization mOrganization;
	
	private TextView mTitleView, mReserveBtn;
	
	private TextView mHeathStateView;
	
	private EditText mContactPersonView, mPhoneView;
	private EditText mEmailView, mAgeView;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_organization_reserve;
	}

	@Override
	protected void initView() {
		setActionBarTitle("床位预约");
		mTitleView = (TextView) findViewById(R.id.title_organization_reserve_activity);
		mContactPersonView = (EditText) findViewById(R.id.contact_person_organization_reserve_activity);
		mPhoneView = (EditText) findViewById(R.id.phone_organization_reserve_activity);
		mPhoneView.addTextChangedListener(this);
		mEmailView = (EditText) findViewById(R.id.email_organization_reserve_activity);
		mAgeView = (EditText) findViewById(R.id.age_organization_reserve_activity);
		mReserveBtn = (TextView) findViewById(R.id.reserve_organization_reserve_activity);
		mReserveBtn.setOnClickListener(this);
		mHeathStateView = (TextView) findViewById(R.id.heath_state_organization_reserve_activity);
		mHeathStateView.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOrganization = (Organization) intent.getSerializableExtra("organization");
		
		mTitleView.setText(mOrganization.getAgency_name());
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reserve_organization_reserve_activity:
			// 预约点击按钮
			reserve();
			break;
		case R.id.heath_state_organization_reserve_activity:
			// 预约点击按钮
			new ActionSheetDialog(this)
			.builder()
			.setCancelable(true)
			.setCanceledOnTouchOutside(true)
			.addSheetItem("完全自理", SheetItemColor.Red,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							mHeathStateView.setText("完全自理");
						}
					})
					.addSheetItem("基本自理", SheetItemColor.Red,
							new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							mHeathStateView.setText("基本自理");
						}
					})
			.addSheetItem("完全不能自理", SheetItemColor.Red,
			new OnSheetItemClickListener() {
				@Override
				public void onClick(int which) {
					mHeathStateView.setText("完全不能自理");
				}
			}).show();
			break;

		default:
			break;
		}

	}
	/**
	 * 预约操作
	 */
	private void reserve() {
		String contactPerson = mContactPersonView.getText().toString().trim();
		String phone = mPhoneView.getText().toString().replace(" ", "");
		String email = mEmailView.getText().toString().trim();
		String age = mAgeView.getText().toString().trim();
		String heath = mHeathStateView.getText().toString().trim();
		if(StringUtils.isEmpty(contactPerson)) {
			showToast("联系人不能为空");
			return;
		}
		if(!checkPhoneLegal(phone)) {
			return;
		}
		if(!StringUtils.isEmail(email)) {
			showToast("邮箱格式错误");
			return;
		}
		if(StringUtils.isEmpty(age)) {
			showToast("年龄不能为空");
			return;
		}
		if(StringUtils.isEmpty(heath)) {
			showToast("请选择老人自理能力");
			return;
		}
		Reserve reserve = new Reserve();
		reserve.setAge(age);
		reserve.setAgencyId(mOrganization.getAgency_id());
		reserve.setContactPerson(contactPerson);
		reserve.setMailbox(email);
		reserve.setMobile(phone);
		reserve.setOrderbed(mOrganization.getAgency_id());
		reserve.setSelfCareAbility(heath);
		LefuApi.reserveOrganization(reserve, new RequestCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				showToast("预约成功");
				finish();
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast("预约失败");
			}
		});
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
	protected EditText getPhoneEditText() {
		return mPhoneView;
	}

}
