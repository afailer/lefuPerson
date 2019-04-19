package com.lefuyun.api.http.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.GZIPInputStream;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.BufferedSource;

import org.json.JSONObject;

import com.lefuyun.api.ApiOkHttp;
import com.lefuyun.api.common.Delivery;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.http.exception.ExceptionStatusCode;
import com.lefuyun.api.http.request.ApiRequest;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.bean.User;
import com.lefuyun.util.TLog;

/**
 * OkHttp请求响应解析器
 * 同步请求请调用无参构造函数，异步请求请调用有参构造函数
 * 如果要自定义自己的解析器，只需要继承此类，重写方法parser(BufferedSource source)即可
 */
public abstract class ApiParser<T> {
	/**
	 * 当前请求的请求监听，异步请求才有用
	 */
	protected ApiRequest<T> request;
	/**
	 * 返回字符串结果时的内容长度
	 */
	protected long contentLength;
	/**
	 * 当次请求的编码名称
	 */
	protected String charsetName;
	/**
	 * 同步请求的解析调用此构造函数
	 */
	public ApiParser(ApiRequest<T> request) {
		this.request = request;
	}
	
	public static int num;
	
	/**
	 * 解析OkHttp同步请求响应，此请求返回单个实体对象
	 * @param response				待解析的请求响应体
	 * @param claxx					返回的实体对象Class对象
	 * @return						解析成功返回{@link List}
	 * @throws ApiHttpException 	请求失败状态、解析失败均抛出异常
	 */
	public final T parserSync(Response response) throws ApiHttpException {
		
		if(response.isSuccessful()) {
			try {
				getResponseBodyInfo(response.body());
				return parser(response.body().source(), response.request());
			} catch (IOException e) {
				throw new ApiHttpException(ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR, e.getMessage());
			}
		} else {
			throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, response.message());
		}
	}
	
	/**
	 * 解析OkHttp异步请求响应
	 * @param response		待解析的请求响应体
	 */
	public final void parserAsync(Response response, Request newRequest) {
		TLog.log("response.request() == " + response.request().url().toString());
		if(response.isSuccessful()) {
			try {
				getResponseBodyInfo(response.body());
				T data = parser(response.body().source(), newRequest);
				Delivery.get().deliverySuccessResult(data, request.getRequestCallback());
			} catch (IOException e) {
				Delivery.get().deliveryFailureResult(new ApiHttpException(ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR, e.getMessage()), request.getRequestCallback());
			} catch (ApiHttpException e) {
				Delivery.get().deliveryFailureResult(e, request.getRequestCallback());
			}
		} else {
			Delivery.get().deliveryFailureResult(new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, response.message()), request.getRequestCallback());
		}
	}
	
	/**
	 * 获取此次请求的编码方式和内容长度
	 * @param body
	 * @throws IOException
	 */
	private final void getResponseBodyInfo(ResponseBody body) throws IOException {
		MediaType contentType = body.contentType();
	    Charset charset = contentType != null ? contentType.charset(Charset.forName("UTF_8")) : Charset.forName("UTF_8");
		this.charsetName = charset.name();
		this.contentLength = body.contentLength();
	}
	
	/**
	 * 解析OkHttp请求的结果流，并返回解析得到的实体对象
	 * @param source	待解析的响应结果流
	 * @return
	 * @throws ApiHttpException 	解析失败抛出异常
	 */
	public abstract T parser(BufferedSource source, Request request) throws ApiHttpException;
	
	/**
	 * 返回请求对象
	 * @return
	 */
	public ApiRequest<T> getTigerRequest() {
		return request;
	}
	
	/**
	 * 获取请求返回的结果
	 * @param response				待解析的请求响应体
	 * @return						返回请求响应字符串
	 * @throws ApiHttpException 	请求失败状态、解析失败均抛出异常
	 */
	public final String getWrapperResult(BufferedSource source, Request newRequest) throws ApiHttpException {
		try {
		    if(contentLength > Integer.MAX_VALUE) {
		    	throw new IOException("Cannot buffer entire body for content length: " + contentLength);
		    }
		    byte[] bytes;
		    try {
		    	bytes = source.readByteArray();
		    	
		    } finally {
		    	Util.closeQuietly(source);
		    }
		    if(contentLength != -1 && contentLength != bytes.length) {
		    	throw new IOException("Content-Length and stream length disagree");
		    }
//		    String bodyString = new String(bytes, charsetName);
		    String bodyString = getRealString(bytes);
		    return getWrapperResult(bodyString, newRequest);
		} catch (Exception e) {
			if(e instanceof ApiHttpException) {
				throw (ApiHttpException)e;
			} else {
				throw new ApiHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
			}
		}
	}
	
	/**
	 * 获取请求返回的结果
	 * @param bodyString	待解析的结果字符串
	 * @return
	 * @throws ApiHttpException
	 */
	private final String getWrapperResult(String bodyString, final Request newRequest) throws ApiHttpException {
		TLog.log("ApiParser   --->   json == " + bodyString);
		try {
			if(!ApiOkHttp.getConfig().isWrapperResult) {
				// 结果没有进行包装过
				return bodyString;
			}
			JSONObject json = new JSONObject(bodyString);
			json = json.getJSONObject("ResponseMsg");
			if(!json.has(ApiOkHttp.getConfig().wrapperJsonResult.code_name)) {
				throw new ApiHttpException(ExceptionStatusCode.STATUS_RESULT_FORMAT_ERROR, "结果没有进行包装，无法以包装结果进行解析");
			}
			int flag = json.getInt(ApiOkHttp.getConfig().wrapperJsonResult.code_name);
			if(flag == ApiOkHttp.getConfig().wrapperJsonResult.code_error_value) {
				num = 0;
				// 当前返回了错误状态码
				throw new ApiHttpException(ExceptionStatusCode.STATUS_RESULT_EXIST_ERRORINFO, json.getString(ApiOkHttp.getConfig().wrapperJsonResult.error_name));
			}else if(flag == ApiOkHttp.getConfig().wrapperJsonResult.code_overtime_value) {
				// 当前用户未登录状态
//				throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_OVERTIME, json.getString(ApiOkHttp.getConfig().wrapperJsonResult.error_name));
				num++;
				@SuppressWarnings("rawtypes")
				final ApiParser apiParser = ApiParser.this;
				if(num <= 1) {
					LefuApi.autoLogin(null, new RequestCallback<User>() {
						
						@SuppressWarnings("unchecked")
						@Override
						public void onSuccess(User result) {
							num = 0;
							TLog.log("超时后要去请求的接口" + newRequest.url().toString());
							ApiOkHttp.getOkHttpExecutor().asyncExecute(newRequest, apiParser);
						}
						
						@Override
						public void onFailure(ApiHttpException e) {
							
						}
					});
				}
				return "";
			}else if(flag == ApiOkHttp.getConfig().wrapperJsonResult.code_parameter_error_value) {
				num = 0;
				// 参数错误
				throw new ApiHttpException(ExceptionStatusCode.STATUS_PARAMETER_ERROR, json.getString(ApiOkHttp.getConfig().wrapperJsonResult.error_name));
			}
			return json.getString(ApiOkHttp.getConfig().wrapperJsonResult.result_name);
		} catch (Exception e) {
			if(e instanceof ApiHttpException) {
				throw (ApiHttpException)e;
			} else {
				throw new ApiHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
			}
		}
	}
	/**
	 * 解析jzip文件
	 * @param data
	 * @return
	 */
	private String getRealString(byte[] data) {
		byte[] h = new byte[2];
		h[0] = (data)[0];
		h[1] = (data)[1];
		int head = getShort(h);
		boolean t = head == 0x1f8b;
		InputStream in;
		StringBuilder sb = new StringBuilder();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			if (t) {
				in = new GZIPInputStream(bis);
			} else {
				in = bis;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(in), 4096);
			for (String line = r.readLine(); line != null; line = r.readLine()) {
				sb.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}
}
