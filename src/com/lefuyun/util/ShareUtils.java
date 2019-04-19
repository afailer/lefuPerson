package com.lefuyun.util;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.lefuyun.AppContext;

/**
 * 分享工具类
 */
public class ShareUtils {
	
	/**
	 * 微信好友分享
	 */
	public static final String WECHAT_FRIEND = Wechat.NAME;
	/**
	 * 微信朋友圈分享
	 */
	public static final String WECHAT_FRIEND_CIRCLE = WechatMoments.NAME;
	/**
	 * 微信收藏
	 */
	public static final String WECHAT_COLLECT = WechatFavorite.NAME;
	/**
	 * QQ好友分享
	 */
	public static final String QQ_FRIEND = QQ.NAME;
	/**
	 * QQ空间分享
	 */
	public static final String QQ_ZONE = QZone.NAME;
	/**
	 * 新浪微博分享
	 */
	public static final String SINA_WEIBO = SinaWeibo.NAME;
	
	/**
	 * 
	 * @param platform 分享的类别
	 * @param title 分享条目标题
	 * @param des 分享条目描述
	 * @param desImgUrl 分享条目描述图片URL
	 * @param url 分享详细内容URL
	 * @param isTitle 微信朋友不支持text而新浪微博不支持title
	 */
	public static void shareMessage(String platform, String title, 
			String des, String desImgUrl, String url, boolean isTitle) {
		OnekeyShare oks = new OnekeyShare();
		// 关闭SSO授权
		oks.disableSSOWhenAuthorize();
		oks.setPlatform(platform);
		oks.setShareContentCustomizeCallback(
				new ShareContentCustomize(title, des, desImgUrl, url, isTitle));
		oks.setCallback(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				ToastUtils.show(AppContext.sCotext, "分享失败");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				ToastUtils.show(AppContext.sCotext, "分享成功");
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {}
		});
		// 启动分享GUI
		oks.show(AppContext.sCotext);
	}

	/**
	 * 分享监听回调方法
	 */
	private static class ShareContentCustomize implements
			ShareContentCustomizeCallback {
		
		private String mTitle; // 分享条目标题
		private String mDes; // 分享条目描述
		private String mDesImgUrl; // 分享条目描述图片URL
		private String mUrl; // 分享详细内容URL
		private boolean isTitle; // 是否显示标题
		
		/**
		 * 构造方法
		 * @param title 分享条目标题
		 * @param des 分享条目描述
		 * @param desImgUrl 分享条目描述图片URL
		 * @param url 分享详细内容URL
		 */
		public ShareContentCustomize(String title, String des,
				String desImgUrl, String url, boolean isTitle) {
			this.mTitle = title;
			this.mDes = des;
			this.mDesImgUrl = desImgUrl;
			this.mUrl = url;
			this.isTitle = isTitle;
		}
		
		@Override
		public void onShare(Platform platform, ShareParams paramsToShare) {

			if (Wechat.NAME.equals(platform.getName())) {
				// 微信好友分享
				paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
				paramsToShare.setTitle(mTitle);
				paramsToShare.setText(mDes);
				paramsToShare.setImageUrl(mDesImgUrl);
				paramsToShare.setUrl(mUrl);
			}  else if (WechatMoments.NAME.equals(platform.getName())) {
				// 微信朋友圈分享
				paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
				paramsToShare.setTitle(isTitle ? mTitle : mDes);
				paramsToShare.setImageUrl(mDesImgUrl);
				paramsToShare.setUrl(mUrl);
			}else if (WechatFavorite.NAME.equals(platform.getName())) {
				// 微信收藏
				paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
				paramsToShare.setTitle(mTitle);
				paramsToShare.setText(mDes);
				paramsToShare.setImageUrl(mDesImgUrl);
				paramsToShare.setUrl(mUrl);
			}else if (QQ.NAME.equals(platform.getName())) {
				// QQ好友分享
				paramsToShare.setTitle(mTitle);
				paramsToShare.setTitleUrl(mUrl);
				paramsToShare.setText(mDes);
				paramsToShare.setImageUrl(mDesImgUrl);
			}else if (QZone.NAME.equals(platform.getName())) {
				// QQ空间分享
				paramsToShare.setTitle(mTitle);
				paramsToShare.setTitleUrl(mUrl);
				paramsToShare.setSite("乐福健康");
				paramsToShare.setSiteUrl("http://www.lefukj.cn");
				paramsToShare.setText(mDes);
				paramsToShare.setImageUrl(mDesImgUrl);
			}else if(SinaWeibo.NAME.equals(platform.getName())) {
				// 新浪微博分享
				paramsToShare.setText(mDes);
				paramsToShare.setImageUrl(mDesImgUrl);
			}
		}
	}

}
