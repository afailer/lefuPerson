package com.lefuyun.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.util.StringUtils;

public class ConcernElderlyInviteActivity extends BaseActivity {
	
	private TextView mButton;
	
	private EditText mCodeView;
	
	private long mUserId;
	
	private long elderId;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_concern_elder_invite;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("添加关注");
		mCodeView = (EditText) findViewById(R.id.code_concern_elder_activity_invite);
		mButton = (TextView) findViewById(R.id.btn_concern_elder_activity_invite);
		mButton.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mUserId = intent.getLongExtra("userId", 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_concern_elder_activity_invite:
			binding();
			break;

		default:
			break;
		}
	}

	/**
	 * 根据邀请码绑定老人
	 */
	private void binding() {
		final String code = mCodeView.getText().toString().trim();
		if(StringUtils.isEmpty(code)) {
			showToast("邀请码不能为空");
			return;
		}
		if(code.length() < 6) {
			showToast("邀请码格式不正确");
			return;
		}
		
		LefuApi.bindingElder(mUserId, code, new RequestCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				elderId = Long.parseLong(code.substring(0, code.length() - 5));
				Intent intent = new Intent(ConcernElderlyInviteActivity.this, 
						ConcernElderlySuccessActivity.class);
				startActivityForResult(intent, 100);
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == AppContext.BINDING_SUCCESS_SKIP) {
			Intent intent = new Intent();
			intent.putExtra("currentOldPeopleID", elderId);
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
}
