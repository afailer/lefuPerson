package com.lefuyun.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.adapter.MeFragmentAdapter;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.BaseFragment;
import com.lefuyun.bean.PersonalPreference;
import com.lefuyun.bean.User;
import com.lefuyun.ui.ConcernElderlyActivity;
import com.lefuyun.ui.ContactUsActivity;
import com.lefuyun.ui.LoginActivity;
import com.lefuyun.ui.MainActivity;
import com.lefuyun.ui.ResetPasswordActivity;
import com.lefuyun.ui.UserInfoActivity;
import com.lefuyun.ui.webview.LefuInfoActivity;
import com.lefuyun.util.TLog;
import com.lefuyun.util.UpdateManager;
import com.lefuyun.util.WindowUtil;
import com.lefuyun.widget.CircleImageView;

public class MeFragment extends BaseFragment implements OnItemClickListener{
	
	private ListView mListView;
	private List<PersonalPreference> mList;
	private MeFragmentAdapter mAdapter;
	private MainActivity mMainActivity;
	private User mUser;
	// 用户图像
	private CircleImageView mImag;
	// 会员信息以及当前绑定的老人个数
	private TextView mMemberView, mBindNum;
	// 记录当前用户是否已经登录
	private boolean isLogin;
	// 登录时候是退出按钮,未登录时是点击按钮
	private TextView mBtn;
	private UpdateManager mUpdateManager;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_me;
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) mActivity;
		mImag = (CircleImageView) view.findViewById(R.id.image_head_me_fragment);
		mMemberView = (TextView) view.findViewById(R.id.member_me_fragment);
		mBindNum = (TextView) view.findViewById(R.id.num_binding_me_fragment);
		mListView = (ListView) view.findViewById(R.id.content_me_fragment);
		mBtn = (TextView) view.findViewById(R.id.quite_me_fragment);
		mBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mUser = mMainActivity.getUser();
		if(mUser != null) {
			isLogin = true;
			ImageLoader.loadImg(mUser.getIcon(), mImag);
			mMemberView.setText(mUser.getUser_name());
			if(mMainActivity.getBindSize() > 0) {
				mBindNum.setText("已关注" + mMainActivity.getBindSize() + "名亲人");
			}else {
				mBindNum.setText("您未关注任何人");
			}
			mBtn.setText("退出");
		}else {
			isLogin = false;
			mMemberView.setText("您尚未登陆");
			mBindNum.setVisibility(View.GONE);
			mBtn.setText("登录");
		}
		
		// 初始化ListView
		if(mList == null) {
			mList = new ArrayList<PersonalPreference>();
		}else {
			mList.clear();
		}
		initList();
		if(mAdapter == null) {
			mAdapter = new MeFragmentAdapter(mActivity, mList, R.layout.item_fragment_me);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(this);
		}else {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quite_me_fragment:
			if(isLogin) {
				// 退出
				mMainActivity.clearUserInfo();
				initData();
			}else {
				// 登录,跳转到登录页面
				Intent intent = new Intent(mActivity, LoginActivity.class);
				startActivityForResult(intent, 100);
			}
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		// 进行页面跳转
		PersonalPreference pp = mList.get(position);
		// 空条目不能跳转
		if(pp.getClzz() != null) {
			// 能进行页面跳转
			Intent intent = new Intent(mActivity, pp.getClzz());
			if(pp.isSkip()) {
				// 跳转到指定的页面
				intent.putExtra("isAnimator", true);
			}else {
				// 跳转到登录页面,可以进行返回
				intent.putExtra("isBack", true);
			}
			if(mUser != null) {
				intent.putExtra("user", mUser);
				intent.putExtra("userId", mUser.getUser_id());
			}
			startActivityForResult(intent, 100);
			mActivity.overridePendingTransition(R.animator.slide_right_in, R.animator.slide_left_out);
			if("更换账号".equals(pp.getTitle())) {
				AppContext.clearUserInfo();
				mActivity.finish();
			}
		}else {
			if("检测更新".equals(pp.getTitle())) {
				if(mUpdateManager == null) {
					mUpdateManager = new UpdateManager(mActivity, true);
				}
				mUpdateManager.checkUpdate();
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		TLog.log("MeFragment拿到了信息反馈   返回码是 == " + resultCode);
		if(resultCode == 200) {
			// 从关注老人页面跳转
			long oldPeopleId = data.getLongExtra("currentOldPeopleID", 0);
			mMainActivity.showLefuFragment(oldPeopleId, true);
		}else if(resultCode == AppContext.UPDATE_USERINFO_SUCCESS) {
			// 用户个人信息修改成功
			TLog.log("用户信息修改反馈成功");
			User user = (User) data.getSerializableExtra("user");
			mMainActivity.setUser(user);
			initData();
		}
	}
	
	/**
	 * 初始化条目
	 */
	private void initList() {
		// 如果中间有空格,可以将title设置成空字符串
		mList.add(initPersonalPreference("个人设置", "", isLogin, UserInfoActivity.class));
		mList.add(initPersonalPreference("密码重置", "", isLogin, ResetPasswordActivity.class));
		mList.add(initPersonalPreference("我的关注", "", isLogin, ConcernElderlyActivity.class));
		// 会员中心暂时不需要
//		mList.add(initPersonalPreference("会员中心", isLogin, DemoActivity.class));
		mList.add(initPersonalPreference("", "", false, null));
		String message = " v" + WindowUtil.getVersionName(mMainActivity);
		mList.add(initPersonalPreference("检测更新", message, true, null));
		mList.add(initPersonalPreference("关于乐福", "", true, LefuInfoActivity.class));
		mList.add(initPersonalPreference("联系我们", "", true, ContactUsActivity.class));
		mList.add(initPersonalPreference("更换账号", "", true, LoginActivity.class));
	}
	/**
	 * 创建我的页面的条目信息
	 * @param title 条目名称
	 * @param clzz 当前条目要跳转到的页面
	 * @return
	 */
	private PersonalPreference initPersonalPreference(String title, 
			String message, boolean skip, Class<? extends BaseActivity> clzz) {
		PersonalPreference personalPreference = new PersonalPreference();
		personalPreference.setTitle(title);
		personalPreference.setMessage(message);
		personalPreference.setSkip(skip);
		if(!skip && clzz != null) {
			// 未登录状态,跳转到登录页面
			personalPreference.setClzz(LoginActivity.class);
		}else if(skip) {
			personalPreference.setClzz(clzz);
		}
		return personalPreference;
	}

}
