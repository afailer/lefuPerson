package com.lefuyun.Jpush;
import org.json.JSONException;
import org.json.JSONObject;

import com.lefuyun.AppContext;
import com.lefuyun.AppStart;
import com.lefuyun.bean.JpushNotificationBean;
import com.lefuyun.bean.JpushNotificationDetailBean;
import com.lefuyun.bean.MyEvent;
import com.lefuyun.bean.News;
import com.lefuyun.ui.ImportantNotifyForNotifyListActivity;
import com.lefuyun.ui.LoginActivity;
import com.lefuyun.ui.NewsDetailsActivity;
import com.lefuyun.util.DateUtils;
import com.lefuyun.util.DialogHelper;
import com.lefuyun.util.JsonUtil;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.ToastUtils;
import com.lefuyun.util.Utils;
import com.lefuyun.widget.dialog.ConfirmDialogFragment.OnClickListener;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.util.ac;
import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
/**
 * 接收推送消息的广播
 */
public class JpushReceiver extends BroadcastReceiver {
	private static final String TAG="JpushReceiver";
	public static JpushNotificationBean jpushNotificationBean;
	public static JpushNotificationDetailBean detailBean;
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		Log.i(TAG, "action------"+action);
		if(action.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){//接收推送自定义消息
			//获取推送消息内容
			Bundle bundle = intent.getExtras();
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			//获取推送的附加字段
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.i(TAG, "message----"+message+"extras---:"+extras);
		}else if(action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)){//接收通知推送和富媒体推送
			//获取通知标题
			Bundle bundle = intent.getExtras();         
			String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);//获取通知内容
			//获取推送的附加字段
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.i(TAG, "title----"+title+"-----content----"+content+"-----extras------"+extras);
		    jpushNotificationBean=new JpushNotificationBean();
		    jpushNotificationBean.setTitle(title);
		    jpushNotificationBean.setContent(content);
		    LogUtil.i("jpushNotificationBean", jpushNotificationBean.getTitle()+","+jpushNotificationBean.getContent());
		    try {
				JSONObject jsonObject=new JSONObject(extras);
				jpushNotificationBean.setType(jsonObject.getString("type")); 
				LogUtil.i("jpushNotificationBean", jpushNotificationBean.getType()+"");
				detailBean=new JpushNotificationDetailBean();
				detailBean=(JpushNotificationDetailBean) JsonUtil.jsonToBean(jsonObject.optString("data"),JpushNotificationDetailBean.class);
				LogUtil.i("detailBean", detailBean.getType()+"");
				if(jpushNotificationBean.getType().equals("00101")){//登录
					// 清除保存的用户名以及密码
	            //	AppContext.clearUserInfo();
	            	LogUtil.i("reciver_isBackground_noclick", Utils.isBackground(context)+"");
	            	if(!Utils.isBackground(context)){//如果应用在前台
	            		Intent i = new Intent(context, ShowDialogActivity.class);  //自定义打开的界面
	    			    i.putExtra("detailBean", detailBean);
	                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    context.startActivity(i);
	          	    }else{
	          	       Intent i = new Intent(context, AppStart.class);  //跳到主页面
        			   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       i.putExtra("detailBean", detailBean);
                       i.putExtra("jumptype", "loginout");
                       LogUtil.i("detailBean", detailBean.toString());
                       context.startActivity(i);	          	    	
	          	    }
				}
		    } catch (JSONException e) {
				e.printStackTrace();
			}
		    
		}else if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){//
			Log.i(TAG, "用户点击了打开通知");
			// 在这里可以自己写代码去定义用户点击后的行为
            if(jpushNotificationBean==null){
                 return;
            }
            LogUtil.i("activitysize:", AppContext.getInstance().getActivityList().size()+"");
           // ToastUtils.show(context,"activity:"+AppContext.getInstance().getActivityList().size()+"");
            if(Utils.isBackground(context)&&AppContext.getInstance().getActivityList().size()==0){//如果应用在后台
        		LogUtil.i("ttag", "应用在后台");
                 if(jpushNotificationBean.getType().equals("00101")){
                	 Intent i = new Intent(context, AppStart.class);  //跳到主页面
      			    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     i.putExtra("detailBean", detailBean);
                     i.putExtra("jumptype", "loginout");
                     LogUtil.i("detailBean", detailBean.toString());
                     context.startActivity(i);	
                 }else if(jpushNotificationBean.getType().equals("00201")){//通知
        			  LogUtil.i("ttag", "点击通知");
        			  Intent i = new Intent(context, AppStart.class);  //跳到主页面
        			  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      i.putExtra("detailBean", detailBean);
                      i.putExtra("jumptype", "notice");
                      LogUtil.i("detailBean", detailBean.toString());
                      context.startActivity(i);
        		  }else if(jpushNotificationBean.getType().equals("00301")){//新闻
        			  LogUtil.i("ttag", "点击新闻");
        			  Intent i = new Intent(context, AppStart.class);  //新闻详情界面
        			  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        			  i.putExtra("id", detailBean.getId());
                      i.putExtra("jumptype", "news");
                      context.startActivity(i);
        		  }
        		  return;
        	}
        	LogUtil.i("jpushNotificationBean", jpushNotificationBean.getType()+"");
            if(jpushNotificationBean.getType().equals("00101")){//退出
            		Intent i = new Intent(context, ShowDialogActivity.class);  //自定义打开的界面
    			    i.putExtra("detailBean", detailBean);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
            }else if(jpushNotificationBean.getType().equals("00201")){//通知
            		  Intent i = new Intent(context, ImportantNotifyForNotifyListActivity.class);  //新闻详情界面
                      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      i.putExtra("detailBean", detailBean);
                      context.startActivity(i);
            }else if(jpushNotificationBean.getType().equals("00301")){//新闻
            		 Intent i = new Intent(context, NewsDetailsActivity.class);  //新闻详情界面
                     i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     i.putExtra("id", detailBean.getId());
                     context.startActivity(i);
            }
		}
	}
}
