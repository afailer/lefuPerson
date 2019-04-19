package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonPagerAdapter;
import com.lefuyun.bean.MyEvent;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.WeekFood;
import com.lefuyun.bean.WeekRecipe;
import com.lefuyun.bean.WeekRecipe.pic;
import com.lefuyun.bean.WeekRecipeBean;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.util.WeekRecipeDialog;
import com.lefuyun.widget.CircleIndicators;
import com.lefuyun.widget.SelectableRoundedImageView;

import de.greenrobot.event.EventBus;

public class WeekFoodActivity extends BaseActivity {
	
	ViewPager weekFoodPager;
	long weekRecipeId;
	WeekFoodAdapter adapter=null;
	CircleIndicators indicator;
	EventBus eventBus;
	RelativeLayout recipe_parent,real_nodata;
	OldPeople old;
	TextView tv_nodatatext;
	int width;
	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_week_food;
	}

	@Override
	protected void initView() {
		setActionBarTitle("一周食谱");
		weekFoodPager=(ViewPager) findViewById(R.id.week_food_pager);
		//weekFoodPager.setTransitionEffect(TransitionEffect.CubeOut);
		old=(OldPeople) getIntent().getSerializableExtra("old");
		LogUtil.e("old", old+"");
		indicator=(CircleIndicators) findViewById(R.id.indicator);
		eventBus=EventBus.getDefault();
		recipe_parent=(RelativeLayout) findViewById(R.id.recipe_parent);
		real_nodata=(RelativeLayout) findViewById(R.id.real_nodata);
		tv_nodatatext=(TextView) findViewById(R.id.tv_nodatatext);
		tv_nodatatext.setText("本周未上传食谱");
		WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth()/48;
	}

	@Override
	protected void initData() {
		 firstLoad();
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
	protected boolean hasRightImageView() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected int getActionBarRightImage() {
		return R.drawable.his_important;
	}
	@Override
	public void onRightViewClick(View view) {
		LogUtil.e("old", old+"");
		Intent intent = new Intent(getApplicationContext(), MonthFoodActivity.class);
		intent.putExtra("old", old);
		startActivityForResult(intent, 2);
	}
	/**
	 * 第一次进来通过当前是第几周查询这一周对应的食谱id
	 */
	private void firstLoad(){
		LefuApi.queryWeekRecipeListByWeekNum(old,new Date().getTime(),new RequestCallback<List<WeekFood>>() {
			
			@Override
			public void onSuccess(List<WeekFood> result) {
				if(result==null){
					return;
				}
				if(result.size()==0){
					List<WeekRecipe> recipes=new ArrayList<WeekRecipe>();
					for(int i=0;i<7;i++){
						recipes.add(new WeekRecipe());
					}
					adapter=new WeekFoodAdapter(getApplicationContext(), recipes, R.layout.week_food_item);
					weekFoodPager.setAdapter(adapter);
					indicator.setViewPager(weekFoodPager);
				}else{
					startTime=result.get(0).getFirstDayOfWeek();
					weekRecipeId=result.get(0).getId();
					load();//根据获取到的食谱id查询食谱
				}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	WeekRecipe data;
	private void load(){
		LefuApi.queryWeekRecipeById(weekRecipeId,old,new RequestCallback<WeekRecipeBean>() {
			@Override
			public void onSuccess(WeekRecipeBean result) {
				if(result==null){
					return;
				}
					if(result.getDayRecipeBeans().size()==0){
						List<WeekRecipe> recipes=new ArrayList<WeekRecipe>();
						for(int i=0;i<7;i++){
							recipes.add(new WeekRecipe());
						}
						adapter=new WeekFoodAdapter(getApplicationContext(), recipes, R.layout.week_food_item);
						weekFoodPager.setAdapter(adapter);
						indicator.setViewPager(weekFoodPager);
					}
					if(result.getDayRecipeBeans().size()>0){
						data=result.getDayRecipeBeans().get(0);
						adapter=new WeekFoodAdapter(getApplicationContext(), result.getDayRecipeBeans(), R.layout.week_food_item);
						weekFoodPager.setAdapter(adapter);
						indicator.setViewPager(weekFoodPager);
					}
					
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	//int[] bgs={R.drawable.shape_color2_color_big_circular,R.drawable.shape_color1_color_big_circular,R.drawable.shape_color6_color_big_circular,R.drawable.shape_color5_color_big_circular,R.drawable.shape_color3_color_big_circular,R.drawable.shape_color4_color_big_circular};
	int[] weekBgs={R.drawable.week1,R.drawable.week2,R.drawable.week3,R.drawable.week4,R.drawable.week5,R.drawable.week6,R.drawable.week7};
	class WeekFoodAdapter extends CommonPagerAdapter<WeekRecipe>{

		public WeekFoodAdapter(Context context, List<WeekRecipe> datas,
				int layoutId) {
			super(context, datas, layoutId);
			
		}

		@Override
		public void processItem(View view, final WeekRecipe t,final int position) {
			pic hasPic = t.hasPic();//有没有图片或文字
			
			SelectableRoundedImageView imgView = (SelectableRoundedImageView) view.findViewById(R.id.weekfood_img_item);
			LinearLayout container1=(LinearLayout) view.findViewById(R.id.food_container1);
			LinearLayout container2=(LinearLayout) view.findViewById(R.id.food_container2);
			TextView foodWeekTime=(TextView) view.findViewById(R.id.food_week_time);
			TextView foodDayTime=(TextView) view.findViewById(R.id.food_day_time);
			if(hasPic!=null){
				LogUtil.e("pic", hasPic.toString());
				final long times=startTime+position*86400000;
				List<pic> picUrlList = t.getPicUrlList();
				//List<String> meatStr=new ArrayList<String>();
				//meatStr.add("早餐");meatStr.add("早餐加餐");meatStr.add("午餐");meatStr.add("午餐加餐");meatStr.add("晚餐");meatStr.add("晚餐加餐");
				for(int i=0;i<picUrlList.size();i++){
					RelativeLayout viewText=(RelativeLayout) View.inflate(getApplicationContext(), R.layout.food_item_type, null);
					TextView text=(TextView) viewText.findViewById(R.id.text_food_type);
					text.setTextColor(Color.parseColor("#FFFFFF"));
					text.setPadding(width,10, width, 10);
					text.setText(picUrlList.get(i).getMeatName());
					text.setBackgroundResource(Utils.getIntDrawable(i));
					
					if(i<4){
						container1.addView(viewText);//TimeZoneUtil.normalSdf.format(new Date(times))
					}else{
						container2.addView(viewText);
					}
				}
				if(times>8*86400000){
					foodWeekTime.setText(TimeZoneUtil.getWeekOfDate(times));
					foodDayTime.setText(TimeZoneUtil.normalSdf.format(times));
				}else{
					foodWeekTime.setText(getWeekStr(position+1));
				}
				if("".equals(t.getPicUrl())){
					imgView.setImageResource(weekBgs[position]);
					//imgView.setVisibility(View.GONE);//没有图片显示默认图
					//bg.setVisibility(View.VISIBLE);
				}else{
					ImageLoader.loadImg(t.getPicUrl(), imgView);
				}
				
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						t.setCreateTime(times);
						new WeekRecipeDialog(WeekFoodActivity.this, t, new MyEvent(3, position));//为每一个item设置弹出dialog的监听
					}
				});
				
			}else{
				data=null;
				imgView.setImageResource(weekBgs[position]);
				foodWeekTime.setText(getWeekStr(position+1));
				foodDayTime.setText("暂无数据");
				for(int i=0;i<6;i++){
						RelativeLayout viewText=(RelativeLayout) View.inflate(getApplicationContext(), R.layout.food_item_type, null);
						TextView text=(TextView) viewText.findViewById(R.id.text_food_type);
						text.setPadding(width,10, width, 10);
						text.setTextColor(Color.parseColor("#FFFFFF"));
						text.setBackgroundResource(Utils.getIntDrawable(i));
					if(i==0){
						text.setText("早餐");
					}else if(i==1){
						text.setText("早餐加餐");
					}else if(i==2){
						text.setText("午餐");
					}else if(i==3){
						text.setText("午餐加餐");
					}else if(i==4){
						text.setText("晚餐");
					}else if(i==5){
						text.setText("晚餐加餐");
					}
					if(i<4){
						container1.addView(viewText);//TimeZoneUtil.normalSdf.format(new Date(times))
					}else{
						container2.addView(viewText);
					}
				}
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						ToastUtils.show(getApplicationContext(), "当天暂无食谱");
					}
				});
			}
		}
	}
	private String getWeekStr(int pos){
		switch (pos) {
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		case 7:
			return "星期天";
		default :
			return "";
		}
	}
	long startTime=0;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==2&&resultCode==2){
			weekRecipeId=data.getLongExtra("data", 1);
			startTime=data.getLongExtra("startTime", 1);
			LogUtil.e("data", resultCode+" "+requestCode+" "+weekRecipeId);
			load();
		}
	}
}
