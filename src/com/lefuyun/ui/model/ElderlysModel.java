package com.lefuyun.ui.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.ElderlysModelAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.Portrait;
import com.lefuyun.fragment.LefuFragment;
import com.lefuyun.ui.ExploreDetailsActivity;
import com.lefuyun.ui.ProposalActivity;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.ScreenShot;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;

public class ElderlysModel extends BaseModel<List<OldPeople>> {
	
	private BaseActivity mContext;
	private View mContentView;
	private ViewGroup mRoot;
	private List<OldPeople> mList;
	private OldPeople mCurrentOldPeople;
	private ElderlysModelAdapter mAdapter;
	private LefuFragment mFragment;
	
	// 老人健康状况控件
	private ViewPager mViewPager;
	// 日期和星期控件
	private TextView mDate, mWeek;
	// 显示上次登录时间控件
	private TextView mlastLogin;
	// 建议簿按钮
	private TextView mProposalBtn;
	// 天气控件
	private ImageView mAirImg;
	private TextView mAirView, mAriStateView, mCityView;
	
	private LayoutInflater mLnflater;
	// 分享
	private TextView mShare;
	// 老人姓名以及老人所属机构
	private TextView mOldPeopleView, mOrganizationView;
	
	public ElderlysModel(BaseActivity context, View root, LefuFragment fragment) {
		mContext = context;
		mLnflater = LayoutInflater.from(context);
		mRoot = (ViewGroup) root;
		mFragment = fragment;
	}

	@Override
	public View getView() {
		mContentView = mLnflater.inflate(R.layout.model_fragment_lefu_elderlys, mRoot, false);
		mViewPager = (ViewPager) mContentView.findViewById(R.id.view_pager_lefu_fragment);
		// 建议簿按钮
		mProposalBtn = (TextView) mContentView.findViewById(R.id.proposal_lefu_fragment);
		mProposalBtn.setOnClickListener(this);
		mDate = (TextView) mContentView.findViewById(R.id.date_lefu_fragment);
		mWeek = (TextView) mContentView.findViewById(R.id.week_lefu_fragment);
		mlastLogin = (TextView) mContentView.findViewById(R.id.before_login_lefu_fragment);
		mOldPeopleView = (TextView) mContentView.findViewById(R.id.old_man_lefu_fragment);
		mShare=(TextView) mContentView.findViewById(R.id.share_lefu_fragment);
		mShare.setOnClickListener(this);
		mOrganizationView = (TextView) mContentView.findViewById(R.id.organization_lefu_fragment);
		mOrganizationView.setOnClickListener(this);
		mAirImg = (ImageView) mContentView.findViewById(R.id.img_air_temperature_lefu_fragment);
		mAirView = (TextView) mContentView.findViewById(R.id.air_temperature_lefu_fragment);
		mAriStateView = (TextView) mContentView.findViewById(R.id.air_state_lefu_fragment);
		mCityView = (TextView) mContentView.findViewById(R.id.city_lefu_fragment);
		initDefault();
		return mContentView;
	}

	@Override
	public void initData(List<OldPeople> t) {
		if(mList == null) {
			// 初始化
			mList = t;
			mAdapter = new ElderlysModelAdapter(mContext, mList);
			mViewPager.setAdapter(mAdapter);
			mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		}else {
			// 更新数据
			TLog.log("当前ElderlysModel的个数是:" + mList.size());
			mAdapter = new ElderlysModelAdapter(mContext, mList);
			mViewPager.setAdapter(mAdapter);
		}
		if(mList.size() != 0) {
			// 获取第一个老人
			mCurrentOldPeople = mList.get(0);
		}else {
			mCurrentOldPeople = null;
		}
		setOldPeopleData();
	}
	/**
	 * 初始化默认数据,即老人无关数据
	 */
	private void initDefault() {
		mDate.setText(StringUtils.getDataTime("yyyy年MM月dd日"));
		mWeek.setText(StringUtils.getCurrentWeek());
		String lastTime = (String) SPUtils.get(mContext, "last_time_login", 
				StringUtils.getCurTimeStr());
		mlastLogin.setText("最近登录:" + StringUtils.friendly_time(lastTime));
	}
	/**
	 * 老人切换时跟新老人关联的控件
	 */
	private void setOldPeopleData() {
		if(mCurrentOldPeople != null) {
			mOldPeopleView.setText(mCurrentOldPeople.getElderly_name());
			mOrganizationView.setText(mCurrentOldPeople.getAgency_name());
			mFragment.setCurrentOldPeople(mCurrentOldPeople);
			ImageLoader.loadImgByNormalImg(mCurrentOldPeople.getImg(), 
					R.drawable.pic_air_temperature, R.drawable.pic_air_temperature, mAirImg);
			mAirView.setText(mCurrentOldPeople.getTem());
			mAriStateView.setText(mCurrentOldPeople.getTxt());
			mCityView.setText(mCurrentOldPeople.getCity());
		}else {
			mOldPeopleView.setText("");
			mOrganizationView.setText("");
			mAirView.setText("");
			mAriStateView.setText("");
			mCityView.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.proposal_lefu_fragment:
			// 建议按钮点击事件
			// 跳转建议界面
			intent = new Intent(mContext, ProposalActivity.class);
			intent.putExtra("oldPeople", mCurrentOldPeople);
			mContext.startActivity(intent);
			break;
		case R.id.share_lefu_fragment:
			//跳转到分享界面
			shareMainPage();
			break;
		case R.id.organization_lefu_fragment:
			// 跳转到当前机构详情页面
			if(mCurrentOldPeople != null) {
				intent = new Intent(mContext, ExploreDetailsActivity.class);
				intent.putExtra("organizationId", mCurrentOldPeople.getAgency_id());
				mContext.startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener{
		
		@Override
		public void onPageSelected(int position) {
			mCurrentOldPeople = mList.get(position);
			setOldPeopleData();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
	}
	/**
	 * 通过老人id显示当前指定的老人
	 * @param peopleId
	 */
	public void setCurrentPosition(long peopleId) {
		int position = -1;
		if(mList != null) {
			for (int i = 0; i < mList.size(); i++) {
				if(mList.get(i).getId() == peopleId) {
					position = i;
					break;
				}
			}
		}
		if(position != -1) {
			TLog.log("ViewPager的position是" + position);
			mViewPager.setCurrentItem(position, false);
		}
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private void shareMainPage() {
		mContext.showWaitDialog();
		// 判断是否挂载了SD卡
		String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/lefuyun/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }
        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
        	mContext.showToast("无法保存照片，请检查SD卡是否挂载");
        	mContext.hideWaitDialog();
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
        .format(new Date());
	    String fileName = "lefu_" + timeStamp + ".png";// 照片命名
	    File out = new File(savePath, fileName);
		ScreenShot.shoot(mContext, out);
		LefuApi.uploadScreenCutImg(out, new RequestCallback<Portrait>() {
			
			@Override
			public void onSuccess(Portrait result) {
				String url = LefuApi.getAbsoluteApiUrl("lefuyun/shiroPictureDetailCtr/toInfoPage") 
						+ "?old_people_name=" + mCurrentOldPeople.getElderly_name()
						+ "&score=" + mCurrentOldPeople.getScore()
						+ "&id=" + result.getId();
				String message = "我的家人" + mCurrentOldPeople.getElderly_name() 
						+ "  综合评定: " + mCurrentOldPeople.getScore();
				String imgUrl;
				if(mCurrentOldPeople.getScore() >= 95) {
					imgUrl = LefuApi.IMG_URL + LefuApi.EXCELLENT_IMG_URL;
				}else if(mCurrentOldPeople.getScore() >= 80) {
					imgUrl = LefuApi.IMG_URL + LefuApi.GOOD_IMG_URL;
				}else if(mCurrentOldPeople.getScore() >= 60) {
					imgUrl = LefuApi.IMG_URL + LefuApi.AVERAGE_IMG_URL;
				}else {
					imgUrl = LefuApi.IMG_URL + LefuApi.BAD_IMG_URL;
				}
				LefuApi.sharePage(mContext, "我的家人我关心......", message ,
						imgUrl, url, true);
				mContext.hideWaitDialog();
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				mContext.showToast("分享失败");
				mContext.hideWaitDialog();
			}
		});
	}

}
