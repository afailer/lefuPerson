package com.lefuyun.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseCheckPhoneActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;

public class ConcernElderlyDirectlyActivity extends BaseCheckPhoneActivity {
	
	private static final int BEFORE_EXPLORE = 1;
	private static final int SUCCESS_EXPLORE = 2;
	
	private int currentType = BEFORE_EXPLORE;
	// 身份证号输入
	private EditText mIdentityCard;
	private View mBar;
	// 搜索成功前后的页面
	private LinearLayout mBeforeView, mSuccessView;
	// 绑定老人按钮,搜索老人按钮
	private TextView mButton, mExploreBtn;
	// 搜索成功后获取的老人对象
	private OldPeople mOldPeople;
	
	// 搜索成功老人信息显示控件
	private TextView mElderName, mElderId;
	// 家属信息输入控件
	private EditText mFamilyName, mFamilyPhone;


	@Override
	protected int getLayoutId() {
		return R.layout.activity_concern_elder_directly;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("添加关注");
		mBar = findViewById(R.id.item_concern_elder_directly);
		mIdentityCard = (EditText) findViewById(R.id.id_concern_elder_directly);
		mBeforeView = (LinearLayout) findViewById(R.id.before_explore_concern_elder_directly);
		mSuccessView = (LinearLayout) findViewById(R.id.success_explore_concern_elder_directly);
		mSuccessView.setVisibility(View.GONE);
		mButton = (TextView) findViewById(R.id.btn_concern_elder_directly);
		mExploreBtn = (TextView) findViewById(R.id.explore_concern_elder_directly);
		mButton.setOnClickListener(this);
		mExploreBtn.setOnClickListener(this);
		// 老人搜索成功后老人信息显示控件
		mElderName = (TextView) findViewById(R.id.name_elder_concern_elder_directly);
		mElderId = (TextView) findViewById(R.id.id_success_concern_elder_directly);
		
		mFamilyName = (EditText) findViewById(R.id.name_concern_elder_directly);
		mFamilyPhone = (EditText) findViewById(R.id.phone_concern_elder_directly);
		mFamilyPhone.addTextChangedListener(this);
	}
	
	@Override
	protected void initData() {
		
	}
	/**
	 * 搜索老人成功之后, 设置布局
	 */
	private void successExplore() {
		currentType = SUCCESS_EXPLORE;
		mBar.setBackgroundColor(Color.parseColor("#F4F4F4"));
		mBeforeView.setVisibility(View.GONE);
		mSuccessView.setVisibility(View.VISIBLE);
		mElderName.setText(mOldPeople.getElderly_name());
		mElderId.setText(mOldPeople.getDocument_number());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.explore_concern_elder_directly:
			// 搜索老人
			searchOldPeople();
			break;
		case R.id.btn_concern_elder_directly:
			// 绑定老人
			if(currentType == BEFORE_EXPLORE) {
				showToast("请先搜索老人");
			}else if(currentType == SUCCESS_EXPLORE) {
				concernOldPeople();
			}
			
			break;

		default:
			break;
		}
	}
	/**
	 * 绑定老人
	 */
	private void concernOldPeople() {
		TLog.log("");
		String identityCard = mOldPeople.getDocument_number();
		String phone = mFamilyPhone.getText().toString().trim();
		phone = phone.replace(" ", "");
		String name = mFamilyName.getText().toString().trim();
		if(!checkPhoneLegal(phone)) {
			return;
		}
		LefuApi.bindingElder(identityCard, phone, name, new RequestCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				TLog.log("result == " + result);
				Intent intent = new Intent(ConcernElderlyDirectlyActivity.this, 
						ConcernElderlySuccessActivity.class);
				startActivityForResult(intent, 100);
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				TLog.log("异常result == " + e.toString());
				showToast(e.getMessage());
			}
		});
	}

	/**
	 * 通过身份证号查找老人
	 */
	private void searchOldPeople() {
		String id = mIdentityCard.getText().toString().trim();
		if(StringUtils.isEmpty(id)) {
			showToast("请输入老人身份证号");
			return;
		}
		if(id.length() != 18) {
			showToast("身份证号格式错误");
			return;
		}
		showWaitDialog();
		LefuApi.getOldPeople(id, new RequestCallback<OldPeople>() {
			
			@Override
			public void onSuccess(OldPeople result) {
				mOldPeople = result;
				successExplore();
				hideWaitDialog();
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
				hideWaitDialog();
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TLog.log("直接添加返回的返回码是  == " + resultCode);
		if(resultCode == AppContext.BINDING_SUCCESS_SKIP) {
			Intent intent = new Intent();
			intent.putExtra("currentOldPeopleID", mOldPeople.getId());
			setResult(AppContext.BINDING_SUCCESS_SKIP, intent);
		}else {
			setResult(AppContext.BINDING_SUCCESS);
		}
		finish();
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
		return mFamilyPhone;
	}
}
