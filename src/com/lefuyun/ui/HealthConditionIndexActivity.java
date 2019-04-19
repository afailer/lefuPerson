package com.lefuyun.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.ShowViewBean;
import com.lefuyun.bean.SignDataBean;
import com.lefuyun.healthcondition.DateAdapter;
import com.lefuyun.healthcondition.MyGridView;
import com.lefuyun.healthcondition.SignDataViewAdapter;
import com.lefuyun.healthcondition.SignDataViewPagerAdapter;
import com.lefuyun.healthcondition.SigndataVerticalAdapter;
import com.lefuyun.healthcondition.SpecialCalendar;
import com.lefuyun.util.ConfigUtils;
import com.lefuyun.util.DateUtils;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.SignDataUtils;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.util.share.ConstantUtil;
import com.lefuyun.widget.viewpager.VerticalViewPager;

/**
 * @author
 * @date 2016-5-10
 * @description 健康状况首页
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class HealthConditionIndexActivity extends BaseActivity{
    private VerticalViewPager signdataViewPager;
	private ViewFlipper flipper1 = null;
	private static String TAG = "ZzL";
	private GridView gridView = null;
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private int week_c = 0;
	private int week_num = 0;
	private String currentDate = "";
	private static int jumpWeek = 0;
	private static int jumpMonth = 0;
	private static int jumpYear = 0;
	private DateAdapter dateAdapter;
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int weeksOfMonth = 0;
	private SpecialCalendar sc = null;
	private boolean isLeapyear = false; // 是否为闰年
	private int selectPostion = 0;
	private String dayNumbers[] = new String[7];
	private TextView tvDate;
	private int currentYear;
	private int currentMonth;
	private int currentWeek;
	private int currentDay;
	private int currentNum;
	private boolean isStart;// 是否是交接的月初
	OldPeople oldPeople;
	/*** 老人健康状况 */
	TextView tv_oldhealth_status;
	/*** 体征数据类型 */
	TextView tv_signdata_text;
	/*** 体征数据的最新值 */
	TextView tv_signdata_value;
	/*** 体征数据的单位 */
	TextView tv_signdata_unit;
	/*** 上箭头 */
	TextView tv_up;
	/*** 下箭头 */
	TextView tv_bottom;
	/*** 老人姓名 */
	TextView tv_oldname;
	/*** 机构名称 */
	TextView tv_orgname;
	/*** 老人年龄 */
	TextView tv_old_age;
    /*** 下箭头*/
    RelativeLayout real_bottom;
    /*** 上箭头*/
	RelativeLayout real_up;
	
	ViewPager viewpager;
	List<ShowViewBean>showViewBeans;
	SignDataViewPagerAdapter signDataViewPagerAdapter;
	int arrow_position=0;
    List<View>view_vertical_list=new ArrayList<View>();
	LayoutInflater inflater;
	SigndataVerticalAdapter signdataVerticalAdapter;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			  if(msg.what==ConstantUtil.BLOODPRESSURE){
				  LefuApi.queryLastSignDataForPressure(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

						@Override
						public void onSuccess(SignDataBean result) {
							if(result==null){
								result=new SignDataBean();
								result.setHasdata(1);
							}
							//map.put(ConstantUtil.BLOODPRESSURE, result);
							View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.BLOODPRESSURE, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
							handler.sendEmptyMessage(ConstantUtil.BLOODSUGAR);
						}
                        
						@Override
						public void onFailure(ApiHttpException e) {
							Utils.hideWaitDialog();
						}
			        });
			  }else if(msg.what==ConstantUtil.BLOODSUGAR){
				  LefuApi.queryLastSignDataForSugar(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

			 			@Override
			 			public void onSuccess(SignDataBean result) {
			 				if(result==null){
								result=new SignDataBean();
								result.setHasdata(1);
							}
			 				View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.BLOODSUGAR, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
			 				handler.sendEmptyMessage(ConstantUtil.PULSE);
			 			}

			 			@Override
			 			public void onFailure(ApiHttpException e) {
			 				Utils.hideWaitDialog();
			 			}
			 		
			         });
			  }else if(msg.what==ConstantUtil.PULSE){
				    LefuApi.queryLastSignDataForPulse(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

			 			@Override
			 			public void onSuccess(SignDataBean result) {
			 				if(result==null){
								result=new SignDataBean();
								result.setHasdata(1);
							}
			 				View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.PULSE, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
			 				handler.sendEmptyMessage(ConstantUtil.DEFECATION);
			 			}

			 			@Override
			 			public void onFailure(ApiHttpException e) {
			 				Utils.hideWaitDialog();
			 			}
			 		
			         });
			  }else if(msg.what==ConstantUtil.DEFECATION){

			        LefuApi.queryLastSignDataForDefecation(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

			 			@Override
			 			public void onSuccess(SignDataBean result) {
			 				if(result==null){
								result=new SignDataBean();
								result.setHasdata(1);
							}
			 				View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.DEFECATION, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
							handler.sendEmptyMessage(ConstantUtil.BREATHING);
			 			}

			 			@Override
			 			public void onFailure(ApiHttpException e) {
			 				Utils.hideWaitDialog();
			 			}
			 		
			         });
			  }else if(msg.what==ConstantUtil.BREATHING){
				   LefuApi.queryLastSignDataForBreath(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

			 			@Override
			 			public void onSuccess(SignDataBean result) {
			 				if(result==null){
								result=new SignDataBean();
								result.setHasdata(1);
							}
			 				View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.BREATHING, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
			 				handler.sendEmptyMessage(ConstantUtil.DRINKWATER);
			 			}

			 			@Override
			 			public void onFailure(ApiHttpException e) {
			 				Utils.hideWaitDialog();
			 			}
			 		
			         });
			  }else if(msg.what==ConstantUtil.DRINKWATER){
				  LefuApi.queryLastSignDataForDrink(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

		    			@Override
		    			public void onSuccess(SignDataBean result) {
		    				if(result==null){
		    					result=new SignDataBean();
		    					result.setHasdata(1);
		    				}
		    				View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.DRINKWATER, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
		    				handler.sendEmptyMessage(ConstantUtil.SLEEPING);
		    			}

		    			@Override
		    			public void onFailure(ApiHttpException e) {
		    				Utils.hideWaitDialog();
		    			}
		    		
		            });
			  }else if(msg.what==ConstantUtil.SLEEPING){
				  LefuApi.queryLastSignDataForSleep(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

						@Override
						public void onSuccess(SignDataBean result) {
							if(result==null){
								result=new SignDataBean();
								result.setHasdata(1);
							}
							View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.SLEEPING, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
							handler.sendEmptyMessage(ConstantUtil.MEAL);
						}

						@Override
						public void onFailure(ApiHttpException e) {
							Utils.hideWaitDialog();
						}
					
			        });
			  }else if(msg.what==ConstantUtil.MEAL){
				  LefuApi.queryLastSignDataForMeal(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {

						@Override
						public void onSuccess(SignDataBean result) {
							if(result==null){
								result=new SignDataBean();
								result.setHasdata(1);
							}
							View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
							TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
							TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
							SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.MEAL, tv_signdata_value, tv_signdata_unit, oldPeople);
							view_vertical_list.add(view);
							 Utils.hideWaitDialog();
						     signdataVerticalAdapter=new SigndataVerticalAdapter(view_vertical_list);
						     signdataViewPager.setAdapter(signdataVerticalAdapter);
							  handler.sendEmptyMessageDelayed(100, 1000);
						}

						@Override
						public void onFailure(ApiHttpException e) {
							Utils.hideWaitDialog();
						}
					
			        });
				    //当请求完数据，从体温开始展示
			  }else if(msg.what==100){
				  //如果最后一个
				  if(view_vertical_list.size()==9){
					  if(arrow_position==view_vertical_list.size()-1){
						  isAhead=false;
					  }else if(arrow_position==0){
						  isAhead=true;
					  }
					  tv_signdata_text.setText(SignDataUtils.getSigndataStr(arrow_position+1));
					  signdataViewPager.setCurrentItem(arrow_position,true);
					  if(isAhead){
						  arrow_position++;
					  }else if(!isAhead){
						  arrow_position--;
					  }
				  }
				  handler.sendEmptyMessageDelayed(100,2000);
			  }
		};
	};
	boolean isAhead=true;
    public HealthConditionIndexActivity() {
    	Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date);
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		currentYear = year_c;
		currentMonth = month_c;
		currentDay = day_c;
		sc = new SpecialCalendar();
		getCalendar(year_c, month_c);
		week_num = getWeeksOfMonth();
		currentNum = week_num;
		if (dayOfWeek == 7) {
			week_c = day_c / 7 + 1;
		} else {
			if (day_c <= (7 - dayOfWeek)) {
				week_c = 1;
			} else {
				if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
					week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
				} else {
					week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
				}
			}
		}
		currentWeek = week_c;
		getCurrent();

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_health_condition_index;
	}

	@Override
	protected void initView() {
		setActionBarTitle("健康状况");
		tvDate = (TextView) findViewById(R.id.tv_date);
		tvDate.setText(year_c + "年" + month_c + "月" + day_c + "日");
		flipper1 = (ViewFlipper) findViewById(R.id.flipper1);
		dateAdapter = new DateAdapter(this, getResources(), currentYear,currentMonth, currentWeek, currentNum, selectPostion,
				currentWeek == 1 ? true : false);
		addGridView();
		dayNumbers = dateAdapter.getDayNumbers();
		gridView.setAdapter(dateAdapter);
		selectPostion = dateAdapter.getTodayPosition();
		gridView.setSelection(selectPostion);
		flipper1.addView(gridView, 0);
		tv_oldhealth_status=(TextView) findViewById(R.id.tv_oldhealth_status);
		tv_signdata_text=(TextView) findViewById(R.id.tv_signdata_text);
		tv_signdata_value=(TextView) findViewById(R.id.tv_signdata_value);
		tv_signdata_unit=(TextView) findViewById(R.id.tv_signdata_unit);
		tv_up=(TextView) findViewById(R.id.tv_up);
		tv_bottom=(TextView) findViewById(R.id.tv_bottom);
		tv_oldname=(TextView) findViewById(R.id.tv_oldname);
		tv_orgname=(TextView) findViewById(R.id.tv_orgname);
		tv_old_age=(TextView) findViewById(R.id.tv_old_age);
		real_up=(RelativeLayout) findViewById(R.id.real_up);
		real_bottom=(RelativeLayout) findViewById(R.id.real_bottom);
		viewpager=(ViewPager) findViewById(R.id.viewpager);
		signdataViewPager=(VerticalViewPager) findViewById(R.id.signdataViewPager);
		inflater=LayoutInflater.from(HealthConditionIndexActivity.this);
		
		signdataViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				arrow_position=arg0;
				signdataViewPager.setCurrentItem(arrow_position);
				tv_signdata_text.setText(SignDataUtils.getSigndataStr(arrow_position+1));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	@Override
	protected void initData() {
		oldPeople=(OldPeople) getIntent().getSerializableExtra("oldPeople");
		if(oldPeople!=null){
			tv_oldname.setText(oldPeople.getElderly_name());
			tv_orgname.setText(oldPeople.getAgency_name());
			tv_old_age.setText("年龄："+oldPeople.getAge()+"岁");
			tv_oldhealth_status.setText("健康状况： "+SignDataUtils.getOldStatusStr(oldPeople));
		}else{
			return ;
		}
		initBottomSignView();
    	inspect_dt=getParamTime(currentYear,currentMonth,currentDay);
		getData();
	
	}
	List<View>view_childlist=new ArrayList<View>();
	List<LinearLayout>view_child_child_list=new ArrayList<LinearLayout>();
    /**
     * 初始化下方体征数据的列表
     */
	int pos;
    public void initBottomSignView(){
    	Map<String, Object>map=SignDataUtils.initBottomViewPager(HealthConditionIndexActivity.this);
    	view_childlist=(List<View>) map.get(SignDataUtils.SHOWVIEW);
    	showViewBeans=(List<ShowViewBean>) map.get(SignDataUtils.SHOWVIEWBEAN);
    	view_child_child_list=(List<LinearLayout>) map.get(SignDataUtils.SHOWCHILD_CHILD_VIEW);
    	signDataViewPagerAdapter=new SignDataViewPagerAdapter(view_childlist,showViewBeans);
		viewpager.setAdapter(signDataViewPagerAdapter);
		for(pos=0;pos<view_child_child_list.size();pos++){
			final LinearLayout linearLayout=view_child_child_list.get(pos);
			linearLayout.setTag(pos);
			linearLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 int ppos=(Integer)linearLayout.getTag();
					 if(showViewBeans.get(ppos).getName().equals("")){
						 return;
					 }
	                 if(showViewBeans.get(ppos).getSignType()==ConstantUtil.TEMPERATURE||showViewBeans.get(ppos).getSignType()==ConstantUtil.BLOODPRESSURE||showViewBeans.get(ppos).getSignType()==ConstantUtil.BLOODSUGAR||showViewBeans.get(ppos).getSignType()==ConstantUtil.PULSE){
	                	 Intent intent=new Intent(HealthConditionIndexActivity.this,HealthConditionActivity.class);
						 intent.putExtra("signType", showViewBeans.get(ppos).getSignType());
		                 intent.putExtra("oldPeople", oldPeople);
		                 startActivity(intent);
					 }else{
						Intent intent=new Intent(HealthConditionIndexActivity.this,SignDataRecordActivity.class);
						intent.putExtra("signType", showViewBeans.get(ppos).getSignType());
		                 intent.putExtra("oldPeople", oldPeople);
		                 startActivity(intent);
					 }
				}
			});
		}
    }
	@Override
	public void onClick(View v) {
       switch(v.getId()){
        case R.id.real_up:
        	if(arrow_position!=view_vertical_list.size()-1){
            	arrow_position++;
            	signdataViewPager.setCurrentItem(arrow_position);
    		    tv_signdata_text.setText(SignDataUtils.getSigndataStr(arrow_position+1));
        	}else{
        		ToastUtils.show(this, "没有更多数据了");
        	}
            break;
         case R.id.real_bottom:
        	 if(arrow_position!=0){
             	arrow_position--;
            	signdataViewPager.setCurrentItem(arrow_position);
        		tv_signdata_text.setText(SignDataUtils.getSigndataStr(arrow_position+1));
         	}else{
         		ToastUtils.show(this, "没有更多数据了");
         	}
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
	 long inspect_dt;
	 /**
     * 获取网络数据
     */
    public void getData(){
    	Utils.showWaitDialog(this,"数据加载中");
        LogUtil.i("tag", inspect_dt+","+oldPeople.getId());
        LogUtil.i("tag", DateUtils.getStringtimeFromLong(inspect_dt));
        LefuApi.queryLastSignDataForTemperature(oldPeople.getId(), inspect_dt, new RequestCallback<SignDataBean>() {
			
			@Override
			public void onSuccess(SignDataBean result) {
				if(result==null){
					result=new SignDataBean();
					result.setHasdata(1);
				}
				View view=inflater.inflate(R.layout.viewpager_vertical_item, null);
				TextView tv_signdata_value=(TextView) view.findViewById(R.id.tv_signdata_value);
				TextView tv_signdata_unit=(TextView) view.findViewById(R.id.tv_signdata_unit);
				SignDataUtils.setShowSignDataStr(HealthConditionIndexActivity.this, result, ConstantUtil.TEMPERATURE, tv_signdata_value, tv_signdata_unit, oldPeople);
				view_vertical_list.add(view);
				handler.sendEmptyMessage(ConstantUtil.BLOODPRESSURE);
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				
			}
		});
    }
	public long getParamTime(int currentYear,int currentMonth,int currentDay){
	   	String  param_year=currentYear+"";
    	String param_month="";
    	String param_day="";
    	if((currentMonth+"").length()==1){
    		param_month="0"+currentMonth;
    	}else{
    		param_month=currentMonth+"";
    	}
        if((currentDay+"").length()==1){
        	param_day="0"+currentDay;
        }else{
        	param_day=currentDay+"";
        }
        String param_date=param_year+"-"+param_month+"-"+param_day+" 23:59:59";
        long paramtime=DateUtils.getLongtimeFromString(param_date);
	    return paramtime; 
	}
	
      @Override
	protected String getActionBarRightText() {
		return "切换";
	}
	@Override
	public void onRightViewClick(View view) {
      Intent intent=new Intent(this,SwitchElderlyActivity.class);
      startActivityForResult(intent, 200);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if(resultCode==200){
		           oldPeople=(OldPeople) intent.getSerializableExtra("oldPeople");
		       	  if(oldPeople!=null){
					tv_oldname.setText(oldPeople.getElderly_name());
					tv_orgname.setText(oldPeople.getAgency_name());
					tv_old_age.setText("年龄："+oldPeople.getAge()+"岁");
					tv_oldhealth_status.setText("健康状况： "+SignDataUtils.getOldStatusStr(oldPeople));
				 }
		       	  tv_signdata_text.setText("体温数据");
		    	 inspect_dt=getParamTime(currentYear,currentMonth,currentDay);
		    	 arrow_position=0;
		    	 view_vertical_list=new ArrayList<View>();
		    	 signdataVerticalAdapter.notifyDataSetChanged();
		    	 handler.removeCallbacksAndMessages(null);
				 getData();
		   }
	}
	
	
//-------------------------------------------------------------------------------------
	/**
	 * 判断某年某月所有的星期数
	 * 
	 * @param year
	 * @param month
	 */
	public int getWeeksOfMonth(int year, int month) {
		// 先判断某月的第一天为星期几
		int preMonthRelax = 0;
		int dayFirst = getWhichDayOfWeek(year, month);
		int days = sc.getDaysOfMonth(sc.isLeapYear(year), month);
		if (dayFirst != 7) {
			preMonthRelax = dayFirst;
		}
		if ((days + preMonthRelax) % 7 == 0) {
			weeksOfMonth = (days + preMonthRelax) / 7;
		} else {
			weeksOfMonth = (days + preMonthRelax) / 7 + 1;
		}
		return weeksOfMonth;

	}

	/**
	 * 判断某年某月的第一天为星期几
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public int getWhichDayOfWeek(int year, int month) {
		return sc.getWeekdayOfMonth(year, month);

	}

	/**
	 * 
	 * @param year
	 * @param month
	 */
	public int getLastDayOfWeek(int year, int month) {
		return sc.getWeekDayOfLastMonth(year, month,
				sc.getDaysOfMonth(isLeapyear, month));
	}

	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
	}

	public int getWeeksOfMonth() {
		// getCalendar(year, month);
		int preMonthRelax = 0;
		if (dayOfWeek != 7) {
			preMonthRelax = dayOfWeek;
		}
		if ((daysOfMonth + preMonthRelax) % 7 == 0) {
			weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
		} else {
			weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
		}
		return weeksOfMonth;
	}

	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setLayoutParams(params);
	}

	@Override
	protected void onPause() {
		super.onPause();
		jumpWeek = 0;
	}


	/**
	 * 重新计算当前的年月
	 */
	public void getCurrent() {
		if (currentWeek > currentNum) {
			if (currentMonth + 1 <= 12) {
				currentMonth++;
			} else {
				currentMonth = 1;
				currentYear++;
			}
			currentWeek = 1;
			currentNum = getWeeksOfMonth(currentYear, currentMonth);
		} else if (currentWeek == currentNum) {
			if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
			} else {
				if (currentMonth + 1 <= 12) {
					currentMonth++;
				} else {
					currentMonth = 1;
					currentYear++;
				}
				currentWeek = 1;
				currentNum = getWeeksOfMonth(currentYear, currentMonth);
			}

		} else if (currentWeek < 1) {
			if (currentMonth - 1 >= 1) {
				currentMonth--;
			} else {
				currentMonth = 12;
				currentYear--;
			}
			currentNum = getWeeksOfMonth(currentYear, currentMonth);
			currentWeek = currentNum - 1;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
}
