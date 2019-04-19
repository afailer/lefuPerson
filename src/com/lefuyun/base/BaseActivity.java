package com.lefuyun.base;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.interf.ActionBarControl;
import com.lefuyun.interf.DialogControl;
import com.lefuyun.util.DialogHelper;
import com.lefuyun.util.TLog;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.widget.ActionBarModel;
import com.lefuyun.widget.WaitDialog;
import com.lefuyun.widget.dialog.ConfirmDialogFragment;

public abstract class BaseActivity extends FragmentActivity implements
		DialogControl, OnClickListener, ActionBarControl {

	protected LayoutInflater mInflater;
	// WaitDialog是否可见
	protected boolean isVisible;
	public WaitDialog mWaitDialog;
	private ActionBarModel mActionBar;
	private View mView;
	private LinearLayout mLinearLayout;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isVisible = true;
//		WindowUtil.cancelWindow(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mInflater = getLayoutInflater();
		onBeforeSetContentLayout();
		if(getLayoutId() != 0) {
			// 将用户的布局文件转换成View对象
			mView = mInflater.inflate(getLayoutId(), null);
		}else {
			throw new NullPointerException("please set your LayoutId!");
		}
		// 先判断是否要添加标题栏
		if (hasActionBar()) {
			// 要添加标题栏
			mLinearLayout = new LinearLayout(this);
			mLinearLayout.setOrientation(LinearLayout.VERTICAL);
			// 设置状态栏颜色
//			mLinearLayout.setFitsSystemWindows(true);
//			mLinearLayout.setBackground(mView.getBackground());
			// 初始化标题栏
			initActionBar(mLinearLayout);
			// 添加用户布局
			mLinearLayout.addView(mView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			setContentView(mLinearLayout, params);
		} else {
//			mView.setFitsSystemWindows(true);
			// 设置状态栏颜色
			setContentView(mView);
		}

		init(savedInstanceState);
		initView();
		initData();
		//把每一个activity都加入到一个集合中
		AppContext.getInstance().addActivity(this);
	}
	

	protected abstract int getLayoutId();
	
	protected abstract void initView();

	protected abstract void initData();

	protected void init(Bundle savedInstanceState) {
	}
	/**
	 * 设置布局前调用此方法
	 */
	protected void onBeforeSetContentLayout() {
	}

	/**
	 * 初始化ActionBar条目
	 */
	private void initActionBar(LinearLayout linearLayout) {
		mActionBar = new ActionBarModel(this, mInflater, linearLayout);
		mActionBar.setTitle(getActionBarTitle());
		if(hasBackButton()) {
			// 设置返回按钮
			mActionBar.setBackButton();
		}
		if(hasRightImageView()) {
			mActionBar.setRightImageView(getActionBarRightImage());
			mActionBar.setActionBarControl(this);
		}else if(hasRightTextColor()){
			mActionBar.setRightTextView(getActionBarRightText(),getRightTextColor());
			mActionBar.setActionBarControl(this);
		}else {
			mActionBar.setRightTextView(getActionBarRightText());
			mActionBar.setActionBarControl(this);
		}
		
		linearLayout.addView(mActionBar.getView());
	}
	/**
	 * 当不设置actionBar是,是否设置StatusBar
	 * @return
	 */
	protected boolean hasStatusBar() {
		return false;
	}
	/**
	 * 是否设置ActionBar
	 * @return
	 */
	protected boolean hasActionBar() {
		return false;
	}
	/**
	 * 是否设置回退按钮
	 * @return
	 */
	protected boolean hasBackButton() {
		return false;
	}
	/**
	 * 右面的文字是否有颜色
	 * @return
	 */
	protected boolean hasRightTextColor(){
		return false;
	}
	/**
	 * 是否设置右侧图标, 如果返回true,右边控件是一个图片,需要复写getActionBarRightImage()方法
	 * 如果右边是文档可以不复写本方法,但是要复写getActionBarRightText()方法
	 * @return
	 */
	protected boolean hasRightImageView() {
		return false;
	}
	
	protected String getRightTextColor(){
		return "";
	}
	
	/**
	 * 设置右侧图标
	 * @return
	 */
	protected int getActionBarRightImage() {
		return 0;
	}
	/**
	 * 设置右侧标题
	 * @return
	 */
	protected String getActionBarRightText() {
		return "";
	}
	/**
	 * actionBar右侧图标的点击事件
	 */
	@Override
	public void onRightViewClick(View view) {
	}

	/**
	 * 获取标题,子类可重写此方法
	 * 
	 * @return
	 */
	protected String getActionBarTitle() {
		return "";
	}
	/**
	 * 调用此方法需要复写hasActionBar()方法,并返回true
	 * @param title
	 */
	protected void setActionBarTitle(String title) {
		if(mActionBar == null) {
			return;
		}
		mActionBar.setTitle(title);
	}
	/**
	 * 设置actionBar的背景颜色
	 * @param color
	 */
	protected void setActionBarBackground(int color) {
		if(mActionBar == null) {
			return;
		}
		mActionBar.getView().setBackgroundColor(color);
	}
	/**
	 * 是否填充整个屏幕
	 * @param isDisplay
	 */
	@SuppressLint("NewApi")
	public void setViewFillWindow(boolean isDisplay) {
//		if (hasActionBar()) {
//			// 要添加标题栏
//			mLinearLayout.setFitsSystemWindows(!isDisplay);
//		} else {
//			mView.setFitsSystemWindows(!isDisplay);
//		}
	}
	/**
	 * 设置状态栏的颜色
	 * @param colorResId
	 */
	public void setStatusBarResource(int colorResId) {
		if(mLinearLayout != null) {
			mLinearLayout.setBackgroundResource(colorResId);
		}
	}
	/**
	 * 设置状态栏的颜色
	 * @param drawable
	 */
	@SuppressWarnings("deprecation")
	public void setStatusBarDrawable(Drawable drawable) {
		if(mLinearLayout != null) {
			mLinearLayout.setBackgroundDrawable(drawable);
		}
	}
	/**
	 * 设置状态栏的颜色
	 * @param color
	 */
	public void setStatusBarColor(int color) {
		if(mLinearLayout != null) {
			mLinearLayout.setBackgroundColor(color);
		}
	}


	@Override
	public void hideWaitDialog() {
		TLog.log("dialog被影藏了!!!" + getClass().getName());
		if (isVisible && mWaitDialog != null) {
			try {
				mWaitDialog.dismiss();
				mWaitDialog = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public WaitDialog showWaitDialog() {
		TLog.log("dialog显示了!!!" + getClass().getName());
		return showWaitDialog(R.string.loading);
	}

	@Override
	public WaitDialog showWaitDialog(int resid) {
		return showWaitDialog(getString(resid));
	}

	@Override
	public WaitDialog showWaitDialog(String message) {
		if (isVisible) {
			if (mWaitDialog == null) {
				mWaitDialog = DialogHelper.getWaitDialog(this, message);
			}
			if (mWaitDialog != null) {
				mWaitDialog.setMessage(message);
				mWaitDialog.show();
			}
			return mWaitDialog;
		}
		return null;
	}
	/**
	 * 显示提示框
	 * @param title 标题
	 * @param message 内容
	 * @param l
	 */
	public void showConfirmDialog(String title, String message, ConfirmDialogFragment.OnClickListener l) {
		DialogHelper.getConfirmDialog(this, title, message, l);
	}

	public void showToast(int resId) {
		ToastUtils.show(this, resId);
	}

	public void showToast(String text) {
		ToastUtils.show(this, text);
	}
}
