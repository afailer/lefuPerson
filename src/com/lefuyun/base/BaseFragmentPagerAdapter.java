package com.lefuyun.base;

import java.util.ArrayList;
import java.util.List;

import com.lefuyun.R;
import com.lefuyun.bean.ViewPageInfo;
import com.lefuyun.widget.PagerSlidingTabStrip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private Context mContext;
	private ViewPager mViewPager;
	private PagerSlidingTabStrip mPagerStrip;
	private List<ViewPageInfo> mTabs = new ArrayList<ViewPageInfo>();
	List<Fragment> fs=new ArrayList<Fragment>();
	public BaseFragmentPagerAdapter(FragmentManager fm, 
			PagerSlidingTabStrip pageStrip, ViewPager pager) {
		super(fm);
		mContext = pager.getContext();
        mPagerStrip = pageStrip;
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mPagerStrip.setViewPager(mViewPager);
	}
	
	public void addTab(String title, Class<?> clss, Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, clss, args);
        addFragment(viewPageInfo);
    }
	
	private void addFragment(ViewPageInfo info) {
        if (info == null) {
            return;
        }
        // 加入tab title
        View v = LayoutInflater.from(mContext).inflate(
                R.layout.base_viewpage_fragment_tab_item, null, false);
        TextView title = (TextView) v.findViewById(R.id.tab_title);
        title.setText(info.title);
        mPagerStrip.addTab(v);
        mTabs.add(info);
        fs.add(Fragment.instantiate(mContext, info.clss.getName(), info.args));
        notifyDataSetChanged();
    }

	@Override
	public Fragment getItem(int arg0) {
		//ViewPageInfo pageInfo = mTabs.get(arg0);
		return fs.get(arg0); 
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mTabs.get(position).title;
	}

}
