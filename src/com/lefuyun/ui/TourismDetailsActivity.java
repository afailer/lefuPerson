package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.ShowPicturesAdapter;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.Tourism;
import com.lefuyun.interf.ScrollViewListener;
import com.lefuyun.widget.MyScrollView;
import com.lefuyun.widget.circleindicator.CircleIndicator;
import com.nineoldandroids.animation.ObjectAnimator;

public class TourismDetailsActivity extends BaseActivity {
	
	// 渐变的高度
	private static final float HEIGHT = 560;
	
	// actionBar的背景颜色控件
	private View mBackground;
	// actionBar的返回键
	private ImageView mBackBtn, mShareBtn;
	
	// 悬浮栏按钮
	private TextView mSignUpOnlineBtn, mSignUpOnPhoneBtn;
	
	// 当前actionBar的透明度
	private float mRatio;
	
	private ViewPager mViewPager;
	
	private Tourism mTourism;
	private WebView mWebView;
	private MyScrollView mScrollView;
	private CircleIndicator circleIndicator;
	private ArrayList<String> mList;
	private ShowPicturesAdapter mAdapter;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_tourism_details;
	}

	@Override
	protected void initView() {
		
		initCurrentActionBar();
		
		// 初始化WebView
		mWebView = (WebView) findViewById(R.id.web_tourism_details_activity);
		WebSettings settings = mWebView.getSettings();
		String userAgentString = settings.getUserAgentString();
		if(!userAgentString.contains("lefuAppP")){
			userAgentString+=UserInfo.getUserAgent();
		}
		settings.setUserAgentString(userAgentString);
		
		initScrollView();
		
		mViewPager = (ViewPager) findViewById(R.id.vp_tourism_details_activity);
		circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
	}
	/**
	 * 初始化ScrollView
	 */
	private void initScrollView() {
		mScrollView = (MyScrollView) findViewById(R.id.sv_tourism_details_activity);
		mScrollView.setScrollViewListener(new ScrollViewListener() {
			
			@Override
			public void onScrollChanged(int x, int y, int oldx, int oldy) {
				// 根据ScrollView滑动的距离设置actionBar的透明度
				if(oldy <= HEIGHT && oldy >= 0) {
					mRatio = (float) oldy / HEIGHT;
					ObjectAnimator.ofFloat(mBackground, "alpha", mRatio).setDuration(0).start();
					mBackBtn.setImageResource(R.drawable.back_round_main_color);
					mShareBtn.setImageResource(R.drawable.share_round_main_color);
				}else if(oldy > 560) {
					ObjectAnimator.ofFloat(mBackground, "alpha", 1).setDuration(0).start();
					mBackBtn.setImageResource(R.drawable.back_round_gray);
					mShareBtn.setImageResource(R.drawable.share_round_gray);
				}else if(oldy < 0) {
					ObjectAnimator.ofFloat(mBackground, "alpha", 0).setDuration(0).start();
					mBackBtn.setImageResource(R.drawable.back_round_main_color);
					mShareBtn.setImageResource(R.drawable.share_round_main_color);
				}
				
			}
		});
		
	}

	/**
	 * 初始化状态栏以及悬浮栏
	 */
	private void initCurrentActionBar() {
		// 初始化状态栏的背景颜色
		mBackground = findViewById(R.id.backgroud_action_bar);
		// 初始化返回按钮
		mBackBtn = (ImageView) findViewById(R.id.back_action_bar);
		mBackBtn.setOnClickListener(this);
		ObjectAnimator.ofFloat(mBackground, "alpha", mRatio).setDuration(0).start();
		mBackground.setVisibility(View.VISIBLE);
		mShareBtn = (ImageView) findViewById(R.id.share_action_bar);
//		mShareBtn.setOnClickListener(this);
		mShareBtn.setVisibility(View.GONE);
		
		// 初始化悬浮栏
		mSignUpOnlineBtn = (TextView) findViewById(R.id.signup_online_tourism_details_activity);
		mSignUpOnPhoneBtn = (TextView) findViewById(R.id.signup_onphone_tourism_details_activity);
		mSignUpOnlineBtn.setOnClickListener(this);
		mSignUpOnPhoneBtn.setOnClickListener(this);
		
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mTourism = (Tourism) intent.getSerializableExtra("tourism");
		if(mTourism.getType() == 2) {
			mSignUpOnlineBtn.setVisibility(View.GONE);
			mSignUpOnPhoneBtn.setVisibility(View.GONE);
		}
		String url = LefuApi.getAbsoluteApiUrl("lefuyun/productCtr/toPageInfo");
		url = url + "?id=" + mTourism.getId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("User-Agent", mWebView.getSettings().getUserAgentString());
		// 防止打开系统默认浏览器
		mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {}
            
            @Override
            public void onPageFinished(WebView view, String url) {}
        });
		mWebView.loadUrl(url, map);
		mList = new ArrayList<String>();
		Collections.addAll(mList, mTourism.getIntroduction_img().split(";"));
		mAdapter = new ShowPicturesAdapter(getApplicationContext(), mList);
		mViewPager.setAdapter(mAdapter);
		circleIndicator.setViewPager(mViewPager);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_action_bar:
			finish();
			break;
		case R.id.share_action_bar:
			// 分享当前二维码
//			String url = LefuApi.getAbsoluteApiUrl("lefuyun/newsCenterCtr/toInfoPage");
//			url = url + "?id=" + mNew.getId();
//			String imgUrl = LefuApi.IMG_URL + mNew.getPicture();
//			LefuApi.sharePage(this, mNew.getTheme(), mNew.getHeadline(), imgUrl, url, true);
			break;
		case R.id.signup_online_tourism_details_activity:
			// 在线预约
			Intent intent = new Intent(this, ReserveTourism.class);
			intent.putExtra("tourism", mTourism);
			startActivity(intent);
			break;
		case R.id.signup_onphone_tourism_details_activity:
			// 电话预约
			Intent intentT = new Intent(Intent.ACTION_DIAL);
			Uri data = Uri.parse("tel:" + mTourism.getPhone());
			intentT.setData(data);
			startActivity(intentT);
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	} 

}
