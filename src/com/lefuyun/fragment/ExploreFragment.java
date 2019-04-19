package com.lefuyun.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lefuyun.R;
import com.lefuyun.base.BaseFragment;
import com.lefuyun.base.BaseFragmentPagerAdapter;
import com.lefuyun.bean.City;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.PagerSlidingTabStrip;

public class ExploreFragment extends BaseFragment {
	
	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	
	static private BaseFragmentPagerAdapter mAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_explore;
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pager_tabstrip_explore_fragment);
		mViewPager = (ViewPager) view.findViewById(R.id.pager_explore_fragment);
	
		
	}
	
	public static void OnResult(City city){
		/*//City city = (City) data.getSerializableExtra("city");
		//LogUtil.e("ExploreResult", requestCode+" "+resultCode+city.getRegion_name());
		List<Fragment> fragments = getChildFragmentManager().getFragments();
		OrganizationFragment oFragment = null;
		//TLog.log("当前选中的城市city==" + city);
		if(fragments != null) {
		    for(Fragment fragment : fragments) {
		    	if(fragment instanceof OrganizationFragment) {
		    		oFragment = (OrganizationFragment) fragment;
		    		TLog.log("当前选中的城市oFragment==" + oFragment);
		        	break;
		    	}
		    }
		}*/
		OrganizationFragment oFragment = (OrganizationFragment) mAdapter.getItem(1);
		oFragment.setCurrentCity(city);
	}
	
	
/*	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.e("ExploreResult", requestCode+" "+resultCode);
		if(resultCode == 1000) {
			City city = (City) data.getSerializableExtra("city");
			LogUtil.e("ExploreResult", requestCode+" "+resultCode+city.getRegion_name());
			List<Fragment> fragments = getChildFragmentManager().getFragments();
			OrganizationFragment oFragment = null;
			TLog.log("当前选中的城市city==" + city);
			if(fragments != null) {
			    for(Fragment fragment : fragments) {
			    	if(fragment instanceof OrganizationFragment) {
			    		oFragment = (OrganizationFragment) fragment;
			    		TLog.log("当前选中的城市oFragment==" + oFragment);
			        	break;
			    	}
			    }
			}
			oFragment.setCurrentCity(city);
		}
	}*/

	@Override
	protected void initData() {
		mAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mTabStrip, mViewPager);
		mAdapter.addTab("旅游", TourismFragment.class, new Bundle());
//		mAdapter.addTab("机构", OrganizationFragment.class, new Bundle());
	}
	
	@Override
	public void onClick(View v) { }
	
}
