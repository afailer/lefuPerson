package com.lefuyun.ui;

import android.content.Intent;
import android.graphics.Paint;
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
import com.lefuyun.bean.User;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.togglebutton.ToggleButton;
import com.lefuyun.widget.togglebutton.ToggleButton.OnToggleChanged;

public class RegisterActivity extends BaseUserActivity{
	
	private ToggleButton mToggleButton;
	private TextView mRegisterBtn, mProtocolBtn;
	// 注册用户所需手机号、密码和短信验证码
	private EditText mPhone, mPassword, mVerifyCode;
	private TextView mSendBtn;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_register;
	}

	@Override
	protected void initView() {
		mPhone = (EditText) findViewById(R.id.phone_register_activity);
		mPhone.addTextChangedListener(this);
		mPassword = (EditText) findViewById(R.id.password_regidter_activity);
		mVerifyCode = (EditText) findViewById(R.id.verify_code_regidter_activity);
		mRegisterBtn = (TextView) findViewById(R.id.confirm_register_activity);
		mSendBtn = (TextView) findViewById(R.id.send_verify_code_register_activity);
		mSendBtn.setOnClickListener(this);
		mProtocolBtn = (TextView) findViewById(R.id.user_protocol_register_activity);
		mProtocolBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); // 下划线
		mProtocolBtn.getPaint().setAntiAlias(true);// 抗锯齿
		mRegisterBtn.setOnClickListener(this);
		mProtocolBtn.setOnClickListener(this);
		initToggleButton();
	}
	/**
	 * 初始化ToggleButton,控制密码的显示与否
	 */
	private void initToggleButton() {
		mToggleButton = (ToggleButton) findViewById(R.id.togglebutton_regidter_activity);
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
		String phone = intent.getStringExtra("phone");
		if(!StringUtils.isEmpty(phone)) {
			mPhone.setText(phone);
			mPhone.setSelection(mPhone.length());
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_register_activity:
			// 注册
			register();
			break;
		case R.id.user_protocol_register_activity:
			// 跳转到用户协议页面
			Intent userIntent = new Intent(this, UserProtocolActivity.class);
			startActivity(userIntent);
			break;
		case R.id.send_verify_code_register_activity:
			// 发送验证码
			countDown(mPhone, mSendBtn);
			break;
		default:
			break;
		}
		
	}
	/**
	 * 注册
	 */
	private void register() {
		
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
		LefuApi.register(phone, password, verifyCode,new RequestCallback<User>() {
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
			}

			@Override
			public void onSuccess(User result) {
				TLog.log("注册成功 == " + result);
				showToast("注册成功");
				login(phone, password, null);
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
	protected boolean isLogin() {
		return false;
	}

}
