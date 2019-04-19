package com.lefuyun.base;

import java.util.Set;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.lefuyun.AppContext;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.bean.User;
import com.lefuyun.ui.MainActivity;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.util.Utils;

public abstract class BaseUserActivity extends BaseCheckPhoneActivity {
	/**
	 * 记录验证码是否获取成功
	 */
	protected boolean isSendSuccess;
	
	/**
	 * 是否是登录页面
	 * @return
	 */
	protected boolean isLogin(){
		return true;
	};
	
	/**
	 * 获取倒计时时间控制对象
	 * 
	 * @param view
	 * @return
	 */
	private CountDownTimer verifyCodeCountdown(final TextView view) {
		CountDownTimer timer = new CountDownTimer(160000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				view.setClickable(false);
				view.setFocusable(false);
				view.setText(millisUntilFinished / 1000 + "s");
			}

			@Override
			public void onFinish() {
				view.setText("重新发送");
				view.setClickable(true);
				view.setFocusable(true);
			}
		}.start();
		return timer;
	}

	protected boolean checkLegal(String phone, String password,
			String verifyCode) {
		if (!checkPhoneLegal(phone)) {
			return false;
		}
		if (password != null) {
			if (StringUtils.isEmpty(password)) {
				showToast("密码不能为空");
				return false;
			}
		}
		if (verifyCode != null) {
			if (StringUtils.isEmpty(verifyCode)) {
				showToast("验证码不能为空");
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取验证码
	 * @param mPhone
	 *            要接收验证码的手机号
	 * @param view 显示倒计时控件
	 * 				
	 */
	protected void countDown(EditText mPhone, final TextView view) {
		final String phone = mPhone.getText().toString().replace(" ", "");
		if (!checkPhoneLegal(phone)) {
			return;
		}
		// 判断此手机号是否已经注册
		LefuApi.isRegister(phone, new RequestCallback<User>() {
			
			@Override
			public void onSuccess(User result) {
				// 返回为1未注册
				if(isLogin()) {
					// 登录页面
					showToast("该号码未注册,请先注册");
				}else {
					// 注册页面
					getMobileCode(phone, view);
				}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				// 返回为2已经注册
				if(isLogin()) {
					// 登录页面
					getMobileCode(phone, view);
				}else {
					// 注册页面
					showToast("该号码已注册,请登录");
				}
			}
		});
	}
	/**
	 * 获取验证码
	 * @param phone 要回去验证码的手机号
	 * @param view 显示倒计时的控件
	 */
	private void getMobileCode(String phone, final TextView view) {
		final CountDownTimer timer = verifyCodeCountdown(view);
		isSendSuccess = false;
		LefuApi.getMobileCode(phone, new RequestCallback<String>() {

			@Override
			public void onSuccess(String result) {
				TLog.log("获取验证码result == " + result);
				showToast("验证码发送成功,注意查收");
				isSendSuccess = true;
			}

			@Override
			public void onFailure(ApiHttpException e) {
				TLog.log("验证码获取失败result == " + e.toString());
				showToast("手机号错误,或者您发送验证码过于频繁");
				view.setText("重新发送");
				view.setClickable(true);
				view.setFocusable(true);
				timer.cancel();
			}
		});
	}
	/**
	 * 密码登录接口
	 * @param phone
	 * @param password
	 * @param mPasswordView
	 */
	protected void login(String phone, String password, final EditText mPasswordView) {
		if(!checkLegal(phone, password, null)) {
			return;
		}
		Utils.showWaitDialog(BaseUserActivity.this, "登录中");
		LefuApi.login(phone, password, new RequestCallback<User>() {
			
			@Override
			public void onFailure(ApiHttpException e) {
				Utils.hideWaitDialog();
				if(mPasswordView != null) {
					mPasswordView.setText("");
					showToast(e.getMessage());
				}else {
					showToast(e.getMessage());
				}
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
				Intent intent = new Intent(BaseUserActivity.this, MainActivity.class);
				intent.putExtra("user", result);
				setResult(AppContext.LOGIN_SUCCESS); // 通知SwitchActivity自动关闭
				startActivity(intent);
				finish();
				//设置别名，不同的用户有不同的别名
				JPushInterface.setAlias(getApplicationContext(),result.getUser_id()+"", new TagAliasCallback() {
					
					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
						if(arg0==0){//为成功
							TLog.log("别名设置成功");
						}else{
							TLog.log("别名设置失败");
						}
					}
				});				
			}
		});
	}
}
