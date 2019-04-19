package com.lefuyun.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BNaviSettingManager.DayNightMode;
import com.baidu.navisdk.adapter.BNaviSettingManager.PowerSaveMode;
import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.util.TLog;

public class SatnavActivity extends BaseActivity {
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_around_citys;
	}

	@Override
	protected void initView() {
		initSetting();
		View view = BNRouteGuideManager.getInstance().onCreate(this, new OnNavigationListener() {
			
			@Override
			public void onNaviGuideEnd() {
				TLog.log("onNaviGuideEnd");
				finish();
			}
			
			@Override
			public void notifyOtherAction(int arg0, int arg1, int arg2, Object arg3) {
				TLog.log("notifyOtherAction");
				
			}
		});
		if (view != null) {
			setContentView(view);
		}

//		Intent intent = getIntent();
//		if (intent != null) {
//			Bundle bundle = intent.getExtras();
//			if (bundle != null) {
//				mBNRoutePlanNode = (BNRoutePlanNode) bundle
//						.getSerializable(AroundCitiesActivity.ROUTE_PLAN_NODE);
//
//			}
//		}

	}
	
	private void initSetting() {
		BNaviSettingManager.setDayNightMode(DayNightMode.DAY_NIGHT_MODE_AUTO);
		BNaviSettingManager.setPowerSaveMode(PowerSaveMode.AUTO_MODE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		BNRouteGuideManager.getInstance().onResume();
	}

	protected void onPause() {
		super.onPause();
		BNRouteGuideManager.getInstance().onPause();
	};
	

	@Override
	protected void onStop() {
		super.onStop();
		BNRouteGuideManager.getInstance().onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BNRouteGuideManager.getInstance().onDestroy();
	}


	@SuppressLint("HandlerLeak")
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		BNRouteGuideManager.getInstance().onBackPressed(true);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
	};

	@Override
	protected void initData() {}
	
	@Override
	public void onClick(View v) {}

}
