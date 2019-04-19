package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.SignDataBean;
import com.lefuyun.healthcondition.SignDataRecordAdapter;
import com.lefuyun.util.DateUtils;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.SignDataUtils;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.util.share.ConstantUtil;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;
import com.lefuyun.widget.pickerview.TimePickerView;
import com.lefuyun.widget.pickerview.TimePickerView.OnTimeSelectListener;

/**
 * @author chenshichao
 * @date   2016-5-11
 * @description 体征数据记录
 */
public class SignDataRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadListener{
	CircleImageView image_oldicon;
    TextView tv_oldname;
    TextView tv_last_time;
    ListView listview;
    RefreshLayout refreshlayout;
    int signType;
    OldPeople oldPeople;
    int pageNo=1;
    long inspect_dt;
    long current_dt;
    List<SignDataBean>signDataBeans_totallist=new ArrayList<SignDataBean>();
    SignDataRecordAdapter signDataRecordAdapter;
	// 时间选择控件
	private TimePickerView mTimePickerView;
	
    RelativeLayout  real_nodata;
	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_signdata_record;
	}

	@Override
	protected void initView() {
		image_oldicon=(CircleImageView) findViewById(R.id.image_oldicon);
		tv_oldname=(TextView) findViewById(R.id.tv_oldname);
		tv_last_time=(TextView) findViewById(R.id.tv_last_time);
		listview=(ListView) findViewById(R.id.listview);
		refreshlayout=(RefreshLayout) findViewById(R.id.refreshlayout);
		real_nodata=(RelativeLayout) findViewById(R.id.real_nodata);
		refreshlayout.post(new Thread(new Runnable() {
			
			@Override
			public void run() {
				refreshlayout.setRefreshing(true);
				refreshlayout.setRefreshing(false);
			}
		}));
		refreshlayout.setColorSchemeResources(R.color.main_background);
		refreshlayout.setOnRefreshListener(this);
		refreshlayout.setOnLoadListener(this);
	}

	@Override
	protected void initData() {
		signType=getIntent().getIntExtra("signType", 0);
		setActionBarTitle(SignDataUtils.getSigndataStr(signType));
		oldPeople=(OldPeople) getIntent().getSerializableExtra("oldPeople");
		String currentstr=DateUtils.dateFormatYMD(System.currentTimeMillis());
		inspect_dt=DateUtils.getLongtimeFromString(currentstr+" 23:59:59");
		current_dt=inspect_dt;
		if(oldPeople!=null){
			tv_oldname.setText(oldPeople.getElderly_name());
			ImageLoader.loadImg(oldPeople.getIcon(), image_oldicon);
			getData( inspect_dt,0);
		}
		initTimePickerView();
	}
	/**
	 * 初始化时间控件
	 */
	private void initTimePickerView() {
		// 时间选择器
		mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		// 控制时间范围
		mTimePickerView.setTime(new Date());
		mTimePickerView.setCyclic(false);
		mTimePickerView.setCancelable(true);
		mTimePickerView.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				LogUtil.i("date", date.getTime()+"");
				inspect_dt=date.getTime();
				getData(inspect_dt, 0);
			}
		});
		
	}
	public void getData(final long inspect_dt,final int type){
		Utils.showWaitDialog(this,"加载数据中");
		if(type==1||type==0){
			pageNo=1;
		}else if(type==2){
			pageNo++;
		}
		LogUtil.i("pageNo", pageNo+","+inspect_dt+","+oldPeople.getId()+"");
		switch(signType){
		  case ConstantUtil.TEMPERATURE:
			  LefuApi.queryAllSignDataForTemperature(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
				    Utils.hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.BLOODPRESSURE:
			  LefuApi.queryAllSignDataForPressure(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					 Utils.hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.BLOODSUGAR:
			  LefuApi.queryAllSignDataForSugar(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					 Utils. hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.PULSE:
			  LefuApi.queryAllSignDataForPulse(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					 Utils.hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.DEFECATION:
			  LefuApi.queryAllSignDataForDefecation(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
				    Utils.hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.BREATHING:
			  LefuApi.queryAllSignDataForBreath(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					 Utils. hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.DRINKWATER:
			  LefuApi.queryAllSignDataForDrink(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					 Utils.hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.SLEEPING:
			  LefuApi.queryAllSignDataForSleep(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					 Utils.hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
					 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.MEAL:
			  LefuApi.queryAllSignDataForMeal(oldPeople.getId(),inspect_dt, pageNo,1,new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					 Utils.hideWaitDialog();
					 if(type==0){
						    isShowNodata(result);
			    			signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
			    			signDataRecordAdapter=new SignDataRecordAdapter(SignDataRecordActivity.this, signDataBeans_totallist,signType,oldPeople);
			    			listview.setAdapter(signDataRecordAdapter);
			    			if(result!=null&&result.size()>0&&inspect_dt==current_dt){
			    				tv_last_time.setText("最后一次测量时间："+TimeZoneUtil.getTimeCompute(result.get(0).getInspect_dt()));
			    			}
			    	 }else if(type==1){
						    isShowNodata(result);
							signDataBeans_totallist.clear();
			    			signDataBeans_totallist.addAll(result);
							signDataRecordAdapter.notifyDataSetChanged();
							refreshlayout.setRefreshing(false);
					 }else if(type==2){
						 if(result!=null&&result.size()==0){
							 ToastUtils.show(getApplicationContext(), "没有更多数据");
						 }
						 refreshlayout.setLoading(false);
						 signDataBeans_totallist.addAll(result);
		    			 signDataRecordAdapter.notifyDataSetChanged();
					 }
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					 Utils.hideWaitDialog();
				}
			});
			  break;
		}
	}
	
	/**
	 * 检测是否要显示无数据的图
	 * @param result
	 */
	public void isShowNodata(List<SignDataBean>result){
		  if(result==null||(result!=null&&result.size()==0)){
		    	real_nodata.setVisibility(View.VISIBLE);
		    	 refreshlayout.setVisibility(View.GONE);
		     }else{
		    	 real_nodata.setVisibility(View.GONE);
		    	 refreshlayout.setVisibility(View.VISIBLE);
		     }
		   if(result==null||(result!=null&&result.size()==0)){
			   real_nodata.setVisibility(View.VISIBLE);
		    	 refreshlayout.setVisibility(View.GONE);
		     }else{
		    	 real_nodata.setVisibility(View.GONE);
			    	 refreshlayout.setVisibility(View.VISIBLE);
		     }
	}
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	@Override
	protected int getActionBarRightImage() {
		return R.drawable.select_time_icon;
	}
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	@Override
	protected boolean hasRightImageView() {
		return true;
	}
	@Override
	public void onRightViewClick(View view) {
		// 弹出日期选择窗口
	   mTimePickerView.show();
	}
	@Override
	public void onLoad() {
		getData(inspect_dt,2);
	}

	@Override
	public void onRefresh() {
		getData(inspect_dt,1);
	}

}
