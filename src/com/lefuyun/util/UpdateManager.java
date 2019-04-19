package com.lefuyun.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.Version;
import com.lefuyun.interf.ICallbackResult;
import com.lefuyun.service.DownloadService;
import com.lefuyun.service.DownloadService.DownloadBinder;
import com.lefuyun.widget.WaitDialog;
import com.lefuyun.widget.dialog.ConfirmDialogFragment.OnClickListener;

/**
 * 更新管理类
 * 
 */

public class UpdateManager {

	private Version mVersion;

	private BaseActivity mActivity;
	// 是否显示等待dialog
	private boolean isShow = false;
	
	private MyServiceConnection conn;
	
	private WaitDialog _waitDialog;
	
	private DownloadBinder mIBinder;
	
	private final ICallbackResult callback = new ICallbackResult() {

        @Override
        public void OnBackResult(Object s) {}
    };

	public UpdateManager(BaseActivity activity, boolean isShow) {
		this.mActivity = activity;
		this.isShow = isShow;
	}
	
	public boolean haveNewVersion() {
		if (mVersion == null) {
			return false;
		}
		boolean haveNew = false;
		String curVersionName = WindowUtil.getVersionName(mActivity);
		if (!curVersionName.equals(mVersion.getVersion())) {
			haveNew = true;
		}
		return haveNew;
	}

	public void checkUpdate() {
		if(mIBinder == null || !mIBinder.isCanceled()) {
			if (isShow) {
				showCheckDialog();
			}
			LefuApi.updateApp(new RequestCallback<Version>() {
				
				@Override
				public void onSuccess(Version result) {
					hideCheckDialog();
					mVersion = result;
					onFinshCheck();
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					hideCheckDialog();
					if (isShow) {
						showFaileDialog();
					}
					
				}
			});
		}else {
			mActivity.showToast("正在后台下载请稍后！");
		}
	}
	
	private void onFinshCheck() {
		if (haveNewVersion()) {
			showUpdateInfo();
		} else {
			if (isShow) {
				showLatestDialog();
			}
		}
	}

	private void showCheckDialog() {
		if (_waitDialog == null) {
			_waitDialog = DialogHelper.getWaitDialog((Activity) mActivity, "正在获取新版本信息...");
		}
		_waitDialog.show();
	}

	private void hideCheckDialog() {
		if (_waitDialog != null) {
			_waitDialog.dismiss();
		}
	}
	
	private void showUpdateInfo() {
		if (mVersion == null) {
			return;
		}
		
		DialogHelper
				.getConfirmDialog(mActivity, "发现新版本", mVersion.getDesc(), 
						new OnClickListener() {
					
					@Override
					public void onPositiveClick(View v) {
						// 开启下载服务
						openDownLoadService();
					}
					
					@Override
					public void onNegativeClick(View v) {
						
					}
				});
	}
	
	/**
	 * 开启下载服务
	 */
	private void openDownLoadService() {
		conn = new MyServiceConnection();
		Intent intent = new Intent(mActivity, DownloadService.class);
		intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, mVersion.getAppUrl());
		intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, mVersion.getVersion());
		mActivity.startService(intent);
		mActivity.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}
	
    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceDisconnected(ComponentName name) {}

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadBinder binder = (DownloadBinder) service;
            mIBinder = binder;
            binder.addCallback(callback);
            binder.start();
        }
    };

	private void showLatestDialog() {
		mActivity.showToast("已经是最新版本");
	}
	
	private void showFaileDialog() {
		mActivity.showToast("网络异常，无法获取新版本信息");
	}
}
