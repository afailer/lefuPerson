package com.lefuyun.util;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonPagerAdapter;
import com.lefuyun.bean.DailyLifeRecord;
import com.lefuyun.bean.MediaBean;
import com.lefuyun.widget.CircleIndicators;

public class DailyCareDialog {
	
	private BaseActivity mActivity;
	private Dialog mDialog;
	ViewPager pager;
	RelativeLayout nurseParent;
	LinearLayout nurseDialogContainer;
	TextView nurseTimeDetail,nurseTitleDetail,nurseContentDetail;
	CircleIndicators indicator;
	//List<String> datas=new ArrayList<String>();
	ImageView share_nurse;
	TextView share_title;
	 String title="",content="",url="";
	 List<MediaBean> medias=new ArrayList<MediaBean>();
	 SeekBar seekBar=null;
	@SuppressLint("NewApi")
	public DailyCareDialog(final BaseActivity activity,final DailyLifeRecord record){
		medias.clear();
		mActivity = activity;
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
		// 将xml布局解析
		View view = mDialog.getLayoutInflater().inflate(R.layout.nurse_pic_detail, null);
		TextView close=(TextView) view.findViewById(R.id.nurse_pic_close);
		share_nurse=(ImageView) view.findViewById(R.id.share_nurse);
		share_title=(TextView) view.findViewById(R.id.share_title);
		indicator=(CircleIndicators) view.findViewById(R.id.indicator);
		share_title.setText("日常生活");
		nurseParent=(RelativeLayout) view.findViewById(R.id.nurse_parent);
		nurseDialogContainer=(LinearLayout) view.findViewById(R.id.nurse_dialog_container);
		nurseTimeDetail=(TextView) view.findViewById(R.id.nurse_time_detail);
		nurseTitleDetail=(TextView) view.findViewById(R.id.nurse_title_detail);
		nurseContentDetail=(TextView) view.findViewById(R.id.nurse_content_detail);
		pager=(ViewPager) view.findViewById(R.id.nurse_pic_detail);
		if(AppContext.mediaPlayer!=null){
			mediaPlayer=AppContext.mediaPlayer;
		}else{
			mediaPlayer=new MediaPlayer();
		}
		mediaPlayer.reset();//把各项参数恢复到初始状态  
//		ImageView nurse_pic_close_haspic=(ImageView) view.findViewById(R.id.nurse_pic_close_haspic);
//		nurse_pic_close_haspic.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				mDialog.dismiss();
//			}
//		});
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
			}
		});
		 //final String[] urlList = Utils.getUrls(record.getMedia(),Utils.picType);
		this.medias.addAll(Utils.getUrlList(record.getMedia(), Utils.picType));//getUrls(nurseRecord.getMedia(),Utils.picType);
		this.medias.addAll(Utils.getUrlList(record.getMedia(), Utils.videoType));
		this.medias.addAll(Utils.getUrlList(record.getMedia(), Utils.audioType));
		if(medias.size()!=0){
			title=getCareStr(record);
			if("".equals(record.getReserved().trim())){
				content=TimeZoneUtil.getTimeNormal(record.getInspect_dt());
			}else{
				content=record.getReserved();
			}
			url=getCareUrl(record);
			share_nurse.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(medias.size()!=0){
						LefuApi.sharePage(activity, title, content, medias.get(0).getMediaPath(), url, true);
					}
				}
			});
			
			nurseDialogContainer.setVisibility(View.VISIBLE);
			nurseParent.setVisibility(View.GONE);
			pager.setAdapter(new DailyPagerAdapter(activity, medias, R.layout.pic_detail_item));
			indicator.setViewPager(pager);
			nurseTimeDetail.setText(TimeZoneUtil.getTimeCompute(record.getInspect_dt()));
			nurseTitleDetail.setText(getCareStr(record));
			nurseContentDetail.setText(record.getReserved());
			TextView nurse_pic_zan=(TextView) view.findViewById(R.id.nurse_pic_zan);
			nurse_pic_zan.setVisibility(View.GONE);
			ImageView zan=(ImageView) view.findViewById(R.id.nurse_pager_zan);
			zan.setVisibility(View.GONE);
		}else{
			title=getCareStr(record);
			if("".equals(record.getReserved().trim())){
				content=TimeZoneUtil.getTimeNormal(record.getInspect_dt());
			}else{
				content=record.getReserved();
			}
			url=getCareUrl(record);
			share_nurse.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					LefuApi.sharePage(activity, title, content, LefuApi.IMG_URL + LefuApi.LEFU_IMG_URL, url, true);
				}
			});
			nurseDialogContainer.setVisibility(View.GONE);
			nurseParent.setVisibility(View.VISIBLE);
			ImageView zan=(ImageView) view.findViewById(R.id.nurse_zan_img);
			TextView nurse_time=(TextView) view.findViewById(R.id.nurse_time_nopic);
			TextView nurse_title=(TextView) view.findViewById(R.id.nurse_title_nopic);
			TextView nurse_content=(TextView) view.findViewById(R.id.nurse_content_nopic);
			nurse_time.setText(TimeZoneUtil.getTimeCompute(record.getInspect_dt()));
			nurse_title.setText(getCareStr(record));
			nurse_content.setText(record.getReserved());
			zan.setVisibility(View.GONE);
		}
		// 显示当前dialog
		mDialog.show();
		// 为当前dialog添加控件
		mDialog.setContentView(view);
		
		setDialogWindowInfo();
	}
	private String getCareStr(DailyLifeRecord record) {
		// TODO Auto-generated method stub
		switch (record.getType()) {
		case 1:
			return "精彩生活";
		case 2:
			return "您的家人在吃"+getMealTypeStr(record.getMeal_type())+",食量 "+getMeal_amountStr(record.getMeal_amount());
		case 3:
			return "您的家人在睡觉，睡眠质量:"+record.getSleep_quality();
		default:
			return "";
		}
	}
	private String getCareUrl(DailyLifeRecord record){
		switch (record.getType()) {
		case 1:
			 url=LefuApi.getAbsoluteApiUrl("lefuyun/dailyLifeRecordCtr/toInfoPage");
			 url=url+"?daily_id="+record.getId() + "&version=" + UserInfo.getVersion();
			return url;
		case 2:
			url=LefuApi.getAbsoluteApiUrl("lefuyun/singndata/meal/toInfoPage");
			 url=url+"?id="+record.getId() + "&version=" + UserInfo.getVersion();
			return url;
		case 3:
			url=LefuApi.getAbsoluteApiUrl("lefuyun/singndata/sleep/toInfoPage");
			 url=url+"?id="+record.getId() + "&version=" + UserInfo.getVersion();
			 return url;
		default:
			return "";
		}
	}
	private String getMealTypeStr(int meal_type){
		switch (meal_type) {
		case 1:
			return "早餐";
		case 2:
			return "午餐";
		case 3:
			return "晚餐";
		default:
			return "";
		}
	}
	private String getMeal_amountStr(int meal_amount){
		switch (meal_amount) {
		case 1:
			return "偏少";
		case 2:
			return "正常";
		case 3:
			return "偏多";
		default:
			return "";
		}
	}
	class DailyPagerAdapter extends CommonPagerAdapter<MediaBean>{
		
		public DailyPagerAdapter(Context context, List<MediaBean> datas,
				int layoutId) {
			super(context, datas, layoutId);
			
		}

		@Override
		public void processItem(View view, final MediaBean t,int position) {
			if(seekBar==null){
				seekBar=(SeekBar) view.findViewById(R.id.audio_seekbar);
			}
			ImageView img = (ImageView) view.findViewById(R.id.pic_detail);
			media=(ImageView) view.findViewById(R.id.media_img);
			TextView mediaType=(TextView) view.findViewById(R.id.media_type);
			Utils.setImageByMediaBean(img,media,mediaType,seekBar,t, mActivity);
			if(medias.get(position).getMediaType()==3){
				
				initAudio(medias.get(position));
				
			}
		}
		
	}
	ImageView media;
	MediaPlayer mediaPlayer;
	boolean playing=true;
	private void initAudio(final MediaBean mediaBean){
		media.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				play(Uri.parse(LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath())), 0);
			}
		});
		//seekBar.setOnSeekBarChangeListener(new )
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			media.setImageResource(R.drawable.media_audio);
		};
	};
	private void play(Uri uri,int pos){
		if(mediaPlayer.isPlaying()){
			seekBar.setVisibility(View.VISIBLE);
			mediaPlayer.pause();
			media.setImageResource(R.drawable.media_audio);
		}else{
			media.setImageResource(R.drawable.audio_stop);
			seekBar.setVisibility(View.VISIBLE);
				playing=true;
				try {
					mediaPlayer.setDataSource(mActivity,uri);
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.prepare();  //进行缓冲  
					seekBar.setProgress(pos);
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				 mediaPlayer.start();
			
	       
	        new Thread(new Runnable() {
				@Override
				public void run() {
					while(playing){
						if(mediaPlayer.isPlaying()){
							seekBar.setMax(mediaPlayer.getDuration());
							seekBar.setProgress(mediaPlayer.getCurrentPosition());
						}else{
							Looper.prepare();
							handler.sendEmptyMessage(1);
							playing=false;
						}
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		
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
}
