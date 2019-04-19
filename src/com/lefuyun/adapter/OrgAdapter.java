package com.lefuyun.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.OrgActive;
import com.lefuyun.ui.OrgDetailActivity;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.MyViewPager;
import com.lefuyun.widget.circleindicator.CircleIndicator;

public class OrgAdapter extends CommonAdapter<OrgActive> {
	
	// 上下文
	private Context mContext;
	// 数据源
	private List<OrgActive> mDatas;
	
	//定义item类型的常量
	private final int ITEM_SPECIAL = 0; // 含有ViewPager的item
	private final int ITEM_NORMAL = 1; // 普通类型的item

	public OrgAdapter(Context context, List<OrgActive> datas, int layoutId) {
		super(context, datas, layoutId);
		mContext = context;
		mDatas = datas;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position == 0) {
			return ITEM_SPECIAL;
		}
		return ITEM_NORMAL;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		OrgActive orgActive = mDatas.get(position);
		//根据当前position的item类型，去加载对应的View
		switch (getItemViewType(position)) {
		case ITEM_SPECIAL:
			if(convertView==null){
				convertView = View.inflate(mContext,R.layout.org_active_title, null);
			}
			ViewHolderSpecial holderS = ViewHolderSpecial.getHolder(convertView, mContext);
			holderS.initData(orgActive);
			break;
		case ITEM_NORMAL:
			if(convertView==null){
				convertView = View.inflate(mContext, R.layout.org_active_item, null);
			}
			ViewHolderNormal holder = ViewHolderNormal.getHolder(convertView);
			ImageLoader.loadImg(orgActive.getPic(), holder.mIcon);
			holder.mTheme.setText(orgActive.getTheme());
			holder.mReserved.setText(orgActive.getReserved());
			holder.mPraiseNum.setText(orgActive.getPraise_number() + "");
			holder.mReadNum.setText(orgActive.getRead_number() + "");
			break;
		}
		return convertView;
	}
	/**
	 * 特殊条目的缓存器
	 */
	static class ViewHolderSpecial {
		
		private Context context;
		private MyViewPager mViewPager;
		private CircleIndicator mIndicator;
		private TextView mTheme,mReserved;
		private ArrayList<String> mList;
		private OrgPicturesAdapter mAdapter;
		private long mId;
		private OrgActive mOrgActive;
		
		public ViewHolderSpecial(View convertView, Context context){
			this.context = context;
			mViewPager = (MyViewPager) convertView.findViewById(R.id.orgActivePager);
			mIndicator = (CircleIndicator) convertView.findViewById(R.id.indicator);
			mTheme = (TextView) convertView.findViewById(R.id.orgActiveName);
			mReserved = (TextView) convertView.findViewById(R.id.org_active_content);
		}
		public void initData(final OrgActive orgActive) {
			mOrgActive = orgActive;
			int id = orgActive.getId();
			mTheme.setText(orgActive.getTheme());
			mReserved.setText(orgActive.getReserved());
			if(mId != id) {
				mId = id;
				TLog.log("网络获取数据");
				mList = new ArrayList<String>();
				mAdapter = new OrgPicturesAdapter(context, mList);
				mViewPager.setAdapter(mAdapter);
				mIndicator.setViewPager(mViewPager);
				initViewPagerData(orgActive);
			}else {
				TLog.log("本地获取数据");
			}
			
		}
		/**
		 * 初始化ViewPager数据
		 * @param orgActive
		 */
		private void initViewPagerData(OrgActive orgActive) {
			LefuApi.queryAgencyActivitePictures(orgActive.getId(), new RequestCallback<List<String>>() {
				
				@Override
				public void onSuccess(List<String> result) {
					TLog.log("当前列表中的所有的图片 == " + result);
					mList.clear();
					if(result != null && result.size() > 0) {
						mList.addAll(result);
						mIndicator.notifyDataSetChanged();
					}
				}
				
				@Override
				public void onFailure(ApiHttpException e) {}
			});
			
		}
		public static ViewHolderSpecial getHolder(View convertView, Context context){
			ViewHolderSpecial holder = (ViewHolderSpecial) convertView.getTag();
			if(holder==null){
				holder = new ViewHolderSpecial(convertView, context);
				convertView.setTag(holder);
			}
			return holder;
		}
		/**
		 * ViewPager适配器
		 */
		public class OrgPicturesAdapter extends PagerAdapter {
			
			private Context mContext;
			private List<String> mList;
			
			public OrgPicturesAdapter(Context context, List<String> list) {
				mContext = context;
				mList = list;
			}

			@Override
			public int getCount() {
				return mList.size();
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}
			
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ImageView imageView  = new ImageView(mContext);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				ImageLoader.loadImg(mList.get(position), imageView);
				container.addView(imageView);
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, OrgDetailActivity.class);
						intent.putExtra("OrgActive", mOrgActive);
						context.startActivity(intent);
						
					}
				});
				return imageView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}
		}
	}
	/**
	 * 普通条目的缓存器
	 */
	static class ViewHolderNormal{
		
		ImageView mIcon;
		TextView mTheme,mReserved,mPraiseNum,mReadNum;
		
		public ViewHolderNormal(View convertView){
			mIcon = (ImageView) convertView.findViewById(R.id.org_item_pic);
			mTheme = (TextView) convertView.findViewById(R.id.org_item_title);
			mReserved = (TextView) convertView.findViewById(R.id.org_item_content);
			mPraiseNum = (TextView) convertView.findViewById(R.id.click_num);
			mReadNum = (TextView) convertView.findViewById(R.id.read_time);
		}
		public static ViewHolderNormal getHolder(View convertView){
			ViewHolderNormal holder = (ViewHolderNormal) convertView.getTag();
			if(holder==null){
				holder = new ViewHolderNormal(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

	@Override
	public void convert(ViewHolder holder, OrgActive t,int position) {}
	
}
