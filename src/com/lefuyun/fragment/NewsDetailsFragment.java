package com.lefuyun.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lefuyun.R;
import com.lefuyun.adapter.NewsDetailsFragmentAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseFragment;
import com.lefuyun.bean.News;
import com.lefuyun.ui.NewsDetailsActivity;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class NewsDetailsFragment extends BaseFragment implements OnRefreshListener, OnLoadListener {
	
	public static final String BUNDLE_NEWS_TYPE = "BUNDLE_NEWS_TYPE";
	
	private static final int INIT_STATE = 1; // 初始化状态
	private static final int REFRESH_STATE = 2; // 刷新状态
	private static final int LOAD_STATE = 3; // 上拉加载跟多的状态
	
	private int mPageNo;
	
	private int newsType;
	private List<News> mList;
	private RefreshLayout mContainer;
	private ListView mListView;
	private NewsDetailsFragmentAdapter mAdapter;
	// 记录当前上拉加载数据是否还存在数据
	private boolean isLoadComplete;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_news_details;
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mListView = (ListView) view.findViewById(R.id.lv_news_details_fragment);
		mListView.setOnItemClickListener(new MyOnItemClickListener());
		mContainer = (RefreshLayout) view.findViewById(R.id.container_news_details_fragment);
		mContainer.post(new Thread(new Runnable() {
			
			@Override
			public void run() {
				mContainer.setRefreshing(true);
				mContainer.setRefreshing(false);
			}
		}));
		mContainer.setColorSchemeResources(R.color.main_background);
		mContainer.setOnRefreshListener(this);
		mContainer.setOnLoadListener(this);
	}

	@Override
	protected void initData() {
		Bundle args = getArguments();
		if (args != null) {
			// 获取当前新闻的类型
			newsType = args.getInt(BUNDLE_NEWS_TYPE, -1);
		}
		mPageNo = 1;
		getData(INIT_STATE, mPageNo);
	}
	
	private void getData(final int state, int pageNo) {
		if(state == INIT_STATE) {
			showWaitDialog();
		}
		LefuApi.getAgencyNews(newsType, pageNo, new RequestCallback<List<News>>() {
			
			@Override
			public void onSuccess(List<News> result) {
				if(state == INIT_STATE) {
					// 初始化状态
					mList = result;
					mAdapter = new NewsDetailsFragmentAdapter(mActivity, mList, 
							R.layout.item_fragment_news_details);
					mListView.setAdapter(mAdapter);
					hideWaitDialog();
				}else if(state == REFRESH_STATE) {
					// 下拉刷新
					mList.clear();
					mList.addAll(result);
					mAdapter.notifyDataSetChanged();
					mContainer.setRefreshing(false);
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
					mContainer.setLoading(false);
				}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
				if(state == INIT_STATE) {
					hideWaitDialog();
				}else if(state == REFRESH_STATE) {
					mContainer.setRefreshing(false);
				}else if(state == LOAD_STATE) {
					mContainer.setLoading(false);
				}
			}
		});
	}
	
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 查看新闻的详细内容
			News news = mList.get(position);
			Intent intent = new Intent(mActivity, NewsDetailsActivity.class);
			intent.putExtra("new", news);
			mActivity.startActivity(intent);
			news.setRead_number(news.getRead_number() + 1);
		}
		
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
	
	@Override
	public void onClick(View v) {
		
	}
	
}
