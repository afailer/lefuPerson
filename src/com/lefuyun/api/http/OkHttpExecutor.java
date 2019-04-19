package com.lefuyun.api.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.lefuyun.api.common.Delivery;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.http.exception.ExceptionStatusCode;
import com.lefuyun.api.http.parser.ApiParser;
import com.lefuyun.util.TLog;

/**
 * 请求执行器，发起OkHttp请求
 */
public class OkHttpExecutor {
	private OkHttpClient mOkHttpClient;

	public OkHttpExecutor(OkHttpClient okHttpClient) {
		super();
		this.mOkHttpClient = okHttpClient;
	}
	
	/**
	 * 异步执行某个OkHttp请求
	 * @param request
	 * @param parser	请求结果解析器
	 */
	public <T> void asyncExecute(final Request request, final ApiParser<T> parser) {
		TLog.log("异步请求地址：" + request.url().toString());
		if(parser == null) {
			throw new IllegalArgumentException("not set Data Parser");
		}
		Delivery.get().deliveryOnPreExecute(parser.getTigerRequest().getRequestCallback());
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onFailure(Call arg0, IOException e) {
				Delivery.get().deliveryFailureResult(new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_IO_ERROR, "网络请求失败"), parser.getTigerRequest().getRequestCallback());
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				parser.parserAsync(response, request);
			}
		});
	}
	
	/**
	 * 同步执行某个OkHttp请求，并返回实体对象
	 * @param request				
	 * @return						请求解析成功返回实体对象
	 * @throws ApiHttpException 	请求失败、解析失败均抛出异常
	 */
	public <T> T syncExecute(Request request, ApiParser<T> parser) throws ApiHttpException {
		TLog.log("同步请求地址：" + request.url().toString());
		if(parser == null) {
			throw new IllegalArgumentException("not set Data Parser");
		}
		Response response = null;
		try {
			response = mOkHttpClient.newCall(request).execute();
		} catch (IOException e) {
			throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, e.getMessage());
		}
		return parser.parserSync(response);
	}
}
