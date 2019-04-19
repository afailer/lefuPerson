package com.lefuyun.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.UserInfo;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.MediaBean;
import com.lefuyun.bean.NurseMedia;
import com.lefuyun.ui.PhotoViewActivity;
import com.lefuyun.ui.VideoPlayActivity;
import com.lefuyun.widget.RefreshLayout;
import com.lefuyun.widget.WaitDialog;

@SuppressLint("UseSparseArrays")
public class Utils {
	
	public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
  	  BitmapFactory.Options newOpts = new BitmapFactory.Options();
  	  // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
  	  newOpts.inJustDecodeBounds = true;
  	  newOpts.inPreferredConfig = Config.RGB_565;
  	  // Get bitmap info, but notice that bitmap is null now
  	  Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
  	  newOpts.inJustDecodeBounds = false;
  	  int w = newOpts.outWidth;
  	  int h = newOpts.outHeight;
  	  // 想要缩放的目标尺寸
  	  float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
  	  float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
  	  // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
  	  int be = 1;// be=1表示不缩放
  	  if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
  	   be = (int) (w / ww)+2;
  	  } else if (h > w && h > hh) {// 如果高度高的话根据宽度固定大小缩放
  	   be = (int) (h / hh)+2;
  	  }
  	  if (be <= 0)
  	   be = 1;
  	  newOpts.inSampleSize = be;// 设置缩放比例
  	  // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
  	  bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
  	  // 压缩好比例大小后再进行质量压缩
  	  // return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
  	  return bitmap;
  	 }
	public static String cutPage(View v,Context mContext){
		View rootView = v.getRootView();
		rootView.setDrawingCacheEnabled(true);
		rootView.buildDrawingCache();
		Bitmap bitmap = rootView.getDrawingCache();
		String imageUrl = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "lefu"+System.currentTimeMillis(), System.currentTimeMillis()+"");
		return imageUrl;
	}
	public static int typeNurse=1,typeDaily=2;
	public static int picType=1,audioType=3,videoType=2;
	public static String[] getUrlList(String media){
		 String[] split = media.split(",");
		return split;
	}
	public static List<String> getUrlList(List<NurseMedia> medias){
		List<String> listUrl=new ArrayList<String>();
		for(int i=0;i<medias.size();i++){
			String[] list = getUrlList(medias.get(i).getMedia());
			for(int j=0;j<list.length;j++){
				listUrl.add(list[j]);
			}
		}
		return listUrl;
	}
	public static List<MediaBean> getUrlList(String medias,int mediaType){
		String media=getMediaStrByType(medias, mediaType);
		String picMedia ="";
		try{
			switch (mediaType) {
			case 1://图片类型
				if(media.contains("P:")&&media.contains("|")){
					System.out.println(media.indexOf("|"));
					picMedia= media.substring(media.indexOf("P:")+2,media.indexOf("|"));
				}else if(media.contains("P:")){
					picMedia= media.substring(media.indexOf("P:")+2,media.length());
				}
				break;
			case 2://音频
				if(media.contains("V:")&&media.contains("|")){
					picMedia= media.substring(media.indexOf("V:")+2,media.indexOf("|"));
				}else if(media.contains("V:")){
					picMedia= media.substring(media.indexOf("V:")+2,media.length());
				}
				break;
			case 3://视频
				if(media.contains("A:")&&media.contains("|")){
					picMedia= media.substring(media.indexOf("A:")+2,media.indexOf("|"));
				}else if(media.contains("A:")){
					picMedia= media.substring(media.indexOf("A:")+2,media.length());
				}
				break;
			default:
				break;
			}
		}catch(Exception e){
			
		}
		String[] split;
		if("".equals(picMedia)){
			split=new String[0];
		}else{
			split=picMedia.split(",");
		}
		List<MediaBean> mediaBeans=new ArrayList<MediaBean>();
		for(int i=0;i<split.length;i++){
			mediaBeans.add(new MediaBean(mediaType, split[i]));
		}
		return mediaBeans;
	}
	public static void setImageByMediaBean(ViewHolder holder,MediaBean mediaBean,int viewId){
		
		switch (mediaBean.getMediaType()) {
		case 1://picType
			holder.setImageByUrl(viewId,mediaBean.getMediaPath());
			//holder.setCircleImageByResource(imgRes, R.drawable.video_player);
			break;
		case 2://videoType
			holder.setCircleImageByResource(viewId, R.drawable.img_video);
			break;
		case 3://audioType
			holder.setCircleImageByResource(viewId, R.drawable.img_audio);
			break;
		default:
			break;
		}
	}
	public static void setImageByMediaBean(ViewHolder holder,MediaBean mediaBean,int viewId,String history){
		
		switch (mediaBean.getMediaType()) {
		case 1://picType
			holder.setImageByUrl(viewId,mediaBean.getMediaPath());
			//holder.setCircleImageByResource(imgRes, R.drawable.video_player);
			break;
		case 2://videoType
			holder.setImageResource(viewId, R.drawable.img_video3);
			break;
		case 3://audioType
			holder.setImageResource(viewId, R.drawable.img_audio3);
			break;
		default:
			break;
		}
	}
	public static void setImageByMediaBean(ImageView img,ImageView media,TextView mediaType,SeekBar seekBar,final MediaBean mediaBean,final BaseActivity mContext){
		switch (mediaBean.getMediaType()) {
		case 1:
			ImageLoader.loadImg(mediaBean.getMediaPath(), img);
			media.setVisibility(View.GONE);
			mediaType.setVisibility(View.GONE);
			seekBar.setVisibility(View.GONE);
			img.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent(mContext, PhotoViewActivity.class);
					intent.putExtra("path", mediaBean.getMediaPath());
					mContext.startActivity(intent);
				}
			});
			break;
		case 2://视频
			img.setImageResource(R.drawable.media_bg);
			media.setImageResource(R.drawable.media_video);
			mediaType.setText("视频");
			seekBar.setVisibility(View.GONE);
			img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					startVideo(LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath()));
					/*Uri uri = Uri.parse(LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath()));     
					//调用系统自带的播放器    
					Intent intent = new Intent(Intent.ACTION_VIEW);    
					intent.setData(uri);
					mContext.startActivity(intent);*/
				}
			});
			break;
		case 3://音频
			img.setImageResource(R.drawable.media_bg);
			media.setImageResource(R.drawable.media_audio);
			mediaType.setText("音频");
			seekBar.setVisibility(View.VISIBLE);
			/*img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(mContext, AudioService.class);
					intent.putExtra("type", ConstantUtil.audioStart);
					intent.putExtra("url",LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath()));
					mContext.startService(intent);
					
					startAudio(mContext,LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath()),"");
					
					Uri uri = Uri.parse(LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath()));
					Intent m_intent = new Intent(Intent.ACTION_VIEW,uri);
					m_intent.setDataAndType(uri, "audio/mp3");
				    mContext.startActivity(m_intent);
					
					
					Uri uri = Uri.parse(LefuApi.getAbsoluteApiUrl(mediaBean.getMediaPath()));     
					//调用系统自带的播放器    
					Intent intent = new Intent(Intent.ACTION_VIEW);    
					intent.setData(uri);
					mContext.startActivity(intent);
				}
			});*/
			break;
		default:
			break;
		}
	}
	public static void startVideo(String path){
		Intent intent=new Intent(AppContext.sCotext, VideoPlayActivity.class);
		intent.putExtra("path", path);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    AppContext.sCotext.startActivity(intent);
	    
	    /*Uri uri = Uri.parse(LefuApi.getAbsoluteApiUrl(path));     
		//调用系统自带的播放器    
		Intent intent = new Intent(Intent.ACTION_VIEW);    
		intent.setData(uri);
		context.startActivity(intent);   
		Intent intent=new Intent(context, VideoPlayActivity.class);
		intent.putExtra("path", path);
		context.startActivity(intent);*/
	}
	public static void startAudio(BaseActivity activity,String url,String title){
		
		
		
		new AudioDialog(activity, url, title);
		/*if(audioPath==path&&mediaPlayer.isPlaying()){
				mediaPlayer.stop();
				return;
			}
			ToastUtils.show(context, "再次点击停止播放");
			mediaPlayer.reset();//把各项参数恢复到初始状态  
		    Uri uri=Uri.parse(LefuApi.IMG_URL+path);
		    try {
		    	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDataSource(context,uri);
				mediaPlayer.prepare();  //进行缓冲  
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		    mediaPlayer.start();
		    audioPath=path;
		    
		    Uri uri = Uri.parse(LefuApi.getAbsoluteApiUrl(path));
			Intent m_intent = new Intent(Intent.ACTION_VIEW,uri);
			m_intent.setDataAndType(uri, "audio/mp3");
		    context.startActivity(m_intent);
		    */
	}
	public static String[] getUrl(String medias,int mediaType){
		String media=getMediaStrByType(medias, mediaType);
		String picMedia ="";
		try{
			switch (mediaType) {
			case 1://图片类型
				if(media.contains("P:")&&media.contains("|")){
					System.out.println(media.indexOf("|"));
					picMedia= media.substring(media.indexOf("P:")+2,media.indexOf("|"));
				}else if(media.contains("P:")){
					picMedia= media.substring(media.indexOf("P:")+2,media.length());
				}
				break;
			case 3://音频
				if(media.contains("V:")&&media.contains("|")){
					picMedia= media.substring(media.indexOf("V:")+2,media.indexOf("|"));
				}else if(media.contains("V:")){
					picMedia= media.substring(media.indexOf("V:")+2,media.length());
				}
				break;
			case 2://视频
				if(media.contains("A:")&&media.contains("|")){
					picMedia= media.substring(media.indexOf("A:")+2,media.indexOf("|"));
				}else if(media.contains("A:")){
					picMedia= media.substring(media.indexOf("A:")+2,media.length());
				}
				break;
			default:
				break;
			}
		}catch(Exception e){
			
		}
		String[] split;
		if("".equals(picMedia)){
			split=new String[0];
		}else{
			split=picMedia.split(",");
		}
		return split;
	}
	public static String getMediaStrByType(String media,int mediaType){
		if(media==null){
			return "";
		}
		String[] medias = media.split("\\|");
		for(int i=0;i<medias.length;i++){
			if(medias[i].contains("P:")){
				if(mediaType==picType){
					return medias[i];
				}
			}
			if(medias[i].contains("A:")){
				if(mediaType==audioType){
					return medias[i];
				}
			}
			if(medias[i].contains("V:")){
				if(mediaType==videoType){
					return medias[i];
				}
			}
		}
		return "";
	}
	public static String getTimeStr(String timeStr){
		String year = timeStr.substring(0, 4);
		String month = timeStr.substring(5, timeStr.length());
		return year+"年"+month+"月";
	}
	public static void fixSwiplayout(final RefreshLayout mSwiplayout){
		mSwiplayout.post(new Thread(new Runnable() {
			@Override
			public void run() {
				mSwiplayout.setRefreshing(false);
			}
		}));
		mSwiplayout.setColorSchemeResources(R.color.main_background,
				R.color.main_gray,
				R.color.main_background);
	}
	public static void finishLoad(final RefreshLayout mSwiplayout){
		try{
			mSwiplayout.setLoading(false);
		}catch(Exception e){
		}
		try{
			mSwiplayout.setRefreshing(false);
		}catch(Exception e){
			
		}
	}
	public static Map<String, String> getWebViewHeaderMap(WebView mWebView){
		WebSettings settings = mWebView.getSettings();
		String userAgentString = settings.getUserAgentString();
		if(!userAgentString.contains("lefuAppP")){
			userAgentString+=UserInfo.getUserAgent();
		}
		settings.setUserAgentString(userAgentString);
		Map<String, String> map = new HashMap<String, String>();
		map.put("User-Agent", mWebView.getSettings().getUserAgentString());
		return map;
	}
	public static int getIntDrawable(int position){
		switch (position) {
		case 0:
			return R.drawable.shape_main_color_big_circular;
		case 1:
			return R.drawable.shape_color1_color_big_circular;
		case 2:
			return R.drawable.shape_color2_color_big_circular;
		case 3:
			return R.drawable.shape_color3_color_big_circular;
		case 4:
			return R.drawable.shape_color5_color_big_circular;
		case 5:
			return R.drawable.shape_color6_color_big_circular;
		default:
			return R.drawable.shape_color4_color_big_circular;
		}
	}
	private static WaitDialog mWaitDialog;
	public static WaitDialog showWaitDialog(Activity activity,String message) {
		    try {
		    	if (mWaitDialog == null) {
					mWaitDialog = DialogHelper.getWaitDialog(activity, message);
				}
				if (mWaitDialog != null) {
					mWaitDialog.setMessage(message);
					mWaitDialog.show();
				}
		     } catch (Exception e) {
		
		    }
			return mWaitDialog;
	}
	public static void hideWaitDialog() {
		if (mWaitDialog != null) {
			try {
				mWaitDialog.dismiss();
				mWaitDialog = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * 设置通知栏颜色
	 * @param activity
	 * @param i
	 */
	public static void systemBarColor(Activity activity,int i) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = activity.getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			if (true) {
				winParams.flags |= bits;
			} else {
				winParams.flags &= ~bits;
			}
			win.setAttributes(winParams);
		}
		// 创建状态栏的管理实例
		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 设置一个颜色给系统栏
		if(i==0){
			tintManager.setStatusBarTintResource(R.color.white);
		}
	}
	/**
	 * 获取设备的imei
	 * @param context
	 * @return
	 */
	public static String getDeviceIMEI(Context context){
	 	String imei = ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
	 	if(imei==null||"".equals(imei)){
	 		UUID uuid = UUID.randomUUID(); 
	 		String result=uuid.toString();
	 		result=result.replace("-", "");
	 		result.substring(0, 20);
			return result;
	 	}else{
	 		return imei;
	 	}
	}
	public static boolean isBackground(final Context context) {
	        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	        if (!tasks.isEmpty()) {
	            ComponentName topActivity = tasks.get(0).topActivity;
	            if (!topActivity.getPackageName().equals(context.getPackageName())) {
	                return true;
	            }
	        }
	        return false;  
	}
}
