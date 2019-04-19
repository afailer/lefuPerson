package com.lefuyun.ui;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseUserActivity;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.togglebutton.ToggleButton;
import com.lefuyun.widget.togglebutton.ToggleButton.OnToggleChanged;

public class ResetPasswordActivity extends BaseUserActivity{
	
	private ToggleButton mToggleButton;
	private TextView mLoginBtn;
	// 注册用户所需手机号、密码和短信验证码
	private EditText mPhone, mPassword, mVerifyCode;
	private TextView mSendBtn;
	private boolean isAnimator;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_reset_password;
	}

	@Override
	protected void initView() {
		mPhone = (EditText) findViewById(R.id.phone_reset_password_activity);
		mPhone.addTextChangedListener(this);
		mPassword = (EditText) findViewById(R.id.password_reset_password_activity);
		mVerifyCode = (EditText) findViewById(R.id.verify_code_reset_password_activity);
		mLoginBtn = (TextView) findViewById(R.id.login_reset_password_activity);
		mSendBtn = (TextView) findViewById(R.id.send_verify_code_reset_password_activity);
		mSendBtn.setOnClickListener(this);
		mLoginBtn.setOnClickListener(this);
		initToggleButton();
	}
	/**
	 * 初始化ToggleButton,控制密码的显示与否
	 */
	private void initToggleButton() {
		mToggleButton = (ToggleButton) findViewById(R.id.togglebutton_reset_password_activity);
		mToggleButton.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(boolean on) {
				if(on) {
					// 密码可见
					mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					mPassword.setSelection(mPassword.length());
				}else {
					mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
					mPassword.setSelection(mPassword.length());
				}
			}
		});
		
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		isAnimator = intent.getBooleanExtra("isAnimator", false);
		if(isAnimator) {
			setActionBarTitle("重置密码");
			mLoginBtn.setText("重置密码");
		}else {
			setActionBarTitle("忘记密码");
			mLoginBtn.setText("登录");
			String phone = intent.getStringExtra("phone");
			if(!StringUtils.isEmpty(phone)) {
				mPhone.setText(phone);
				mPhone.setSelection(mPhone.length());
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_reset_password_activity:
			// 跳转到注册页面的第二个页面
			resetPassword();
			break;
		case R.id.send_verify_code_reset_password_activity:
			// 发送验证码
			countDown(mPhone, mSendBtn);
			break;
		default:
			break;
		}
		
	}
	/**
	 * 重置密码
	 */
	private void resetPassword() {
		
		if(!isSendSuccess) {
			showToast("请获取验证码");
			return;
		}
		final String phone = mPhone.getText().toString().replace(" ", "");
		final String password = mPassword.getText().toString().trim();
		String verifyCode = mVerifyCode.getText().toString().trim();
		if(!checkLegal(phone, password, verifyCode)) {
			return;
		}
		LefuApi.resetPassword(phone, password, verifyCode,new RequestCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				TLog.log("密码重置成功 == " + result);
				showToast("密码重置成功,请牢记你的密码");
				login(phone, password, null);
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
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
		return mPhone;
	}
	
	@Override
	public void finish() {
		super.finish();
		if(isAnimator) {
			// 设置当前activity关闭时的动画
			overridePendingTransition(R.animator.slide_left_in, R.animator.slide_right_out);
		}
	}

}
