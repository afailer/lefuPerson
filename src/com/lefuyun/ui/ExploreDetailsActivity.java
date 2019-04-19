package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.ShowPicturesAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.Organization;
import com.lefuyun.interf.ScrollViewListener;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.MyScrollView;
import com.lefuyun.widget.circleindicator.CircleIndicator;
import com.nineoldandroids.animation.ObjectAnimator;

public class ExploreDetailsActivity extends BaseActivity {
	
	// 渐变的高度
	private static final float HEIGHT = 560;
	// actionBar的背景颜色控件
	private View mBackground;
	// 当前actionBar的透明度
	private float mRatio;
	
	private ViewPager mViewPager;
	// 本机构总床位数、剩余床位数以及机构位置的数据展示控件
	private TextView mBedTotal, mBedResidual, mLocation;
	private TextView mTitleView, mAddressView;
	// 跳转到机构活动页面按钮,以及预约本机构按钮
	private TextView mEventBtn, mApplyBtn;
	// actionBar的返回键,分享按钮
	private ImageView mBackBtn, mShareBtn;
	
	private Organization mOrganization;
	private long mOrganizationId;
	
	private WebView mWebView;
	private MyScrollView mScrollView;
	private CircleIndicator circleIndicator;
	// 存放所有当前机构展示的图片
	private List<String> mList;
	private ShowPicturesAdapter mAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_explore_details;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initView() {
		initCurrentActionBar();
		
		mTitleView = (TextView) findViewById(R.id.title_explore_details_activity);
		mAddressView = (TextView) findViewById(R.id.address_explore_details_activity);
		
		// 初始化WebView
		mWebView = (WebView) findViewById(R.id.web_explore_details_activity);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		String userAgentString = settings.getUserAgentString();
		if(!userAgentString.contains("lefuAppP")){
			userAgentString += UserInfo.getUserAgent();
		}
		settings.setUserAgentString(userAgentString);
		
		initScrollView();
		
		mViewPager = (ViewPager) findViewById(R.id.vp_explore_details_activity);
		circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
		
		mBedTotal = (TextView) findViewById(R.id.bed_total_explore_details_activity);
		mBedResidual = (TextView) findViewById(R.id.residual_bed_explore_details_activity);
		mLocation = (TextView) findViewById(R.id.location_explore_details_activity);
		
		mEventBtn = (TextView) findViewById(R.id.event_explore_details_activity);
		mApplyBtn = (TextView) findViewById(R.id.apply_explore_details_activity);
		mEventBtn.setOnClickListener(this);
		mApplyBtn.setOnClickListener(this);
	}
	/**
	 * 初始化ScrollView
	 */
	private void initScrollView() {
		mScrollView = (MyScrollView) findViewById(R.id.sv_explore_details_activity);
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
	 * 初始化状态栏
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
		mShareBtn.setOnClickListener(this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOrganization = (Organization) intent.getSerializableExtra("organization");
		mOrganizationId = intent.getLongExtra("organizationId", 0);
		long id = 0;
		if(mOrganization != null && mOrganizationId == 0) {
			// 从机构列表传递过来的数据
			id = mOrganization.getAgency_id();
			setOrganizationInfo();
		}
		else if(mOrganization == null && mOrganizationId > 0) {
			// 从LefuFragment页面跳转过来的
			id = mOrganizationId;
			getOrganization();
		}else {
			return;
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
		// 加载webView信息
		String url = LefuApi.getAbsoluteApiUrl("lefuyun/agencyIntroCtr/toInfoPage");
		url = url + "?id=" + id + "&version=" + UserInfo.getVersion();
		Map<String, String> map = new HashMap<String, String>();
		map.put("User-Agent", mWebView.getSettings().getUserAgentString());
		// 防止打开系统默认浏览器
		mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
		
		mWebView.loadUrl(url, map);
		mList = new ArrayList<String>();
		mAdapter = new ShowPicturesAdapter(getApplicationContext(), mList);
		mViewPager.setAdapter(mAdapter);
		circleIndicator.setViewPager(mViewPager);
		initViewPagerData(id);
	}
	
	/**
	 * js返回按钮的点击事件
	 */
	private class ClickInterface {
		
		@JavascriptInterface
		public void comeHere() {
			// 跳转到机构所在地图上页面
			Intent intent = new Intent(ExploreDetailsActivity.this, CurrentCitiesMapActivity.class);
			intent.putExtra("organization", mOrganization);
			startActivity(intent);
		}
	}
	/**
	 * 初始化ViewPager数据
	 */
	private void initViewPagerData(long id) {
		LefuApi.getOrganizationPicture(id, new RequestCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				TLog.log("当前列表中的所有的图片 == " + result);
				mList.clear();
				if(result != null && result.size() > 0) {
					mList.addAll(result);
				}
				circleIndicator.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
			}
		});
	}
	
	/**
	 * 设置机构的总床位数,剩余数以及机构地理位置
	 */
	private void setOrganizationInfo() {
		mBedTotal.setText(mOrganization.getBed_total() + "");
		mBedResidual.setText(mOrganization.getAgency_property_text() + "");
		mLocation.setText(mOrganization.getRegion_name());
		mTitleView.setText(mOrganization.getAgency_name());
		mAddressView.setText(mOrganization.getAddress());
	}
	/**
	 * 从网络中拿取机构信息
	 */
	private void getOrganization() {
		if(mOrganizationId > 0) {
			LefuApi.getOrganization(mOrganizationId, new RequestCallback<Organization>() {
				
				@Override
				public void onSuccess(Organization result) {
					TLog.log("Organization result == " + result);
					if(result != null) {
						mOrganization = result;
						setOrganizationInfo();
					}
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					showToast(e.getMessage());
					
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back_action_bar:
			finish();
			break;
		case R.id.event_explore_details_activity:
			// 跳转机构活动页面
			intent = new Intent(this, OrgActivity.class);
			intent.putExtra("id", mOrganization.getAgency_id());
			intent.putExtra("name", mOrganization.getAgency_name());
			startActivity(intent);
			break;
		case R.id.apply_explore_details_activity:
			// 跳转机构预约页面
			intent = new Intent(this, ReserveOrganization.class);
			intent.putExtra("organization", mOrganization);
			startActivity(intent);
			break;
		case R.id.share_action_bar:
			// 分享当前二维码
			String url = LefuApi.getAbsoluteApiUrl("lefuyun/agencyIntroCtr/toInfoPage");
			url = url + "?id=" + mOrganization.getAgency_id() + "&version=" + UserInfo.getVersion();
			String imgUrl = LefuApi.IMG_URL + mOrganization.getExterior_pic();
			LefuApi.sharePage(this, mOrganization.getAgency_name(), mOrganization.getRemark(), imgUrl, url, true);
			break;

		default:
			break;
		}
	}

}
