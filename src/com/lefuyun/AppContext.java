package com.lefuyun;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.baidu.mapapi.SDKInitializer;
import com.lefuyun.api.ApiOkHttp;
import com.lefuyun.api.http.config.HttpConfig;
import com.lefuyun.bean.User;
import com.lefuyun.util.ConfigForSpeialOldUtils;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;
import com.tencent.bugly.crashreport.CrashReport;

public class AppContext extends Application {
	// 登录成功返回码
	public static final int LOGIN_SUCCESS = 600;
	// 用户信息修改成功
	public static final int UPDATE_USERINFO_SUCCESS = 800;
	// 老人绑定成功,并跳转到主页面
	public static final int BINDING_SUCCESS_SKIP = 700;
	// 老人绑定成功
	public static final int BINDING_SUCCESS = 900;
	
	public static Context sCotext;
	
	// 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "lefuyun"
            + File.separator + "download" + File.separator;
	public static MediaPlayer mediaPlayer=null;
	@Override
	public void onCreate() {
		super.onCreate();
		sCotext = this;
		instance = this;
		// false不打印Log
		TLog.DEBUG = false;
		// 初始化网络请求类工具
		ApiOkHttp.init(new HttpConfig(getApplicationContext()));
		// 初始化分享工具类
		ShareSDK.initSDK(this);
		
		String appId = "900006426";
		CrashReport.initCrashReport(this, appId, false);
		setLoadCityState(false);
		
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		// 初始化百度地图SDK
		SDKInitializer.initialize(this);
		
		mediaPlayer=new MediaPlayer();
	}
	/**
	 * 保存用户手机号和密码
	 * @param phone 手机号
	 * @param password 密码
	 */
	public static void saveUserInfo(String phone, String password,long uid,User result) {
		SPUtils.put(sCotext, "phone", phone);
		SPUtils.put(sCotext, "password", password);
		SPUtils.put(sCotext,SPUtils.ANGENCY_ID,result.getAgency_id()+"");
		SPUtils.put(sCotext, "uid", uid);
		SPUtils.put(sCotext, SPUtils.CONFIG_JSON_DATA, "");
		SPUtils.put(sCotext, SPUtils.LASTUPDATETIME, "");
		ConfigForSpeialOldUtils.oldPeoples_all=null;
	}
	/**
	 * 记录用户当前登录时间
	 */
	public static void recordLoginTime() {
		String currentTime = StringUtils.getCurTimeStr();
		// 获取上次打开应用的时间
		SPUtils.put(sCotext, "last_time_login", SPUtils.get(sCotext, "current_time_login", currentTime));
		// 保存当前打开此应用的时间
		SPUtils.put(sCotext, "current_time_login", currentTime);
	}
	/**
	 * 清除用户登录信息
	 */
	public static void clearUserInfo() {
		SPUtils.put(sCotext, "phone", "");
		SPUtils.put(sCotext, "password", "");
		SPUtils.put(sCotext, "last_time_login", "");
		SPUtils.put(sCotext, "current_time_login", "");
	}
	/**
	 * 判断用户是不是第一次进入探索页面
	 * @return
	 */
	public static boolean isFirstExplore() {
		return (Boolean) SPUtils.get(sCotext, "firstExplore", true);
	}
	/**
	 * 用户已经进入过并置已进入状态
	 */
	public static void setFirstExplore() {
		SPUtils.put(sCotext, "firstExplore", false);
	}
	/**
	 * 设置当前是否加载过城市列表
	 * @param flag true : 数据已经加载
	 */
	public static void setLoadCityState(boolean flag) {
		SPUtils.put(sCotext, "loadCity", flag);
	}
	/**
	 * 判断当前是否加载过城市列表
	 * @return
	 */
	public static boolean isLoadCity() {
		return (Boolean) SPUtils.get(sCotext, "loadCity", false);
	}
//--------------------------------------------------------
	private List<Activity> activityList = new LinkedList<Activity>();
	public static AppContext instance;
    
	public List<Activity> getActivityList() {
		return activityList;
	}
	public AppContext() {
	}

	// 创建对象
	public static AppContext getInstance() {
		if (null == instance) {
			instance = new AppContext();
		}
		return instance;
	}

	// 添加activity
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历 逐一finish
	public void exit() {
		LogUtil.i("tag","activityList:"+activityList.size());
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}
