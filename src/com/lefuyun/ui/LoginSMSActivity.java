package com.lefuyun.ui;

import java.util.Set;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseUserActivity;
import com.lefuyun.bean.User;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.util.Utils;

public class LoginSMSActivity extends BaseUserActivity {
	
	private EditText mPhone, mVerifyCode;
	private TextView mSendBtn, mLogin, mBack;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_login_sms;
	}

	@Override
	protected void initView() {
		mPhone = (EditText) findViewById(R.id.phone_login_sms_activity);
		mPhone.addTextChangedListener(this);
		mVerifyCode = (EditText) findViewById(R.id.verify_code_login_sms_activity);
		mSendBtn = (TextView) findViewById(R.id.send_verify_code_login_sms_activity);
		mLogin = (TextView) findViewById(R.id.button_login_sms_activity);
		mSendBtn.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mBack = (TextView) findViewById(R.id.other_login_sms_activity);
		mBack.setOnClickListener(this);
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
		case R.id.send_verify_code_login_sms_activity:
			// 发送验证码
			countDown(mPhone, mSendBtn);
			break;
		case R.id.button_login_sms_activity:
			// 登陆
			login();
			break;
		case R.id.other_login_sms_activity:
			// 返回短信登录
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected boolean hasStatusBar() {
		return true;
	}
	
	/**
	 * 登录操作
	 */
	private void login() {
		final String phone = mPhone.getText().toString().replace(" ", "");
		final String code = mVerifyCode.getText().toString().trim();
		if(!checkLegal(phone, null, code)) {
			return;
		}
		Utils.showWaitDialog(LoginSMSActivity.this, "登录中");
		LefuApi.loginByCode(phone, code, new RequestCallback<User>() {
			
			@Override
			public void onFailure(ApiHttpException e) {
				Utils.hideWaitDialog();
				mVerifyCode.setText("");
				showToast(e.getMessage());
				TLog.log(e.toString());
			}

			@Override
			public void onSuccess(User result) {
				Utils.hideWaitDialog();
				// 登录成功
				// 保存用户个人信息
				AppContext.saveUserInfo(result.getMobile(), result.getPassword(),result.getUser_id(),result);
				AppContext.recordLoginTime();
				// 调转到主页面
				Intent intent = new Intent(LoginSMSActivity.this, MainActivity.class);
				intent.putExtra("user", result);
				setResult(AppContext.LOGIN_SUCCESS); // 通知SwitchActivity自动关闭
				startActivity(intent);
				finish();
				//设置别名，不同的用户有不同的别名
				JPushInterface.setAlias(getApplicationContext(),result.getUser_id()+"", new TagAliasCallback() {
					
					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
						if(arg0==0){//为成功
							 TLog.log("设置别名成功");
						 }else{
							 TLog.log("设置别名失败");
						 }
					}
				});	
			}
		});
	}
	
	@Override
	protected EditText getPhoneEditText() {
		return mPhone;
	}

}
