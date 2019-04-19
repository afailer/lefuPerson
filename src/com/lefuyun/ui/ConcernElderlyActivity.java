package com.lefuyun.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.adapter.ConcernElderActivityAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.TLog;
import com.lefuyun.util.WindowUtil;
import com.lefuyun.zxing.CaptureActivity;

public class ConcernElderlyActivity extends BaseActivity {
	
	private GridView mGridView;
	private PopupWindow mPopupWindow;
	private ConcernElderActivityAdapter mAdapter;
	// actionBar对象
	private RelativeLayout mActionBar;
	// 是否是初始化数据
	private boolean isInitData;
	// 保存绑定老人对象
	private List<OldPeople> mList;
	// 添加新的老人点击按钮
	private TextView mAddBtn;
	// 判断页面是否从我的页面跳转过来
	private boolean isAnimator;
	// 当前用户ID
	private long mUserId;
	// 是否要进行刷新
	private boolean isRefresh;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_concern_elder;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("我的关注");
		mGridView = (GridView) findViewById(R.id.gv_concern_elder_activity);
		mActionBar = (RelativeLayout) findViewById(R.id.item_action_bar);
		mAddBtn = (TextView) findViewById(R.id.btn_elder_activity);
		mAddBtn.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		Intent intent = getIntent();
		isAnimator = intent.getBooleanExtra("isAnimator", false);
		mUserId = intent.getLongExtra("userId", 0);
		isInitData = true;
		// 初始化数据
		getData();
	}
	/**
	 * 从服务器获取数据
	 */
	private void getData() {
		showWaitDialog();
		LefuApi.getAllElders(new RequestCallback<List<OldPeople>>() {
			
			@Override
			public void onSuccess(List<OldPeople> result) {
				if(isInitData || mList == null) {
					mList = result;
					if(mList != null) {
						mAdapter = new ConcernElderActivityAdapter(
								ConcernElderlyActivity.this, mList, 
								R.layout.item_activity_concern_elder);
						mGridView.setAdapter(mAdapter);
						mGridView.setOnItemClickListener(new MyOnItemClickListener());
						isInitData = false;
					}
				}else {
					mList.clear();
					mList.addAll(result);
					mAdapter.notifyDataSetChanged();
				}
				hideWaitDialog();
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				TLog.log("ApiHttpException e == " + e.toString());
				showToast(e.getMessage());
				hideWaitDialog();
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_elder_activity:
			// 获取ActionBar的高度
			int height = (int) getResources().getDimension(R.dimen.title_bar_height);
			// 获取状态栏的高度
			int paddingTop = WindowUtil.getStatusBarHeight(this);
			// 获取布局上移的距离
			height = 0 - paddingTop - height;
			showPopWindow(mActionBar, 0, height, paddingTop);
			break;
		case R.id.scan_pop_window_concern_elder:
			// 扫一扫按钮被点击
			intent = new Intent(this, CaptureActivity.class);
			intent.putExtra("userId", mUserId);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, 100);
			if(mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.invite_pop_window_concern_elder:
			// 邀请码按钮被点击
			intent = new Intent(this, ConcernElderlyInviteActivity.class);
			intent.putExtra("userId", mUserId);
			startActivityForResult(intent, 100);
			if(mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.directly_pop_window_concern_elder:
			// 直接添加按钮被点击
			intent = new Intent(this, ConcernElderlyDirectlyActivity.class);
			startActivityForResult(intent, 100);
			if(mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
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
	protected String getActionBarRightText() {
		return "取消关注";
	}

	@Override
	public void onRightViewClick(View view) {
		// 跳转到解除绑定的页面
		Intent intent = new Intent(this, ConcernElderlyCancelActivity.class);
		startActivityForResult(intent, 100);
	}
	
	@SuppressWarnings("deprecation")
	private void showPopWindow(View view, int xoff, int yoff, int paddingTop) {
		// 初始化控件
		View contentView = mInflater.inflate(R.layout.pop_window_activity_concern_elder, null);
		contentView.setPadding(0, paddingTop, 0, 0);
		LinearLayout scanView = (LinearLayout) contentView.findViewById(R.id.scan_pop_window_concern_elder);
		LinearLayout inviteView = (LinearLayout) contentView.findViewById(R.id.invite_pop_window_concern_elder);
		LinearLayout directlyView = (LinearLayout) contentView.findViewById(R.id.directly_pop_window_concern_elder);
		scanView.setOnClickListener(this);
		inviteView.setOnClickListener(this);
		directlyView.setOnClickListener(this);
		// 创建PopupWindow
		mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.showAsDropDown(view, xoff, yoff);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == AppContext.BINDING_SUCCESS_SKIP) {
			long id = data.getLongExtra("currentOldPeopleID", 0);
			if(id > 0) {
				Intent intent = new Intent();
				intent.putExtra("currentOldPeopleID", id);
				setResult(AppContext.BINDING_SUCCESS_SKIP, intent);
			}else {
				setResult(AppContext.BINDING_SUCCESS);
			}
			finish();
		}else if(resultCode == AppContext.BINDING_SUCCESS) {
			isRefresh = true;
			// 更新数据
			getData();
		}
	}
	/**
	 * GridView条目点击事件
	 */
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			OldPeople oldPeople = mList.get(position);
			if("0".equals(oldPeople.getStatus())) {
				// 审核中
				showToast("审核中");
				return;
			}
			Intent data = new Intent();
			data.putExtra("currentOldPeopleID", mList.get(position).getId());
			setResult(200, data);
			finish();
		}
		
	}
	
	@Override
	public void finish() {
		if(isRefresh) {
			// 数据有更新
			Intent intent = new Intent();
			intent.putExtra("elderlyNum", mList.size());
			setResult(AppContext.BINDING_SUCCESS, intent);
		}
		super.finish();
		// 设置当前activity关闭时的动画
		if(isAnimator) {
			overridePendingTransition(R.animator.slide_left_in, R.animator.slide_right_out);
		}
	}

}
