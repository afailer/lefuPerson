package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.HelpInfo;
import com.lefuyun.bean.MediaBean;
import com.lefuyun.bean.MyEvent;
import com.lefuyun.bean.NurseRecord;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.NurseCareDialog;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class NurseHistoryActivity extends BaseActivity implements OnRefreshListener, OnLoadListener {
	
	RefreshLayout list_container;
	private ListView mListView;
	List<NurseRecord> records=new ArrayList<NurseRecord>();
	OldPeople old;
	long nurs_items_id=0;
	int pageNo=1;//页码
	int type=0;//护理类型id
	long nurseDate=0;
	NurseHistoryAdapter nurseHistoryAdapter;
	List<HelpInfo> list=new ArrayList<HelpInfo>();
	RelativeLayout chose_nurse_type_click;
	TextView chose_nurse_type,lastTime;
	//选择护理类型的popupWindow
	PopupWindow popupWindow;
	CircleImageView circle_img;
	TextView nurse_his_oldname;
	View real_nodata;
	View view;
	ListView listView;
	
	private String mCurrentType = "全部";
	
	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_nurse_history;
	}

	@Override
	protected void initView() {
		setActionBarTitle("历史记录");
		list_container=(RefreshLayout) findViewById(R.id.list_container);
		mListView=(ListView) findViewById(R.id.new_list);
		Utils.fixSwiplayout(list_container);
		real_nodata=findViewById(R.id.real_nodata);
		old=(OldPeople) getIntent().getSerializableExtra("old");
		circle_img=(CircleImageView) findViewById(R.id.circle_img);
		ImageLoader.loadImg(old.getIcon(), circle_img);
		lastTime=(TextView) findViewById(R.id.last_time);
		nurse_his_oldname=(TextView) findViewById(R.id.nurse_his_oldname);
		nurse_his_oldname.setText(old.getElderly_name());
		chose_nurse_type_click=(RelativeLayout) findViewById(R.id.chose_nurse_type_click);
		chose_nurse_type=(TextView) findViewById(R.id.chose_nurse_type);
		chose_nurse_type_click.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showPopType();//选择护理内容
			}
		});
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				new NurseCareDialog(NurseHistoryActivity.this, nurseHistoryAdapter.getItem(position),2,new MyEvent(2, position));
			}
		});
		list_container.setOnRefreshListener(this);
		list_container.setOnLoadListener(this);
	}
	
	@SuppressWarnings("deprecation")
	private void showPopType(){
		if(popupWindow==null){
			view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.nurse_type_list, null);
			listView=(ListView) view.findViewById(R.id.typelist);
			listView.setAdapter(new NurseListAdapter(getApplicationContext(), list, R.layout.nurse_type_item));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					chose_nurse_type.setText(list.get(arg2).getNursing_content());
					mCurrentType = list.get(arg2).getNursing_content();
					nurs_items_id=list.get(arg2).getItem_id();
					pageNo=1;
					nurseHistoryAdapter.clearData();
					popupWindow.dismiss();
					loadData();
				}
			});
			popupWindow=new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true);
			popupWindow.setFocusable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
		}
		popupWindow.showAsDropDown(chose_nurse_type_click);
	}
	
	private void loadData(){
		LefuApi.queryDailyNursingRecordByUid(pageNo, old.getId(), nurseDate, nurs_items_id, new RequestCallback<List<NurseRecord>>() {
			
			@Override
			public void onSuccess(List<NurseRecord> result) {
				try{
				if(pageNo==1){
					if(result==null||result.size()==0){
						real_nodata.setVisibility(View.VISIBLE);
						list_container.setVisibility(View.GONE);
					}
				}
				if(pageNo==1&&result!=null&&result.size()>0){
					lastTime.setText("最后一次记录时间："+TimeZoneUtil.getTimeCompute(result.get(0).getNursing_dt()));
				}
				if(nurseHistoryAdapter==null){
					nurseHistoryAdapter=new NurseHistoryAdapter(getApplicationContext(), result, R.layout.item_module);
					mListView.setAdapter(nurseHistoryAdapter);
				}else{
					nurseHistoryAdapter.addData(result);
				}
				pageNo++;
				Utils.finishLoad(list_container);
				}catch(Exception e){}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				
			}
		});
		
	}
	
	@Override
	protected void initData() {
		loadData();
		LefuApi.getNurseTypeByAgencyId(old.getAgency_id(),new RequestCallback<List<HelpInfo>>() {

			@Override
			public void onSuccess(List<HelpInfo> result) {
				try{
				list.addAll(result);
				HelpInfo info = new HelpInfo();
				info.setItem_id(0);
				info.setNursing_content("全部");
				list.add(0, info);
				}catch(Exception e){}
			}

			@Override
			public void onFailure(ApiHttpException e) {
				
			}
		});
	}
	/**
	 * @author liuting
	 * 护理内容列表适配器
	 */
	class NurseHistoryAdapter extends CommonAdapter<NurseRecord>{

		public NurseHistoryAdapter(Context context, List<NurseRecord> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, NurseRecord t,int position) {
			holder.setText(R.id.module_content, "您的家人刚刚接受了护理服务,项目:\t"+t.getItems()
					+ " " + t.getReserved())
				.setText(R.id.module_time, 
							StringUtils.getFormatData(t.getNursing_dt(), "MM-dd HH:mm"))
				.setText(R.id.module_title, t.getItems());
			
			List<MediaBean> medias = Utils.getUrlList(t.getMedia(),Utils.picType);
			medias.addAll(Utils.getUrlList(t.getMedia(), Utils.audioType));
			medias.addAll(Utils.getUrlList(t.getMedia(), Utils.videoType));
			if(medias.size()==0){
				// 没有图片的情况
				holder.goneView(R.id.module_img);
			}else{
				// 有图片的情况
				holder.visibleView(R.id.module_img);
				Utils.setImageByMediaBean(holder, medias.get(0), R.id.module_img,"");
			}
		}
	}
	class NurseListAdapter extends CommonAdapter<HelpInfo>{

		public NurseListAdapter(Context context, List<HelpInfo> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, HelpInfo t,int position) {
			holder.setText(R.id.type_item,t.getNursing_content());
			if(mCurrentType.equals(t.getNursing_content())) {
				((TextView) holder.getView(R.id.type_item)).setTextColor(Color.parseColor("#06BEBD"));
			}else {
				((TextView) holder.getView(R.id.type_item)).setTextColor(Color.parseColor("#C7C7CD"));
			}
		}
		
	}
	@Override
	public void onRefresh() {
		pageNo=1;
		nurseHistoryAdapter.clearData();
		loadData();
	}

	@Override
	public void onLoad() {
		loadData();
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