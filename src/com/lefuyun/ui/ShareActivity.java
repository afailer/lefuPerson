package com.lefuyun.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

import com.lefuyun.R;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.util.ShareUtils;
import com.lefuyun.util.ToastUtils;

public class ShareActivity extends BaseActivity {
	
	private TextView mWeChatFriendShare, mWeChatFriendCircleShare, mWechatCollect;
	private TextView mQQZoneShare, mQQShare, mSinaShare;
	private TextView mBackBtn;
	
	private String mTitle; // 分享条目标题
	private String mDes; // 分享条目描述
	private String mDesImgUrl; // 分享条目描述图片URL
	private String mUrl; // 分享详细内容URL
	private boolean isTitle; // 分享详细内容URL
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_share;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("分享");
		mWeChatFriendShare = (TextView) findViewById(R.id.wechat_friend);
		mWeChatFriendCircleShare = (TextView) findViewById(R.id.wechat_friend_circle);
		mWechatCollect = (TextView) findViewById(R.id.wechat_friend_history);
		mQQZoneShare = (TextView) findViewById(R.id.qzone);
		mQQShare = (TextView) findViewById(R.id.qq);
		mSinaShare=(TextView) findViewById(R.id.sina);
		mBackBtn = (TextView) findViewById(R.id.back);
		
		mWeChatFriendShare.setOnClickListener(this);
		mWeChatFriendCircleShare.setOnClickListener(this);
		mWechatCollect.setOnClickListener(this);
		mQQZoneShare.setOnClickListener(this);
		mQQShare.setOnClickListener(this);
		mSinaShare.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mTitle = intent.getStringExtra("title");
		mDes = intent.getStringExtra("des");
		mDesImgUrl = intent.getStringExtra("imgUrl");
		mUrl = intent.getStringExtra("url");
		isTitle = intent.getBooleanExtra("isTitle", true);
		if(TextUtils.isEmpty(mDesImgUrl)) {
			mDesImgUrl = LefuApi.IMG_URL + LefuApi.LEFU_IMG_URL;
		}
	}
	
	@Override
	public void onClick(View view) {
		ShareSDK.initSDK(getApplicationContext());
		switch (view.getId()) {
		
		case R.id.wechat_friend:
			shareMessage(ShareUtils.WECHAT_FRIEND);
			break;
		case R.id.wechat_friend_circle:
			shareMessage(ShareUtils.WECHAT_FRIEND_CIRCLE);
			break;
		case R.id.wechat_friend_history:
			shareMessage(ShareUtils.WECHAT_COLLECT);
			break;
		case R.id.qq:
			shareMessage(ShareUtils.QQ_FRIEND);
			break;
		case R.id.qzone:
			shareMessage(ShareUtils.QQ_ZONE);
			break;
		case R.id.sina:
			shareMessage(ShareUtils.SINA_WEIBO);
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
	/**
	 * 分享
	 * @param type 分享到的应用类型
	 */
	private void shareMessage(String type) {
		ShareUtils.shareMessage(type, mTitle, mDes, mDesImgUrl, mUrl, isTitle);
	}

	@Override
	protected boolean hasActionBar() {
		return true;
	}
	@Override
	protected boolean hasBackButton() {
		return true;
	}
}
