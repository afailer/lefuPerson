package com.lefuyun.adapter;

import java.util.List;

import com.lefuyun.bean.ImportantNotice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ImportantHistoryAdapter extends FragmentPagerAdapter{

	public ImportantHistoryAdapter(FragmentManager fm,List<ImportantNotice> notices) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
