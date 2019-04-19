package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonPagerAdapter;
import com.lefuyun.bean.ImportantMsg;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.User;
import com.lefuyun.util.ImportantNoticeDialog;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.widget.CircleWaveView;
import com.lefuyun.widget.viewpager.VerticalViewPager;

public class ImportantNoticeActivity extends BaseActivity implements OnPageChangeListener {
	
	CircleWaveView circleWaveView; //雷达控件
	RelativeLayout refreshParent,noticeAll;
	ImageView refresh_btn;
	OldPeople currentOld;
	User user;
	int middle,circleWaveRadius;
	TextView oldName,oldLoc,oldAge,important_notice_num;
	VerticalViewPager importantPager;
	NoticePagerAdapter noticePagerAdapter;
	int pos=2;
	TextView important_nodata;
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.refresh_btn:
			//stopRefresh();
			break;
		case R.id.notice_all:
			if(nodata){
				Intent intent = new Intent(getApplicationContext(), ImportantUnreceivedListActivity.class);
				intent.putExtra("old", currentOld);
				intent.putExtra("user", user);
				startActivity(intent);
			}else{
				ToastUtils.show(getApplicationContext(), "暂无重要通知");
			}
			
			break;
		default:
			break;
		}
	}
	boolean isAhead=true;
	Handler pagerHandler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		LogUtil.e("pos", ""+pos);
    		pagerHandler.sendEmptyMessageDelayed(1, 1500);
    		if(noticePagerAdapter.getCount()>1){
                if( pos == noticePagerAdapter.getCount()) { //末位之后，跳转到首位（1）  
                	isAhead=false;
                	//importantPager.setCurrentItem(pos,false); //false:不显示跳转过程的动画  
                }else if(pos==0){
                	isAhead=true;
                }
                if(isAhead){
        			pos++;
        		}else{
        			pos--;
        		}
        		importantPager.setCurrentItem(pos, true);
    		}
    		
    	};
    };
	@Override
	protected int getLayoutId() {
		return R.layout.activity_important_notice;  //布局id
	}
	int pointNum=0;
	/**
	 * 接到重要通知后不断向视图中加通知点
	 */
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==5){
				refresh_btn.setBackgroundResource(R.drawable.important_notice_refresh);
				refresh_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						refresh_btn.setBackgroundResource(R.drawable.important_notice_normal);
						for(int i=0;i<viewList.size();i++){
							refreshParent.removeView(viewList.get(i));
						}
						viewList.clear();
						loadData();
					}
				});
			}else{
				addPoint((ImportantMsg)msg.obj);
				pointNum++;
				if(pointNum==importantMsg.size()){
					stopCircle();
				}
			}
			
		};
	};
	boolean nodata=false;
	TextView notice_detail;
	List<ImportantMsg> importantMsg=new ArrayList<ImportantMsg>();
	LinearLayout old_container;
	@Override
	protected void initView() {
		setActionBarTitle("重要通知");
		WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
		middle = wm.getDefaultDisplay().getWidth()/2;
		int marginBottom=(int) (wm.getDefaultDisplay().getHeight()/2.9);
		currentOld=(OldPeople) getIntent().getSerializableExtra("old");
		user= (User) getIntent().getSerializableExtra("user");
		circleWaveView=(CircleWaveView) findViewById(R.id.circle);
		notice_detail=(TextView) findViewById(R.id.notice_detail);
		refreshParent=(RelativeLayout) findViewById(R.id.refresh_parent);
		circleWaveRadius=refreshParent.getWidth()/2;
		refresh_btn=(ImageView) findViewById(R.id.refresh_btn);
		noticeAll=(RelativeLayout) findViewById(R.id.notice_all);
		oldName=(TextView) findViewById(R.id.important_old_name);
		oldAge=(TextView) findViewById(R.id.important_old_age);
		oldLoc=(TextView) findViewById(R.id.important_old_loc);
		oldName.setText(currentOld.getElderly_name());
		oldLoc.setText(currentOld.getAgency_name());
		oldAge.setText("年龄："+currentOld.getAge()+"岁");
		importantPager=(VerticalViewPager) findViewById(R.id.importantPager);
		importantPager.setCurrentItem(1);
		importantPager.setOnPageChangeListener(this);
		noticeAll.setOnClickListener(this);
		refresh_btn.setOnClickListener(this);
		important_nodata=(TextView) findViewById(R.id.important_nodata);
		old_container=(LinearLayout) findViewById(R.id.old_container);
		android.widget.RelativeLayout.LayoutParams params=new android.widget.RelativeLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.setMargins(0, 0, 0, marginBottom);
		old_container.setLayoutParams(params);
		//startRefresh();
		loadData();
	}
	private void loadData(){
		/**
		 * 获取未读的重要通知
		 */
		LefuApi.queryUnreceivedNoticeList(user.getUser_id(), currentOld.getId(), 0, new RequestCallback<List<ImportantMsg>>() {

			@Override
			public void onSuccess(List<ImportantMsg> result) {
				if(result==null){
					return;
				}
				if(result.size()==0||(result.size()==1&&result.get(0)==null)){
					
					important_nodata.setText("暂无通知");
					notice_detail.setText("暂无通知");
					nodata=false;
					stopCircle();
					ToastUtils.show(getApplicationContext(), "暂无通知"); 
				}else{
					notice_detail.setText("通知详情");
					important_nodata.setText("");
					nodata=true;
					importantMsg.clear();
					importantMsg.addAll(result);
					int time=1000;
					pointNum=0;
					for(int i=0;i<result.size();i++){
						time+=500;
						Message msg=Message.obtain();
						msg.obj=result.get(i);
						msg.what=1;
						handler.sendMessageDelayed(msg, time);//通知添加点
					}
					handler.sendEmptyMessageDelayed(5, time);
					if(noticePagerAdapter!=null){
						noticePagerAdapter.deleteData();
					}
					noticePagerAdapter = new NoticePagerAdapter(getApplicationContext(), result, R.layout.important_pager_item);
					importantPager.setAdapter(noticePagerAdapter);
					pagerHandler.sendEmptyMessageDelayed(1, 1000);
				}
				
			}

			@Override
			public void onFailure(ApiHttpException e) {
				// TODO Auto-generated method stub
				//stopRefresh();
			}
		});
	}
	List<View> viewList=new ArrayList<View>();
	private void addPoint(final ImportantMsg msg){
		final ImageView point=new ImageView(getApplicationContext());
		point.setImageResource(R.drawable.data_point);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//与父容器的左侧对齐
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);//与父容器的上侧对齐
				lp.leftMargin=(middle/(1+(int)(Math.random()*5)))+middle/4;
				lp.topMargin=(middle/(1+(int)(Math.random()*5)))+middle/4 ;
		point.setLayoutParams(lp);
		point.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new ImportantNoticeDialog(ImportantNoticeActivity.this, msg, currentOld);
				/*// TODO Auto-generated method stub
				Intent intent=new Intent(getApplicationContext(), NurseH5DetailActivity.class);
				intent.putExtra("importantMsg", msg);
				intent.putExtra("old", currentOld);
				intent.putExtra("pageType", 2);
				//refreshParent.removeView(point);
				startActivity(intent);*/
			}
		});
		viewList.add(point);
		refreshParent.addView(point);
	}
	@Override
	protected void initData() {
		
	}
	
	
	@Override
	protected int getActionBarRightImage() {
		// TODO Auto-generated method stub
		return R.drawable.his_important;
	}
	@Override
	protected boolean hasRightImageView() {
		return true;
	}
	@Override
	public void onRightViewClick(View view) {
		Intent intent=new Intent(getApplicationContext(), ImportantHistoryNoticeActivity.class);
		intent.putExtra("old",currentOld);
		intent.putExtra("user", user);
		startActivity(intent);
	}
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	
	public void stopCircle(){
		circleWaveView.stop();
	}
	@Override
	protected boolean hasBackButton() {
		// TODO Auto-generated method stub
		return true;
	}
	/*private int getColor(int position){
		int i=position%4;
		if(i==1){
			return Color.parseColor("#ff0000");
		}else if(i==2){
			return Color.parseColor("#00ff00");
		}else if(i==3){
			return Color.parseColor("#0000ff");
		}else{
			return Color.parseColor("#333333");
		}
	}
	int ts=1;*/
	class NoticePagerAdapter extends CommonPagerAdapter<ImportantMsg>{

		public NoticePagerAdapter(Context context,
				List<ImportantMsg> datas, int layoutId) {
			super(context, datas, layoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void processItem(View view, final ImportantMsg t,int position) {
			// TODO Auto-generated method stub
			TextView item=(TextView) view.findViewById(R.id.impotant_item);
			item.setText(t.getTheme());
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					new ImportantNoticeDialog(ImportantNoticeActivity.this, t, currentOld);
				}
			});
		}
		
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		LogUtil.e(position+"", position+"");
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
			pagerHandler.removeCallbacksAndMessages(null);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(noticePagerAdapter!=null){
			pagerHandler.sendEmptyMessage(1);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}
