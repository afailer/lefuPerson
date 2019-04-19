package com.lefuyun.ui;

import java.util.List;
import android.content.Context;
import android.content.Intent;
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
import com.lefuyun.bean.ImportantMsg;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.User;
import com.lefuyun.util.ImportantNoticeDialog;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class ImportantHistoryNoticeActivity extends BaseActivity implements OnRefreshListener, OnLoadListener {

	CircleImageView oldCircleImg;
	TextView oldName,oldLastTime;
	RefreshLayout listContainer;
	ListView listView;
	ImportantHistoryAdapter adapter;
	OldPeople old;
	User mUser;
	int pageNo=1;
	long update_dt=0;
	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_important_history_notice;
	}

	@Override
	protected void initView() {
		oldCircleImg=(CircleImageView) findViewById(R.id.important_his_circle_img);
		oldName=(TextView) findViewById(R.id.important_his_old_name);
		oldLastTime=(TextView) findViewById(R.id.important_his_last_time);
		listContainer = (RefreshLayout) findViewById(R.id.list_container_his);
		listView=(ListView) findViewById(R.id.list_his);
		Utils.fixSwiplayout(listContainer);
		old=(OldPeople) getIntent().getSerializableExtra("old");
		mUser=(User) getIntent().getSerializableExtra("user");
		ImageLoader.loadImg(old.getIcon(), oldCircleImg);
		oldName.setText(old.getElderly_name());
		setActionBarTitle("历史记录");
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				new ImportantNoticeDialog(ImportantHistoryNoticeActivity.this, adapter.getItem(position), old);
				/*Intent intent=new Intent(getApplicationContext(), NurseH5DetailActivity.class);
				intent.putExtra("importantMsg", adapter.getItem(position));
				intent.putExtra("old", old);
				intent.putExtra("pageType", 2);
				startActivity(intent);*/
			}
		});
		listContainer.setOnRefreshListener(this);
		listContainer.setOnLoadListener(this);
	}

	@Override
	protected void initData() {
		loadData();
	}
	private void loadData(){
		LefuApi.queryReceivedNoticeList(mUser.getUser_id(), old.getId(), update_dt, pageNo, new RequestCallback<List<ImportantMsg>>() {

			@Override
			public void onSuccess(List<ImportantMsg> result) {
				if(result==null){
					return;
				}
				pageNo++;
				if(adapter==null){
					adapter=new ImportantHistoryAdapter(getApplicationContext(), result, R.layout.item_module);
					listView.setAdapter(adapter);
					if(result.size()>0){
						oldLastTime.setText(TimeZoneUtil.getTimeCompute(result.get(0).getUpdate_dt()));
					}
					
				}else{
					adapter.addData(result);
				}
				try{
					listContainer.setLoading(false);
				}catch(Exception e){}
				try{
					listContainer.setRefreshing(false);
				}catch(Exception e){
					
				}
			}

			@Override
			public void onFailure(ApiHttpException e) {
				
			}
		});
	}
	class ImportantHistoryAdapter extends CommonAdapter<ImportantMsg>{

		public ImportantHistoryAdapter(Context context,
				List<ImportantMsg> datas, int layoutId) {
			super(context, datas, layoutId);
			
		}
		@Override
		public void convert(ViewHolder holder, ImportantMsg t,int position) {
			if("".equals(t.getPicture())){
				holder.goneView(R.id.module_img);
			}else{
				holder.visibleView(R.id.module_img);
				holder.setImageByUrl(R.id.module_img, t.getPicture());
			}
			holder.setText(R.id.module_title, t.getTheme()).setText(R.id.module_content,t.getHeadline())
			.setText(R.id.module_time, TimeZoneUtil.getTimeCompute(t.getCreate_dt()));
		}
	}
	@Override
	public void onRefresh() {
		pageNo=1;
		adapter.clearData();
		loadData();
	}

	@Override
	public void onLoad() {
		loadData();
	}
	@Override
	protected boolean hasActionBar() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected boolean hasBackButton() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
