package com.lefuyun.ui.webview;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lefuyun.R;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;

public class LefuInfoActivity extends BaseActivity {
	
	private WebView mWebView;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_webview_lefu_info;
	}

	@Override
	protected void initView() {
		mWebView = (WebView) findViewById(R.id.webview_lefu_info_activity);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initData() {
		String url = LefuApi.getAbsoluteApiUrl("lefuyun/appointmentCtr/toLefuInfo");
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			url = url + "?titlehight=" + 15;
		}
		WebSettings settings = mWebView.getSettings();
		String userAgentString = settings.getUserAgentString();
		if(!userAgentString.contains("lefuAppP")){
			userAgentString+=UserInfo.getUserAgent();
		}
		settings.setUserAgentString(userAgentString);
		settings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new ClickInterface(),"webBtn");
		// 防止打开系统默认浏览器
		mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            	showWaitDialog();
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
            	hideWaitDialog();
            }
        });
		Map<String, String> map = new HashMap<String, String>();
		map.put("User-Agent", settings.getUserAgentString());
		mWebView.loadUrl(url, map);
	}
	/**
	 * js返回按钮的点击事件
	 */
	class ClickInterface {
		
		@JavascriptInterface
		public void close() {
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
	}
	
	@Override
	protected boolean hasStatusBar() {
		return true;
	}
	
	@Override
	public void finish() {
		super.finish();
		// 设置当前activity关闭时的动画
		overridePendingTransition(R.animator.slide_left_in, R.animator.slide_right_out);
	}

}
