package com.lefuyun.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
/**
 * 随便看看页面和登录页面的切换activity
 */
public class SwitchActivity extends BaseActivity {
	
	private LinearLayout mVisitor, mRightLogin;
	private boolean isExit;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_switch;
	}

	@Override
	protected void initView() {
		mVisitor = (LinearLayout) findViewById(R.id.visitor_switch_activity);
		mRightLogin = (LinearLayout) findViewById(R.id.right_login_switch_activity);
		mVisitor.setOnClickListener(this);
		mRightLogin.setOnClickListener(this);
	}

	@Override
	protected void initData() {
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.visitor_switch_activity:
			// 随便看看
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.right_login_switch_activity:
			// 跳转到登录页面
			intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 100);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == AppContext.LOGIN_SUCCESS) {
			// 登录页面登录成功
			finish();
		}
	}
	
	/** 
     * 菜单、返回键响应 
     */  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_BACK) {    
               exitBy2Click();      //调用双击退出函数  
        }  
        return false;  
    }
    /**
     * 双击退出
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出 
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务 
      
        } else {
            finish();
            System.exit(0);
        }
    }

}
