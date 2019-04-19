package com.lefuyun.util;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

public final class LogUtil {
	
	public static boolean APP_DBG = true; // 是否是debug模式

	public static void init(Context context){
		APP_DBG =isApkDebugable(context);
		System.out.println("APP_DBG"+APP_DBG);
	}
	
	/**
	 * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
	 * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
	 * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
 	 * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
	 * 
	 * @param context
	 * @return
	 * @author SHANHY
	 * @date   2015-8-7
	 */
	public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
                return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {
            
        }
        return false;
    }
	public static void d(String tag, String str) {
		if (APP_DBG) {
			try {
				Log.d(tag, str);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public static void e(String tag, String str) {
		if (APP_DBG) {
			try {
				Log.e(tag, str);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public static void i(String tag, String str) {
		if (APP_DBG) {
			try {
				Log.i(tag, str);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public static void v(String tag, String str) {
		if (APP_DBG) {
			try {
				Log.v(tag, str);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
