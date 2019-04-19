package com.lefuyun.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.adapter.OrganizationFragmentAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseFragment;
import com.lefuyun.bean.City;
import com.lefuyun.bean.Organization;
import com.lefuyun.db.CityDao;
import com.lefuyun.ui.AroundCitiesActivity;
import com.lefuyun.ui.ExploreDetailsActivity;
import com.lefuyun.ui.SearchActivity;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class OrganizationFragment extends BaseFragment implements OnRefreshListener, OnLoadListener{

	private static final int INIT_STATE = 1;
	private static final int REFRESH_STATE = 2;
	private static final int LOAD_MORE_STATE = 3;
	
	private long mRegionId; // 查询的区域ID, 0为查找全部
	private String mSortWay; // 排序方式
	
	// 首页提示点击按钮
	private TextView mSortBtn;
	private RelativeLayout mSortContainer;
	
	private RelativeLayout mActionBar;
	private List<Organization> mOrganizationList;
	private ListView mListView;
	private OrganizationFragmentAdapter mAdapter;
	private RefreshLayout mContainer;
	private boolean isLoadComplete;
	
	private int mCurrentPageNo;
	private TextView mRightTextView;
	private TextView mLeftTextView;
	private PopupWindow mPopupWindow;
	private TextView mCharWayView;
	private TextView mBedTotalView;
	private TextView mBedSurplusView;
	private View mContentView;
	// 加载城市列表数据
	private CityDao mCityDao;
	private boolean isLoadingSuccess;
	
	// 定位模块
	private LocationClient mLocationClient;
	private BDLocation mBDLocation;
	private boolean isFirstLoc = true;
	
	// 定位和加载城市列表网络数据请求完成个数
	private int mLoadingSuccessNum;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_organization;
	}
	
	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		mCityDao = new CityDao(getActivity().getApplicationContext());
		initMainPage(view);
		initLocationClient();
		initPopWindow();
	}
	
	/**
	 * 初始化定位功能
	 */
	private void initLocationClient() {
		showWaitDialog();
		mLocationClient = new LocationClient(getActivity().getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();
		// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 10;
//        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
	}
	
	/** 
	* 定位SDK监听函数 
	*/  
	public class MyLocationListenner implements BDLocationListener {  
	 
		@SuppressLint("NewApi")
		@Override  
	    public void onReceiveLocation(BDLocation location) {  
	        // map view 销毁后不在处理新接收的位置  
			mBDLocation = location;
			if(isFirstLoc) {
				isFirstLoc = false;
				// 统计数据
				statisticNetWork();
			}
	    }
	 
	}
	
	/**
	 * 统计网络的请求数
	 */
	private synchronized void statisticNetWork() {
		TLog.log("某一项数据加载完成数据加载完成");
		mLoadingSuccessNum++;
		if(mLoadingSuccessNum == 2) {
			TLog.log("");
			if(mBDLocation != null) {
				List<City> citys = mCityDao.findCityByRegionName(mBDLocation.getCity());
				if(citys.size() > 0) {
					City city = citys.get(0);
					mRegionId = city.getId();
					mRightTextView.setText(city.getRegion_name());
				}else {
					mRegionId = 0;
					mRightTextView.setText("全部");
				}
			}else{
				mRegionId = 0;
				mRightTextView.setText("全部");
			}
			// 初始化数据
			initMainPageData();
			mLoadingSuccessNum = 0;
		}
		
	}

	private void initPopWindow() {
		mContentView = mInflater.inflate(R.layout.pop_window_fragment_explore, null);
		mContentView.setPadding(0, 0, 0, 0);
		mCharWayView = (TextView) mContentView.findViewById(R.id.char_pop_window_explore);
		mBedTotalView = (TextView) mContentView.findViewById(R.id.bed_total_pop_window_explore);
		mBedSurplusView = (TextView) mContentView.findViewById(R.id.bed_surplus_pop_window_explore);
		mCharWayView.setOnClickListener(this);
		mBedTotalView.setOnClickListener(this);
		mBedSurplusView.setOnClickListener(this);
	}

	/**
	 * 初始化主要页面
	 * @param view
	 */
	private void initMainPage(View view) {
		// 初始化标题栏
		mActionBar = (RelativeLayout) view.findViewById(R.id.item_action_bar);
		mActionBar.setBackgroundResource(R.color.window_background);
		TextView titleView = (TextView) view.findViewById(R.id.title_action_bar);
		mLeftTextView = (TextView) view.findViewById(R.id.back_action_bar);
		mLeftTextView.setVisibility(View.VISIBLE);
		mLeftTextView.setText("搜周边");
		mLeftTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mLeftTextView.setOnClickListener(this);
		
		mRightTextView = (TextView) view.findViewById(R.id.text_right_action_bar);
		mRightTextView.setText("全部");
		mRightTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_bottom_small, 0);
		mRightTextView.setCompoundDrawablePadding(20);
		mRightTextView.setOnClickListener(this);
		titleView.setText("发现");
		mContainer = (RefreshLayout) view.findViewById(R.id.container_organization_fragment);
		mContainer.post(new Thread(new Runnable() {
			
			@Override
			public void run() {
				mContainer.setRefreshing(true);
				mContainer.setRefreshing(false);
			}
		}));
		mContainer.setColorSchemeResources(R.color.main_background);
		mListView = (ListView) view.findViewById(R.id.lv_organization_fragment);
		mContainer.setOnRefreshListener(this);
		mContainer.setOnLoadListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity, ExploreDetailsActivity.class);
				intent.putExtra("organization", mOrganizationList.get(position));
				startActivity(intent);
			}
		});
		mSortBtn = (TextView) view.findViewById(R.id.sort_organization_fragment);
		mSortContainer = (RelativeLayout) view.findViewById(R.id.rl_sort_organization_fragment);
		mSortBtn.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		TLog.log("城市列表从网络中拿到数据");
		isLoadingSuccess = false;
		LefuApi.getCitys(new RequestCallback<List<City>>() {

			@Override
			public void onSuccess(List<City> result) {
				statisticNetWork();
				isLoadingSuccess = true;
				mCityDao.addAll(result);
				AppContext.setLoadCityState(true);
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				statisticNetWork();
				showToast(e.getMessage());
			}
		});

	}
	
	/**
	 * 初始化主页面数据
	 */
	private void initMainPageData() {
		isLoadComplete = false;
		mCurrentPageNo = 1;
		// 默认的排序方式是按字母排序
		mSortWay = "";
		loadData(mCurrentPageNo, INIT_STATE);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_action_bar:
			// 搜周边
			Intent intent1 = new Intent(mActivity, AroundCitiesActivity.class);
			intent1.putExtra("mRegionId", mRegionId);
			startActivity(intent1);
			break;
		case R.id.text_right_action_bar:
			// 选择地方
			if(isLoadingSuccess) {
				Intent intent = new Intent(mActivity, SearchActivity.class);
				startActivityForResult(intent, 100);
			}else {
				showToast("数据正在加载中...");
			}
			break;
		case R.id.sort_organization_fragment:
			// 排序
			showPopWindow(mContentView, mSortContainer);
			break;
		case R.id.char_pop_window_explore:
			// 按字母排序
			if(!mSortWay.equals("")) {
				mSortWay = "";
				mCharWayView.setTextColor(Color.parseColor("#06BEBD"));
				mBedTotalView.setTextColor(Color.parseColor("#C7C7CD"));
				mBedSurplusView.setTextColor(Color.parseColor("#C7C7CD"));
				onRefresh();
			}
			if(mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.bed_total_pop_window_explore:
			// 按床位数排序
			if(!mSortWay.equals("bed_total")) {
				mSortWay = "bed_total";
				mCharWayView.setTextColor(Color.parseColor("#C7C7CD"));
				mBedTotalView.setTextColor(Color.parseColor("#06BEBD"));
				mBedSurplusView.setTextColor(Color.parseColor("#C7C7CD"));
				onRefresh();
			}
			if(mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.bed_surplus_pop_window_explore:
			// 按剩余床位数排序
			if(!mSortWay.equals("bed_surplus")) {
				mSortWay = "bed_surplus";
				mCharWayView.setTextColor(Color.parseColor("#C7C7CD"));
				mBedTotalView.setTextColor(Color.parseColor("#c7c7cd"));
				mBedSurplusView.setTextColor(Color.parseColor("#06BEBD"));
				onRefresh();
			}
			if(mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param pageNo
	 * @param loadType
	 */
	private void loadData(int pageNo, final int loadType) {
		LefuApi.getAllOrganization(mRegionId, mSortWay, pageNo, new RequestCallback<List<Organization>>() {

			@Override
			public void onSuccess(List<Organization> result) {
				if(loadType == INIT_STATE) {
					mOrganizationList = result;
					mAdapter = new OrganizationFragmentAdapter(mActivity, 
							mOrganizationList, R.layout.item_fragment_organization);
					mListView.setAdapter(mAdapter);
					hideWaitDialog();
				}else if(loadType == REFRESH_STATE) {
					mOrganizationList.clear();
					mOrganizationList.addAll(result);
					mAdapter.notifyDataSetChanged();
					mContainer.setRefreshing(false);
				}else if(loadType == LOAD_MORE_STATE) {
					if(result.size() == 0) {
						// 已经加载完了
						isLoadComplete = true;
						showToast("没有更多数据");
					}else {
						mOrganizationList.addAll(result);
						mAdapter.notifyDataSetChanged();
					}
					mContainer.setLoading(false);
				}
			}

			@Override
			public void onFailure(ApiHttpException e) {
				showToast(e.getMessage());
				if(loadType == INIT_STATE) {
					hideWaitDialog();
				}else if(loadType == REFRESH_STATE) {
					mContainer.setRefreshing(false);
				}else if(loadType == LOAD_MORE_STATE) {
					mContainer.setLoading(false);
				}
			}
			
		});
	}
	
	public void setCurrentCity(City city) {
		if(city != null) {
			mRegionId = city.getId();
			mRightTextView.setText(city.getRegion_name());
			onRefresh();
		}
	}

	@Override
	public void onLoad() {
		if(isLoadComplete) {
			showToast("没有更多数据");
			mContainer.setLoading(false);
			return;
		}
		mCurrentPageNo=mCurrentPageNo+1;
		loadData(mCurrentPageNo, LOAD_MORE_STATE);
	}

	@Override
	public void onRefresh() {
		mCurrentPageNo = 1;
		isLoadComplete = false;
		loadData(mCurrentPageNo, REFRESH_STATE);
	}
	
	@SuppressWarnings("deprecation")
	private void showPopWindow(View contentView, View view) {
		// 创建PopupWindow
		mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.showAsDropDown(view, 0, 0);
	}

}
