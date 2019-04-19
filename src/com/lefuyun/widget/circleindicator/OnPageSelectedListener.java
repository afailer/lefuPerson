package com.lefuyun.widget.circleindicator;

public interface OnPageSelectedListener {
	
	/**
	 * 当前某一页面被选中时调用
	 * @param position 被选中的位置
	 */
	public void onPageSelected(int position);
	/**
	 * 页面滑动过程中被调用
	 * @param position 当前滑动开始的位置
	 * @param positionOffset 当前滑动距离开始位置偏移的距离
	 * @param positionOffsetPixels
	 */
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels);

	/**
	 * 当前页面滚动状态发生变化时调用
	 * @param arg0
	 */
	public void onPageScrollStateChanged(int arg0);

}
