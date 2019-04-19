package com.lefuyun.api.http.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.util.TLog;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {
	
	public final HashMap<String, List<Cookie>> allCookies = new HashMap<String, List<Cookie>>();
	private final HttpUrl mHttpUrl = HttpUrl.parse(LefuApi.getAbsoluteApiUrl("lefuyun/userInfoCtr/queryUserInfo"));

	@Override
	public List<Cookie> loadForRequest(HttpUrl httpUrl) {
		List<Cookie> cookies = allCookies.get(httpUrl.host());
		if(mHttpUrl.uri().toString().equals(httpUrl.uri().toString())) {
			TLog.log("取cookie时的URL是登录接口");
			cookies = null;
			allCookies.clear();
		}
		if (cookies == null) {
			cookies = new ArrayList<Cookie>();
			// 防止首次登陆,保存时候遍历的值为null
			allCookies.put(httpUrl.host(), cookies);
		}
		return cookies;
	}

	@Override
	public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
		List<Cookie> oldCookies = allCookies.get(httpUrl.host());
		List<Cookie> needRemove = new ArrayList<Cookie>();

		for (Cookie newCookie : cookies) {
			for (Cookie oldCookie : oldCookies) {
				if (newCookie.name().equals(oldCookie.name())) {
					needRemove.add(oldCookie);
				}
			}
		}
		oldCookies.removeAll(needRemove);
		oldCookies.addAll(cookies);
	}
}
