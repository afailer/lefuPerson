package com.lefuyun.api.common;

import android.os.Build;

public class UserInfo {
	
	public static String getUserAgent() {
		return " lefuAppP (" + Build.MODEL + " ; Android " + Build.VERSION.RELEASE + ")";
	}
	
	public static String getVersion() {
		return "160427";
	}

}
