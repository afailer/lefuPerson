package com.lefuyun.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.lefuyun.AppContext;
import com.lefuyun.base.BaseActivity;

public class WindowUtil {

	/**
	 * 设置屏幕的背景透明度
	 * 
	 * @param activity
	 *            指定的activity
	 * @param bgAlpha
	 *            指定透明度
	 */
	public static void backgroundAlpha(BaseActivity activity, float bgAlpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * 移除状态栏
	 * 
	 * @param activity
	 */
	@TargetApi(19)
	public static void cancelWindow(BaseActivity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	/**
	 * 获取状态栏的高度
	 * 
	 * @return
	 */
	public static int getStatusBarHeight(BaseActivity activity) {
		int result = 0;
		int resourceId = activity.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = activity.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 获取当前应用的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取屏幕的宽度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getWindowWidth(BaseActivity activity) {
		DisplayMetrics outMetrics = getDisplayMetrics(activity);
		return outMetrics.widthPixels;
	}

	/**
	 * 获取屏幕的高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getWindowHeight(BaseActivity activity) {
		DisplayMetrics outMetrics = getDisplayMetrics(activity);
		return outMetrics.heightPixels;
	}

	/**
	 * 获取屏幕的宽高
	 * 
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(BaseActivity activity) {
		WindowManager windowManager = activity.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
	/**
	 * 将文本复制到剪切板上
	 * @param text
	 */
	@SuppressLint("NewApi")
	public static void copyToShearPlate(String text) {
		Context cotext = AppContext.sCotext;
		ClipboardManager clipboard = (ClipboardManager) cotext.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("simple text", text);
		clipboard.setPrimaryClip(clip);
	}

}
