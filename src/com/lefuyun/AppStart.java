package com.lefuyun;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.view.View;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.lefuyun.Jpush.ShowDialogActivity;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.JpushNotificationDetailBean;
import com.lefuyun.bean.News;
import com.lefuyun.bean.User;
import com.lefuyun.ui.ImportantNotifyForNotifyListActivity;
import com.lefuyun.ui.LoginActivity;
import com.lefuyun.ui.MainActivity;
import com.lefuyun.ui.NewsDetailsActivity;
import com.lefuyun.ui.SwitchActivity;
import com.lefuyun.ui.WelcomeActivity;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.util.Utils;
import com.lefuyun.util.WindowUtil;

public class AppStart extends BaseActivity {
	
	private String isFirst;
	// 欢迎界面保留时间
	private int mPersistTime = 3;
	
	/*** 跳转过来的*/
	String jumptype;
	/*** 传过来的通知实体*/
	JpushNotificationDetailBean detailBean;
	/*** 传过来的新闻实体*/
	News news;
	@Override
	protected int getLayoutId() {
		Utils.systemBarColor(this, 0);
		return R.layout.activity_start;
	}

	@Override
	protected void initView() {
		setViewFillWindow(true);
		//String versionName = WindowUtil.getVersionName(this);
		isFirst = (String) SPUtils.get(this, "isFirst", "");
		LogUtil.d("isFirst", isFirst);
		jumptype=getIntent().getStringExtra("jumptype");
		LogUtil.i("jumptypeaaa", jumptype);
		detailBean=(JpushNotificationDetailBean) getIntent().getSerializableExtra("detailBean");
        news=(News) getIntent().getSerializableExtra("new");
        if("loginout".equals(jumptype)){
			if(detailBean!=null){
				Intent intent2=new Intent(AppStart.this,ShowDialogActivity.class);
				LogUtil.i("detailBean不为null++++", detailBean.toString());
				intent2.putExtra("detailBean", detailBean);
				intent2.putExtra("jumptype", jumptype);
				startActivity(intent2);
			}
			return;
		}
        if(isFirst.equals("")) {
			Intent intent = new Intent(this, WelcomeActivity.class);
			startActivity(intent);
			finish();
		}else {
			if(!"".equals(SPUtils.get(this, "phone", ""))) {
				TLog.log("用户自动登录");
				// 已登录用户进行自动登录
				LefuApi.autoLogin(this, new RequestCallback<User>() {
					
					@Override
					public void onSuccess(final User result) {
						//设置别名，不同的用户有不同的别名,用于推送
						JPushInterface.setAlias(getApplicationContext(),result.getUser_id()+"", new TagAliasCallback() {
							
							@Override
							public void gotResult(int arg0, String arg1, Set<String> arg2) {
								if(arg0==0){//为成功
									 LogUtil.i("tag", "设置别名成功");
								 }else{
									 LogUtil.i("tag", "设置别名失败");
								 }
							}
						});	
						//----------------------------------------------------
						AppContext.recordLoginTime();
						SPUtils.put(getApplicationContext(), "uid", result.getUser_id());
						
						Intent intent = new Intent(AppStart.this, MainActivity.class);
						intent.putExtra("user", result);
			    		if("notice".equals(jumptype)){
			    			LogUtil.i("detailBean不为null+++++++", detailBean+"可能为空");
			    			if(detailBean!=null){
			    				LogUtil.i("detailBean不为null++++", detailBean.toString());
			    				intent.putExtra("detailBean", detailBean);
			    				intent.putExtra("jumptype", jumptype);
			    			}
			    		}
			    		if("news".equals(jumptype)){
			    			  LogUtil.i("news不为null++++++", news+"可能为空");
			   			  if(news!=null){
			   				  LogUtil.i("news不为null+++++", news.toString());
			   				  intent.putExtra("news", news);
			   				  intent.putExtra("jumptype", jumptype);
			   			   }
			   		    }
			    		startActivity(intent); //执行  
				    	finish();
					}
					
					@Override
					public void onFailure(ApiHttpException e) {
						// 登录失败,调转到登录页面,重新登录
						showToast(e.getMessage());
						Intent intent = new Intent(AppStart.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}else {
				// 没有登录过,用户可以进行登录,也可以以游客的身份进行查看
				final Intent intent = new Intent(this, SwitchActivity.class);
				Timer timer = new Timer(); 
				TimerTask task = new TimerTask() {  

				    @Override  
				    public void run() {
				    	startActivity(intent); //执行  
				    	finish();
				    }
				};
				timer.schedule(task, 1000 * mPersistTime);
			}
		}
		//设置标签
		String imei=Utils.getDeviceIMEI(AppContext.getInstance());
		LogUtil.i("imei", imei);
		Set<String> tags=new HashSet<String>();
		tags.add(imei);
		JPushInterface.setTags(getApplicationContext(),tags, new TagAliasCallback(){

			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				if(arg0==0){//为成功
					LogUtil.i("tag", "设置标签成功");
				 }
			}
		});
	}

	@Override
	protected void initData() {
		
	}
	
	@Override
	public void onClick(View v) {
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onResume();
		JPushInterface.onPause(this);
	};
}
