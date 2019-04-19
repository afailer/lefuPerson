package com.lefuyun.ui;
import java.util.List;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.OrgAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OrgActive;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

//机构活动页面
public class OrgActivity extends BaseActivity implements OnLoadListener, OnRefreshListener {
	
	private static final int INIT_STATE = 1; // 初始化状态
	private static final int REFRESH_STATE = 2; // 刷新状态
	private static final int LOAD_STATE = 3; // 上拉加载跟多的状态
	
	private int mCurrentState; // 记录当前的状态

	private long mAgencyId; // 当前机构ID
	private String mAgencyName; // 当前机构名称
	// 当前机构的名称以及最新活动时间
	private TextView mAgencyNameView, mLastTimeView;
	
	private ListView mListView;
	private OrgAdapter mAdapter; // ListView的适配器
	private List<OrgActive> mList;
	private int mPageNo; // 当前要请求数据的页号
	// 上拉加载数据是否已经加载完毕
	private boolean isLoadComplete;
	
	private View mEmptyView; // 没有数据时候的布局
	
	private int mCurrentClickItem;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_org;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("机构活动");
		
		mAgencyNameView = (TextView) findViewById(R.id.org_activities_agency_name);
		mLastTimeView = (TextView) findViewById(R.id.active_latest_time);
		mListView = (ListView) findViewById(R.id.new_list);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				mCurrentClickItem = position;
				OrgActive orgActive = mList.get(position);
				Intent intent = new Intent(OrgActivity.this, OrgDetailActivity.class);
				intent.putExtra("OrgActive", orgActive);
				startActivityForResult(intent, 100);
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				if(mAdapter!=null && mListView.getLastVisiblePosition()==mAdapter.getCount()-1){
					mCurrentState = LOAD_STATE;
					if(isLoadComplete) {
						showToast("没有更多数据");
						return;
					}
					mPageNo = mPageNo + 1;
					loadData();
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		mEmptyView = findViewById(R.id.real_nodata);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 200) {
			int num = data.getIntExtra("num", 0);
			OrgActive orgActive = mList.get(mCurrentClickItem);
			orgActive.setPraise_number(orgActive.getPraise_number() + num);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mAgencyId = intent.getLongExtra("id", -1);
		mAgencyName = intent.getStringExtra("name");
		mAgencyNameView.setText(mAgencyName);
		// 初始化数据的状态
		mCurrentState = INIT_STATE;
		mPageNo = 1;
		loadData();
	}
	
	/**
	 * 从网络中加载数据
	 */
	private void loadData(){
		if(mCurrentState == INIT_STATE) {
			// 初始化状态
			showWaitDialog();
		}
		LefuApi.queryActivitesListWithArea(0, mAgencyId, mPageNo, new RequestCallback<List<OrgActive>>() {
			
			@Override
			public void onSuccess(List<OrgActive> result) {
				if(mCurrentState == INIT_STATE) {
					// 初始化状态
					mList = result;
					if(result.size() == 0) {
						mEmptyView.setVisibility(View.VISIBLE);
					}else if(result != null && result.size() > 0) {
						mLastTimeView.setText(StringUtils.friendly_time(result.get(0).getHoldTime()));
					}
					mAdapter = new OrgAdapter(OrgActivity.this, mList, 
							R.layout.item_fragment_news_details);
					mListView.setAdapter(mAdapter);
					hideWaitDialog();
				}else if(mCurrentState == REFRESH_STATE) {
					// 下拉刷新
					if(result != null && result.size() > 0) {
						mLastTimeView.setText(StringUtils.friendly_time(result.get(0).getHoldTime()));
					}
					mList.clear();
					mList.addAll(result);
					mAdapter.notifyDataSetChanged();
				}else if(mCurrentState == LOAD_STATE) {
					// 加载更多
					if(result.size() == 0) {
						// 已经加载完了
						isLoadComplete = true;
						showToast("没有更多数据");
					}else {
						mList.addAll(result);
						mAdapter.notifyDataSetChanged();
					}
				}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
				if(mCurrentState == INIT_STATE) {
					hideWaitDialog();
				}else if(mCurrentState == REFRESH_STATE) {
				}else if(mCurrentState == LOAD_STATE) {
				}
			}
		});
	}
	
	@Override
	public void onLoad() {
		mCurrentState = LOAD_STATE;
		if(isLoadComplete) {
			showToast("没有更多数据");
			return;
		}
		mPageNo = mPageNo + 1;
		loadData();
	}

	@Override
	public void onRefresh() {
		mPageNo = 1;
		isLoadComplete = false;
		mCurrentState = REFRESH_STATE;
		loadData();
	}
	
	@Override
	public void onClick(View arg0) {
		
	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
}
