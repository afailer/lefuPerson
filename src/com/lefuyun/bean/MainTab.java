package com.lefuyun.bean;

import com.lefuyun.R;
import com.lefuyun.fragment.ExploreFragment;
import com.lefuyun.fragment.LefuFragment;
import com.lefuyun.fragment.MeFragment;
import com.lefuyun.fragment.NewsFragment;

public enum MainTab {

	LEFU(0, R.string.main_tab_name_lefu, R.drawable.tab_icon_lefu,
			LefuFragment.class),

	EXPLORE(1, R.string.main_tab_name_explore, R.drawable.tab_icon_explore,
			ExploreFragment.class),

	NEWS(2, R.string.main_tab_name_news, R.drawable.tab_icon_news,
			NewsFragment.class),

	ME(3, R.string.main_tab_name_my, R.drawable.tab_icon_me,
			MeFragment.class);

	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
