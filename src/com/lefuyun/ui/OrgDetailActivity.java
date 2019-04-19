package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonPagerAdapter;
import com.lefuyun.bean.OrgActive;
import com.lefuyun.interf.ScrollViewListener;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.util.WindowUtil;
import com.lefuyun.widget.MyScrollView;
import com.lefuyun.widget.PeriscopeLayout;
import com.lefuyun.widget.circleindicator.CircleIndicator;
import com.nineoldandroids.animation.ObjectAnimator;

public class OrgDetailActivity extends BaseActivity {

	MyScrollView mScrollView;
	// 渐变的高度
	private static final float HEIGHT = 560;
	// 当前actionBar的透明度
	private float mRatio;
	// actionBar的背景颜色控件
	private View mBackground;
	// actionBar的返回键
	private ImageView mBackBtn;
	private WebView mWebView;
	private ViewPager mViewPager;
	CircleIndicator circleIndicator;
	private OrgActive orgActive;
	NurseAdapter mAdapter;
	RelativeLayout zanH5,shareH5;
	String url;
	PeriscopeLayout textView;
	private int num;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_action_bar:
			finish();
			break;
		case R.id.zan_h5:
			LefuApi.parseOrgActive(orgActive.getId(), new RequestCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					num++;
					textView.addHeart();
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
			break;
		case R.id.share_h5:
			LefuApi.sharePage(getApplicationContext(), orgActive.getTheme(), orgActive.getReserved(), LefuApi.getAbsoluteApiUrl(orgActive.getPic()), url, true);
			break;
		default:
			break;
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_h5_detail;
	}

	@Override
	protected void initView() {
		setViewFillWindow(true);
		Intent intent = getIntent();
		orgActive = (OrgActive) intent.getSerializableExtra("OrgActive");
		textView=(PeriscopeLayout) findViewById(R.id.tv_one);
		LayoutParams params = textView.getLayoutParams();
		params.width = WindowUtil.getWindowWidth(this) / 2;
		textView.setLayoutParams(params);
		zanH5=(RelativeLayout) findViewById(R.id.zan_h5);
		shareH5=(RelativeLayout) findViewById(R.id.share_h5);
		zanH5.setOnClickListener(this);
		shareH5.setOnClickListener(this);
		initCurrentActionBar();
		mWebView = (WebView) findViewById(R.id.nurse_detail_webview);
		initScrollView();
		mAdapter = new NurseAdapter(getApplicationContext(), datas, R.layout.pic_detail_item);
		mViewPager = (ViewPager) findViewById(R.id.nurse_detail_pager);
		mViewPager.setAdapter(mAdapter);
		circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
		circleIndicator.setViewPager(mViewPager);
	}

	@Override
	protected void initData() {
		url= LefuApi.getAbsoluteApiUrl("lefuyun/agencyActivites/toInfoPage");
		url = url + "?id=" + orgActive.getId()+"&titlehight=0&version="+UserInfo.getVersion();
		LogUtil.e("orgUrl", url);
		// 防止打开系统默认浏览器
		mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
		mWebView.loadUrl(url,Utils.getWebViewHeaderMap(mWebView));
		
		initViewPagerData();
	}
	List<String> datas=new ArrayList<String>();
	/**
	 * 初始化ViewPager数据
	 */
	private void initViewPagerData() {
		LefuApi.queryAgencyActivitePictures(orgActive.getId(), new RequestCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				try{
				mAdapter = new NurseAdapter(getApplicationContext(), result, R.layout.pic_detail_item);
				mViewPager.setAdapter(mAdapter);
				circleIndicator.notifyDataSetChanged();
				}catch(Exception e){}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
			}
		});
	}
	
	
	class NurseAdapter extends CommonPagerAdapter<String>{

		public NurseAdapter(Context context, List<String> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void processItem(View view, String t, int position) {
			ImageView imgTitle = (ImageView) view.findViewById(R.id.pic_detail);
			final Intent intent=new Intent(getApplicationContext(), PhotoViewActivity.class);
			intent.putExtra("path", t);
			imgTitle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					startActivity(intent);
				}
			});
			ImageLoader.loadImg(t, imgTitle);
		}

		
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
	}
	
	/**
	 * 初始化ScrollView
	 */
	private void initScrollView() {
		mScrollView=(MyScrollView) findViewById(R.id.nurse_h5_scroll);
		mScrollView.setScrollViewListener(new ScrollViewListener() {
			
			@Override
			public void onScrollChanged(int x, int y, int oldx, int oldy) {
				// 根据ScrollView滑动的距离设置actionBar的透明度
				if(oldy <= HEIGHT && oldy >= 0) {
					mRatio = (float) oldy / HEIGHT;
					ObjectAnimator.ofFloat(mBackground, "alpha", mRatio).setDuration(0).start();
					mBackBtn.setImageResource(R.drawable.back_round_main_color);
				}else if(oldy > 560) {
					ObjectAnimator.ofFloat(mBackground, "alpha", 1).setDuration(0).start();
					mBackBtn.setImageResource(R.drawable.back_round_gray);
				}else if(oldy < 0) {
					ObjectAnimator.ofFloat(mBackground, "alpha", 0).setDuration(0).start();
					mBackBtn.setImageResource(R.drawable.back_round_main_color);
				}
				
			}
		});
		
	}
	
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("num", num);
		setResult(200, data);
		super.finish();
	}

}
