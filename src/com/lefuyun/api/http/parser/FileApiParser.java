package com.lefuyun.api.http.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okio.BufferedSource;

import com.lefuyun.api.common.Delivery;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.http.exception.ExceptionStatusCode;
import com.lefuyun.api.http.request.ApiRequest;

/**
 * 文件解析器，下载文件时将文件流解析成文件
 */
public class FileApiParser extends ApiParser<File> {
	/**
	 * 下载文件的存储路径
	 */
	private String downloadFilePath;
	
	public FileApiParser(String downloadFilePath, ApiRequest<File> request) {
		super(request);
		this.downloadFilePath = downloadFilePath;
	}
	
	@Override
	public File parser(BufferedSource source, Request newRequest) throws ApiHttpException {
		byte[] buf = new byte[2048];
        int len = 0;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
        	is = source.inputStream();

        	long hasRead = 0;
        	long totalCount = contentLength;
        	if(totalCount == -1) {
        		totalCount = is.available();
        	}
        	
        	if(totalCount == 0) {
        		throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, "download fail, please check the download service");
        	}

        	if(request.getRequestCallback() != null) {
        		Delivery.get().deliveryOnLoading(totalCount, hasRead, request.getRequestCallback());
        	}
          
            makeParentDir(downloadFilePath);
            File file = new File(downloadFilePath);
            if(file.isDirectory()) {
            	throw new ApiHttpException(ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR, "文件存储地址必须为一个具体的地址，不能为目录(" + downloadFilePath + ")");
            }
            if(file.exists()) {
            	file.delete();
            }
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                if(request.getRequestCallback() != null) {
                	hasRead += len;
                	Delivery.get().deliveryOnLoading(totalCount, hasRead, request.getRequestCallback());
                }
            }
            fos.flush();
            return file;
        } catch (Exception e) {
        	if(e instanceof ApiHttpException) {
				throw (ApiHttpException)e;
			} else {
				throw new ApiHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e);
			}
        } finally {
            try {
            	if (fos != null) {
            		fos.close();
            	}
                if (is != null) {
                	is.close();
                }
            } catch (IOException e) {
            }
        }
	}
	
	/**
	 * 判断其父目录是否存在，不存在则创建
	 * Author: hyl
	 * Time: 2015-8-21下午11:13:35
	 * @param path
	 */
	private void makeParentDir(String path) {
		String parentPath = getParentPath(path);
		File file = new File(parentPath);
		if(!file.exists()) {
			makeParentDir(parentPath);
			file.mkdir();
		}
	}
	
	/**
	 * 获取父级目录
	 * Author: hyl
	 * Time: 2015-8-21下午11:12:44
	 * @param path
	 * @return
	 */
	private String getParentPath(String path) {
		if (path.equals("/")) {
			return path;
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		path = path.substring(0, path.lastIndexOf("/"));
		return path.equals("") ? "/" : path;
	}
}
