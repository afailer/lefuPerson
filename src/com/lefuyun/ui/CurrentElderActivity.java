package com.lefuyun.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.Portrait;
import com.lefuyun.util.ScreenShot;
import com.lefuyun.util.SignDataUtils;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.ProperRatingBar;

public class CurrentElderActivity extends BaseActivity {
	
	private TextView mBackBtn;
	private View mLine;
	TextView tv_health_status;
	// 当前绑定老人的姓名,所属机构
	private TextView mElderName, mElderOrganization;
	// 老人年龄
	private TextView mElderAge;
	// 老人头像
	private CircleImageView mElderImg;
	
	private OldPeople mOldPeople;
	private ProperRatingBar mRatingBar1, mRatingBar2, mRatingBar3, mRatingBar4;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_current_elder;
	}

	@Override
	protected void initView() {
		setActionBarBackground(Color.parseColor("#05bebc"));
		tv_health_status=(TextView) findViewById(R.id.tv_health_status);
		// 初始化ActionBar
		mBackBtn = (TextView) findViewById(R.id.back_action_bar);
		mBackBtn.setTextColor(Color.WHITE);
		mBackBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left_white, 0, 0, 0);
		mLine = findViewById(R.id.line_action_bar);
		mLine.setVisibility(View.GONE);
		
		mElderName = (TextView) findViewById(R.id.name_elder_current_activity);
		mElderOrganization = (TextView) findViewById(R.id.organization_elder_current_activity);
		mElderAge = (TextView) findViewById(R.id.age_elder_current_activity);
		mElderImg = (CircleImageView) findViewById(R.id.img_elder_current_activity);
		
		mRatingBar1 = (ProperRatingBar) findViewById(R.id.ratingBar1);
		mRatingBar2 = (ProperRatingBar) findViewById(R.id.ratingBar2);
		mRatingBar3 = (ProperRatingBar) findViewById(R.id.ratingBar3);
		mRatingBar4 = (ProperRatingBar) findViewById(R.id.ratingBar4);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
		//老人健康状况
		tv_health_status.setText("综合健康状况  "+SignDataUtils.getOldStatusStr(mOldPeople));
		// 设置老人头像
		ImageLoader.loadImg(mOldPeople.getIcon(), mElderImg);
		// 设置老人姓名
		mElderName.setText(mOldPeople.getElderly_name());
		// 设置老人所属机构名称
		mElderOrganization.setText(mOldPeople.getAgency_name());
		// 设置老人年龄
		mElderAge.setText("年龄:  " + mOldPeople.getAge() + "岁");
		mRatingBar1.setRating((int) (5 * (mOldPeople.getSignData() * 1.0f / 100)));
		mRatingBar2.setRating((int) (5 * (mOldPeople.getDailyLife() * 1.0f / 100)));
		mRatingBar3.setRating((int) (5 * (mOldPeople.getDailyNurs() * 1.0f / 100)));
		mRatingBar4.setRating((int) (5 * (mOldPeople.getDrinkMeal() * 1.0f / 100)));
	}
	
	@Override
	public void onClick(View v) {
		 Intent intent;
		switch (v.getId()) {
		case R.id.back_action_bar:
			finish();
			break;
		case R.id.image_action_bar:
			// 分享
			break;
		case R.id.organization_elder_current_activity:
			//机构详情
			intent = new Intent(this, ExploreDetailsActivity.class);
			intent.putExtra("organizationId", mOldPeople.getAgency_id());
			startActivity(intent);
			break;
		 case R.id.liner_healthstatus://健康状况
			 intent=new Intent(this,HealthConditionIndexActivity.class);
			 intent.putExtra("oldPeople", mOldPeople);
			 startActivity(intent);
		  break;
		 case R.id.liner_dailylife://日常生活
			  intent=new Intent(this,NurseCareActivity.class);
			  intent.putExtra("type", Utils.typeDaily); 
			 intent.putExtra("old", mOldPeople);
			 startActivity(intent);
			 break;
		 case R.id.liner_orgserve://院方服务
			  intent=new Intent(this,NurseCareActivity.class);
			  intent.putExtra("type", Utils.typeNurse); 
			  intent.putExtra("old", mOldPeople);
			  startActivity(intent);
			 break;
		 case R.id.liner_mealstatus://饮食情况
			  intent=new Intent(this,WeekFoodActivity.class);
			  intent.putExtra("type", Utils.typeNurse); 
			  intent.putExtra("old", mOldPeople);
			   startActivity(intent);
			 break;
		 case R.id.build_invite_code_my_activity:
				// 跳转到生成邀请码页面
				intent = new Intent(this, SuccessBuildInviteCodeActivity.class);
				intent.putExtra("oldPeople", mOldPeople);
				startActivity(intent);
			 break;
		 case R.id.build_zxing_code_my_activity:
				// 跳转到生成二维码页面
				intent = new Intent(this, SuccessBuildZxingCodeActivity.class);
				intent.putExtra("oldPeople", mOldPeople);
				startActivity(intent);
			 break;
		default:
			break;
		}

	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	protected boolean hasRightImageView() {
		return true;
	}
	
	@Override
	protected int getActionBarRightImage() {
		return R.drawable.share_white;
	}
	
	@Override
	public void onRightViewClick(View view) {
		// 进行分享
		shareMainPage();
	}
	
	@SuppressLint("SimpleDateFormat")
	private void shareMainPage() {
		showWaitDialog();
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
        	showToast("无法保存照片，请检查SD卡是否挂载");
        	hideWaitDialog();
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
        .format(new Date());
	    String fileName = "lefu_" + timeStamp + ".png";// 照片命名
	    File out = new File(savePath, fileName);
		ScreenShot.shoot(this, out);
		LefuApi.uploadScreenCutImg(out, new RequestCallback<Portrait>() {
			
			@Override
			public void onSuccess(Portrait result) {
				String url = LefuApi.getAbsoluteApiUrl("lefuyun/shiroPictureDetailCtr/toInfoPage") 
						+ "?old_people_name=" + mOldPeople.getElderly_name()
						+ "&score=" + mOldPeople.getScore()
						+ "&id=" + result.getId();
				String message = "综合评定" + mOldPeople.getScore();
				String imgUrl;
				if(mOldPeople.getScore() >= 95) {
					imgUrl = LefuApi.IMG_URL + LefuApi.EXCELLENT_IMG_URL;
				}else if(mOldPeople.getScore() >= 80) {
					imgUrl = LefuApi.IMG_URL + LefuApi.GOOD_IMG_URL;
				}else if(mOldPeople.getScore() >= 60) {
					imgUrl = LefuApi.IMG_URL + LefuApi.AVERAGE_IMG_URL;
				}else {
					imgUrl = LefuApi.IMG_URL + LefuApi.BAD_IMG_URL;
				}
				LefuApi.sharePage(CurrentElderActivity.this, "我的家人"+mOldPeople.getElderly_name(), message, 
						imgUrl, url, true);
				hideWaitDialog();
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast("分享失败");
				hideWaitDialog();
			}
		});
	}

}
