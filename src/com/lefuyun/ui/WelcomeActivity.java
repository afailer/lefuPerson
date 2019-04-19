package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.WelcomeActivityAdapter;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.WindowUtil;
import com.lefuyun.widget.circleindicator.CircleIndicator;
import com.lefuyun.widget.circleindicator.OnPageSelectedListener;

public class WelcomeActivity extends BaseActivity {
	
	private ViewPager mViewPager;
	private List<ImageView> mList;
	private WelcomeActivityAdapter mAdapter;
	private TextView mButton;
	private CircleIndicator circleIndicator;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_welcome;
	}

	@Override
	protected void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vertical_pager_welcome_activity);
		mButton = (TextView) findViewById(R.id.btn_welcome_activity);
		mButton.setOnClickListener(this);
		mButton.setVisibility(View.GONE);
		circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
	}

	@Override
	protected void initData() {
		mList = new ArrayList<ImageView>();
		//创建Iamgview对象
		ImageView imageView0 = new ImageView(getApplicationContext());
		imageView0.setBackgroundResource(R.drawable.first_start_page1);
		ImageView imageView1 = new ImageView(getApplicationContext());
		imageView1.setBackgroundResource(R.drawable.first_start_page2);
		ImageView imageView2 = new ImageView(getApplicationContext());
		imageView2.setBackgroundResource(R.drawable.first_start_page3);
		ImageView imageView3 = new ImageView(getApplicationContext());
		imageView3.setBackgroundResource(R.drawable.first_start_page4);
		//添加到集合中
		mList.add(imageView0);
		mList.add(imageView1);
		mList.add(imageView2);
		mList.add(imageView3);
		mAdapter = new WelcomeActivityAdapter(this, mList);
		mViewPager.setAdapter(mAdapter);
		circleIndicator.setViewPager(mViewPager);
		circleIndicator.setOnPageSelectedLisrener(new OnPageSelectedListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position == mList.size() - 1) {
					mButton.setVisibility(View.VISIBLE);
				}else {
					mButton.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_welcome_activity) {
			String versionName = WindowUtil.getVersionName(this);
			SPUtils.put(this, "isFirst", versionName);
			Intent intent = new Intent(this, SwitchActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
