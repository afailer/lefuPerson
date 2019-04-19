package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.ImportantMsg;
import com.lefuyun.bean.JpushNotificationDetailBean;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.User;
import com.lefuyun.util.ConfigForSpeialOldUtils;
import com.lefuyun.util.ImportantNoticeDialog;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;

/**
 * @author chenshichao
 * @date   2016-5-20
 * @description  重要通知从推送的通知跳转过来的
 */
public class ImportantNotifyForNotifyListActivity extends BaseActivity implements OnRefreshListener, OnLoadListener {
		JpushNotificationDetailBean detailBean;
		private RefreshLayout mSwiplayout;
		private ListView mListView;	
		private OldPeople currentOld;//当前老人
		private DetailAdapter adapter;
		List<ImportantMsg> importantMsg=new ArrayList<ImportantMsg>();
		String type="unread";
		View headview;
		
		/**
		 * 上拉刷新
		 */
		@Override
		public void onRefresh() {
			adapter.clearData();
			loadData();
		}

		/**
		 * 加载更多
		 */
		@Override
		public void onLoad() {
			//loadData();
			Utils.finishLoad(mSwiplayout);
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
	public void onClick(View arg0) {
		
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_important_detail_notice;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initView() {
		setActionBarTitle("重要通知");
		mSwiplayout = (RefreshLayout)findViewById(R.id.swiplayout);
		headview=findViewById(R.id.headview);
		headview.setVisibility(View.GONE);
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
				LogUtil.i("tag", "点击的是listview");
				currentOld=new OldPeople();
				currentOld.setId(Long.parseLong(adapter.getItem(position).getOldPeopleIds()));
				new ImportantNoticeDialog(ImportantNotifyForNotifyListActivity.this, adapter.getItem(position), currentOld);
			}
		});
	}

	@Override
	protected void initData() {
		detailBean=(JpushNotificationDetailBean) getIntent().getSerializableExtra("detailBean");
		loadData();
	}
	private void loadData(){
		/**
		 * 获取未读的重要通知
		 */
		if(type.equals("unread")){
			LefuApi.queryNoticeByNoticeid(detailBean.getId(), new RequestCallback<List<ImportantMsg>>() {
	
				@Override
				public void onSuccess(List<ImportantMsg> result) {
					if(result==null){
						return;
					}
					Utils.finishLoad(mSwiplayout);
					ConfigForSpeialOldUtils.getHaveConfigOld(ImportantNotifyForNotifyListActivity.this);
					List<OldPeople>oldPeoples_all=ConfigForSpeialOldUtils.oldPeoples_all;
					LogUtil.i("oldPeoples_all", oldPeoples_all.size()+"");
                    for(int i=0;i<result.size();i++){
                    	 for(int j=0;j<oldPeoples_all.size();j++){
                			if(oldPeoples_all.get(j).getId()==Long.parseLong(result.get(i).getOldPeopleIds())){
                				result.get(i).setOld_name(oldPeoples_all.get(j).getElderly_name());
                        	}
                         }
                    }
					adapter.addData(result);
					if(result.size()==1){
						currentOld=new OldPeople();
						currentOld.setId(Long.parseLong(result.get(0).getOldPeopleIds()));
						new ImportantNoticeDialog(ImportantNotifyForNotifyListActivity.this, result.get(0), currentOld);
					}
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
			try{
				if("".equals(t.getPicture())){
					holder.goneView(R.id.module_img);
				}else{
					holder.visibleView(R.id.module_img);
					holder.setImageByUrl(R.id.module_img, t.getPicture());
				}
				holder.setText(R.id.module_title, t.getTheme());
				holder.setText(R.id.module_content, t.getHeadline());
				holder.setText(R.id.moudle_oldname, t.getOld_name());
				TextView tv_moudle_oldname=holder.getView(R.id.moudle_oldname);
				tv_moudle_oldname.setTextColor(getResources().getColor(R.color.black));
			}catch(Exception e){
				
			}
		}
	}
}
