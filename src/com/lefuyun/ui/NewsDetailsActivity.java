package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
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
import com.lefuyun.bean.News;
import com.lefuyun.interf.ScrollViewListener;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.MyScrollView;
import com.lefuyun.widget.circleindicator.CircleIndicator;
import com.nineoldandroids.animation.ObjectAnimator;

public class NewsDetailsActivity extends BaseActivity {
	
	// 渐变的高度
	private static final float HEIGHT = 560;
	private String format = "yyyy-MM-dd HH:mm";
	// actionBar的背景颜色控件
	private View mBackground;
	// actionBar的返回键
	private ImageView mBackBtn, mShareBtn;
	// 当前actionBar的透明度
	private float mRatio;
	
	private TextView mTitleView, mDateView, mTimesView;
	
	private ViewPager mViewPager;
	
	private News mNew;
	private WebView mWebView;
	private MyScrollView mScrollView;
	private CircleIndicator circleIndicator;
	private ArrayList<String> mList;
	private ShowPicturesAdapter mAdapter;
    int id;//新闻id
    int news_id;
    private Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
      		if(msg.what==1){
    			LefuApi.getNewsDetailById(id, new RequestCallback<News>() {

    				@Override
    				public void onSuccess(News result) {
    					mNew=result;
    					mTitleView.setText(mNew.getTheme());
    					mDateView.setText(StringUtils.getFormatData(mNew.getCreate_dt(), format));
    					mTimesView.setText(mNew.getRead_number() + "次阅读量");
    				}

    				@Override
    				public void onFailure(ApiHttpException e) {
    					
    				}
    			});	
    		}else if(msg.what==2){
    			mTitleView.setText(mNew.getTheme());
    			mDateView.setText(StringUtils.getFormatData(mNew.getCreate_dt(), format));
    			mTimesView.setText(mNew.getRead_number() + "次阅读量");
    		}
    	};
    };
	@Override
	protected int getLayoutId() {
		return R.layout.activity_news_details;
	}

	@Override
	protected void initView() {
		
		initCurrentActionBar();
		
		mTitleView = (TextView) findViewById(R.id.title_news_details_activity);
		mDateView = (TextView) findViewById(R.id.date_news_details_activity);
		mTimesView = (TextView) findViewById(R.id.times_news_details_activity);
		
		// 初始化WebView
		mWebView = (WebView) findViewById(R.id.web_news_details_activity);
		WebSettings settings = mWebView.getSettings();
		String userAgentString = settings.getUserAgentString();
		if(!userAgentString.contains("lefuAppP")){
			userAgentString+=UserInfo.getUserAgent();
		}
		settings.setUserAgentString(userAgentString);
		
		initScrollView();
		
		mViewPager = (ViewPager) findViewById(R.id.vp_news_details_activity);
		circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
		
	}
	/**
	 * 初始化ScrollView
	 */
	private void initScrollView() {
		mScrollView = (MyScrollView) findViewById(R.id.sv_news_details_activity);
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

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mNew = (News) intent.getSerializableExtra("new");
		id=intent.getIntExtra("id", 0);
		if(mNew!=null){
			news_id=(int) mNew.getId();
		}else{
			news_id=id;
		}
		String url = LefuApi.getAbsoluteApiUrl("lefuyun/newsCenterCtr/toInfoPage");
		url = url + "?id=" +news_id;
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
		mAdapter = new ShowPicturesAdapter(getApplicationContext(), mList);
		mViewPager.setAdapter(mAdapter);
		circleIndicator.setViewPager(mViewPager);
		initViewPagerData();
	}
	/**
	 * 初始化ViewPager数据
	 */
	private void initViewPagerData() {
		LefuApi.getNewsPicture(news_id, new RequestCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				TLog.log("当前列表中的所有的图片 == " + result);
				mList.clear();
				if(result != null && result.size() > 0) {
					mList.addAll(result);
					circleIndicator.notifyDataSetChanged();
					//从通知跳过来的
					if(id!=0){
						handler.sendEmptyMessage(1);
					}else{
						handler.sendEmptyMessage(2);
					}
				}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_action_bar:
			finish();
			break;
		case R.id.share_action_bar:
			// 分享当前二维码
			String url = LefuApi.getAbsoluteApiUrl("lefuyun/newsCenterCtr/toInfoPage");
			url = url + "?id=" + mNew.getId();
			String imgUrl = LefuApi.IMG_URL + mNew.getPicture();
			LefuApi.sharePage(this, mNew.getTheme(), mNew.getHeadline(), imgUrl, url, true);
			break;

		default:
			break;
		}
	}

}
