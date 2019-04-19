package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.MonthFood;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.WeekFood;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class MonthFoodActivity extends BaseActivity{
	int pageNo=1;
	List<MonthFood> monthFoods=new ArrayList<MonthFood>();
	ListView listView;
	WeekFood lastWeekFood=null;
	FoodAdapter foodAdapter;
	MonthFood currentMonth=null;
	TextView foodpage_old_age,foodpage_old_name,foodpage_old_loc;
	OldPeople old;
	int width, height;
	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_month_food;
	}
	boolean hasMoreData=true;
	@Override
	protected void initView() {
		WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

		width= (int) (wm.getDefaultDisplay().getWidth()/2.7);
		height = (int) (width*15/11);
		listView=(ListView) findViewById(R.id.new_list);
		foodAdapter=new FoodAdapter(getApplicationContext(), monthFoods, R.layout.food_item);
		listView.setAdapter(foodAdapter);
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int pos) {
				// TODO Auto-generated method stub
				if(hasMoreData&&foodAdapter!=null&&listView.getLastVisiblePosition()==foodAdapter.getCount()-1){
					load();
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		old=(OldPeople) getIntent().getSerializableExtra("old");
		foodpage_old_age=(TextView) findViewById(R.id.foodpage_old_age);
		foodpage_old_loc=(TextView) findViewById(R.id.foodpage_old_loc);
		foodpage_old_name=(TextView) findViewById(R.id.foodpage_old_name);
		LogUtil.e("old", old+" ");
		foodpage_old_loc.setText(old.getAgency_name());
		foodpage_old_name.setText(old.getElderly_name());
		foodpage_old_age.setText(old.getAge()+"岁");
		setActionBarTitle("历史食谱");
	}

	@Override
	protected void initData() {
		load();
	}
	int lastRecordMonth=0;
	private void load(){
			LefuApi.queryWeekRecipeWithMonth(pageNo,old,getApplicationContext(),new RequestCallback<List<WeekFood>>() {
				
				@Override
				public void onSuccess(List<WeekFood> result) {
					if(result==null){
						return;
					}
					if(result.size()==0){
						hasMoreData=false;
					}
					pageNo++;
					for(int i=0;i<result.size();i++){
						if(currentMonth==null){
							currentMonth=new MonthFood( TimeZoneUtil.getMonthNum(result.get(i).getLastDayOfWeek()),TimeZoneUtil.getMonthNum(result.get(i).getLastDayOfWeek())+"");
							currentMonth.addWeekFood(result.get(i));
							foodAdapter.addSingleData(currentMonth);
						}else{
							int currentMonthTime = foodAdapter.getItem(foodAdapter.getCount()-1).getMonthNum(); //currentMonth.getMonthNum();
							int monthTime = TimeZoneUtil.getMonthNum(result.get(i).getFirstDayOfWeek());
							LogUtil.e("month","currentMonthTime:"+currentMonthTime+" monthTime"+monthTime);
							if(currentMonthTime==monthTime){
								currentMonth.addWeekFood(result.get(i));
							}else{
								currentMonth=new MonthFood( TimeZoneUtil.getMonthNum(result.get(i).getLastDayOfWeek()),TimeZoneUtil.getMonthNum(result.get(i).getLastDayOfWeek())+"");
								currentMonth.addWeekFood(result.get(i));
								int currentMonthTime2 = foodAdapter.getItem(foodAdapter.getCount()-1).getMonthNum(); //currentMonth.getMonthNum();
								int monthTime2 = TimeZoneUtil.getMonthNum(result.get(i).getFirstDayOfWeek());
								if(currentMonthTime2!=monthTime2){
									foodAdapter.addSingleData(currentMonth);
								}
							}
						}
						
					}
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}
	class FoodAdapter extends CommonAdapter<MonthFood>{

		public FoodAdapter(Context context, List<MonthFood> datas, int layoutId) {
			super(context, datas, layoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder holder, MonthFood t,int position) {
			final List<WeekFood> foods = t.getWeekFoods();
			LogUtil.e("MonthFood", width+" "+height+" "+ foods.size()+" "+TimeZoneUtil.getTimeCompute(foods.get(0).getInspect_dt()));
			TextView tv=holder.getView(R.id.food_month);
			ImageView img5 = holder.getView(R.id.recipe_img5);
			img5.setLayoutParams(new LayoutParams(width, height));
			TextView time5=holder.getView(R.id.recipe_time5);
			ImageView img4 = holder.getView(R.id.recipe_img4);
			img4.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			TextView time4=holder.getView(R.id.recipe_time4);
			ImageView img3 = holder.getView(R.id.recipe_img3);
			img3.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			TextView time3=holder.getView(R.id.recipe_time3);
			ImageView img2=holder.getView(R.id.recipe_img2);
			img2.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			TextView time2=holder.getView(R.id.recipe_time2);
			ImageView img1=holder.getView(R.id.recipe_img1);
			img1.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
			TextView time1=holder.getView(R.id.recipe_time1);
			tv.setText(Utils.getTimeStr(t.getInspect_dt()));
			LinearLayout layout = holder.getView(R.id.horizontal_container);
			layout.setLayoutParams(new FrameLayout.LayoutParams(android.widget.FrameLayout.LayoutParams.WRAP_CONTENT, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));
			switch (foods.size()) {
				case 5:
					img5.setImageResource(R.drawable.recipe_page5);
					img5.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							intent.putExtra("data", foods.get(4).getId());
							intent.putExtra("startTime", foods.get(4).getFirstDayOfWeek());
							MonthFoodActivity.this.setResult(2,intent);
							MonthFoodActivity.this.finish();
						}
					});
					time5.setText(TimeZoneUtil.sdfDay.format(new Date(foods.get(4).getFirstDayOfWeek()))+"-"+TimeZoneUtil.sdfDay.format(new Date(foods.get(4).getLastDayOfWeek())));
				case 4:
					img4.setImageResource(R.drawable.recipe_page4);
					img4.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							intent.putExtra("data", foods.get(3).getId());
							intent.putExtra("startTime", foods.get(3).getFirstDayOfWeek());
							MonthFoodActivity.this.setResult(2,intent);
							MonthFoodActivity.this.finish();
						}
					});
					time4.setText(TimeZoneUtil.sdfDay.format(new Date(foods.get(3).getFirstDayOfWeek()))+"-"+TimeZoneUtil.sdfDay.format(new Date(foods.get(3).getLastDayOfWeek())));
				case 3:
					img3.setImageResource(R.drawable.recipe_page3);
					img3.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							intent.putExtra("data", foods.get(2).getId());
							intent.putExtra("startTime", foods.get(2).getFirstDayOfWeek());
							MonthFoodActivity.this.setResult(2,intent);
							MonthFoodActivity.this.finish();
						}
					});
					time3.setText(TimeZoneUtil.sdfDay.format(new Date(foods.get(2).getFirstDayOfWeek()))+"-"+TimeZoneUtil.sdfDay.format(new Date(foods.get(2).getLastDayOfWeek())));
				case 2:
					img2.setImageResource(R.drawable.recipe_page2);
					img2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							intent.putExtra("data", foods.get(1).getId());
							intent.putExtra("startTime", foods.get(1).getFirstDayOfWeek());
							MonthFoodActivity.this.setResult(2,intent);
							MonthFoodActivity.this.finish();
						}
					});
					time2.setText(TimeZoneUtil.sdfDay.format(new Date(foods.get(1).getFirstDayOfWeek()))+"-"+TimeZoneUtil.sdfDay.format(new Date(foods.get(1).getLastDayOfWeek())));
				case 1:
					img1.setImageResource(R.drawable.recipe_page1);
					img1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Intent intent=new Intent();
							intent.putExtra("data", foods.get(0).getId());
							intent.putExtra("startTime", foods.get(0).getFirstDayOfWeek());
							MonthFoodActivity.this.setResult(2,intent);
							MonthFoodActivity.this.finish();
						}
					});
					time1.setText(TimeZoneUtil.sdfDay.format(new Date(foods.get(0).getFirstDayOfWeek()))+"-"+TimeZoneUtil.sdfDay.format(new Date(foods.get(0).getLastDayOfWeek())));
			}
			switch (foods.size()+1) {
			case 1:
				img1.setImageResource(R.drawable.recipe_page_default);
				img1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(getApplicationContext(),"暂无数据");
					}
				});
				time1.setText("暂无数据");
			case 2:
				img2.setImageResource(R.drawable.recipe_page_default);
				img2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(getApplicationContext(),"暂无数据");
					}
				});
				time2.setText("暂无数据");
			case 3:
				img3.setImageResource(R.drawable.recipe_page_default);
				img3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(getApplicationContext(),"暂无数据");
					}
				});
				time3.setText("暂无数据");
			case 4:
				img4.setImageResource(R.drawable.recipe_page_default);
				img4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(getApplicationContext(),"暂无数据");
					}
				});
				time4.setText("暂无数据");
			case 5:
				img5.setImageResource(R.drawable.recipe_page_default);
				img5.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(getApplicationContext(),"暂无数据");
					}
				});
				time5.setText("暂无数据");
			default:
				break;
			}
		}
		
	}
	private String getMonthStr(String month){
		return month.substring(5, month.length())+" 月";
	}
	@Override
	protected boolean hasActionBar() {
		return true;
	}
}
