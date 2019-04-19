package com.lefuyun.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.lefuyun.R;
import com.lefuyun.base.BaseFragment;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.User;
import com.lefuyun.ui.ConcernElderlyActivity;
import com.lefuyun.ui.MainActivity;
import com.lefuyun.ui.MyActivity;
import com.lefuyun.ui.model.ElderlyDetailsModel;
import com.lefuyun.ui.model.ElderlysModel;
import com.lefuyun.util.TLog;
/**
 * 被开启的类需要显示指定的老人,需要通过intent返回值currentOldPeopleID类型为long
 * resultCode需为200
 * 
 */
public class LefuFragment extends BaseFragment {
	
	private ImageView mAddElderBtn, mLoginBtn;
	
	private ElderlysModel mElderlys;
	private ElderlyDetailsModel mElderlyDetails;
	private User mUser;
	// 当前用户绑定的所有老人
	private List<OldPeople> mOldPeoples;
	// 当前页面用户选择的老人
	private OldPeople mCurrentOldPeople;
	private MainActivity mMainActivity;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_lefu;
	}
	
	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		// 第一层控件初始化
		initTopView(view);
		// 初始化第二层控件
		mElderlys = new ElderlysModel(mActivity, view, this);
		LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 4);
		((LinearLayout)view).addView(mElderlys.getView(), params1);
		// 初始化第三层控件
		mElderlyDetails = new ElderlyDetailsModel(mActivity,this,view);
		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 3);
		((LinearLayout)view).addView(mElderlyDetails.getView(), params2);
	}
	/**
	 * 第一层控件初始化
	 * @param view
	 */
	private void initTopView(View view) {
		// 跳转到添加老人页面的按钮
		mAddElderBtn = (ImageView) view.findViewById(R.id.add_elder_lefu_fragment);
		// 跳转到老人页面点击按钮
		mLoginBtn = (ImageView) view.findViewById(R.id.login_lefu_fragment);
		mAddElderBtn.setOnClickListener(this);
		mLoginBtn.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		mMainActivity = (MainActivity)mActivity;
		mUser = mMainActivity.getUser();
		// 获取
	}
	
	@Override
	public void onClick(View v) {
		
		Intent intent = null;
		switch (v.getId()) {
		case R.id.add_elder_lefu_fragment:
			// 关注老人按钮点击事件
			if(mUser == null) {
				showToast("请先登录");
				return;
			}
			// 跳转关注老人界面
			intent = new Intent(mActivity, ConcernElderlyActivity.class);
			intent.putExtra("userId", mUser.getUser_id());
			startActivityForResult(intent, 100);
			break;
		case R.id.login_lefu_fragment:
			// 老人个人页面
			if(mCurrentOldPeople == null) {
				showToast("您还没有关注任何人");
				return;
			}
			// 跳转到老人个人页面
			intent = new Intent(mActivity, MyActivity.class);
			intent.putExtra("username", mUser.getUser_name());
			intent.putExtra("oldPeople", mCurrentOldPeople);
			mActivity.startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	/**
	 * 初始化当前用户绑定的老人, 此方法由MainActivity调用
	 * @param oldPeoples
	 */
	public void setOldPeopleList(List<OldPeople> oldPeoples) {
		// 可能为空,为空的情况是session超时
		if(mOldPeoples == null) {
			mOldPeoples = oldPeoples;
		}
		if(mOldPeoples.size() == 0) {
			// 如果绑定数据老人信息为空,初始化老人为空
			mElderlyDetails.initData(null);
		}
		mElderlys.initData(mOldPeoples);
	}
	/**
	 * 设置当前被选中的老人, 此方法只能由第二层控件调用
	 * @param oldPeople
	 */
	public void setCurrentOldPeople(OldPeople oldPeople) {
		mCurrentOldPeople = oldPeople;
		mElderlyDetails.initData(mCurrentOldPeople);
	}
	/**
	 * 设置当前被选中的老人,由MainActivity调用和本Fragment调用
	 * @param id
	 */
	public void setSelectElder(long id) {
		if(id != 0) {
			mElderlys.setCurrentPosition(id);
		}
	}
	/**
	 * 设置当前用户
	 * @param user
	 */
	public void setUser(User user) {
		mUser = user;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		TLog.log("LefuFragment拿到返回信息");
		if(resultCode == 200) {
			// 关注老人页面返回的结果
			long peopleId = data.getLongExtra("currentOldPeopleID", 0);
			if(peopleId != 0) {
				setSelectElder(peopleId);
			}
		}
	}
	
}

