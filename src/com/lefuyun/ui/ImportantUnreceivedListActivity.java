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
import com.lefuyun.bean.ImportantMsg;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.User;
import com.lefuyun.util.ImportantNoticeDialog;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.TimeZoneUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

public class ImportantUnreceivedListActivity extends BaseActivity implements OnRefreshListener, OnLoadListener {
		
		
		private RefreshLayout mSwiplayout;
		private ListView mListView;	
		private int currentPageNo=1;  //当前第几页
		private User user;	//当前用户
		private OldPeople currentOld;//当前老人
		private DetailAdapter adapter;
		List<ImportantMsg> importantMsg=new ArrayList<ImportantMsg>();
		String type="unread";
		CircleImageView important_his_circle_img;
		TextView important_his_old_name,important_his_last_time;
		/**
		 * 上拉刷新
		 */
		@Override
		public void onRefresh() {
			currentPageNo=1;
			adapter.clearData();
			loadData();
		}

		/**
		 * 加载更多
		 */
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_important_detail_notice;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initView() {
		setActionBarTitle("重要通知");
		important_his_circle_img=(CircleImageView) findViewById(R.id.important_his_circle_img);
		important_his_old_name=(TextView) findViewById(R.id.important_his_old_name);
		important_his_last_time=(TextView) findViewById(R.id.important_his_last_time);
		user=(User) getIntent().getSerializableExtra("user");
		currentOld=(OldPeople) getIntent().getSerializableExtra("old");
		//importantMsg.addAll((List<ImportantMsg>) getIntent().getSerializableExtra("importants"));
		ImageLoader.loadImg(currentOld.getIcon(),important_his_circle_img);
		important_his_old_name.setText(currentOld.getElderly_name());
		
		mSwiplayout = (RefreshLayout)findViewById(R.id.swiplayout);
		mSwiplayout.post(new Thread(new Runnable() {
			@Override
			public void run() {
				mSwiplayout.setRefreshing(false);
			}
		}));
		mSwiplayout.setColorSchemeResources(R.color.main_background,
				R.color.main_gray,
				R.color.main_background);
		mListView = (ListView)findViewById(R.id.list);
		mSwiplayout.setOnRefreshListener(this);
		mSwiplayout.setOnLoadListener(this);
		adapter=new DetailAdapter(getApplicationContext(),importantMsg, R.layout.item_module);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				new ImportantNoticeDialog(ImportantUnreceivedListActivity.this, adapter.getItem(position), currentOld);
				/*Intent intent=new Intent(getApplicationContext(), NurseH5DetailActivity.class);
				intent.putExtra("importantMsg", adapter.getItem(position));
				intent.putExtra("old", currentOld);
				intent.putExtra("pageType", 2);
				startActivity(intent);*/
				//adapter.removeData(adapter.getItem(position));
			}
		});
	}

	@Override
	protected void initData() {
		loadData();
	}
	private void loadData(){
		/**
		 * 获取未读的重要通知
		 */
		if(type.equals("unread")){
			LefuApi.queryUnreceivedNoticeList(user.getUser_id(), currentOld.getId(), currentPageNo, new RequestCallback<List<ImportantMsg>>() {
	
				@Override
				public void onSuccess(List<ImportantMsg> result) {
					if(result==null){
						return;
					}
					if(currentPageNo==1){
						if(result.size()>0){
							important_his_last_time.setText(TimeZoneUtil.getTimeCompute(result.get(0).getUpdate_dt()));
						}
					}
					currentPageNo++;
					adapter.addData(result);
					Utils.finishLoad(mSwiplayout);
				}
	
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}else if(type.equals("all")){
			
		}
	}
	class DetailAdapter extends CommonAdapter<ImportantMsg>{

		public DetailAdapter(Context context, List<ImportantMsg> datas,
				int layoutId) {
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder holder, ImportantMsg t,int position) {
			// TODO Auto-generated method stub
			if("".equals(t.getPicture())){
				holder.goneView(R.id.module_img);
			}else{
				holder.visibleView(R.id.module_img);
				holder.setImageByUrl(R.id.module_img, t.getPicture());
			}
			try{
				holder.setText(R.id.module_title, t.getTheme());
				holder.setText(R.id.module_content, t.getHeadline());
			}catch(Exception e){
				
			}
		}
	}
}
