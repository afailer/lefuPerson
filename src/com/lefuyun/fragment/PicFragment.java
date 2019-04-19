package com.lefuyun.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.NurseMedia;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.RefreshLayout.OnLoadListener;
import com.lefuyun.widget.StickyGridHeaders.gridview.StickyGridHeadersGridView;
import com.lefuyun.widget.StickyGridHeaders.gridview.StickyGridLoadMoreListener;
import com.lefuyun.widget.StickyGridHeaders.header.GridItem;
import com.lefuyun.widget.StickyGridHeaders.header.StickyGridAdapter;
import com.lefuyun.widget.StickyGridHeaders.header.YMComparator;

@SuppressLint("ValidFragment")
public class PicFragment extends Fragment implements OnLoadListener, OnRefreshListener{
	
	private StickyGridHeadersGridView mGridView;
	RefreshLayout sticky_container;
	int pageNo=1;
	private  int section = 1;
	private Map<String, Integer> sectionMap = new HashMap<String, Integer>();
	BaseActivity activity;
	StickyGridAdapter stickyGridAdapter=null;
	int lastType=1;
	OldPeople old;
	private int mediaType=Utils.picType;
	View mediaContainer=null;
	int pathType;
	@SuppressLint("ValidFragment")
	public static PicFragment getInstance(OldPeople oldPeople,int mediaType,int type){
		return new PicFragment(oldPeople,mediaType,type);
	}
	private PicFragment(OldPeople oldPeople,int mediaType,int type){
		this.old=oldPeople;
		this.mediaType=mediaType;
		this.pathType=type;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		pageNo=1;
		if(mediaContainer==null){
			mediaContainer =  inflater.inflate(R.layout.pic_layout, null);
		}else{
			ViewParent parent = container.getParent();
			if(parent instanceof ViewGroup){
				ViewGroup group = (ViewGroup) parent;
				group.removeView(mediaContainer);
			}
		}
		sticky_container=(RefreshLayout)mediaContainer.findViewById(R.id.sticky_container);
		mGridView=(StickyGridHeadersGridView)mediaContainer.findViewById(R.id.stickygrid);
		Utils.fixSwiplayout(sticky_container);
		sticky_container.setOnRefreshListener(this);
		sticky_container.setOnLoadListener(this);
		loadMedia(old.getId(), mediaType);
		mGridView.setStickyGridLoadMoreListener(new StickyGridLoadMoreListener() {
			
			@Override
			public void loadMore() {
				loadMedia(old.getId(), mediaType);
			}
		});
		return mediaContainer;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity=(BaseActivity)activity;
	}
	private void loadMedia(long old_people_id,final int type){
		if(pathType==Utils.typeNurse){
			LefuApi.queryMediaByOldPeopleId(old_people_id, type, pageNo, new RequestCallback<List<NurseMedia>>() {
				
				@Override
				public void onSuccess(List<NurseMedia> result) {
					Utils.finishLoad(sticky_container);
					try{
						pageNo++;
						setGridview(result,type);
					}catch(Exception e){}
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}else if(pathType==Utils.typeDaily){
			LefuApi.querydailyLifeMediaByOldPeopleId(old_people_id, type, pageNo, new RequestCallback<List<NurseMedia>>() {
				
				@Override
				public void onSuccess(List<NurseMedia> result) {
					Utils.finishLoad(sticky_container);
					try{
						pageNo++;
						setGridview(result,type);
					}catch(Exception e){}
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
	}

	private void setGridview(List<NurseMedia> pathes,int type){
		List<GridItem> mGirdList = new ArrayList<GridItem>();
		for(NurseMedia media:pathes){
			String[] urlList = null;
			urlList = Utils.getUrlList(media.getMedia());
			for (int i=0;i<urlList.length;i++) {
				long times=media.getNursing_dt();
				GridItem mGridItem = new GridItem(urlList[i], paserTimeToYM(times),media);
				mGirdList.add(mGridItem);
			}
		}
		//Collections.sort(mGirdList, new YMComparator());
		
		for(ListIterator<GridItem> it = mGirdList.listIterator(); it.hasNext();){
			GridItem mGridItem = it.next();
			String ym = mGridItem.getTime();
			if(!sectionMap.containsKey(ym)){
				mGridItem.setSection(section);
				sectionMap.put(ym, section);
				section ++;
			}else{
				mGridItem.setSection(sectionMap.get(ym));
			}
		}
		LogUtil.e("data", type+" "+lastType+"");
		if(stickyGridAdapter==null){
			stickyGridAdapter=new StickyGridAdapter(activity, mGirdList, mGridView,type);
			mGridView.setAdapter(stickyGridAdapter);
		}else if(type!=lastType){
			stickyGridAdapter.clearData();
			stickyGridAdapter=new StickyGridAdapter(activity, mGirdList, mGridView,type);
			mGridView.setAdapter(stickyGridAdapter);
			stickyGridAdapter.notifyDataSetChanged();
		}else{
			stickyGridAdapter.addData(mGirdList);
		}
		lastType=type;
		
	}
	public static String paserTimeToYM(long time) {
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		return format.format(new Date(time));
	}
	@Override
	public void onLoad() {
		
	}

	@Override
	public void onRefresh() {
		stickyGridAdapter.clearData();
		pageNo=1;
		loadMedia(old.getId(), mediaType);
	};
}
