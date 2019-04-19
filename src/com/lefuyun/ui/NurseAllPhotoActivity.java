package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.NurseMediaSum;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.fragment.PicFragment;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;

public class NurseAllPhotoActivity extends BaseActivity{
	
	CircleImageView circleImage;
	OldPeople old;
	TextView nurse_meidia1,nurse_media2,nurse_meida3,oldName,nurseVideoNum,nursePicNum,nurseAudioNum;
	int mediaType=Utils.picType;
	ViewPager media_pager;
	int type;
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.nurse_media1://图片
			changeMediaType(Utils.picType);
			scrollViewPager(Utils.picType);
			break;
		case R.id.nurse_media2://视频
			changeMediaType(Utils.videoType);
			scrollViewPager(Utils.videoType);
			break;
		case R.id.nurse_meida3://音频
			changeMediaType(Utils.audioType);
			scrollViewPager(Utils.audioType);
			break;
		default:
			break;
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_nurse_all_photo;
	}

	@Override
	protected void initView() {
		type=getIntent().getIntExtra("type",1);
		media_pager=(ViewPager) findViewById(R.id.media_pager);
		circleImage=(CircleImageView) findViewById(R.id.nurse_photo_circleImage);
		old=(OldPeople) getIntent().getSerializableExtra("old");
		nurse_meidia1=(TextView) findViewById(R.id.nurse_media1);
		nurse_media2=(TextView) findViewById(R.id.nurse_media2);
		nurse_meida3=(TextView) findViewById(R.id.nurse_meida3);
		nursePicNum=(TextView) findViewById(R.id.nurse_pic_num);
		nurseAudioNum=(TextView) findViewById(R.id.nurse_audio_num);
		nurseVideoNum=(TextView) findViewById(R.id.nurse_video_num);
		nurse_meidia1.setOnClickListener(this);
		nurse_media2.setOnClickListener(this);
		nurse_meida3.setOnClickListener(this);
		oldName=(TextView) findViewById(R.id.nurse_photo_oldName);
		oldName.setText(old.getElderly_name());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<Fragment> fragmentList=new ArrayList<Fragment>();
				fragmentList.add(PicFragment.getInstance(old,Utils.picType,type));
				fragmentList.add(PicFragment.getInstance(old, Utils.videoType,type));
				fragmentList.add(PicFragment.getInstance(old, Utils.audioType, type));//new AudioFragment(old,type)
				MediaPagerAdapter adapter=new MediaPagerAdapter(getSupportFragmentManager(),fragmentList);
				media_pager.setOffscreenPageLimit(3);
				media_pager.setAdapter(adapter);
			}
		}).start();
		media_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				changeMediaType(position+1);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		ImageLoader.loadImg(old.getIcon(), circleImage);
		changeMediaType(mediaType);
	}
	@SuppressLint("ResourceAsColor")
	private void changeMediaType(final int mediaType){
		switch (mediaType) {
		case 1://图片
			nurse_meidia1.setTextColor(Color.parseColor("#06BEBD"));
			nurse_media2.setTextColor(Color.parseColor("#808080"));
			nurse_meida3.setTextColor(Color.parseColor("#808080"));
			break;
		case 2://视频
			nurse_meidia1.setTextColor(Color.parseColor("#808080"));
			nurse_media2.setTextColor(Color.parseColor("#06BEBD"));
			nurse_meida3.setTextColor(Color.parseColor("#808080"));
			break;
		case 3://音频
			nurse_meidia1.setTextColor(Color.parseColor("#808080"));
			nurse_media2.setTextColor(Color.parseColor("#808080"));
			nurse_meida3.setTextColor(Color.parseColor("#06BEBD"));
			break;
		default:
			break;
		}
	}
	private void scrollViewPager(int mediaType){
		switch (mediaType) {
		case 1:
			media_pager.setCurrentItem(0);
			break;
		case 2:
			media_pager.setCurrentItem(1);
			break;
		case 3:
			media_pager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
	private void loadSumNum(long old_people_id,int type){
		if(type==Utils.typeNurse){
			LefuApi.queryMediaSumByOldPeopleId(old_people_id, new RequestCallback<NurseMediaSum>() {
				
				@Override
				public void onSuccess(NurseMediaSum result) {
					// TODO Auto-generated method stub
					nursePicNum.setText(result.getPictureSum()+"");
					nurseAudioNum.setText(result.getAudioSum()+"");
					nurseVideoNum.setText(result.getVideoSum()+"");
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}else if(type==Utils.typeDaily){
			LefuApi.queryDailyLifeMediaSumByOldPeopleId(old_people_id, new RequestCallback<NurseMediaSum>() {
				
				@Override
				public void onSuccess(NurseMediaSum result) {
					// TODO Auto-generated method stub
					try{
					nursePicNum.setText(result.getPictureSum()+"");
					nurseAudioNum.setText(result.getAudioSum()+"");
					nurseVideoNum.setText(result.getVideoSum()+"");
					}catch(Exception e){}
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	class MediaPagerAdapter extends FragmentPagerAdapter{
		List<Fragment> mFragmentList;
		public MediaPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
			super(fm);
			this.mFragmentList=fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}
	
	@Override
	protected void initData() {
		loadSumNum(old.getId(),type);
	}
	@Override
	protected boolean hasActionBar() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected boolean hasBackButton() {
		// TODO Auto-generated method stub
		return true;
	}


}
