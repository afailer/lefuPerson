package com.lefuyun.api.http.request;

import java.lang.reflect.Type;

import com.lefuyun.api.http.parser.ApiParser;
import com.lefuyun.api.http.parser.JsonApiParser;

/**
 * JSON结果请求
 * 服务返回为JSON字符串，此请求会自动将JSON字符串转换指定的JavaBean对象或是String
 */
public class JsonApiRequest<T> extends ApiRequest<T> {
	/**
	 * 结果返回类型，与泛型T相对应(同步请求必须设置)
	 */
	protected Type resultType;
	/**
	 * 结果解析器
	 */
	protected ApiParser<T> dataParser;

	public JsonApiRequest(String url) {
		super(url);
	}
	
	/**
	 * @param url
	 * @param resultType	结果返回类型，与泛型T相对应(同步请求必须设置)
	 */
	public JsonApiRequest(String url, Type resultType) {
		super(url);
		this.resultType = resultType;
	}
	
	/**
	 * 设置结果返回类型，同步请求设置
	 * @param resultType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends JsonApiRequest<T>> S setResultType(Type resultType) {
		this.resultType = resultType;
		return (S) this;
	}

	@Override
	public ApiParser<T> getDataParser() {
		if(dataParser == null) {
			dataParser = new JsonApiParser<T>(this, resultType);
		}
		return dataParser;
	}

	/**
	 * 设置结果解析器
	 * @param dataParser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <S extends JsonApiRequest<T>> S setDataParser(ApiParser<T> dataParser) {
		this.dataParser = dataParser;
		return (S) this;
	}
}
