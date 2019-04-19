package com.lefuyun.api.common;

import android.os.Handler;
import android.os.Looper;

import com.lefuyun.api.http.exception.ApiHttpException;

/**
 * 异步结果分发，单例
 */
public class Delivery {
	private static Delivery delivery;
	private Handler mDeliveryHandler;
	
	private Delivery() {
		mDeliveryHandler = new Handler(Looper.getMainLooper());
	}
	
	public static Delivery get() {
		if(delivery == null) {
			synchronized (Delivery.class) {
				if(delivery == null) {
					delivery = new Delivery();
				}
			}
		}
		return delivery;
	}

	/**
	 * 请求执行之前的响应分发
	 * @param callback
	 */
	public <T> void deliveryOnPreExecute(final RequestCallback<T> callback) {
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onPreExecute();
				}
			}
		});
	}
	
	/**
	 * 请求成功之后对结果进行分发
	 * @param result
	 * @param callback
	 */
	public <T> void deliverySuccessResult(final T result, final RequestCallback<T> callback) {
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onSuccess(result);
					callback.onAfterExecute();
				}
			}
		});
	}
	
	/**
	 * 请求失败之后的响应分发
	 * @param req
	 * @param e
	 * @param callback
	 */
	public <T> void deliveryFailureResult(final ApiHttpException e, final RequestCallback<T> callback) {
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onFailure(e);
					callback.onAfterExecute();
				}
			}
		});
	}
	
	/**
	 * 请求过程中的响应分发
	 * @param req
	 * @param e
	 * @param callback
	 */
	public <T> void deliveryOnLoading(final long totalCount, final long currCount, final RequestCallback<T> callback) {
		if(callback != null) {
			callback.onLoadingSubThread(totalCount, currCount);
		}
		mDeliveryHandler.post(new Runnable() {
			@Override
			public void run() {
				if(callback != null) {
					callback.onLoading(totalCount, currCount);
				}
			}
		});
	}
}
