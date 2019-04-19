package com.lefuyun.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.lefuyun.R;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.ImportantMsg;
import com.lefuyun.bean.OldPeople;

public class ImportantNoticeDialog {
	private Dialog mDialog;
	String url;
	WebView webView;
	@SuppressLint("NewApi")
	public ImportantNoticeDialog(final BaseActivity activity,final ImportantMsg importantMsg,OldPeople old){
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.dialog);
		setBuilderClickInfo(builder);
		builder.setCancelable(false);
		mDialog = builder.create();
		// 将xml布局解析
		View view = mDialog.getLayoutInflater().inflate(R.layout.important_dialog_layout, null);
		TextView title=(TextView) view.findViewById(R.id.imp_dialog_title);
		title.setText(importantMsg.getTheme());
		
		//关闭按钮
		TextView close=(TextView) view.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
			}
		});
		url=LefuApi.getAbsoluteApiUrl("lefuyun/importantNoticeCtr/toInfoPage");
		url=url+"?id="+importantMsg.getId()+"&uid="+SPUtils.get(activity, "uid",1l)+"&oid="+old.getId();
		ImageView share=(ImageView) view.findViewById(R.id.share);
		
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String imgurl="";
				if(!"".equals(importantMsg.getPicture())&&importantMsg.getPicture()!=null){
					imgurl=LefuApi.getAbsoluteApiUrl(importantMsg.getPicture());
				}
				LefuApi.sharePage(activity,importantMsg.getTheme(), importantMsg.getHeadline(),imgurl,url,true);
			}
		});
		webView=(WebView) view.findViewById(R.id.important_detail_web);
		// 防止打开系统默认浏览器
		webView.setWebViewClient(new WebViewClient(){
		      @Override
		      public boolean shouldOverrideUrlLoading(WebView view, String url) {
		           view.loadUrl(url);
		           return true;
		      }
		            
		      @Override
		      public void onPageStarted(WebView view, String url, Bitmap favicon) {
		          //activity.showWaitDialog();
		      }
		            
		     @Override
		      public void onPageFinished(WebView view, String url) {
		         //activity.hideWaitDialog();
		      }
		});
		webView.loadUrl(url,Utils.getWebViewHeaderMap(webView));
		
		// 显示当前dialog
		mDialog.show();
		// 为当前dialog添加控件
		mDialog.setContentView(view);
					// 取消dialog在软键盘之上的设置
		mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		setDialogWindowInfo(activity);
	}
	/**
	 * 设置当前dialog的属性
	 * @param builder
	 */
	private void setBuilderClickInfo(Builder builder) {
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
		builder.setCancelable(false);
	}
	/**
	 * 设置当前窗体的信息
	 */
	private void setDialogWindowInfo(BaseActivity activity) {
		Window window = mDialog.getWindow();
		// 取消dialog在软键盘之上的设置
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		DisplayMetrics metrics = WindowUtil.getDisplayMetrics(activity);
		LayoutParams params = window.getAttributes();
		// 高度设置为屏幕的0.85
		params.height = (int) (metrics.heightPixels * 0.85);
		// 宽度设置为屏幕的0.9
		params.width = (int) (metrics.widthPixels * 0.9);
		// 设置当前dialog的宽和高
		window.setAttributes(params);
	}
}
