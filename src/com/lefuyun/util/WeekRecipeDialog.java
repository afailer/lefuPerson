package com.lefuyun.util;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.WeekRecipeAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.MyEvent;
import com.lefuyun.bean.WeekRecipe;
import com.lefuyun.widget.PeriscopeLayout;

public class WeekRecipeDialog {
	private Dialog mDialog;
	ViewPager pager;
	RelativeLayout nurseParent;
	RelativeLayout nurseDialogContainer;
	TextView nurseTimeDetail,nurseTitleDetail,nurseContentDetail;
	PeriscopeLayout tv_zan;
	private android.view.animation.Animation animation;
	 String title="",content="",url="/lefuyun/weekrecipe/toInfoPage?id=";
	ImageView share_nurse;
	@SuppressLint("NewApi")
	public WeekRecipeDialog(final BaseActivity activity,final WeekRecipe record,final MyEvent event){
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
		builder.setCancelable(false);
		mDialog = builder.create();
		animation=AnimationUtils.loadAnimation(activity,R.anim.nn);
		// 将xml布局解析
		View view = mDialog.getLayoutInflater().inflate(R.layout.week_recipe_dialog, null);
		share_nurse=(ImageView) view.findViewById(R.id.share_nurse);
		url+=record.getId();
		share_nurse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if("".equals(record.toString())){
					content=TimeZoneUtil.getTimeNormal(record.getCreateTime());
				}else{
					content=record.toString();
				}
				String imgUrl="";
				if(!"".equals(record.getPicUrl())&&record.getPicUrl()!=null){
					imgUrl=LefuApi.getAbsoluteApiUrl(record.getPicUrl());
				}
				LefuApi.sharePage(activity,"一周食谱",content, imgUrl, LefuApi.getAbsoluteApiUrl(url),true);
			}
		});
		tv_zan=(PeriscopeLayout) view.findViewById(R.id.tv_one);
		tv_zan.setVisibility(View.VISIBLE);
		TextView close=(TextView) view.findViewById(R.id.nurse_pic_close);
		nurseParent=(RelativeLayout) view.findViewById(R.id.nurse_parent);
		nurseDialogContainer=(RelativeLayout) view.findViewById(R.id.nurse_dialog_container);
		nurseTimeDetail=(TextView) view.findViewById(R.id.nurse_time_detail);
		nurseTitleDetail=(TextView) view.findViewById(R.id.nurse_title_detail);
		nurseContentDetail=(TextView) view.findViewById(R.id.nurse_content_detail);
		pager=(ViewPager) view.findViewById(R.id.weekrecipe_pager);
		final ImageView nurse_pic_zan=(ImageView) view.findViewById(R.id.nurse_pager_zan);
		TextView title = (TextView) view.findViewById(R.id.share_title);
		title.setText(TimeZoneUtil.getWeekOfDate(record.getCreateTime()));
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
			}
		});
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
			}
		});
		final List<WeekRecipe.pic> picList=record.getPicUrlList();
		WeekRecipeAdapter recipeAdapter=new WeekRecipeAdapter(activity, picList, R.layout.week_recipe_dialog_item);
		pager.setAdapter(recipeAdapter);
		nurse_pic_zan.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					nurse_pic_zan.setImageResource(R.drawable.praise_main_color);
					LefuApi.parseWeekRecipe(record.getId(), new RequestCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							
							LogUtil.e("result", result);
							tv_zan.addHeart();
						}
						
						@Override
						public void onFailure(ApiHttpException e) {
							e.printStackTrace();
							tv_zan.addHeart();
						}
					});
				}
		});
		
		
		// 显示当前dialog
		mDialog.show();
		// 为当前dialog添加控件
		mDialog.setContentView(view);
		// 取消dialog在软键盘之上的设置
		mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		setDialogWindowInfo(activity);
	}
	/**
	 * 设置当前窗体的信息
	 */
	private void setDialogWindowInfo(BaseActivity mActivity) {
		Window window = mDialog.getWindow();
		// 取消dialog在软键盘之上的设置
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		DisplayMetrics metrics = WindowUtil.getDisplayMetrics(mActivity);
		LayoutParams params = window.getAttributes();
		// 高度设置为屏幕的0.85
		params.height = (int) (metrics.heightPixels * 0.85);
		// 宽度设置为屏幕的0.9
		params.width = (int) (metrics.widthPixels * 0.9);
		// 设置当前dialog的宽和高
		window.setAttributes(params);
	}
}
