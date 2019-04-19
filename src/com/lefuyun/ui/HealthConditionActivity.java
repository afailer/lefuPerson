package com.lefuyun.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.BodyData;
import com.lefuyun.bean.Line;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.ShowViewBean;
import com.lefuyun.bean.SignConfigBean;
import com.lefuyun.bean.SignDataBean;
import com.lefuyun.healthcondition.GetBodyData;
import com.lefuyun.healthcondition.MyMarkerView;
import com.lefuyun.healthcondition.SignDataViewPagerAdapter;
import com.lefuyun.util.ConfigUtils;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.SignDataUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.util.share.ConstantUtil;

/**
 * @author 
 * @date   2016-5-10
 * @description 健康状况
 */
@SuppressLint("NewApi")
public class HealthConditionActivity extends BaseActivity implements GetBodyData{
	GridView gridview_date;
	RelativeLayout rela_left;
	RelativeLayout rela_right;
	RelativeLayout chart_day;
	RelativeLayout chart_week;
	RelativeLayout chart_month;
	RelativeLayout chart_year;
	TextView tv_signdataname;
	LinearLayout health_title;
	TextView tv_time_desc;
	TextView tv_alldata;
	ViewPager viewpager;
	private LineChart BodyDataChart;
	MyMarkerView mv;
	Calendar c=Calendar.getInstance();
	SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
	SimpleDateFormat sdfFormater=new SimpleDateFormat("yyyy年MM月dd日");
	SimpleDateFormat sdfDay=new SimpleDateFormat("dd日HH:mm:ss");
	String s="lastTime";
	OldPeople oldPeople;
	SignConfigBean signConfigBean;
	int signType;
	int timeSize=1;
	/** * type 每次增加或减少的时间值*/
	 Long dayTime=(long) 86400000;
	Long todayTimeLong=new Date().getTime();
	Long todayTimeLongLast=new Date().getTime();
	Long weekTimeLong=new Date().getTime();
	Long weekTimeLongLast=new Date().getTime();
	Long monthimeLong=new Date().getTime();
	Long monthimeLongLast=new Date().getTime();
	Long yearTimeLong=new Date().getTime();
	Long yearTimeLongLast=new Date().getTime();
	Long LastToday=new Date().getTime();
	Long LastWeek=new Date().getTime();
	Long LastMonth=new Date().getTime();
	Long LastYear=new Date().getTime();
	private boolean hasdayleft=true;
	private boolean hasweekleft=true;
	private boolean hasmonthleft=true;
	private boolean hasyearleft=true;
	private boolean hasdayRight=true;
	private boolean hasweekRight=true;
	private boolean hasmonthRight=true;
	private boolean hasyearRight=true;
	Stack<Long> stack=new Stack<Long>();
	Long todayTimes=new Date().getTime();
	Long yearTimes=new Date().getTime();
	Long monthTimes=new Date().getTime();
	Long weekTimes=new Date().getTime();
	/** * type 1日2周3月4年*/
	String type="1";
	String order="1";
	boolean b=true;
	@Override
	protected int getLayoutId() {
		return R.layout.activity_health_condition;
	}

	@Override
	protected void initView() {
		setActionBarTitle("健康状况");
		rela_left=(RelativeLayout) findViewById(R.id.rela_left);
		rela_right=(RelativeLayout) findViewById(R.id.rela_right);
		chart_day=(RelativeLayout) findViewById(R.id.chart_day);
		chart_week=(RelativeLayout) findViewById(R.id.chart_week);
		chart_month=(RelativeLayout) findViewById(R.id.chart_month);
		chart_year=(RelativeLayout) findViewById(R.id.chart_year);
		health_title=(LinearLayout) findViewById(R.id.health_title);
		tv_signdataname=(TextView) findViewById(R.id.tv_signdataname);
		viewpager=(ViewPager) findViewById(R.id.viewpager);
		//图表控件
		BodyDataChart=(LineChart) findViewById(R.id.BodyDataChart);
		mv = new MyMarkerView(this, R.layout.custom_marker_view);
	    BodyDataChart.setMarkerView(mv);
	    BodyDataChart.setBgColor(Color.parseColor("#FFFFFF"));
	    //日期与时间
		tv_time_desc=(TextView) findViewById(R.id.tv_time_desc);
	}

	@Override
	protected void initData() {
		oldPeople=(OldPeople) getIntent().getSerializableExtra("oldPeople");
		signType=getIntent().getIntExtra("signType", 0);
		signConfigBean = ConfigUtils.getConfigBean(getApplicationContext(), signType,oldPeople);
		setChartStyle(signConfigBean);
		showDifferentStyle(1);
		initBottomSignView();
		getWeekData();
	}
	
	public void setChartStyle(SignConfigBean signConfigBean){
		 mv.setNum((int)signConfigBean.getAccur());
		 YAxis leftAxis = BodyDataChart.getAxisLeft();
		 leftAxis.removeAllLimitLines();
	        leftAxis.setLabelCount(4,true);
	        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
	        for(int i=0;i<signConfigBean.getLine().size();i++){
	        	Line line = signConfigBean.getLine().get(i);
	        	//设置的是警戒的标线
	        	LimitLine ll1 = new LimitLine((float)line.getValue(), line.getValue()+"");
	        	ll1.setLineColor(Color.parseColor(line.getColor()));
	        	ll1.enableDashedLine(8f,12f , 0);
	            ll1.setLineWidth(0.6f);
	            ll1.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
	            ll1.setTextSize(6f);
	            ll1.setTypeface(tf);
	            ll1.setTextColor(Color.parseColor(line.getColor()));
	            leftAxis.addLimitLine(ll1);
	        }
	}
    /**
     * 初始化下方体征数据的列表
     */
    public void initBottomSignView(){
    	int pos;
    	Map<String, Object>map=SignDataUtils.initBottomViewPager(this);
		final List<ShowViewBean>showViewBeans=(List<ShowViewBean>) map.get(SignDataUtils.SHOWVIEWBEAN);
		List<View>shoViews=(List<View>) map.get(SignDataUtils.SHOWVIEW);
		List<LinearLayout>view_child_child_list=(List<LinearLayout>) map.get(SignDataUtils.SHOWCHILD_CHILD_VIEW);
		SignDataViewPagerAdapter signDataViewPagerAdapter=new SignDataViewPagerAdapter(shoViews, showViewBeans);
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
	                	 signType=showViewBeans.get(ppos).getSignType();
	                	 signConfigBean=ConfigUtils.getConfigBean(HealthConditionActivity.this, signType,oldPeople);
	                	 //重新设置图表
	                	 setChartStyle(signConfigBean);
	                	 //初始化属性
	                	 initvariable();
	                	 //设置显示的样式
	                	 showDifferentStyle(1);
	                	 //获取数据
	                	 getWeekData();
					 }else{
						Intent intent=new Intent(HealthConditionActivity.this,SignDataRecordActivity.class);
						intent.putExtra("signType", showViewBeans.get(ppos).getSignType());
		                 intent.putExtra("oldPeople", oldPeople);
		                 startActivity(intent);
					 }
				}
			});
		}
    }
    /**
     * 重新初始化属性
     */
    public void initvariable(){
        timeSize=1;
    	 todayTimeLong=new Date().getTime();
    	 todayTimeLongLast=new Date().getTime();
    	 weekTimeLong=new Date().getTime();
    	 weekTimeLongLast=new Date().getTime();
    	 monthimeLong=new Date().getTime();
    	 monthimeLongLast=new Date().getTime();
    	 yearTimeLong=new Date().getTime();
    	 yearTimeLongLast=new Date().getTime();
    	 LastToday=new Date().getTime();
    	 LastWeek=new Date().getTime();
    	 LastMonth=new Date().getTime();
    	 LastYear=new Date().getTime();
    	  hasdayleft=true;
    	  hasweekleft=true;
    	  hasmonthleft=true;
    	  hasyearleft=true;
    	  hasdayRight=true;
    	  hasweekRight=true;
    	  hasmonthRight=true;
    	  hasyearRight=true;
    	 stack=new Stack<Long>();
    	 todayTimes=new Date().getTime();
    	 yearTimes=new Date().getTime();
    	 monthTimes=new Date().getTime();
    	 weekTimes=new Date().getTime();
    	/** * type 1日2周3月4年*/
    	 type="2";
    	 order="1";
    	 b=true;
    }
	@Override
	public void onClick(View v) {
        switch (v.getId()) {
		case R.id.chart_day:
			initvariable();
			type="1";
			showDifferentStyle(0);
			getDayData();
			break;
		case R.id.chart_week:
			initvariable();
			type="2";
			showDifferentStyle(1);
			getWeekData();
			break;
		case R.id.chart_month:
			initvariable();
			type="3";
			showDifferentStyle(2);
			getMonthData();
			break;
		case R.id.chart_year:
			initvariable();
			type="4";
			showDifferentStyle(3);
			getYearData();
			break;
		case R.id.tv_alldata:
			Intent intent=new Intent(this,SignDataRecordActivity.class);
			intent.putExtra("signType", signType);
			intent.putExtra("oldPeople",oldPeople);
			startActivity(intent);
			break;
		case R.id.rela_left:
			b=true;
			order=1+"";
			if(type.equals("1")){
				LogUtil.i("type_left", type+",hasdayleft:"+hasdayleft);
				if(hasdayleft){
					getData(signType, type, order, todayTimeLong-dayTime+"");
				}else{
					Utils.hideWaitDialog();
					Toast.makeText(getApplicationContext(), "已无更早数据", 0).show();
				}
			}else if(type.equals("2")){
				LogUtil.i("type_left", type+",hasweekleft:"+hasweekleft);
				if(hasweekleft){
					getData(signType, type, order,weekTimeLong-dayTime+"");
				}else{
					Utils.hideWaitDialog();
					Toast.makeText(getApplicationContext(), "已无更早数据", 0).show();
				}
				
			}else if("3".equals(type)){
				LogUtil.i("type_left", type+",hasmonthleft:"+hasmonthleft);
				if(hasmonthleft){
					getData(signType, type,order, monthimeLong-dayTime+"");
				}else{
					Utils.hideWaitDialog();
					Toast.makeText(getApplicationContext(), "已无更早数据", 0).show();
				}
				
			}else if("4".equals(type)){
				LogUtil.i("type_left", type+",hasyearleft:"+hasyearleft);
				if(hasyearleft){
				   getData(signType, type,order,yearTimeLong-dayTime+"");
				}else{
					Utils.hideWaitDialog();
					Toast.makeText(getApplicationContext(), "已无更早数据", 0).show();
				}
			}
			break;
		case R.id.rela_right:
			if("1".equals(type)){
				LogUtil.i("type_right", type+"hasdayRight:"+hasdayRight);
				if(!hasdayRight){
					Toast.makeText(getApplicationContext(), "已是最新数据", 0).show();
					Utils.hideWaitDialog();
				}else{
					b=true;
					order=2+"";
					getData(signType, type, order,todayTimeLongLast+dayTime+"");
				}
			}else if("2".equals(type)){
				LogUtil.i("type_right", type+"hasweekRight:"+hasweekRight);
				if(!hasweekRight){
					Toast.makeText(getApplicationContext(), "已是最新数据", 0).show();
					Utils.hideWaitDialog();
				}else{
					b=true;
					order=2+"";
					getData(signType, type, order, weekTimeLongLast+dayTime+"");
				}
			}else if("3".equals(type)){
				LogUtil.i("type_right", type+"hasmonthRight:"+hasmonthRight);
				if(!hasmonthRight){
					Toast.makeText(getApplicationContext(), "已是最新数据", 0).show();
					Utils.hideWaitDialog();
				}else{
					b=true;
					order=2+"";
					getData(signType, type, order,monthimeLongLast+dayTime+"");
				}
			}else if("4".equals(type)){
				LogUtil.i("type_right", type+"hasyearRight:"+hasyearRight);
				if(!hasyearRight){
					Toast.makeText(getApplicationContext(), "已是最新数据", 0).show();
					Utils.hideWaitDialog();
				}else{
					b=true;
					order=2+"";
					getData(signType, type, order, yearTimeLongLast+dayTime+"");
				}
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
	/**
	 * 年月日动态改变颜色
	 * @param pos
	 */
	public void showDifferentStyle(int pos){
		 for(int i=0;i<health_title.getChildCount();i++){
			 if(i==pos){
				 RelativeLayout rela_select= (RelativeLayout) health_title.getChildAt(pos);
				 TextView tv_select=(TextView) rela_select.getChildAt(0);
				 tv_select.setTextColor(getResources().getColor(R.color.white));
			     tv_select.setBackground(getResources().getDrawable(R.drawable.oval));
			 }else{
				 RelativeLayout rela_select= (RelativeLayout) health_title.getChildAt(i);
				 TextView tv_select=(TextView) rela_select.getChildAt(0);
				 tv_select.setTextColor(getResources().getColor(R.color.txtgray));
			     tv_select.setBackgroundColor(getResources().getColor(R.color.white)); 
			 }
		 } 
	}
	/**
	 *获取网络数据
	 * @param signType  体征数据类型
	 * @param type   1日2周3月4年
	 * @param beginTime  开始时间
	 */
	public void getData(int signType,final String type,final String order,String beginTime){
		Utils.showWaitDialog(this, "数据加载中");
		  switch(signType){
		  case ConstantUtil.TEMPERATURE:
			  tv_signdataname.setText("体温数据");
			  LefuApi.queryBodyDataForChartTemperature(oldPeople.getId(), beginTime,todayTimeLong+"",type, order, new RequestCallback<List<SignDataBean>>() {
				
				@Override
				public void onSuccess(List<SignDataBean> result) {
					setGlobalvariableValue(result, ConstantUtil.TEMPERATURE);
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					Utils.hideWaitDialog();
				}
			});
			  break;
		  case ConstantUtil.BLOODPRESSURE:
			  tv_signdataname.setText("血压数据");
			  LefuApi.queryBodyDataForChartBressure(oldPeople.getId(), beginTime,todayTimeLong+"",type, order, new RequestCallback<List<SignDataBean>>() {
					
					@Override
					public void onSuccess(List<SignDataBean> result) {
						setGlobalvariableValue(result, ConstantUtil.BLOODPRESSURE);
					}
					
					@Override
					public void onFailure(ApiHttpException e) {
						Utils.hideWaitDialog();
					}
				});
			  break;
		  case ConstantUtil.BLOODSUGAR:
			  tv_signdataname.setText("血糖数据");
				 LefuApi.queryBodyDataForChartSugar(oldPeople.getId(), beginTime,todayTimeLong+"",type, order, new RequestCallback<List<SignDataBean>>() {
						
						@Override
						public void onSuccess(List<SignDataBean> result) {
							setGlobalvariableValue(result, ConstantUtil.BLOODSUGAR);
						}
						
						@Override
						public void onFailure(ApiHttpException e) {
							Utils.hideWaitDialog();
						}
					});
			  break;
		  case ConstantUtil.PULSE:
			  tv_signdataname.setText("心率数据");
			  LefuApi.queryBodyDataForChartPulse(oldPeople.getId(), beginTime,todayTimeLong+"",type, order, new RequestCallback<List<SignDataBean>>() {
					
					@Override
					public void onSuccess(List<SignDataBean> result) {
						setGlobalvariableValue(result, ConstantUtil.PULSE);
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
	 *  设置全局变量的值,在请求获取数据结果之后执行
	 * @param result  体征数据的集合
	 * @param singType  体征数据常量
	 */
	public void setGlobalvariableValue(List<SignDataBean>result,int singType){

		if(result.size()==0){
			if(order.equals("1")){
				if(type.equals("1")){
					hasdayleft=false;
				}else if(type.equals("2")){
					hasweekleft=false;
				}else if(type.equals("3")){
					hasmonthleft=false;
				}else{
					hasyearleft=false;
				}
				Toast.makeText(getApplicationContext(), "已无更早数据", 0).show();
			}else{
				Toast.makeText(getApplicationContext(), "已是最新数据", 0).show();
			}
			Utils.hideWaitDialog();
			return;
		}else if(result.get(0).getInspect_dt()<0){
			if(order.equals("1")){
				if(type.equals("1")){
					hasdayleft=false;
				}else if(type.equals("2")){
					hasweekleft=false;
				}else if(type.equals("3")){
					hasmonthleft=false;
				}else{
					hasyearleft=false;
				}
				Toast.makeText(getApplicationContext(), "已无更早数据", 0).show();
			}else{
				Toast.makeText(getApplicationContext(), "已是最新数据", 0).show();
			}
			Utils.hideWaitDialog();
			return;
		}
		if(type.equals("1")){
			hasdayRight=true;
			hasdayleft=true;
		}else if(type.equals("2")){
			hasweekleft=true;
			hasweekRight=true;
		}else if(type.equals("3")){
			hasmonthleft=true;
			hasmonthRight=true;
		}else if(type.equals(4)){
			hasyearleft=true;
			hasyearRight=true;
		}
		if(result!=null&&result.size()>0){
			if("1".equals(type)){
				if(b){
					todayTimeLong=result.get(0).getInspect_dt();
					todayTimeLongLast=result.get(result.size()-1).getInspect_dt();
					setLastToday(todayTimeLongLast);
					c.setTimeInMillis(todayTimeLong);
					Log.e(s, sdfFormater.format(c.getTime())+">>>>>>");
					b=false;
				}
				c.setTimeInMillis(result.get(0).getInspect_dt());
				tv_time_desc.setText(sdfFormater.format(c.getTime()));
			}else if("2".equals(type)){
				if(b){
					weekTimeLong=result.get(0).getInspect_dt();
					weekTimeLongLast=result.get(result.size()-1).getInspect_dt();
					setLastWeek(weekTimeLongLast);
					c.setTimeInMillis(weekTimeLongLast);
					Log.e(s, sdfFormater.format(c.getTime()));
					
					b=false;
				}
				c.setTimeInMillis(result.get(0).getInspect_dt());
				tv_time_desc.setText(sdfFormater.format(c.getTime()));
			}else if("3".equals(type)){
				if(b){
					monthimeLong=result.get(0).getInspect_dt();
					monthimeLongLast=result.get(result.size()-1).getInspect_dt();
					setLastMonth(monthimeLongLast);
					c.setTimeInMillis(monthimeLongLast);
					Log.e(s, sdfFormater.format(c.getTime()));
					
					b=false;
				}
				c.setTimeInMillis(result.get(0).getInspect_dt());
				tv_time_desc.setText(sdfFormater.format(c.getTime()));
			}else if("4".equals(type)){
				if(b){
					yearTimeLong=result.get(0).getInspect_dt();
					yearTimeLongLast=result.get(result.size()-1).getInspect_dt();
					setLastYear(yearTimeLongLast);
					c.setTimeInMillis(yearTimeLongLast);
					Log.e(s, sdfFormater.format(c.getTime()));
					b=false;
				}
				c.setTimeInMillis(result.get(0).getInspect_dt());
				tv_time_desc.setText(sdfFormater.format(c.getTime()));
			}
		}else{
			tv_time_desc.setText("");
		}
		switch(singType){
		   case ConstantUtil.TEMPERATURE:
			   initChartView(result, "体温", -1);
			   break;
		   case ConstantUtil.BLOODPRESSURE:
			   initChartView(result, "血压", 1);
			   break;
		   case ConstantUtil.BLOODSUGAR:
			   initChartView(result, "血糖", -1);
			   break;
		   case ConstantUtil.PULSE:
			   initChartView(result, "心率", -1);
			   break;
		}
		Utils.hideWaitDialog();
	}
	/**
	 * 先初始化图表控件视图
	 * @param dataList 数据集合
	 * @param chartName 图表名称
	 */
	private void initChartView(List<SignDataBean> dataList,String chartName,int j) {
		// TODO Auto-generated method stub
		BodyDataChart.setTouchEnabled(true);
		BodyDataChart.setNoDataTextDescription("暂无数据");
		BodyDataChart.setDragEnabled(true);
		BodyDataChart.setDescription(chartName);
		BodyDataChart.animateXY(600, 600);
		BodyDataChart.setDrawGridBackground(true);
		//
		XAxis xAxis = BodyDataChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        
        YAxis leftAxis = BodyDataChart.getAxisLeft();
        leftAxis.setLabelCount(4,true);
        leftAxis.setAxisMaxValue((float)signConfigBean.getyMax());
        leftAxis.setAxisMinValue((float)signConfigBean.getyMin());
        leftAxis.setAxisLineColor(Color.TRANSPARENT);
        leftAxis.setStartAtZero(false);
        
        YAxis rightAxis = BodyDataChart.getAxisRight();
        rightAxis.setLabelCount(1,true);
        rightAxis.setGridColor(Color.TRANSPARENT);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
		//最后设置数据
		BodyDataChart.setData(initChartData(dataList, chartName,j));
	}
	private LineData initChartData(List<SignDataBean> dataList,String chartName,int j) {
		/**
		 * 设置图表Y轴的值
		 * 图表线宽，图表点的半径，图表高亮的颜色
		 */
		ArrayList<Entry> e1 = new ArrayList<Entry>();
		Log.e("val1", dataList.size()+"");
        for (int i = 0; i <dataList.size(); i++) {
        	if(dataList.get(i).getVal1()!=null){
        		String vall = dataList.get(i).getVal1();
                e1.add(new Entry(Float.parseFloat(vall), i));
        	}
        }
        LineDataSet d1;
        if(j>0){
        	d1 = new LineDataSet(e1, "收缩压");
        	d1.setColor(Color.parseColor("#FF343F"));
        	d1.setFillColor(Color.parseColor("#FF343F"));
        	d1.setHighLightColor(Color.parseColor("#FF343F"));
        	d1.setCircleColor(Color.parseColor("#FF343F"));
        }else{
        	d1 = new LineDataSet(e1, chartName);
        	d1.setColor(getResources().getColor(R.color.main_text));
        	d1.setFillColor(getResources().getColor(R.color.main_text));
        	d1.setHighLightColor(getResources().getColor(R.color.main_text));
        }
        
        d1.setLineWidth(2.5f);
        d1.setCircleSize(2.6f);
        
        d1.setDrawValues(false);
        
        d1.enableDashedLine(0f, 0f, 0f);
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        /**
         * 设置绑定X轴的值
         */
        	ArrayList<String> XValue=new ArrayList<String>();
	        for(int i=0;i<dataList.size();i++){
	        	Long time = dataList.get(i).getInspect_dt();
	        	if(timeSize==1){
	        		c.setTimeInMillis(time);
	        		String formatTime = sdfDay.format(c.getTime());
		        	XValue.add(formatTime);
	        	}else{
	        		c.setTimeInMillis(time);
		        	String string = sdf.format(c.getTime());
		        	XValue.add(string);
	        	}
	        	
	        }
	        
	        if(j>0){
		        ArrayList<Entry> e2 = new ArrayList<Entry>();
		
		        for (int i = 0; i < dataList.size(); i++) {
		        	String val2 = dataList.get(i).getVal2();
		            e2.add(new Entry(Float.parseFloat(val2), i));
		        }
		
		        LineDataSet d2 = new LineDataSet(e2, "舒张压");
		        d2.setLineWidth(2.5f);
		        d2.setCircleSize(2.6f);
		        d2.setHighLightColor(getResources().getColor(R.color.main_text));
		        d2.setColor(getResources().getColor(R.color.main_text));
		        d2.setCircleColor(getResources().getColor(R.color.main_text));
		        d2.setDrawValues(false);
		        d2.enableDashedLine(0f, 0f, 0f);
		        sets.add(d2);
        }
	    sets.add(d1);
        //sets.add(d2);
        LineData cd = new LineData(XValue, sets);
        return cd;
	}

	public Long getLastToday() {
		return LastToday;
	}
	public void setLastToday(Long lastToday) {
		if(todayTimes==0){
			LastToday = lastToday;
			++todayTimes;
		}
	}
	public Long getLastWeek() {
		return LastWeek;
	}
	public void setLastWeek(Long lastWeek) {
		if(weekTimes==0){
			LastWeek = lastWeek;
			++weekTimes;
		}
	}
	public Long getLastMonth() {
		return LastMonth;
	}
	public void setLastMonth(Long lastMonth) {
		if(monthTimes==0){
			LastMonth = lastMonth;
			++monthTimes;
		}
		
	}
	public Long getLastYear() {
		return LastYear;
	}
	public void setLastYear(Long lastYear) {
		if(yearTimes==0){
			LastYear = lastYear;
			++yearTimes;
		}
		
	}
	@Override
	public void getDayData() {
		type=1+"";
		timeSize=1;
		 getData(signType, type, order, todayTimeLong+"");
	}

	@Override
	public void getWeekData() {
		type=2+"";
		timeSize=2;
		LogUtil.e("WeekData", "week");
	    getData(signType, type, order, weekTimeLong+"");
	}

	@Override
	public void getMonthData() {
		type=3+"";
		timeSize=3;
		LogUtil.e("MonthData", "month");
		  getData(signType, type, order, monthimeLong+"");
	}

	@Override
	public void getYearData() {
		type=4+"";
		timeSize=4;
		LogUtil.e("YearData", "year");
		  getData(signType, type, order, yearTimeLong+"");
	}
}
