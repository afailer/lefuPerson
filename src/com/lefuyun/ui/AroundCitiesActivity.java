package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.Organization;
import com.lefuyun.util.TLog;
import com.lefuyun.util.WindowUtil;
import com.lefuyun.widget.CircleImageView;

public class AroundCitiesActivity extends BaseActivity {
	
	public static final String ROUTE_PLAN_NODE = "ROUTE_PLAN_NODE";
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	private MyLocationListenner mMyLocationListenner;
	private boolean isFirstLoc = true;
	
	// Marker点击弹出条目控件
	private RelativeLayout mMarkerPopWindow;
	private CircleImageView mOrgImg;
	private TextView mOrgNameView, mOrgRemarkView;
	private TextView mOrgBedTotalView, mOrgPropertyView, mOrgLocationView;
	private TextView mOrgNavigationBtn, mOrgApplyBtn;
	
	private List<Organization> mMarkers;
	private Organization mCurrentOrganization;
	private BDLocation mCurrentBDLocation;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_around_citys;
	}

	@Override
	protected void initView() {
		
		initBaiduMap();
		
		initItemView();
		
	    initLocation();
	    
	    initListener();
	    
	    initSatnav();
		
	}
	/**
	 * 初始化导航信息
	 */
	private void initSatnav() {
		BaiduNaviManager.getInstance().init(this, Environment.getExternalStorageDirectory().getAbsolutePath(), "com.huihui.baidumap", 
				new BaiduNaviManager.NaviInitListener() {
					
					@Override
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							TLog.log("key校验成功");
		                } else {
		                	TLog.log("key校验失败");
		                }
						
					}
					
					@Override
					public void initSuccess() {
						TLog.log("百度导航引擎初始化成功");
					}
					
					@Override
					public void initStart() {
						TLog.log("百度导航引擎初始化开始");
					}
					
					@Override
					public void initFailed() {
						TLog.log("百度导航引擎初始化失败");
					}
				}, null, null, new BaiduNaviManager.TTSPlayStateListener() {
					
					@Override
					public void playStart() {
						TLog.log("playStart");
						
					}
					
					@Override
					public void playEnd() {
						TLog.log("playEnd");
						
					}
				});
		
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		long regionId = intent.getLongExtra("mRegionId", -1);
		if(regionId == -1 || regionId == 0) {
			return;
		}
		LefuApi.getAllOrganization(regionId, "", 0, new RequestCallback<List<Organization>>() {

			@Override
			public void onSuccess(List<Organization> result) {
				if(result != null) {
					mMarkers = result;
				}else {
					mMarkers = new ArrayList<Organization>();
				}
				if(mMarkers.size() > 0) {
					for (Organization organization : mMarkers) {
						addPoint(organization.getLatitude(), organization.getLongitude());
					}
					// 定位中心位置
					TLog.log("获取数据成功后,进行中心位置定位");
		            LatLng ll = new LatLng(mMarkers.get(0).getLatitude(),  
		            		mMarkers.get(0).getLongitude());  
		            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		            // 以动画的形式更新状态
		            mBaiduMap.animateMapStatus(u);
				}else {
					showToast("暂无结果");
				}
				
			}

			@Override
			public void onFailure(ApiHttpException e) {
				mMarkers = new ArrayList<Organization>();
				showToast(e.getMessage());
			}
			
		});
	}
	/**
	 * 初始化百度地图控件
	 */
	private void initBaiduMap() {
		mMapView = (MapView) findViewById(R.id.map_around_citys); 
	    mBaiduMap = mMapView.getMap();
	    // 设置地图的显示类型
	    // 普通类型
	    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	    mBaiduMap.setMyLocationEnabled(true);
	    
	    mLocationClient = new LocationClient(getApplicationContext());
	    mMyLocationListenner = new MyLocationListenner();
		mLocationClient.registerLocationListener(mMyLocationListenner);
	}
	/**
	 * 初始化条目控件
	 */
	private void initItemView() {
		// 初始化机构信息展示条目
		mMarkerPopWindow = (RelativeLayout) findViewById(R.id.item_around_citys_activity);
		int height = WindowUtil.getWindowHeight(this);
		int left = getResources().getDimensionPixelOffset(R.dimen.x8);
		mMarkerPopWindow.setPadding(left, 0, left, height / 8);
		mOrgImg = (CircleImageView) findViewById(R.id.image_item_around_citys_activity);
		mOrgNameView = (TextView) findViewById(R.id.name_item_around_citys_activity);
		mOrgRemarkView = (TextView) findViewById(R.id.remark_item_around_citys_activity);
		mOrgBedTotalView = (TextView) findViewById(R.id.bed_total_item_around_citys_activity);
		mOrgPropertyView = (TextView) findViewById(R.id.residual_bed_item_around_citys_activity);
		mOrgLocationView = (TextView) findViewById(R.id.location_item_around_citys_activity);
		mOrgNavigationBtn = (TextView) findViewById(R.id.event_item_around_citys_activity);
		mOrgApplyBtn = (TextView) findViewById(R.id.apply_item_around_citys_activity);
		mOrgNavigationBtn.setOnClickListener(this);
		mOrgApplyBtn.setOnClickListener(this);
		mMarkerPopWindow.setOnClickListener(this);
	}
	/**
	 * 设置条目控件的值
	 * @param organization
	 */
	private void setItemData(Organization organization) {
		ImageLoader.loadImg(organization.getExterior_pic(), mOrgImg);
		mOrgNameView.setText(organization.getAgency_name());
		mOrgRemarkView.setText(organization.getRemark());
		mOrgBedTotalView.setText(organization.getBed_total() + "");
		mOrgPropertyView.setText(organization.getAgency_property_text());
		mOrgLocationView.setText(organization.getRegion_name());
	}
	/**
	 * 初始化百度地图的监听控件
	 */
	private void initListener() {
		mBaiduMap.setOnMyLocationClickListener(new OnMyLocationClickListener() {
			
			@Override
			public boolean onMyLocationClick() {
				return false;
			}
		});
	    mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				LatLng latLng = marker.getPosition();
				Organization organization = null;
				for (Organization o : mMarkers) {
					if(o.getLatitude() == latLng.latitude && o.getLongitude() == latLng.longitude) {
						organization = o;
						mCurrentOrganization = o;
						break;
					}
				}
				if(organization != null) {
					setItemData(organization);
				}
				mMarkerPopWindow.setVisibility(View.VISIBLE);
				return false;
			}
		});
	    
	    mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {
			
			@Override
			public void onTouch(MotionEvent event) {
				mMarkerPopWindow.setVisibility(View.GONE);
			}
		});
	    mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			
			@Override
			public void onMapStatusChangeStart(MapStatus status) {
				// 手势操作地图，设置地图状态等操作导致地图状态开始改变.
			}
			
			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				// 地图状态改变结束
				loadAroundCities(status);
			}
			
			@Override
			public void onMapStatusChange(MapStatus status) {
				// 地图状态变化中
			}
		});
	}
	/**
	 * 加载当前定位周边的养老机构
	 * @param status
	 */
	private void loadAroundCities(MapStatus status) {
		LefuApi.getOrganizationByLocation(status, new RequestCallback<List<Organization>>() {
			
			@Override
			public void onSuccess(List<Organization> result) {
				mBaiduMap.clear();
				if(mMarkers != null && result != null) {
					mMarkers.clear();
					mMarkers.addAll(result);
				}else if(mMarkers != null && result == null) {
					mMarkers.clear();
				}else if(mMarkers == null && result != null) {
					mMarkers = result;
				}else {
					mMarkers = new ArrayList<Organization>();
				}
				if(mMarkers.size() > 0) {
					for (Organization organization : mMarkers) {
						addPoint(organization.getLatitude(), organization.getLongitude());
					}
				}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.event_item_around_citys_activity:
			// 跳转到导航页面
			if(mCurrentOrganization != null && mCurrentBDLocation != null) {
				TLog.log("点击事件 ");
				routeplanToNavi(mCurrentBDLocation, mCurrentOrganization);
			}
			break;
		case R.id.apply_item_around_citys_activity:
			// 跳转到预约养老机构
			if(mCurrentOrganization != null) {
				Intent intent = new Intent(this, ReserveOrganization.class);
				intent.putExtra("organization", mCurrentOrganization);
				startActivity(intent);
				mMarkerPopWindow.setVisibility(View.GONE);
			}
			break;
		case R.id.item_around_citys_activity:
			// 跳转机构活动页面
			if(mCurrentOrganization != null) {
				Intent intent = new Intent(this, ExploreDetailsActivity.class);
				intent.putExtra("organization", mCurrentOrganization);
				startActivity(intent);
				mMarkerPopWindow.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	private void routeplanToNavi(BDLocation bDLocation, Organization o) {
		TLog.log("创建起始点");
	    BNRoutePlanNode sNode = new BNRoutePlanNode(bDLocation.getLongitude(), bDLocation.getLatitude(), bDLocation.getDistrict(), null, CoordinateType.BD09LL);
	    BNRoutePlanNode eNode = new BNRoutePlanNode(o.getLongitude(), o.getLatitude(), o.getRegion_name(), null, CoordinateType.BD09LL);
        if (sNode != null && eNode != null) {
        	TLog.log("创建起始点-----------");
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
	}
	/**
	 * 初始化百度定位功能,实现定位
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
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
	 * 添加Marker节点
	 * @param latitude 经度
	 * @param longitude 纬度
	 */
	private void addPoint(double latitude, double longitude) {
		//定义Maker坐标点  
	    LatLng point = new LatLng(latitude, longitude);  
	    //构建Marker图标  
	    BitmapDescriptor bitmap = BitmapDescriptorFactory  
	        .fromResource(R.drawable.icon_marka);  
	    //构建MarkerOption，用于在地图上添加Marker  
	    OverlayOptions option = new MarkerOptions()  
	        .position(point)
	        .zIndex(9)
	        .icon(bitmap);  
	    //在地图上添加Marker，并显示  
	    mBaiduMap.addOverlay(option);
	}
	
	/** 
	* 定位SDK监听函数 
	*/  
	public class MyLocationListenner implements BDLocationListener {  
	 
		@SuppressLint("NewApi")
		@Override  
	    public void onReceiveLocation(BDLocation location) {
			mCurrentBDLocation = location;
	        // map view 销毁后不在处理新接收的位置  
	        if (location == null || mMapView == null)  
	            return;  
	        MyLocationData locData = new MyLocationData.Builder()
	                .accuracy(location.getRadius())  
	                // 此处设置开发者获取到的方向信息，顺时针0-360  
	                .direction(100).latitude(location.getLatitude())
	                .longitude(location.getLongitude()).build(); 
	        mBaiduMap.setMyLocationData(locData);
	        TLog.log("定位成功后成功后,进行中心位置定位");
	        if (isFirstLoc) {  
	            isFirstLoc = false;  
	            BitmapDescriptor resource = BitmapDescriptorFactory.fromResource(R.drawable.location);
		        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, resource);
		        mBaiduMap.setMyLocationConfigeration(configuration);
		        Intent intent = getIntent();
				long regionId = intent.getLongExtra("mRegionId", -1);
				if(regionId == 0) {
					MapStatus status = mBaiduMap.getMapStatus();
					TLog.log("MapStatus == " + status);
					loadAroundCities(status);
				}
	        }  
	    }  
	}
	
	public class DemoRoutePlanListener implements RoutePlanListener {
		 
	    private BNRoutePlanNode mBNRoutePlanNode = null;
	 
	    public DemoRoutePlanListener(BNRoutePlanNode node) {
	        mBNRoutePlanNode = node;
	    }
	 
	    @Override
	    public void onJumpToNavigator() {
	        /*
	         * 设置途径点以及resetEndNode会回调该接口
	         */
	        Intent intent = new Intent(AroundCitiesActivity.this, SatnavActivity.class);
	        Bundle bundle = new Bundle();
	        bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
	        intent.putExtras(bundle);
	        startActivity(intent);
	        mMarkerPopWindow.setVisibility(View.GONE);
	    }
	 
	    @Override
	    public void onRoutePlanFailed() {
	    	showToast("算路失败");
	    }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	
	@Override
	protected void onDestroy() {
		mLocationClient.unRegisterLocationListener(mMyLocationListenner);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}
