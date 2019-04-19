package com.lefuyun.service;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.interf.ICallbackResult;
import com.lefuyun.ui.MainActivity;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;


public class DownloadService extends Service {

	public static final String BUNDLE_KEY_DOWNLOAD_URL = "download_url";
	
	public static final String BUNDLE_KEY_TITLE = "title";
	
	private static final int NOTIFY_ID = 0;
	// 当前下载数据的大小
	private long mProgress;
	// 下载数据的总大小
	private long mCount;
	private Notification mNotification;
	private NotificationManager mNotificationManager;
	// 当前数据是否正在下载
	private boolean isLoading;
	// 下载文件地址
	private String downloadUrl;
	
	private String mTitle = "正在下载%s";
	
	private RemoteViews mContentview;
	
	// 保存文件的额地址
	private String saveFileName = AppContext.DEFAULT_SAVE_FILE_PATH;
	// 完成接口回调地址
	private ICallbackResult callback;

	private DownloadBinder binder;
	// 记录当前服务是否已经销毁
	private boolean serviceIsDestroy = false;

	private Context mContext = this;
	
	private int rate;
	private int pRate;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			TLog.log("当前线程是 == " + Thread.currentThread().getName());
			int rate = msg.arg1;
			if(rate < 100) {
				mContentview.setTextViewText(R.id.tv_download_state, mTitle + "(" + rate
						+ "%" + ")");
				mContentview.setProgressBar(R.id.pb_download, 100, rate, false);
			}else {
				// 下载完毕后变换通知形式
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mNotification.contentView = null;
				Intent intent = new Intent(mContext, MainActivity.class);
				// 告知已完成
				intent.putExtra("completed", "yes");
				// 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
				PendingIntent contentIntent = PendingIntent.getActivity(
						mContext, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				mNotification.setLatestEventInfo(mContext, "下载完成",
						"文件已下载完毕", contentIntent);
				serviceIsDestroy = true;
				stopSelf();// 停掉服务自身
			}
			mNotificationManager.notify(NOTIFY_ID, mNotification);
		};
	};

	
	@Override
	public IBinder onBind(Intent intent) {
		downloadUrl = intent.getStringExtra(BUNDLE_KEY_DOWNLOAD_URL);
		saveFileName = saveFileName + getSaveFileName(downloadUrl); 
		mTitle = String.format(mTitle, intent.getStringExtra(BUNDLE_KEY_TITLE));
		return binder;
	}
	/**
	 * 获取文件名称
	 * @param downloadUrl
	 * @return
	 */
	private String getSaveFileName(String downloadUrl) {
		if (downloadUrl == null || StringUtils.isEmpty(downloadUrl)) {
			return "";
		}
		return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
	}

	@Override
	public void onCreate() {
		super.onCreate();
		binder = new DownloadBinder();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		stopForeground(true);// 这个不确定是否有作用
	}
	/**
	 * 开始下载数据
	 */
	private void startDownload() {
		isLoading = true;
		File file = new File(AppContext.DEFAULT_SAVE_FILE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		pRate = -1;
		LefuApi.downloadApp(downloadUrl, saveFileName, new RequestCallback<File>() {
			
			@Override
			public void onSuccess(File result) {
				TLog.log("APP更新成功");
				isLoading = false;
				if(callback != null) {
					callback.OnBackResult(result);
				}
				// 下载完毕
				mNotificationManager.cancel(NOTIFY_ID);
				installApk(result);
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				TLog.log("APP更新失败");
				isLoading = false;
				mNotificationManager.cancel(NOTIFY_ID);
			}
			
			@Override
			public void onLoadingSubThread(long count, long current) {
				mProgress = current;
				mCount = count;
				rate = (int) (current * 1.0f / count * 100);
				if(rate % 10 == 0 && rate != pRate) {
					// 每下载10%的数据进行更新
					TLog.log("rate == " + rate);
					pRate = rate;
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = rate;
					mHandler.sendMessage(msg);
				}
			}
		});
	}

	/**
	 * 创建通知,服务状态栏中显示下载信息
	 */
	@SuppressWarnings("deprecation")
	private void setUpNotification() {
		int icon = R.drawable.lefuyun;
		CharSequence tickerText = "准备下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		mContentview = new RemoteViews(getPackageName(),
				R.layout.download_notification_show);
		mContentview.setTextViewText(R.id.tv_download_state, mTitle);
		// 指定个性化视图
		mNotification.contentView = mContentview;

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 指定内容意图
		mNotification.contentIntent = contentIntent;
		// 启动Notification
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

	/**
	 * 安装应用
	 * @param file
	 */
	private void installApk(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
			"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	public class DownloadBinder extends Binder {
		public void start() {
			if(!isLoading) {
				// 没有数据加载
				isLoading = true;
				mProgress = 0;
				setUpNotification();
				startDownload();
			}else {
				Toast.makeText(getApplicationContext(), "正在后台下载请稍后！", Toast.LENGTH_SHORT).show();
			}
		}
		/**
		 * 获取当前文件的总大小
		 * @return
		 */
		public long getCount() {
			return mCount;
		}
		/**
		 * 获取当前下载的数据
		 * @return
		 */
		public long getProgress() {
			return mProgress;
		}
		/**
		 * 下载是否取消
		 * @return
		 */
		public boolean isCanceled() {
			return isLoading;
		}
		/**
		 * 获取当前服务是否已经销毁
		 * @return
		 */
		public boolean serviceIsDestroy() {
			return serviceIsDestroy;
		}
		/**
		 * 取消通知
		 */
		public void cancelNotification() {
			mNotificationManager.cancel(NOTIFY_ID);
		}
		/**
		 * 添加接口回调
		 * @param callback
		 */
		public void addCallback(ICallbackResult callback) {
			DownloadService.this.callback = callback;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mNotificationManager.cancel(NOTIFY_ID);
	}
}
