package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.adapter.ConcernElderCancelActivityAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.dialog.ConfirmDialogFragment;

public class ConcernElderlyCancelActivity extends BaseActivity {
	
	private GridView mGridView;
	private ConcernElderCancelActivityAdapter mAdapter;

	// 是否是初始化数据
	private boolean isInitData;
	// 保存绑定老人对象
	private List<OldPeople> mList;
	// 保存要解绑的老人
	private List<OldPeople> mUnBindingList;
	// 添加新的老人点击按钮
	private TextView mConfirmBtn, mAllSelectBtn;
	// 解绑成功的个数
	private int successNum;
	// 解绑失败的个数
	private int failNum;
	// 数据是否有更新
	private boolean isRefresh;
	// 记录当前是否是全选
	private boolean isAllSelect;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_concern_elder_cancel;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("取消关注");
		mGridView = (GridView) findViewById(R.id.gv_concern_elder_cancel_activity);
		mConfirmBtn = (TextView) findViewById(R.id.btn_elder_cancel_activity);
		mConfirmBtn.setOnClickListener(this);
		mAllSelectBtn = (TextView) findViewById(R.id.select_elder_cancel_activity);
		mAllSelectBtn.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		isInitData = true;
		mUnBindingList = new ArrayList<OldPeople>();
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
					mAdapter = new ConcernElderCancelActivityAdapter(
							ConcernElderlyCancelActivity.this, mList, 
							R.layout.item_activity_concern_elder_cancel);
					mGridView.setAdapter(mAdapter);
					mGridView.setOnItemClickListener(new MyOnItemClickListener());
					isInitData = false;
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
		switch (v.getId()) {
		case R.id.btn_elder_cancel_activity:
			checkUnBindingInfo();
			break;
		case R.id.select_elder_cancel_activity:
			// 全选
			setSelect(!isAllSelect);
			break;

		default:
			break;
		}
	}
	/**
	 * 是否全选
	 * @param isAll
	 */
	private void setSelect(boolean isAll) {
		setSelectViewState(isAll);
		for (OldPeople oldpeople : mList) {
			oldpeople.setSelect(isAll);
		}
		mAdapter.notifyDataSetChanged();
	}
	/**
	 * 是否全选
	 * @param flag
	 */
	private void setSelectViewState(boolean flag) {
		isAllSelect = flag;
		if(flag) {
			// 全选
			mAllSelectBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accept_tick, 0, 0, 0);
		}else {
			// 全不选
			mAllSelectBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.accept_tick_no, 0, 0, 0);
		}
	}

	/**
	 * 校验是否有老人被选中,如果有提示是否要进行解绑
	 */
	private void checkUnBindingInfo() {
		// 清空内容
		mUnBindingList.clear();
		for(OldPeople elder : mList) {
			if(elder.isSelect()) {
				TLog.log("elder == " + elder);
				mUnBindingList.add(elder);
			}
		}
		if(mUnBindingList.size() == 0) {
			showToast("请选择需要取消关注的人！");
			return;
		}
		// 解除与指定老人的绑定
		showConfirmDialog("提示", "是否取消关注？", 
				new ConfirmDialogFragment.OnClickListener() {
			
			@Override
			public void onPositiveClick(View v) {
				unBundingElder();
			}
			
			@Override
			public void onNegativeClick(View v) {}
		});
	}

	/**
	 * 解除绑定的老人
	 */
	private void unBundingElder() {
		showWaitDialog();
		successNum = 0;
		failNum = 0;
		for (OldPeople elder : mUnBindingList) {
			LefuApi.unBindingElders(elder.getMapId(), new RequestCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					statisticNetWork(true);
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					statisticNetWork(false);
				}
			});
		}
		
	}
	
	/**
	 * 统计网络的请求数
	 */
	private synchronized void statisticNetWork(boolean isSuccess) {
		if(isSuccess) {
			successNum++;
		}else {
			failNum++;
		}
		if(mUnBindingList.size() == successNum + failNum) {
			if(successNum == mUnBindingList.size()) {
				isRefresh = true;
				showToast("取消关注成功");
				hideWaitDialog();
				// 更新数据
				getData();
			}else if(failNum == mUnBindingList.size()) {
				showToast("取消关注失败");
				hideWaitDialog();
			}else {
				isRefresh = true;
				showToast("有" + failNum + "个取消关注失败");
				hideWaitDialog();
				getData();
			}
		}
	}
	
	/**
	 * GridView条目点击事件
	 */
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CircleImageView cView = (CircleImageView) view.findViewById(R.id.img_item_concern_elder);
			OldPeople oldPeople = mList.get(position);
			oldPeople.setSelect(!oldPeople.isSelect());
			cView.setmSelectOn(oldPeople.isSelect());
			// 记录当前条目是否有未被选中的
			boolean flag = true;
			for (OldPeople o : mList) {
				if(o.isSelect()) {
					continue;
				}else {
					// 存在未全选的条目, 停止检测
					flag = false;
					break;
				}
			}
			setSelectViewState(flag);
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
	public void finish() {
		if(isRefresh) {
			setResult(AppContext.BINDING_SUCCESS);
		}
		super.finish();
	}

}
