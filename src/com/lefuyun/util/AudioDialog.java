package com.lefuyun.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;

public class AudioDialog extends LefuDialog<String,String>{

	MediaPlayer mediaPlayer;
	boolean playing=true;
	ImageView tongle;
	SeekBar seekBar;
	
	public AudioDialog(BaseActivity activity, String t, String title) {
		super(activity, t,title);
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.audio_dialog;
	}

	@Override
	public int getCloseId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void doView(View view, final String t,String title) {
		// TODO Auto-generated method stub
		tongle=(ImageView) view.findViewById(R.id.audio_controller);
		seekBar=(SeekBar) view.findViewById(R.id.audio_seekbar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser==true){
					play(Uri.parse(t), progress);
					
				}
			}
		});
		tongle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				play(Uri.parse(t),0);
			}
		});
		
		if(AppContext.mediaPlayer!=null){
			mediaPlayer=AppContext.mediaPlayer;
		}else{
			mediaPlayer=new MediaPlayer();
		}
		mediaPlayer.reset();//把各项参数恢复到初始状态  
		play(Uri.parse(t),0);
		
	}
	int currentPos=0;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			tongle.setImageResource(R.drawable.media_audio);
		};
	};
	private void play(Uri uri,int pos){
		if(mediaPlayer.isPlaying()){
			tongle.setImageResource(R.drawable.media_audio);
			currentPos=mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
		}else{
			tongle.setImageResource(R.drawable.audio_stop);
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
							
							seekBar.setMax(mediaPlayer.getDuration());
							//time.setText(mediaPlayer.getCurrentPosition()+"");
							seekBar.setProgress(mediaPlayer.getCurrentPosition());
						}else{
							playing=false;
							Looper.prepare();
							handler.sendEmptyMessage(1);
						}
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}).start();
		}
		
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0.2;
	}

	@Override
	public void onDialogDismiss() {
		// TODO Auto-generated method stub
		mediaPlayer.stop();
		//mediaPlayer.release();
	}
	
}
