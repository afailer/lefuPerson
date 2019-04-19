package com.lefuyun.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.base.BaseFragment;
import com.lefuyun.base.BaseFragmentPagerAdapter;
import com.lefuyun.widget.PagerSlidingTabStrip;

public class NewsFragment extends BaseFragment {
	
	private TextView mTitle;
	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	
	private BaseFragmentPagerAdapter mAdapter;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_news;
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mTitle = (TextView) view.findViewById(R.id.title_action_bar);
		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pager_tabstrip_news_fragment);
		mViewPager = (ViewPager) view.findViewById(R.id.pager_news_fragment);
	}

	@Override
	protected void initData() {
		mTitle.setText("新闻中心");
		mAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mTabStrip, mViewPager);
		mAdapter.addTab("全部", NewsDetailsFragment.class, getBundle(0));
		mAdapter.addTab("养老新闻", NewsDetailsFragment.class, getBundle(1));
		mAdapter.addTab("养生课堂", NewsDetailsFragment.class, getBundle(2));
	}
	
	private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(NewsDetailsFragment.BUNDLE_NEWS_TYPE, newType);
        return bundle;
    }

	@Override
	public void onClick(View v) {
		
	}
	
}
