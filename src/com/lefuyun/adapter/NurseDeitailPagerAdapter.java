package com.lefuyun.adapter;

import java.util.ArrayList;
import java.util.List;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.MediaBean;
import com.lefuyun.bean.NurseRecord;
import com.lefuyun.util.Utils;

public class NurseDeitailPagerAdapter extends PagerAdapter{
	
	//String[] medias;
	List<MediaBean> medias=new ArrayList<MediaBean>();
	String reserved;
	BaseActivity mContext;
	NurseRecord record;
	SeekBar seekBar=null;
	boolean playing=true;
	int currentPos=0;
	MediaPlayer mediaPlayer;
	public NurseDeitailPagerAdapter(BaseActivity context,NurseRecord nurseRecord){
		medias.clear();
		this.medias.addAll(Utils.getUrlList(nurseRecord.getMedia(), Utils.picType));//getUrls(nurseRecord.getMedia(),Utils.picType);
		this.medias.addAll(Utils.getUrlList(nurseRecord.getMedia(), Utils.videoType));
		this.medias.addAll(Utils.getUrlList(nurseRecord.getMedia(), Utils.audioType));
		this.reserved=nurseRecord.getReserved();
		this.record=nurseRecord;
		this.mContext=context;
		if(AppContext.mediaPlayer!=null){
			mediaPlayer=AppContext.mediaPlayer;
		}else{
			mediaPlayer=new MediaPlayer();
		}
		mediaPlayer.reset();//把各项参数恢复到初始状态 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return medias.size();
	}
	public void stopMedia(){
		try{
			if(mediaPlayer!=null){
				mediaPlayer.stop();
			}
		}catch(Exception e){
			
		}
	}
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view==obj;
	}
	ImageView imgDetail,media;
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = View.inflate(mContext, R.layout.pic_detail_item, null);
	    imgDetail = (ImageView) view.findViewById(R.id.pic_detail);
		media=(ImageView) view.findViewById(R.id.media_img);
		TextView mediaType=(TextView) view.findViewById(R.id.media_type);
		seekBar=(SeekBar) view.findViewById(R.id.audio_seekbar);
		Utils.setImageByMediaBean(imgDetail,media,mediaType,seekBar,medias.get(position), mContext);
		if(medias.get(position).getMediaType()==3){
			initAudio(medias.get(position));
		}
		container.addView(view);
		return view;
	}
	private void initAudio(final MediaBean mediaBean){
		media.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				play(Uri.parse(LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath())), 0);
			}
		});
		/*mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer arg0, int percent) {
				if(percent>0){
					//mContext.hideWaitDialog();
				}
			}
		});*/
		//seekBar.setOnSeekBarChangeListener(new )
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				media.setImageResource(R.drawable.media_audio);
			}else if(msg.what==2){
				if(mediaPlayer.getCurrentPosition()>0){
					
				}
			}
			
		};
	};
	private void play(Uri uri,int pos){
		if(mediaPlayer.isPlaying()){
			seekBar.setVisibility(View.VISIBLE);
			mediaPlayer.pause();
			media.setImageResource(R.drawable.media_audio);
		}else{
			media.setImageResource(R.drawable.audio_stop);
			seekBar.setVisibility(View.VISIBLE);
				playing=true;
				
				try {
					mediaPlayer.setDataSource(mContext,uri);
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.prepare();  //进行缓冲  
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				 mediaPlayer.start();
	        new Thread(new Runnable() {
				@Override
				public void run() {
					while(playing){
						if(mediaPlayer.isPlaying()){
							handler.sendEmptyMessage(2);
							seekBar.setMax(mediaPlayer.getDuration());
							seekBar.setProgress(mediaPlayer.getCurrentPosition());
						}else{
							Looper.prepare();
							handler.sendEmptyMessage(1);
							playing=false;
						}
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
		
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		
	}
}
