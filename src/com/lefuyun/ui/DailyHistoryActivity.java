package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.DailyLifeRecord;
import com.lefuyun.bean.MediaBean;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.DailyCareDialog;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class DailyHistoryActivity extends BaseActivity implements OnLoadListener, OnRefreshListener {
	
	RefreshLayout list_container;
	ListView new_list;
	OldPeople old;
	DailyHistoryAdapter dailyAdapter;
	CircleImageView circle_img;
	TextView lastTime,nurse_his_oldname;
	DailyHistoryAdapter mAdapter;
	long nursing_dt;
	int pageNo=1;
	View real_nodata;
	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_daily_history;
	}

	@Override
	protected void initView() {
		setActionBarTitle("日常生活");
		list_container=(RefreshLayout) findViewById(R.id.list_container);
		real_nodata=findViewById(R.id.real_nodata);
		list_container.setOnLoadListener(this);
		list_container.setOnRefreshListener(this);
		new_list=(ListView) findViewById(R.id.new_list);
		new_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				DailyLifeRecord record = dailyAdapter.getItem(position);
				new DailyCareDialog(DailyHistoryActivity.this, record);
			}
		});
		Utils.fixSwiplayout(list_container);
		old=(OldPeople) getIntent().getSerializableExtra("old");
		LogUtil.e("old", old+"");
		circle_img=(CircleImageView) findViewById(R.id.circle_img);
		ImageLoader.loadImg(old.getIcon(), circle_img);
		lastTime=(TextView) findViewById(R.id.last_time);
		nurse_his_oldname=(TextView) findViewById(R.id.nurse_his_oldname);
		nurse_his_oldname.setText(old.getElderly_name());
		
	}

	@Override
	protected void initData() {
		loadData();
	}
	private void loadData(){
		LefuApi.queryDailyLifeRecordByUid(pageNo, old.getId(), 0, new RequestCallback<List<DailyLifeRecord>>() {
			
			@Override
			public void onSuccess(List<DailyLifeRecord> result) {
				if(pageNo==1){
					if(result.size()==0){
						real_nodata.setVisibility(View.VISIBLE);
						list_container.setVisibility(View.GONE);
					}else{
						real_nodata.setVisibility(View.GONE);
						list_container.setVisibility(View.VISIBLE);
					}
				}
				pageNo++;
				if(dailyAdapter==null){
					if(result!=null&&result.size()>0){
						long dt = result.get(0).getInspect_dt();
						lastTime.setText("最近一条更新时间: "+TimeZoneUtil.getTimeCompute(dt));
						dailyAdapter=new DailyHistoryAdapter(getApplicationContext(), result, R.layout.item_module);
						new_list.setAdapter(dailyAdapter);
					}
				}else{
						dailyAdapter.addData(result);
				}
				
				Utils.finishLoad(list_container);
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				
			}
		});
	}
	List<MediaBean> medias=new ArrayList<MediaBean>();
	class DailyHistoryAdapter extends CommonAdapter<DailyLifeRecord>{

		public DailyHistoryAdapter(Context context,
				List<DailyLifeRecord> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, DailyLifeRecord t,int position) {
			List<MediaBean> medias=Utils.getUrlList(t.getMedia(), Utils.picType);
			medias.addAll(Utils.getUrlList(t.getMedia(), Utils.audioType));
			medias.addAll(Utils.getUrlList(t.getMedia(), Utils.videoType));
			//String url="";
			LogUtil.e("daily", t+"");
			holder.setText(R.id.module_title, getTitle(t))
			.setText(R.id.module_content, getCareStr(t) + " " + t.getReserved()).
			setText(R.id.module_time, StringUtils.getFormatData(t.getInspect_dt(), "MM-dd HH:mm"));
			if(medias.size()==0){
				holder.goneView(R.id.module_img);
			}else{
				holder.visibleView(R.id.module_img);
				//holder.setImageByUrl(R.id.module_img,url);
				Utils.setImageByMediaBean(holder, medias.get(0), R.id.module_img,"");
			}
		}
		
	}
	private String getTitle(DailyLifeRecord record) {
		switch (record.getType()) {
		case 1:
			return "随手拍";
		case 2:
			return getMealTypeStr(record.getMeal_type());
		case 3:
			return "睡觉";
		default:
			return "";
		}
	}
	private String getCareStr(DailyLifeRecord record){
		switch (record.getType()) {
		case 2:
			return "您的家人在吃"+getMealTypeStr(record.getMeal_type())+",食量 "+getMeal_amountStr(record.getMeal_amount());
		case 3:
			return "您的家人在睡觉，睡眠质量:"+record.getSleep_quality();
		default:
			return "";
		}
	}
	private String getMealTypeStr(int meal_type){
		switch (meal_type) {
		case 1:
			return "早餐";
		case 2:
			return "午餐";
		case 3:
			return "晚餐";
		default:
			return "";
		}
	}
	private String getMeal_amountStr(int meal_amount){
		switch (meal_amount) {
		case 1:
			return "偏少";
		case 2:
			return "正常";
		case 3:
			return "偏多";
		default:
			return "";
		}
	}

	@Override
	public void onLoad() {
		loadData();
	}

	@Override
	public void onRefresh() {
		try{
			pageNo=1;
			dailyAdapter.clearData();
			loadData();
		}catch(Exception e){
			Utils.finishLoad(list_container);
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
}
