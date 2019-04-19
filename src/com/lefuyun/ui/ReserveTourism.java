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
import com.lefuyun.bean.Tourism;
import com.lefuyun.bean.TourismReserve;
import com.lefuyun.util.StringUtils;

public class ReserveTourism extends BaseCheckPhoneActivity {
	
	private Tourism mTourism;
	
	
	private TextView mReserveBtn;
	
	private EditText mContactNameView;
	
	private EditText mPhoneView, mNumView;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_tourism_reserve;
	}

	@Override
	protected void initView() {
		setActionBarTitle("预约报名");
		mContactNameView = (EditText) findViewById(R.id.contact_person_tourism_reserve_activity);
		mPhoneView = (EditText) findViewById(R.id.phone_tourism_reserve_activity);
		mPhoneView.addTextChangedListener(this);
		mNumView = (EditText) findViewById(R.id.num_tourism_reserve_activity);
		mReserveBtn = (TextView) findViewById(R.id.reserve_tourism_reserve_activity);
		mReserveBtn.setOnClickListener(this);
		
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mTourism = (Tourism) intent.getSerializableExtra("tourism");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reserve_tourism_reserve_activity:
			// 预约点击按钮
			reserve();
			break;
		case R.id.heath_state_organization_reserve_activity:
			
			break;

		default:
			break;
		}

	}
	/**
	 * 预约操作
	 */
	private void reserve() {
		String contactName = mContactNameView.getText().toString().trim();
		String phone = mPhoneView.getText().toString().replace(" ", "");
		if(StringUtils.isEmpty(contactName)) {
			showToast("联系人不能为空");
			return;
		}
		if(!checkPhoneLegal(phone)) {
			return;
		}
		String trim = mNumView.getText().toString().trim();
		if(StringUtils.isEmpty(trim)) {
			showToast("游客人数至少1人");
			return;
		}
		int num = Integer.parseInt(trim);
		if(num <= 0) {
			showToast("游客人数至少1人");
			return;
		}
		TourismReserve reserve = new TourismReserve();
		reserve.setProduct_id(mTourism.getId());
		reserve.setCompany_id(mTourism.getCompany_id());
		reserve.setName(contactName);
		reserve.setCellphone(phone);
		reserve.setPeople_amount(num);
		LefuApi.reserveTourisms(reserve, new RequestCallback<String>() {
			
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
