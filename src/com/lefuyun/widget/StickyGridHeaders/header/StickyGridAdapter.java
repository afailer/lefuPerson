package com.lefuyun.widget.StickyGridHeaders.header;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.ui.PhotoViewActivity;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.StickyGridHeaders.gridview.StickyGridHeadersSimpleAdapter;
import com.lefuyun.widget.StickyGridHeaders.header.MyImageView.OnMeasureListener;

public class StickyGridAdapter extends BaseAdapter implements
		StickyGridHeadersSimpleAdapter {

	private List<GridItem> list;
	private LayoutInflater mInflater;
	//private GridView mGridView;
	private Point mPoint = new Point(0, 0);
	private int mMediaType;
	private BaseActivity context;
	MediaPlayer mediaPlayer=new MediaPlayer();
	String audioPath;
	public StickyGridAdapter(BaseActivity context, List<GridItem> list,
			GridView mGridView,int mediaType) {
		this.mMediaType=mediaType;
		this.context=context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
		//this.mGridView = mGridView;
	}
	public void addData(List<GridItem> list){
		this.list.addAll(list);
		this.notifyDataSetChanged();
	}
	public void clearData(){
		this.list.clear();
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_item, parent, false);
			mViewHolder.mImageView = (MyImageView) convertView
					.findViewById(R.id.grid_item);
			convertView.setTag(mViewHolder);
			
			mViewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {  
                  
                @Override  
                public void onMeasureSize(int width, int height) {  
                    mPoint.set(width, height);  
                }  
            }); 
			
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		final String path = list.get(position).getPath();
		if(mMediaType==Utils.picType){
			ImageLoader.loadImg(path,mViewHolder.mImageView );
			mViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent(context, PhotoViewActivity.class);//VideoPlayActivity.class);
					intent.putExtra("path",path);
					context.startActivity(intent);
				}
			});
		}else if(mMediaType==Utils.videoType){
			mViewHolder.mImageView.setImageResource(R.drawable.video_player);
			mViewHolder.mImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Utils.startVideo(LefuApi.getAbsoluteApiUrl(path));
				}
			});
		}else if(mMediaType==Utils.audioType){
			mViewHolder.mImageView.setImageResource(R.drawable.audio_player);
			mViewHolder.mImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Utils.startAudio(context, LefuApi.getAbsoluteApiUrl(path),"");
				}
			});
			
		}
		return convertView;
	}
	

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder mHeaderHolder;
		if (convertView == null) {
			mHeaderHolder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.header, parent, false);
			mHeaderHolder.mTextView = (TextView) convertView
					.findViewById(R.id.header);
			convertView.setTag(mHeaderHolder);
		} else {
			mHeaderHolder = (HeaderViewHolder) convertView.getTag();
		}

		mHeaderHolder.mTextView.setText(list.get(position).getTime());

		return convertView;
	}

	public static class ViewHolder {
		public MyImageView mImageView;
	}

	public static class HeaderViewHolder {
		public TextView mTextView;
	}

	@Override
	public long getHeaderId(int position) {
		return list.get(position).getSection();
	}

}
