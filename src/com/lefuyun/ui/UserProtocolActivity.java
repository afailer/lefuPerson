package com.lefuyun.ui;

import android.graphics.Color;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;

public class UserProtocolActivity extends BaseActivity {
	
	private TextView mAgreeBtn;
	WebView webView1;
	@Override
	protected int getLayoutId() {
		return R.layout.activity_user_protocol;
	}

	@Override
	protected void initView() {
		setActionBarTitle("用户协议");
		webView1=(WebView) findViewById(R.id.webView1);
		mAgreeBtn = (TextView) findViewById(R.id.agree_user_protocol_activity);
		mAgreeBtn.setOnClickListener(this);
		readHtmlFormAssets();
	}

	@Override
	protected void initData() {
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.agree_user_protocol_activity:
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	 private void readHtmlFormAssets(){
	        WebSettings webSettings = webView1.getSettings();
	        
	        webSettings.setLoadWithOverviewMode(true);
	        webSettings.setUseWideViewPort(true);
	        webSettings.setBuiltInZoomControls(true);
	        webSettings.setSupportZoom(true);
	        webView1.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果，不知道为什么在xml配置中无法设置？
	        webView1.loadUrl("file:///android_asset/www/xieyi.html");
	    }
}
