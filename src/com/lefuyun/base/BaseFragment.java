package com.lefuyun.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.lefuyun.R;
import com.lefuyun.interf.DialogControl;
import com.lefuyun.util.DialogHelper;
import com.lefuyun.widget.WaitDialog;
import com.lefuyun.widget.dialog.ConfirmDialogFragment;

public abstract class BaseFragment extends Fragment implements OnClickListener{
	
	protected BaseActivity mActivity;
	protected LayoutInflater mInflater;
	private View rootView;
	
	protected abstract int getLayoutId();
	protected abstract void initView(View view, Bundle savedInstanceState);
	protected abstract void initData();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof DialogControl && activity instanceof BaseActivity) {
			mActivity = (BaseActivity) activity;
        }else {
        	throw new ClassCastException("your activity must extends BaseActivity");
        }
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		if(rootView == null) {
			rootView = inflater.inflate(getLayoutId(), container, false);
			initView(rootView, savedInstanceState);
			initData();
		}else {
			ViewGroup parent = (ViewGroup)rootView.getParent();
			if(parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}
	
	protected void hideWaitDialog() {
        mActivity.hideWaitDialog();
    }

    protected WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected WaitDialog showWaitDialog(int resid) {
        return mActivity.showWaitDialog(resid);
    }

    protected WaitDialog showWaitDialog(String resid) {
        return mActivity.showWaitDialog(resid);
    }
    
    /**
	 * 显示提示框
	 * @param title 标题
	 * @param message 内容
	 * @param l
	 */
	public void showConfirmDialog(String title, String message, ConfirmDialogFragment.OnClickListener l) {
		DialogHelper.getConfirmDialog(mActivity, title, message, l);
	}
    
    public void showToast(int resId) {
    	mActivity.showToast(resId);
    }
	
	public void showToast(String text) {
		mActivity.showToast(text);
	}

}
