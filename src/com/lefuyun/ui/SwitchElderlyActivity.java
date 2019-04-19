package com.lefuyun.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.adapter.ConcernElderActivityAdapter;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.ConfigForSpecialOldBean;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.ConfigForSpeialOldUtils;
import com.lefuyun.util.JsonUtil;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.util.WindowUtil;
import com.lefuyun.zxing.CaptureActivity;

/**
 * @author chenshichao
 * @date   2016-5-14
 * @description  健康状况切换老人
 */
public class SwitchElderlyActivity extends BaseActivity {
	
	private GridView mGridView;
	private PopupWindow mPopupWindow;
	private ConcernElderActivityAdapter mAdapter;
	// actionBar对象
	private RelativeLayout mActionBar;
	// 是否是初始化数据
	private boolean isInitData;
	// 保存绑定老人对象
	private List<OldPeople> mList;
	// 添加新的老人点击按钮
	private TextView mAddBtn;
	// 判断页面是否从我的页面跳转过来
	private boolean isAnimator;
	// 当前用户ID
	private long mUserId;
	// 是否要进行刷新
	private boolean isRefresh;
	@Override
	protected int getLayoutId() {
		return R.layout.activity_concern_elder;
	}
	
	@Override
	protected void initView() {
		setActionBarTitle("切换关注人");
		mGridView = (GridView) findViewById(R.id.gv_concern_elder_activity);
		mActionBar = (RelativeLayout) findViewById(R.id.item_action_bar);
		mAddBtn = (TextView) findViewById(R.id.btn_elder_activity);
		mAddBtn.setVisibility(View.GONE);
		mAddBtn.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		Intent intent = getIntent();
		String oldjson=(String) SPUtils.get(this, SPUtils.OLDPEOPLE_JSON, "");
		 Type type=new TypeToken<List<OldPeople>>(){}.getType();
	  	mList=(List<OldPeople>) JsonUtil.jsonToList(oldjson, type);
		if(mList!=null){
			mAdapter = new ConcernElderActivityAdapter(
					SwitchElderlyActivity.this, mList, 
					R.layout.item_activity_concern_elder);
			mGridView.setAdapter(mAdapter);
			mGridView.setOnItemClickListener(new MyOnItemClickListener());
		}
	}
	@Override
	public void onClick(View v) {
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
	 * GridView条目点击事件
	 */
	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			OldPeople oldPeople = mList.get(position);
			Intent data = new Intent();
			data.putExtra("oldPeople", oldPeople);
			setResult(200, data);
			finish();
		}
		
	}
}
