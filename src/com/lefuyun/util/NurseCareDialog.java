package com.lefuyun.util;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.NurseDeitailPagerAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.MediaBean;
import com.lefuyun.bean.MyEvent;
import com.lefuyun.bean.NurseRecord;
import com.lefuyun.widget.CircleIndicators;
import com.lefuyun.widget.PeriscopeLayout;

import de.greenrobot.event.EventBus;

public class NurseCareDialog implements OnClickListener{
	
	private Dialog mDialog;
	private BaseActivity mActivity;
	private NurseRecord mNurseRecord;
	private int mType;
	private MyEvent mEvent;
	
	private TextView mBackBtn;
	private ImageView mShareBtn;
	// 包含图片的展示控件
	private LinearLayout mContainerWithImg;
	// 没有图片的展示控件
	private RelativeLayout mContainer;
	List<MediaBean> medias=new ArrayList<MediaBean>();
	
	ViewPager pager;
	TextView nurseTimeDetail,nurseTitleDetail,nurseContentDetail;
	PeriscopeLayout tv_zan;
	PeriscopeLayout tv_one; 
	CircleIndicators indicator;
	@SuppressLint("NewApi")
	public NurseCareDialog(final BaseActivity activity,final NurseRecord record,final int type,final MyEvent event){
		medias.clear();
		mActivity = activity;
		mNurseRecord = record;
		mType = type;
		mEvent = event;
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.dialog);
		setBuilderClickInfo(builder);
		mDialog = builder.create();
		// 将XML布局解析
		View view = mDialog.getLayoutInflater().inflate(R.layout.nurse_pic_detail, null);
		initActionBar(view);
		// 初始化容器
		mContainer = (RelativeLayout) view.findViewById(R.id.nurse_parent);
		mContainerWithImg = (LinearLayout) view.findViewById(R.id.nurse_dialog_container);
		indicator=(CircleIndicators) view.findViewById(R.id.indicator);
		//String[] urlList = Utils.getUrls(record.getMedia(),Utils.picType);
		medias.addAll(Utils.getUrlList(record.getMedia(), Utils.picType));
		medias.addAll(Utils.getUrlList(record.getMedia(), Utils.videoType));
		medias.addAll(Utils.getUrlList(record.getMedia(), Utils.audioType));
		if(medias.size() != 0){
			initContainerWithImgView(view);
		}else{
			initContainerView(view);
		}
		// 显示当前dialog
		mDialog.show();
		// 为当前dialog添加控件
		mDialog.setContentView(view);
		mDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				if(nurseDeitailPagerAdapter!=null){
					nurseDeitailPagerAdapter.stopMedia();
				}
			}
		});
		setDialogWindowInfo();
	}
	
	
	// 初始化ActionBar控件
	private void initActionBar(View view) {
		mBackBtn = (TextView) view.findViewById(R.id.nurse_pic_close);
		mBackBtn.setOnClickListener(this);
		mShareBtn = (ImageView) view.findViewById(R.id.share_nurse);
		mShareBtn.setOnClickListener(this);
	}
	NurseDeitailPagerAdapter nurseDeitailPagerAdapter=null;
	/**
	 * 初始化包含图片信息的控件
	 * @param view
	 */
	private void initContainerWithImgView(View view) {
		mContainerWithImg.setVisibility(View.VISIBLE);
		mContainer.setVisibility(View.GONE);
		// 初始化图片展示控件
		pager=(ViewPager) view.findViewById(R.id.nurse_pic_detail);
		nurseDeitailPagerAdapter = new NurseDeitailPagerAdapter(mActivity, mNurseRecord);
		pager.setAdapter(nurseDeitailPagerAdapter);
		indicator.setViewPager(pager);
		nurseTimeDetail=(TextView) view.findViewById(R.id.nurse_time_detail);
		nurseTitleDetail=(TextView) view.findViewById(R.id.nurse_title_detail);
		nurseContentDetail=(TextView) view.findViewById(R.id.nurse_content_detail);
		nurseTimeDetail.setText(TimeZoneUtil.getTimeCompute(mNurseRecord.getNursing_dt()));
		nurseTitleDetail.setText(Html.fromHtml("您的老人刚刚接受了护理服务<br>项目：" + mNurseRecord.getItems()).toString());
		nurseContentDetail.setText(mNurseRecord.getReserved());
		tv_one=(PeriscopeLayout) view.findViewById(R.id.tv_one);
		tv_one.setVisibility(View.VISIBLE);
		final TextView nurse_pic_zan=(TextView) view.findViewById(R.id.nurse_pic_zan);
		nurse_pic_zan.setText(mNurseRecord.getPraise_number()+"个赞");
		final ImageView zan=(ImageView) view.findViewById(R.id.nurse_pager_zan);
		zan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				zan.setImageResource(R.drawable.praise_main_color);
				LefuApi.praiseNurseing(mNurseRecord.getDaily_id(), new RequestCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						mNurseRecord.setPraise_number(mNurseRecord.getPraise_number()+1);
						nurse_pic_zan.setText(mNurseRecord.getPraise_number()+"个赞");
						if(mType==2){
							EventBus.getDefault().post(mEvent);
						}
						tv_one.addHeart();
					}
					
					@Override
					public void onFailure(ApiHttpException e) {
						
					}
				});
			}
		});
		
	}
	/**
	 * 初始化没有图片的控件
	 * @param view
	 */
	private void initContainerView(View view) {
		mContainerWithImg.setVisibility(View.GONE);
		mContainer.setVisibility(View.VISIBLE);
		final ImageView zan=(ImageView) view.findViewById(R.id.nurse_zan_img);
		TextView nurse_time=(TextView) view.findViewById(R.id.nurse_time_nopic);
		TextView nurse_title=(TextView) view.findViewById(R.id.nurse_title_nopic);
		TextView nurse_content=(TextView) view.findViewById(R.id.nurse_content_nopic);
		final TextView parseNumNoPic=(TextView) view.findViewById(R.id.parse_num);
		tv_zan=(PeriscopeLayout) view.findViewById(R.id.tv_zan);
		tv_zan.setVisibility(View.VISIBLE);
		parseNumNoPic.setText(mNurseRecord.getPraise_number()+"个赞");
		nurse_time.setText(TimeZoneUtil.getTimeCompute(mNurseRecord.getNursing_dt()));
		nurse_title.setText(Html.fromHtml("您的老人刚刚接受了护理服务<br>项目：" + mNurseRecord.getItems()).toString());
		nurse_content.setText(mNurseRecord.getReserved());
		zan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				zan.setImageResource(R.drawable.praise_main_color);
				LefuApi.praiseNurseing(mNurseRecord.getDaily_id(), new RequestCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						mNurseRecord.setPraise_number(mNurseRecord.getPraise_number()+1);
						parseNumNoPic.setText(mNurseRecord.getPraise_number()+"个赞");
						if(mType==2){
							EventBus.getDefault().post(mEvent);
						}
						tv_zan.addHeart();
					}
					
					@Override
					public void onFailure(ApiHttpException e) {
						
					}
				});
			}
		});
		
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
	private void setDialogWindowInfo() {
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
	
	/**
	 * 分享当前页面信息
	 * @param record
	 */
	private void shareInfo() {
		String title = "您的老人刚刚接受了护理服务,项目：" + mNurseRecord.getItems();
		String url = LefuApi.getAbsoluteApiUrl("lefuyun/dailyNursingRecordCtr/toInfoPage");
		url = url + "?daily_id=" + mNurseRecord.getDaily_id();
		String des = "";
		if("".equals(mNurseRecord.getReserved().trim())){
			des = StringUtils.friendly_time(mNurseRecord.getNursing_dt());
		}else{
			des = mNurseRecord.getReserved();
		}
		LefuApi.sharePage(mActivity, title, des, LefuApi.IMG_URL + LefuApi.LEFU_IMG_URL, url, true);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nurse_pic_close:
			mDialog.dismiss();
			break;
		case R.id.share_nurse:
			shareInfo();
			break;

		default:
			break;
		}
		
	}
	
}
