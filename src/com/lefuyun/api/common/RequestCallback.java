package com.lefuyun.api.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.lefuyun.api.http.exception.ApiHttpException;

/**
 * 请求过程中的监听
 */
public abstract class RequestCallback<T> {
	/**
	 * 请求返回类型
	 */
	private Type claxx;
	
	public RequestCallback()  {
		this.claxx = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
	
	/**
	 * 请求执行前监听响应,UI线程(可以设置请求进度条)
	 */
	public void onPreExecute() {
		
	}
	
	/**
	 * 请求执行结束之后的响应监听，UI线程(可以取消请求进度条，onSuccess或onFailure结束之后都会执行此方法)
	 */
	public void onAfterExecute() {
		
	}
	
	/**
	 * 请求成功监听响应,UI线程
	 */
	public abstract void onSuccess(T result);
	
	/**
	 * 请求失败、结果解析失败监听响应,UI线程
	 * @param e 
	 * @param req 
	 */
	public abstract void onFailure(ApiHttpException e);
	
	/**
	 * 执行过程中监听响应，仅用于文件上传、下载过程中的进度条加载显示，UI线程
	 * 使用此方法就不要使用onLoadingSubThread()方法
	 * @param count
	 * @param current
	 */
	public void onLoading(long count, long current) {
		
	}
	/**
	 * 执行过程中监听响应，仅用于文件上传、下载过程中的进度条加载显示，子线程
	 * 使用此方法就不要使用onLoading()方法
	 * @param count
	 * @param current
	 */
	public void onLoadingSubThread(long count, long current) {
		
	}

	/**
	 * 获取请求的返回类型
	 * @return
	 */
	public Type getResultType() {
		return claxx;
	}
}
