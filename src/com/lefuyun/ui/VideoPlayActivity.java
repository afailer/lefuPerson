package com.lefuyun.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.lefuyun.R;
import com.lefuyun.R.id;
import com.lefuyun.R.layout;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.ToastUtils;

public class VideoPlayActivity extends BaseActivity {
	
	VideoView videoView;
	TextView videoPlay,videoStop;
	MediaController mediaController;
	boolean isPush=true;
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int pos = videoView.getCurrentPosition();
			if(pos>0){
				hideWaitDialog();
			}else{
				handler.sendEmptyMessageDelayed(1, 100);
			}
		};
	};
	@Override
	public void onClick(View view) {
		
	}

	@Override
	protected int getLayoutId() {
		
		return R.layout.activity_video_play;
	}

	@Override
	protected void initView() {
		//String extra = getIntent().getStringExtra("name");
		setActionBarTitle("视频");
		videoView=(VideoView) findViewById(R.id.video_view);
		/*videoPlay=(TextView) findViewById(R.id.video_play);
		videoStop=(TextView) findViewById(R.id.video_stop);*/
		String path = getIntent().getStringExtra("path");
		//mediaController=new MediaController(this);
		videoView.setMediaController(new MediaController(this));
		Uri uri=Uri.parse(path);//LefuApi.IMG_URL+path
		videoView.setVideoURI(uri);
		videoView.requestFocus();
		videoView.start();
		showWaitDialog("玩命加载中...");
		handler.sendEmptyMessage(1);
		videoView.getBufferPercentage();
	}

	@Override
	protected void initData() {
		
	}
}
