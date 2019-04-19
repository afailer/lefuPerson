package com.lefuyun.ui;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.ObtainOutHistoryPagerAdapter;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.fragment.ObtainOutHistoryFragment;
import com.lefuyun.util.StringUtils;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.PagerSlidingTabStrip;
import com.lefuyun.widget.PagerSlidingTabStrip.OnPagerChangeLis;
import com.lefuyun.widget.pickerview.TimePickerView;
import com.lefuyun.widget.pickerview.TimePickerView.OnTimeSelectListener;

public class ObtainOutHistoryActivity extends BaseActivity {
	
	// 当前绑定老人的姓名
	private TextView mElderNameView, mLastTimeView;
	// 老人头像
	private CircleImageView mElderImg;
	
	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	private OldPeople mOldPeople;
	
	// 时间选择控件
	private TimePickerView mTimePickerView;
	
	private int mCurrentPage;
	
	private ObtainOutHistoryPagerAdapter mAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_obtain_out_history;
	}

	@Override
	protected void initView() {
		setActionBarTitle("历史记录");
		mElderImg = (CircleImageView) findViewById(R.id.img_elder_obtain_out_history_activity);
		mElderNameView = (TextView) findViewById(R.id.name_elder_obtain_out_history_activity);
		mLastTimeView = (TextView) findViewById(R.id.time_elder_obtain_out_history_activity);
		mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);
		mViewPager = (ViewPager) findViewById(R.id.pager);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
		// 设置当前老人信息
		ImageLoader.loadImg(mOldPeople.getIcon(), mElderImg);
		mElderNameView.setText(mOldPeople.getElderly_name());
		
		mAdapter = new ObtainOutHistoryPagerAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
		mAdapter.addTab("进行中", ObtainOutHistoryFragment.class, getBundle(1));
		mAdapter.addTab("已完成", ObtainOutHistoryFragment.class, getBundle(3));
		mTabStrip.setOnPagerChange(new OnPagerChangeLis() {
			
			@Override
			public void onChanged(int page) {
				// 获取当前的页面号
				mCurrentPage = page;
			}
		});
		
		initTimePickerView();
	}
	/**
	 * 初始化时间控件
	 */
	private void initTimePickerView() {
		// 时间选择器
		mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		// 控制时间范围
		mTimePickerView.setTime(new Date());
		mTimePickerView.setCyclic(false);
		mTimePickerView.setCancelable(true);
		mTimePickerView.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				refreshData(date);
			}
		});
		
	}
	/**
	 * 根据日期选择刷新数据
	 */
	protected void refreshData(Date date) {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		for (Fragment fragment : fragments) {
			ObtainOutHistoryFragment frag = (ObtainOutHistoryFragment) fragment;
			frag.refreshData(date, mCurrentPage == 0 ? 1 : 3);
		}
	}

	private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(ObtainOutHistoryFragment.BUNDLE_OBTAIN_TYPE, newType);
        return bundle;
    }
	/**
	 * 获取老人信息
	 * @return
	 */
	public OldPeople getOldPeople() {
		return mOldPeople;
	}
	/**
	 * 设置最后一次离院时间
	 * @param text
	 */
	public void setLastTimeOut(long time) {
		if(mLastTimeView != null) {
			if(time > 0) {
				String content = "最后一次外出时间: " + StringUtils.friendly_time(time);
				mLastTimeView.setText(content);
			}else {
				mLastTimeView.setText("");
			}
		}
	}
	
	@Override
	public void onClick(View v) {
	}

	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	protected boolean hasRightImageView() {
		return true;
	}
	
	@Override
	protected int getActionBarRightImage() {
		return R.drawable.calendar;
	}
	
	@Override
	public void onRightViewClick(View view) {
		// 弹出日期选择窗口
		mTimePickerView.show();
	}
}
