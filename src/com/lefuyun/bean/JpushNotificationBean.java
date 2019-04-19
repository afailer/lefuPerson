package com.lefuyun.bean;

/**
 * @author chenshichao
 * @date 2016-5-19
 * @description 推送的bean
 */
public class JpushNotificationBean {
	private String title;// 通知的标题
	private String content;// 通知的内容
	private String type;// 00101退出登录00201重要通知00301新闻
	private JpushNotificationDetailBean jpushNotificationDetailBean;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JpushNotificationDetailBean getJpushNotificationDetailBean() {
		return jpushNotificationDetailBean;
	}

	public void setJpushNotificationDetailBean(
			JpushNotificationDetailBean jpushNotificationDetailBean) {
		this.jpushNotificationDetailBean = jpushNotificationDetailBean;
	}

}
