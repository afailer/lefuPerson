package com.lefuyun.ui.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lefuyun.R;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.User;
import com.lefuyun.fragment.LefuFragment;
import com.lefuyun.ui.ExpectActivity;
import com.lefuyun.ui.HealthConditionIndexActivity;
import com.lefuyun.ui.ImportantNoticeActivity;
import com.lefuyun.ui.MainActivity;
import com.lefuyun.ui.NurseCareActivity;
import com.lefuyun.ui.ObtainOutActivity;
import com.lefuyun.ui.OrgActivity;
import com.lefuyun.ui.WeekFoodActivity;
import com.lefuyun.util.Utils;

public class ElderlyDetailsModel extends BaseModel<OldPeople>{
	
	private Context mContext;
	private LefuFragment mLefuFragment;
	private View mContentView;
	private LayoutInflater mLnflater;
	private ViewGroup mRoot;
	private OldPeople mOldPeople;
	private User mUser;
	private ImageView mHealthConditionBtn, mObtainOutBtn,mImportantNotice,mEverydayFeeling;
	private ImageView mNurseCare,mOrgActive,mWeekFood,lefu_givemoney_online;
	public ElderlyDetailsModel(Context context,LefuFragment f, View view) {
		mContext = context;
		mLnflater = LayoutInflater.from(context);
		mRoot = (ViewGroup) view;
		mLefuFragment = f;
	}

	@Override
	public View getView() {
		mContentView = mLnflater.inflate(R.layout.model_fragment_lefu_elderly_details, mRoot, false);
		mHealthConditionBtn = (ImageView) mContentView.findViewById(R.id.lefu_body_condition);
		mObtainOutBtn = (ImageView) mContentView.findViewById(R.id.lefu_hanging_out);
		mImportantNotice=(ImageView) mContentView.findViewById(R.id.lefu_important_notice);
		mEverydayFeeling = (ImageView) mContentView.findViewById(R.id.lefu_eveyday_life);
		mNurseCare=(ImageView) mContentView.findViewById(R.id.lefu_nurse_care);
		mOrgActive=(ImageView) mContentView.findViewById(R.id.lefu_org_active);
		mWeekFood=(ImageView) mContentView.findViewById(R.id.lefu_week_food);
		lefu_givemoney_online=(ImageView) mContentView.findViewById(R.id.lefu_givemoney_online);
		mWeekFood.setOnClickListener(this);
		mEverydayFeeling.setOnClickListener(this);
		mImportantNotice.setOnClickListener(this);
		mHealthConditionBtn.setOnClickListener(this);
		mObtainOutBtn.setOnClickListener(this);
		mNurseCare.setOnClickListener(this);
		mOrgActive.setOnClickListener(this);
		lefu_givemoney_online.setOnClickListener(this);
		return mContentView;
	}

	@Override
	public void initData(OldPeople t) {
		// 获取当前老人
		mOldPeople = t;
		// 获取当前用户
		mUser = ((MainActivity)mLefuFragment.getActivity()).getUser();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.lefu_body_condition:
			if(checkData()) {
				intent = new Intent(mContext, HealthConditionIndexActivity.class);
				intent.putExtra("oldPeople", mOldPeople);
				mContext.startActivity(intent);
			}
			break;
		case R.id.lefu_hanging_out:
			// 外出请假按钮点击事件
			if(checkData()) {
				// 跳转外出请假界面
				intent = new Intent(mContext, ObtainOutActivity.class);
				//intent=new Intent(mContext, ExpectActivity.class);
				intent.putExtra("oldPeople", mOldPeople);
				mContext.startActivity(intent);
			}
			/*if(checkData()) {
				intent=new Intent(mContext, ExpectActivity.class);
				mContext.startActivity(intent);
			}*/
			break;
		case R.id.lefu_important_notice:
			if(checkData()) {
				intent=new Intent(mContext, ImportantNoticeActivity.class);
				intent.putExtra("old", mOldPeople);
				intent.putExtra("user", mUser);
				mContext.startActivity(intent);
			}
			/*if(checkData()) {
				intent=new Intent(mContext, ExpectActivity.class);
				mContext.startActivity(intent);
			}*/
			break;
		case R.id.lefu_eveyday_life:
			if(checkData()) {
				intent =new Intent(mContext, NurseCareActivity.class);
				intent.putExtra("old", mOldPeople);
				intent.putExtra("user", mUser);
				intent.putExtra("type", Utils.typeDaily);
				mContext.startActivity(intent);
			}
			break;
		case R.id.lefu_givemoney_online:
			if(checkData()) {
				intent=new Intent(mContext, ExpectActivity.class);
				mContext.startActivity(intent);
			}
			break;
		case R.id.lefu_nurse_care:
			if(checkData()) {
				intent=new Intent(mContext, NurseCareActivity.class);
				intent.putExtra("old", mOldPeople);
				intent.putExtra("user", mUser);
				intent.putExtra("type", Utils.typeNurse);
				mContext.startActivity(intent);
			}
			break;
		case R.id.lefu_org_active:
			if(checkData()) {
				intent=new Intent(mContext, OrgActivity.class);
				intent.putExtra("id", mOldPeople.getAgency_id());
				intent.putExtra("name", mOldPeople.getAgency_name());
				mContext.startActivity(intent);
			}
			break;
		case R.id.lefu_week_food:
			//intent=new Intent(mContext, WeekFoodActivity.class);
			if(checkData()) {
				intent=new Intent(mContext, WeekFoodActivity.class);
				intent.putExtra("old", mOldPeople);
				intent.putExtra("user", mUser);
				mContext.startActivity(intent);
			}
			break;
		default:
			break;
		}
		
	}
	/**
	 * 校验用户是否登录或者已经关注老人
	 * @return
	 */
	private boolean checkData() {
		if(mUser == null) {
			mLefuFragment.showToast("请先登录");
			return false;
		}
		if(mOldPeople == null) {
			mLefuFragment.showToast("您没有关注任何人");
			return false;
		}
		return true;
	}

}
