package com.lefuyun.api.http;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;

import com.lefuyun.api.http.request.ApiRequest;
import com.lefuyun.api.http.request.ApiRequestBody;
import com.lefuyun.api.http.request.UploadApiRequest;

/**
 * 请求、响应构造器,
 * 这里才是真正的创建了request请求
 */
public class OKHttpBuilder {
	/**
	 * 流请求媒体类型
	 */
	private final static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
    /**
     * 字符串请求类型
     */
    private final static MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain;charset=utf-8");
    
    /*------------------------------------ 对外提供的构造方法 ---开始-----------------------------------------*/
    
    /**
	 * 构造OkHttp中的Get请求对象
	 * @param request
	 * @return
	 */
	public static <T> Request buildGetRequest(ApiRequest<T> request) {
		return buildGetRequest(request.getUrl(), request.getParams(), request.getHeaders()).cacheControl(getCacheControl(request)).build();
	}
	
	/**
	 * 根据请求url和请求参数构造get请求对象
	 * @param url		请求url
	 * @param params	请求参数
	 * @return
	 */
	public static Request.Builder buildGetRequest(String url, Map<String, Object> params, Map<String, String> headers) {
		return buildRequest(buildUrl(url, params), headers);
	}
	
	/**
	 * 构造OkHttp中的普通Post请求对象
	 * @param request
	 * @return
	 */
	public static <T> Request buildPostRequest(ApiRequest<T> request) {
		if(request instanceof UploadApiRequest) {
			return buildPostRequest((UploadApiRequest<T>)request);
		}
		return buildPostRequest(request.getUrl(), request.getParams(), request.getHeaders()).cacheControl(getCacheControl(request)).build();
	}
	
	/**
	 * 构造OkHttp中的文件上传Post请求
	 * @param request
	 * @return
	 */
	public static <T> Request buildPostRequest(UploadApiRequest<T> request) {
		RequestBody body = buildRequestBody(request.getFiles(), request.getParams());
		return buildPostRequest(request.getUrl(), new ApiRequestBody<T>(body, request.getRequestCallback()), request.getHeaders()).cacheControl(getCacheControl(request)).build();
	}
	
	/**
	 * 根据请求url和请求参数构造Post请求对象
	 * @param url		请求url
	 * @param params	请求参数
	 * @param headers 	请求头信息
	 * @return
	 */
	public static Request.Builder buildPostRequest(String url, Map<String, Object> params, Map<String, String> headers) {
		RequestBody body = buildRequestBody(params);
		return buildPostRequest(url, body, headers);
	}
	
	
    /*------------------------------------ 对外提供的构造方法 ---结束-----------------------------------------*/
	
	
	/*------------------------------------ 构造RequestBody部分 ---开始-----------------------------------------*/
	
	/**
	 * 根据请求url和请求头构造新的请求,为get请求
	 * @param url		请求url
	 * @param headers	请求头信息
	 * @return
	 */
	private static Request.Builder buildRequest(String url, Map<String, String> headers) {
		if(headers != null && headers.size() > 0) {
			Request.Builder builder = new Request.Builder().url(buildUrl(url, null));
			for(String key : headers.keySet()) {
				builder.addHeader(key, headers.get(key));
			}
			return builder;
		} else {
			return new Request.Builder().url(buildUrl(url, null));
		}
	}
	
	/**
	 * 根据请求url和请求参数构造一个新的url(将参数添加到url上)
	 * @param url		请求url
	 * @param params	请求参数
	 * @return
	 */
	private static String buildUrl(String url, Map<String, Object> params) {
		if(url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("请求URL不能为null");
		}
		StringBuilder sb = new StringBuilder(url);
		if(params != null) {
			// 在url后面先添加?
			sb.append("?");
			for(String key : params.keySet()) {
				sb.append(key);
				sb.append("=");
				sb.append(params.get(key).toString());
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/**
	 * 根据请求url和请求体构造请求(Post请求)
	 * @param url		请求url
	 * @param body		请求体 
	 * @param headers 	请求头信息
	 * @return
	 */
	private static Request.Builder buildPostRequest(String url, RequestBody body, Map<String, String> headers) {
		if(url == null || url.trim().length() == 0) {
			throw new IllegalArgumentException("请求URL不能为null");
		}
		if(headers != null && headers.size() > 0) {
			Request.Builder builder = new Request.Builder().url(buildUrl(url, null));
			for(String key : headers.keySet()) {
				builder.addHeader(key, headers.get(key));
			}
			return builder.post(body);
		} else {
			return new Request.Builder().url(buildUrl(url, null)).post(body);
		}
	}
	
	/**
	 * 根据请求参数构造Post请求体
	 * @param params
	 * @return
	 */
	private static RequestBody buildRequestBody(Map<String, Object> params) {
		
		FormBody.Builder builder = new FormBody.Builder();
		if(params == null) {
			return builder.build();
		}
//		FormEncodingBuilder builder = new FormEncodingBuilder();
		for(String key : params.keySet()) {
			builder.add(key, params.get(key).toString());
		}
		return builder.build();
	}
	
	/**
	 * 根据多个文件和其他Form参数构造多文件上传请求体
	 * @param files				文件数组
	 * @param formParams		其他附带Form参数
	 * @return
	 */
	private static RequestBody buildRequestBody(File[] files, Map<String, Object> formParams) {
		if(files == null) {
			throw new IllegalArgumentException("files不能为空");
		}
		
		MultipartBody.Builder builder = new Builder();
		builder.setType(MultipartBody.FORM);
//		MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

		if(formParams != null) {
			for (String key : formParams.keySet()) {
				builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
						buildRequestBodyNoneMediaType(formParams.get(key).toString()));
			}
		}
		
		if (files != null) {
		    RequestBody fileBody = null;
		    for (int i = 0; i < files.length; i++) {
		        File file = files[i];
		        String fileName = file.getName();
		        String key = fileName;
		        // 获取文件的名称去掉后缀如".txt"
		        int idx = key.indexOf(".");
		        if(idx > 0) {
		        	key = key.substring(0, idx);
		        }
		        fileBody = RequestBody.create(MediaType.parse(getMimeType(fileName)), file);
		        //根据文件名设置contentType
		        builder.addPart(Headers.of("Content-Disposition",
		                        "form-data; name=\"" + "file" + "\"; filename=\"" + fileName + "\""),
		                fileBody);
		    }
		}
		
		return builder.build();
	}
	
	/**
	 * 根据文件名获取文件的MIME类型
	 * @param fileName
	 * @return
	 */
	private static String getMimeType(String fileName) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(fileName);
        if (mimeType == null) {
        	mimeType = "application/octet-stream";
        }
        return mimeType;
	}
	
	/**
	 * 根据请求获取当前请求的缓存控制器
	 * @param request
	 * @return
	 */
	private static <T> CacheControl getCacheControl(ApiRequest<T> request) {
		CacheControl cacheControl = null;
		switch(request.getCacheStrategy()) {
			case FORCE_NETWORK:
				cacheControl = CacheControl.FORCE_NETWORK;
				break;
			case FORCE_CACHE:
				cacheControl = CacheControl.FORCE_CACHE;
				break;
			default :
				cacheControl = new CacheControl.Builder().build();
				break;
		}
		return cacheControl;
	}
	
	/*------------------------------------ 构造RequestBody部分 ---结束-----------------------------------------*/
	
	
	/*---------------------- 非常用构造 ----------------------------*/
	

	/**
	 * 根据文件路径构造一个请求体
	 * @param filePath	文件绝对路径
	 * @return
	 */
	protected static RequestBody buildFileRequestBody(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			throw new IllegalArgumentException("文件" + filePath + "不存在");
		}
		return buildRequestBody(file);
	}
	
	/**
	 * 构造单个文件上传的请求体(无其他附带form参数)
	 * @param file		上传的文件
	 * @return
	 */
	private static RequestBody buildRequestBody(File file) {
		return buildRequestBody(new File[]{file}, null);
	}
	
	/**
	 * 根据文件构造一个请求体
	 * @param file
	 * @return
	 */
	protected static RequestBody buildRequestBodyStream(File file) {
		return RequestBody.create(MEDIA_TYPE_STREAM, file);
	}
	
	/**
	 * 根据文本内容构造一个请求体
	 * @param bodyString	文本内容，直接作为请求体
	 * @return
	 */
	protected static RequestBody buildRequestBody(String bodyString) {
		return RequestBody.create(MEDIA_TYPE_STRING, bodyString);
	}
	
	/**
	 * 根据文本内容构造一个无媒体类型的请求体
	 * @param bodyString	文本内容，直接作为请求体
	 * @return
	 */
	private static RequestBody buildRequestBodyNoneMediaType(String bodyString) {
		return RequestBody.create(null, bodyString);
	}
	
	/**
	 * 根据字节数组构造一个请求体
	 * @param bodyBytes		字节数组，直接作为请求体
	 * @return
	 */
	protected static RequestBody buildRequestBody(byte[] bodyBytes) {
		return RequestBody.create(MEDIA_TYPE_STREAM, bodyBytes);
	}

	/**
	 * 构造单个文件以及其他Form参数进行文件上传的请求体
	 * @param file			要上传的文件
	 * @param formParams	上传文件时的其他Form参数
	 * @return
	 */
	protected static RequestBody buildRequestBody(File file, Map<String, Object> formParams) {
		return buildRequestBody(new File[]{file}, formParams);
	}
}
