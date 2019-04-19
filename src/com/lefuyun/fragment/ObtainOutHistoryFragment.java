package com.lefuyun.fragment;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lefuyun.R;
import com.lefuyun.adapter.ObtainOutHistoryAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseFragment;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.OlderEgress;
import com.lefuyun.ui.ObtainOutDetailActivity;
import com.lefuyun.ui.ObtainOutHistoryActivity;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class ObtainOutHistoryFragment extends BaseFragment implements OnRefreshListener, OnLoadListener {
	
	public static final String BUNDLE_OBTAIN_TYPE = "BUNDLE_OBTAIN_TYPE";
	
	private static final int INIT_STATE = 1;
	private static final int REFRESH_STATE = 2;
	private static final int LOAD_STATE = 3;
	
	private ObtainOutHistoryActivity mActivity;
	private OldPeople mOldPeople;
	
	private int mPageNo;
	
	private ImageView mNoDataImg;
	
	private int obtainType; // 1:进行中; 3:以完成
	private RefreshLayout mContainer;
	private ListView mListView;
	private List<OlderEgress> mList;
	private ObtainOutHistoryAdapter mAdapter;
	// 记录当前上拉加载数据是否还存在数据
	private boolean isLoadComplete;
	// 查询默认的截止时间, 0为截止目前所有的数据
	private long mTime;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (ObtainOutHistoryActivity) activity;
		mOldPeople = mActivity.getOldPeople();
		Bundle args = getArguments();
		if (args != null) {
			// 获取当前数据类型
			obtainType = args.getInt(BUNDLE_OBTAIN_TYPE, 0);
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_obtain_out_history;
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mNoDataImg = (ImageView) view.findViewById(R.id.no_data_obtain_out_history_fragment);
		mContainer = (RefreshLayout) view.findViewById(R.id.container_obtain_out_history_fragment);
		mListView = (ListView) view.findViewById(R.id.lv_obtain_out_history_fragment);
		mContainer.post(new Thread(new Runnable() {
			
			@Override
			public void run() {
				mContainer.setRefreshing(true);
				mContainer.setRefreshing(false);
			}
		}));
		mContainer.setColorSchemeResources(R.color.main_background);
		mContainer.setOnRefreshListener(this);
		if(obtainType != 1) {
			mContainer.setOnLoadListener(this);
		}
		mListView.setOnItemClickListener(new MyOnItemClickListener());
	}

	@Override
	protected void initData() {
		mPageNo = 1;
		getData(INIT_STATE, mPageNo);
		
	}
	
	private void getData(final int state, int pageNo) {
		if(state == INIT_STATE) {
			showWaitDialog();
		}
		LefuApi.getHistoryOfOBtainOut(obtainType, pageNo, mOldPeople.getId(), 
				mTime, new RequestCallback<List<OlderEgress>>() {
			
			@Override
			public void onSuccess(List<OlderEgress> result) {
				TLog.log("请假详情 result == " + result);
				if(state == INIT_STATE) {
					// 初始化状态
					mList = result;
					if(result.size() == 0) {
						mNoDataImg.setVisibility(View.VISIBLE);
					}else {
						mNoDataImg.setVisibility(View.GONE);
					}
					mAdapter = new ObtainOutHistoryAdapter(mActivity, mList, 
							R.layout.item_fragment_obtain_out_history);
					mListView.setAdapter(mAdapter);
					hideWaitDialog();
					if(obtainType == 3) {
						long time = 0;
						for (OlderEgress egress : mList) {
							if(egress.getLeave_state() == OlderEgress.LEAVE_STATE_COMPLETE) {
								time = egress.getLeave_hospital_dt();
								break;
							}
						}
						TLog.log("最后一次离院时间 = " + time);
						mActivity.setLastTimeOut(time);
					}
				}else if(state == REFRESH_STATE) {
					// 下拉刷新
					if(result.size() == 0) {
						mNoDataImg.setVisibility(View.VISIBLE);
					}else {
						mNoDataImg.setVisibility(View.GONE);
					}
					mList.clear();
					mList.addAll(result);
					mAdapter.notifyDataSetChanged();
					if(mContainer != null) {
						mContainer.setRefreshing(false);
					}
				}else if(state == LOAD_STATE) {
					// 加载更多
					if(result.size() == 0) {
						// 已经加载完了
						isLoadComplete = true;
						showToast("没有更多数据");
					}else {
						mList.addAll(result);
						mAdapter.notifyDataSetChanged();
					}
					if(mContainer != null) {
						mContainer.setLoading(false);
					}
				}
				
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				hideWaitDialog();
			}
		});
	}
	
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 查看新闻的详细内容
			OlderEgress egress = mList.get(position);
			Intent intent = new Intent(mActivity, ObtainOutDetailActivity.class);
			intent.putExtra("olderEgress", egress);
			startActivity(intent);
		}
		
	}
	
	@Override
	public void onClick(View v) {

	}
	
	@Override
	public void onLoad() {
		if(isLoadComplete) {
			showToast("没有更多数据");
			mContainer.setLoading(false);
			return;
		}
		mPageNo = mPageNo + 1;
		getData(LOAD_STATE, mPageNo);
	}

	@Override
	public void onRefresh() {
		mPageNo = 1;
		isLoadComplete = false;
		getData(REFRESH_STATE, mPageNo);
		
	}
	/**
	 * 根据用户的时间段进行数据刷新
	 * @param date
	 * @param i
	 */
	public void refreshData(Date date, int i) {
		if(obtainType == i) {
			mTime = date.getTime();
			onRefresh();
		}
	}

}
