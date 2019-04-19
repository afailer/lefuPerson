package com.lefuyun.ui;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.base.BaseUserActivity;
import com.lefuyun.util.SPUtils;
import com.lefuyun.widget.togglebutton.ToggleButton;
import com.lefuyun.widget.togglebutton.ToggleButton.OnToggleChanged;

public class LoginActivity extends BaseUserActivity {
	
	private ToggleButton mToggleButton;
	
	private EditText mPhone, mPassword;
	private TextView mForgetPassword, mLogin, mOtherLogin;
	// 是否是从设置页面跳转
	private boolean isAnimator;
	// 是否可以返回,显示动画
	private boolean isBack;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}
	
	@Override
	protected void onBeforeSetContentLayout() {
		Intent intent = getIntent();
		isAnimator = intent.getBooleanExtra("isAnimator", false);
		isBack = intent.getBooleanExtra("isBack", false);
	}

	@Override
	protected void initView() {
		// 手机号输入控件
		mPhone = (EditText) findViewById(R.id.username_login_activity);
		// 添加内容修改监听事件
		mPhone.addTextChangedListener(this);
		mPassword = (EditText) findViewById(R.id.password_login_activity);
		// 忘记密码
		mForgetPassword = (TextView) findViewById(R.id.forget_password_login_activity);
		// 登录按钮
		mLogin = (TextView) findViewById(R.id.button_login_activity);
		// 短信验证码方式登录
		mOtherLogin = (TextView) findViewById(R.id.other_login_activity);
		mForgetPassword.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mOtherLogin.setOnClickListener(this);
		
		initToggleButton();
		
	}
	/**
	 * 初始化ToggleButton()
	 */
	private void initToggleButton() {
		// 控制密码的显示
		mToggleButton = (ToggleButton) findViewById(R.id.togglebutton_login_activity);
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
		String phone = (String) SPUtils.get(this, "phone", "");
		String password = (String) SPUtils.get(this, "password", "");
		mPhone.setText(phone);
		mPassword.setText(password);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.forget_password_login_activity:
			// 忘记密码
			// 其他登录方式登陆
			String phoneNum = mPhone.getText().toString().replace(" ", "");
			intent = new Intent(this, ResetPasswordActivity.class);
			if(phoneNum.length() == 11) {
				intent.putExtra("phone", phoneNum);
			}
			startActivityForResult(intent, 100);
			break;
		case R.id.button_login_activity:
			// 登陆
			loginByPassword();
			break;
		case R.id.other_login_activity:
			// 其他登录方式登陆
			String phone = mPhone.getText().toString().replace(" ", "");
			intent = new Intent(this, LoginSMSActivity.class);
			if(phone.length() == 11) {
				intent.putExtra("phone", phone);
			}
			startActivityForResult(intent, 100);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 登录操作
	 */
	private void loginByPassword() {
		final String phone = mPhone.getText().toString().replace(" ", "");
		final String password = mPassword.getText().toString().trim();
		// 登录
		login(phone, password, mPassword);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == AppContext.LOGIN_SUCCESS) {
			// 800为其开启的activity要求其自动销毁
			setResult(AppContext.LOGIN_SUCCESS); // 通知SwitchActivity自动关闭
			finish();
		}
	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return !isAnimator;
	}
	
	@Override
	protected String getActionBarRightText() {
		return "注册";
	}

	@Override
	public void onRightViewClick(View view) {
		// 跳转到注册页面
		Intent registerIntent = new Intent(this, RegisterActivity.class);
		String phone = mPhone.getText().toString().replace(" ", "");
		if(phone.length() == 11) {
			registerIntent.putExtra("phone", phone);
		}
		startActivity(registerIntent);
	}

	@Override
	protected EditText getPhoneEditText() {
		return mPhone;
	}
	
	@Override
	public void finish() {
		super.finish();
		// 设置当前activity关闭时的动画
		if(isBack) {
			overridePendingTransition(R.animator.slide_left_in, R.animator.slide_right_out);
		}
	}
}
