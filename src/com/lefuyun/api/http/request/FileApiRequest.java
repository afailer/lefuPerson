package com.lefuyun.api.http.request;

import java.io.File;

import com.lefuyun.api.http.parser.ApiParser;
import com.lefuyun.api.http.parser.FileApiParser;

/**
 * 文件下载请求
 */
public class FileApiRequest extends ApiRequest<File> {
	/**
	 * 下载文件的存量地址(文件所在绝对地址，包含文件名
	 * 只需要给出地址就行，不需要保证其父目录存在，解析的时候会自动判断，父目录不存在会自动创建)
	 */
	private String fileDownloadPath;
	/**
	 * 文件下载成功的解析器
	 */
	protected FileApiParser fileParser;
	
	public FileApiRequest(String url, String fileDownloadPath) {
		super(url);
		this.fileDownloadPath = fileDownloadPath;
	}

	/**
	 * 获取文件下载后的存储路径
	 * @return
	 */
	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	@Override
	public ApiParser<File> getDataParser() {
		if(fileParser == null) {
			fileParser = new FileApiParser(fileDownloadPath, this);
		}
		return fileParser;
	}
}
