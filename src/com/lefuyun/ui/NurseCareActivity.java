package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.DailyLifeRecord;
import com.lefuyun.bean.MediaBean;
import com.lefuyun.bean.MyEvent;
import com.lefuyun.bean.NurseRecord;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.User;
import com.lefuyun.util.DailyCareDialog;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.NurseCareDialog;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

import de.greenrobot.event.EventBus;

public class NurseCareActivity extends BaseActivity implements OnLoadListener, OnRefreshListener {
	CareAdapter careAdapter;
	DailyAdapter dailyAdapter;
	//List<NurseRecord> items=new ArrayList<NurseRecord>();
	ListView list;
	OldPeople old;
	User mUser;
	int pageNo=1;
	RefreshLayout mSwiplayout;
	RelativeLayout all_pic;
	TextView nurse_old_name,nurse_old_age,nurse_old_loc;
	int type;
	View nodata;
	EventBus eventbus;
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.all_pic:
				Intent intent=new Intent(getApplicationContext(), NurseAllPhotoActivity.class);
				intent.putExtra("type",type);
				intent.putExtra("old",old);
				startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_nurse_care;
	}
	
	@Override
	protected boolean hasRightImageView() {
		return true;
	}
	
	@Override
	protected int getActionBarRightImage() {
		return R.drawable.his_important;
	}
	
	@Override
	public void onRightViewClick(View view) {
		if(type==Utils.typeNurse){
			Intent intent=new Intent(getApplicationContext(), NurseHistoryActivity.class);
			intent.putExtra("old", old);
			startActivity(intent);
		}else{
			Intent intent=new Intent(getApplicationContext(), DailyHistoryActivity.class);
			intent.putExtra("old", old);
			startActivity(intent);
		}
	}
	public void onEventMainThread(MyEvent event){
		try{
		careAdapter.getItem(event.getPosition()).setPraise_number(careAdapter.getItem(event.getPosition()).getPraise_number()+1);
		careAdapter.notifyDataSetChanged();
		}catch(Exception e){}
	}
	@Override
	protected void initView() {
		type = getIntent().getIntExtra("type", 1);
		if(Utils.typeDaily==type){
			setActionBarTitle("日常生活");
		}else if(Utils.typeNurse==type){
			setActionBarTitle("护理服务");
		}
		EventBus.getDefault().register(this);
		eventbus=EventBus.getDefault();
		nodata=findViewById(R.id.real_nodata);
		all_pic=(RelativeLayout) findViewById(R.id.all_pic);
		all_pic.setOnClickListener(this);
		mSwiplayout=(RefreshLayout) findViewById(R.id.swiplayout);
		Utils.fixSwiplayout(mSwiplayout);
		mSwiplayout.setOnRefreshListener(this);
		mSwiplayout.setOnLoadListener(this);
		old=(OldPeople) getIntent().getSerializableExtra("old");
		nurse_old_name=(TextView) findViewById(R.id.nurse_old_name);
		nurse_old_loc=(TextView) findViewById(R.id.nurse_old_loc);
		nurse_old_age=(TextView) findViewById(R.id.nurse_old_age);
		nurse_old_name.setText(old.getElderly_name());
		nurse_old_loc.setText(old.getAgency_name());
		nurse_old_age.setText("年龄："+old.getAge()+"岁");
		mUser=(User) getIntent().getSerializableExtra("user");
		LogUtil.e("mUser", mUser+"");
		list=(ListView) findViewById(R.id.nurselist);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(type==Utils.typeNurse){
					NurseRecord record = careAdapter.getItem(position);
					new NurseCareDialog(NurseCareActivity.this, record,1,null);
				}else if(type==Utils.typeDaily){
					DailyLifeRecord record = dailyAdapter.getItem(position);
					new DailyCareDialog(NurseCareActivity.this, record);
				}
				
			}
		});
		loadData(type);
	}
	private void loadData(int type){
		if(type==Utils.typeNurse){
			LefuApi.queryDailyNursingRecordByUid(pageNo,old.getId(),0, 0, new RequestCallback<List<NurseRecord>>() {
				
				@Override
				public void onSuccess(List<NurseRecord> result) {
					try{
					if(pageNo==1){
						if(result.size()==0){
							nodata.setVisibility(View.VISIBLE);
						}else{
							nodata.setVisibility(View.GONE);
						}
					}
					pageNo++;
					Utils.finishLoad(mSwiplayout);
					if(careAdapter==null){
						careAdapter=new CareAdapter(getApplicationContext(), result, R.layout.complex_item);
						list.setAdapter(careAdapter);
					}else{
						careAdapter.addData(result);
					}
					}catch(Exception e){}
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}else if(type==Utils.typeDaily){
			LefuApi.queryDailyLifeRecordByUid(pageNo, old.getId(), 0, new RequestCallback<List<DailyLifeRecord>>() {

				@Override
				public void onSuccess(List<DailyLifeRecord> result) {
					try{
					if(pageNo==1){
						if(result.size()==0){
							nodata.setVisibility(View.VISIBLE);
						}else{
							nodata.setVisibility(View.GONE);
						}
					}
					pageNo++;
					Utils.finishLoad(mSwiplayout);
					
					if(dailyAdapter==null){
						dailyAdapter=new DailyAdapter(getApplicationContext(), result,  R.layout.complex_item);
						list.setAdapter(dailyAdapter);
					}else{
						dailyAdapter.addData(result);
					}
					}catch(Exception e){}
				}

				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}
	}
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	@Override
	protected void initData() {
		
	}
	class DailyAdapter extends CommonAdapter<DailyLifeRecord>{

		public DailyAdapter(Context context, List<DailyLifeRecord> datas,
				int layoutId) {
			super(context, datas, layoutId);
			
		}
		@Override
		public void convert(ViewHolder holder, DailyLifeRecord dailyRecord,int position) {
			
			holder.setText(R.id.time, TimeZoneUtil.getTimeCompute(dailyRecord.getInspect_dt())+"   --");
			holder.setBackgroundColor(getApplicationContext(),R.id.nursetype, position);
			if(dailyRecord.getType()==1){
				holder.setText(R.id.nursetype, "随手拍");
				if(StringUtils.isEmpty(dailyRecord.getReserved())) {
					holder.setText(R.id.nurse_reserved, "随手拍");
				}else {
					holder.setText(R.id.nurse_reserved, dailyRecord.getReserved());
				}
			}else if(dailyRecord.getType()==2){
				holder.setText(R.id.nursetype, "吃饭");
				if(StringUtils.isEmpty(dailyRecord.getReserved())) {
					holder.setText(R.id.nurse_reserved, "您的家人在吃"+getMealTypeStr(dailyRecord.getMeal_type())+",食量:"+getMeal_amountStr(dailyRecord.getMeal_amount()));
				}else{
					holder.setText(R.id.nurse_reserved, dailyRecord.getReserved());
				}
			}else if(dailyRecord.getType()==3){
				holder.setText(R.id.nursetype, "睡眠");
				if(StringUtils.isEmpty(dailyRecord.getReserved())) {
					holder.setText(R.id.nurse_reserved, "您的家人在睡觉，睡眠质量:"+dailyRecord.getSleep_quality());
				}else{
					holder.setText(R.id.nurse_reserved, dailyRecord.getReserved());
				}
			}
			medias.clear();
			medias.addAll(Utils.getUrlList(dailyRecord.getMedia(),Utils.picType));
			medias.addAll(Utils.getUrlList(dailyRecord.getMedia(), Utils.audioType));
			medias.addAll(Utils.getUrlList(dailyRecord.getMedia(), Utils.videoType));
			LogUtil.e("media", dailyRecord.getMedia());
			LogUtil.e("medias", medias.size()+"");
				if(medias.size()==0){
					holder.goneView(R.id.circleimgmiddle).goneView(R.id.circleimgleft).goneView(R.id.circleimgright);	
				}else if(medias.size()==1){
					holder.visibleView(R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(0), R.id.circleimgleft);
					holder.inVisibleView(R.id.circleimgmiddle).inVisibleView(R.id.circleimgright);
				}else if(medias.size()==2){
					holder.visibleView(R.id.circleimgmiddle).visibleView(R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(0), R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(1), R.id.circleimgmiddle);
					holder.inVisibleView(R.id.circleimgright);
				}else{
					holder.visibleView(R.id.circleimgmiddle).visibleView(R.id.circleimgleft).visibleView(R.id.circleimgright);
					Utils.setImageByMediaBean(holder, medias.get(0), R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(1), R.id.circleimgmiddle);
					Utils.setImageByMediaBean(holder, medias.get(2), R.id.circleimgright);
				}
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
	List<MediaBean> medias=new ArrayList<MediaBean>();
	class CareAdapter extends CommonAdapter<NurseRecord>{

		public CareAdapter(Context context, List<NurseRecord> datas, int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, NurseRecord nurseRecord,int position) {
			holder.setText(R.id.time, TimeZoneUtil.getTimeCompute(nurseRecord.getNursing_dt())+"   --")
			.setText(R.id.nursetype, nurseRecord.getItems());
			holder.setBackgroundColor(getApplicationContext(), R.id.nursetype, position);
			if("".equals(nurseRecord.getReserved())){
				holder.setText(R.id.nurse_reserved, "您的家人刚刚接受了护理服务，项目:"+nurseRecord.getItems());
			}else{
				holder.setText(R.id.nurse_reserved, nurseRecord.getReserved());
			}
			medias.clear();
			medias.addAll(Utils.getUrlList(nurseRecord.getMedia(),Utils.picType));
			medias.addAll(Utils.getUrlList(nurseRecord.getMedia(), Utils.audioType));
			medias.addAll(Utils.getUrlList(nurseRecord.getMedia(), Utils.videoType));
			LogUtil.e("media", nurseRecord.getMedia());
			LogUtil.e("medias", medias.size()+"");
				if(medias.size()==0){
					holder.goneView(R.id.circleimgmiddle).goneView(R.id.circleimgleft).goneView(R.id.circleimgright);	
				}else if(medias.size()==1){
					holder.visibleView(R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(0), R.id.circleimgleft);
					holder.inVisibleView(R.id.circleimgmiddle).inVisibleView(R.id.circleimgright);
				}else if(medias.size()==2){
					holder.visibleView(R.id.circleimgmiddle).visibleView(R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(0), R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(1), R.id.circleimgmiddle);
					holder.inVisibleView(R.id.circleimgright);
				}else{
					holder.visibleView(R.id.circleimgmiddle).visibleView(R.id.circleimgleft).visibleView(R.id.circleimgright);
					Utils.setImageByMediaBean(holder, medias.get(0), R.id.circleimgleft);
					Utils.setImageByMediaBean(holder, medias.get(1), R.id.circleimgmiddle);
					Utils.setImageByMediaBean(holder, medias.get(2), R.id.circleimgright);
				}
		}  
	}
	
	@Override
	public void onRefresh() {
		pageNo=1;
		if(type==Utils.typeNurse){
			careAdapter.clearData();
		}else if(type==Utils.typeDaily){
			dailyAdapter.clearData();
		}
		
		loadData(type);
	}

	@Override
	public void onLoad() {
		loadData(type);
	}
	

}
