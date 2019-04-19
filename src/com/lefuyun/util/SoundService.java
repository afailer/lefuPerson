package com.lefuyun.util;

import java.io.IOException;
import java.net.URI;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;

public class SoundService extends Service{

	private MediaPlayer mediaPlayer=new MediaPlayer();
	private String path;
	private boolean isPause;//暂停状态  
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		LogUtil.e("SoundService", "serviceStart");
		if(mediaPlayer.isPlaying()){
			stop();
		}
		path=intent.getStringExtra("url");
		int msg=intent.getIntExtra("MSG", 0);
		if(msg==1){
			play(0);
		}else if(msg==2){
			stop();
		}else if(msg==3){
			pause();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	/** 
     * 停止音乐 
     */  
    private void stop(){  
        if(mediaPlayer != null) {  
            mediaPlayer.stop();  
            try {  
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    /** 
     * 播放音乐 
     * @param position 
     */  
    private void play(int position) {  
        try {  
            mediaPlayer.reset();//把各项参数恢复到初始状态  
            Uri uri=Uri.parse(path);
            mediaPlayer.setDataSource(getApplicationContext(), uri); 
            mediaPlayer.prepare();  //进行缓冲  
            mediaPlayer.start();
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    /** 
     * 暂停音乐 
     */  
    private void pause() {  
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {  
            mediaPlayer.pause();  
            isPause = true;  
        }  
    }  
    
    
    @Override  
    public void onDestroy() {  
        if(mediaPlayer != null){  
            mediaPlayer.stop();  
            mediaPlayer.release();  
        }  
    }
}
