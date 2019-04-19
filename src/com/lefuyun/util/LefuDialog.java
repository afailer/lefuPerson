package com.lefuyun.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;

public abstract class LefuDialog<T,D>{

	public BaseActivity mContext;
	private Dialog mDialog;
	View view;
	@SuppressLint("NewApi")
	public LefuDialog(BaseActivity activity,T t,D d){
		mContext = activity;
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.dialog);
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
			
		});
		mDialog = builder.create();
		// 将xml布局解析
		if(getLayoutId()==0){
			throw new NullPointerException("The dialog's layoutId is zero,you should override getLayoutId()");
		}
		view = mDialog.getLayoutInflater().inflate(getLayoutId(), null);
		mDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				onDialogDismiss();
			}
		});
		if(getCloseId()!=0){
			TextView close=(TextView) view.findViewById(getCloseId());
			close.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mDialog.dismiss();
				}
			});
			builder.setCancelable(false);
		}else{
			builder.setCancelable(true);
		}
		//子类处理逻辑
		doView(view,t,d);
		
		showDialog();
	}
	
	public void showDialog(){
		// 显示当前dialog
		mDialog.show();
		// 为当前dialog添加控件
		mDialog.setContentView(view);
		setDialogWindowInfo(getWidth(),getHeight());
	}
	/**
	 * 设置当前窗体的信息
	 */
	private void setDialogWindowInfo(double width,double height) {
		Window window = mDialog.getWindow();
		// 取消dialog在软键盘之上的设置
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		DisplayMetrics metrics = WindowUtil.getDisplayMetrics(mContext);
		LayoutParams params = window.getAttributes();
		if(height==0){
			// 高度设置为屏幕的0.85
			params.height = (int) (metrics.heightPixels * 0.85);
		}else{
			params.height = (int) (metrics.heightPixels * getHeight());
		}
		if(width==0){
			// 宽度设置为屏幕的0.9
			params.width = (int) (metrics.widthPixels * 0.9);
		}else{
			params.width = (int) (metrics.widthPixels * getWidth());
		}
		// 设置当前dialog的宽和高
		window.setAttributes(params);
	}
	public abstract int getLayoutId();
	public abstract int getCloseId();
	public abstract void doView(View view,T t,D d);
	public abstract double getWidth();
	public abstract double getHeight();
	public abstract void onDialogDismiss();
}
