package com.lefuyun.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import com.lefuyun.AppContext;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.http.OKHttpBuilder;
import com.lefuyun.api.http.OkHttpExecutor;
import com.lefuyun.api.http.config.HttpConfig;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.http.request.ApiRequest;
import com.lefuyun.util.NetWorkUtils;

/**
 * OKHttp请求管理类，单例模式实现，建议在使用之前在Application中调用{@link init(OkHttpConfig config)}进行初始化
 * 
 * 文件上传请求请使用已经定义好的{@link}TigerUploadRequest，文件下载请求请使用已经定义好的{@link}TigerDownloadRequest
 */
public final class ApiOkHttp {
	/**
	 * OkHttpManager单例对象，保证OKHttpClient全局唯一实例
	 */
	private static ApiOkHttp mOkHttpManager;
	/**
	 * 
	 */
	private static OkHttpClient mOkHttpClient;
	/**
	 * OkHttp相关配置信息
	 */
	private HttpConfig mConfig;
	/**
	 * OkHttp请求执行器
	 */
	private static OkHttpExecutor mOkHttpExecutor;
	
	private ApiOkHttp(HttpConfig config) {
		mConfig = config;
	}
	
	/**
	 * 获取单例对象，建议在使用之前调用此方法进行初始化
	 * @param config
	 * @return
	 */
	public static ApiOkHttp init(HttpConfig config) {
		if(mOkHttpManager == null) {
			synchronized (ApiOkHttp.class) {
				if(mOkHttpManager == null) {
					mOkHttpManager = new ApiOkHttp(config);
					mOkHttpManager.initOkHttpConfig();
					mOkHttpExecutor = new OkHttpExecutor(mOkHttpClient);
				}
			}
		}
		return mOkHttpManager;
	}
	
	private void initOkHttpConfig() {
		mOkHttpClient = new OkHttpClient.Builder()
				.connectTimeout(mConfig.connectTimeOut, TimeUnit.SECONDS)
				.readTimeout(mConfig.readTimeOut, TimeUnit.SECONDS)
				.writeTimeout(mConfig.writeTimeOut, TimeUnit.SECONDS)
				.cookieJar(mConfig.cookieJar)
				.hostnameVerifier(mConfig.hostnameVerifier)
				.build(); // build返回的是新的对象
	}

	public static HttpConfig getConfig() {
		return mOkHttpManager.mConfig;
	}
	
	public static OkHttpExecutor getOkHttpExecutor() {
		return mOkHttpExecutor;
	}
	
	public static OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}
	
	/**
	 * 同步Get请求
	 * @param 	request
	 * @return						请求成功、解析成功后返回实体对象
	 * @throws ApiHttpException 	请求失败，结果解析失败抛出该异常
	 */
	public static <T> T getSync(ApiRequest<T> request) throws ApiHttpException {
		request.setRequestCallback(null);
		return ApiOkHttp.getOkHttpExecutor().syncExecute(OKHttpBuilder.buildGetRequest(request), request.getDataParser());
	}
	
	/**
	 * 异步Get请求
	 * @param request
	 */
	public static <T> void getAsync(ApiRequest<T> request, RequestCallback<T> requestCallback) {
		request.setRequestCallback(requestCallback);
		ApiOkHttp.getOkHttpExecutor().asyncExecute(OKHttpBuilder.buildGetRequest(request), request.getDataParser());
	}
	
	/**
	 * 同步Post请求
	 * @param 	request
	 * @return						请求成功、解析成功后返回实体对象
	 * @throws ApiHttpException 	请求失败，结果解析失败抛出该异常
	 */
	public static <T> T postSync(ApiRequest<T> request) throws ApiHttpException {
		request.setRequestCallback(null);
		return ApiOkHttp.getOkHttpExecutor().syncExecute(OKHttpBuilder.buildPostRequest(request), request.getDataParser());
	}
	
	/**
	 * 异步Post请求(可以返回单个：T为Class 或 多个：T为List<T>)
	 * @param request
	 */
	public static <T> void postAsync(ApiRequest<T> request, RequestCallback<T> requestCallback) {
		if(!NetWorkUtils.hasNetwork(AppContext.sCotext)) {
//			ToastUtils.show(AppContext.sCotext, "当前网络不可用, 请检查网络连接");
			ApiHttpException e = new ApiHttpException(-1, "当前网络不可用, 请检查网络连接");
			requestCallback.onFailure(e);
			return;
		}
		setRequestHeader(request);
		request.setRequestCallback(requestCallback);
		ApiOkHttp.getOkHttpExecutor().asyncExecute(OKHttpBuilder.buildPostRequest(request), request.getDataParser());
	}
	/**
	 * 请求头固定值的添加
	 * @param request
	 */
	public static<T> void setRequestHeader(ApiRequest<T> request){
		request.addHeader("User-Agent", UserInfo.getUserAgent())
		.addHeader("Accept-Encoding", "gzip, deflate");
	}
}
