package com.lefuyun.Jpush;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.JpushNotificationDetailBean;
import com.lefuyun.ui.LoginActivity;
import com.lefuyun.util.DateUtils;
import com.lefuyun.util.DialogHelper;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.dialog.ConfirmDialogFragment;

public class ShowDialogActivity extends BaseActivity{

	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_showdialog;
	}

	@Override
	protected void initView() {
		JpushNotificationDetailBean detailBean=(JpushNotificationDetailBean) getIntent().getSerializableExtra("detailBean");
		LogUtil.i("isBackground",Utils.isBackground(this)+"");
		if(!Utils.isBackground(this)){
			ConfirmDialogFragment confirmdialog=DialogHelper.getConfirmDialog(ShowDialogActivity.this, "", "您的账号于"+DateUtils.dateFormatYMD_HMS(detailBean.getLoginTime()+"")+"在另一台设备上登录。如非本人操作，则密码可能泄露，建议修改密码。", 
					new com.lefuyun.widget.dialog.ConfirmDialogFragment.OnClickListener() {
				
				@Override
				public void onPositiveClick(View v) {
					//AppContext.clearUserInfo();
					 AppContext.getInstance().exit();
				}
				
				@Override
				public void onNegativeClick(View v) {
					
				}
			});
			//隐藏取消按钮
			confirmdialog.flag=false;
		}
	}

	@Override
	protected void initData() {
		
	}
        
}
