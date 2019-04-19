package com.lefuyun.healthcondition;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.bean.ContentTotal;
import com.lefuyun.util.ConfigUtils;
import com.lefuyun.util.JsonUtil;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.NetWorkUtils;
import com.lefuyun.util.SPUtils;

/**
 * @author chenshichao
 * @date   2015-11-13
 * @description  更新配置的广播接收者
 */
public class TimeBroadCastReciver extends BroadcastReceiver {
	private String lastupdatetime, agency_id=""+1;
	public ContentTotal content;
	private Context conntext2;
	Context mContext;
	public static String VERSION = "20151101";// 接口的版本号
	@Override
	public void onReceive(Context context, Intent intent) {
		String NetWorkType = NetWorkUtils.getNetWorkType(context);
		LogUtil.e("net", NetWorkType);
		if(NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkType)){
			return;
		}
		mContext=context;
		conntext2 = context;
		content = ConfigUtils.getConfig(context);
		lastupdatetime = (String) SPUtils.get(conntext2, SPUtils.LASTUPDATETIME, "");
		LogUtil.d("tag", "..." + lastupdatetime);
		if (intent.getAction().equals("com.lefu.updateConfig.action")) {
				if ("".equals(lastupdatetime) ||"".equals(content.getContent())||content.getContent()== null) {// 表示第一次缓存的时候
					getConfigdata(context);
				} else {
					long currenttime = System.currentTimeMillis();
					long lasttime = Long.parseLong(lastupdatetime);
					if (currenttime - lasttime >=86400000) {// 大于24小时
						getConfigdata(context);
					}
				}
		}
	}
	public void getConfigdata(final Context context) {
		if (content.getContent()!=null&&!"".equals(content.getContent()) ) {//有配置但是更新时间到了时间
			 LefuApi.queryConfig(context,agency_id, content.getTag(), new RequestCallback<ContentTotal>() {
				
				@Override
				public void onSuccess(ContentTotal result) {
					String configJSon="";
					if(result!=null){
						configJSon=JsonUtil.objectToJson(result);
						SPUtils.put(context,SPUtils.CONFIG_JSON_DATA,configJSon);
					}
					// 保存下时间
					SPUtils.put(conntext2,SPUtils.LASTUPDATETIME,System.currentTimeMillis()+""); 
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}else{//没配置的时候
			 LefuApi.queryConfig(context,agency_id, "0", new RequestCallback<ContentTotal>() {
					
					@Override
				public void onSuccess(ContentTotal result) {
						LogUtil.i("config", result.getContent());
						String configJSon="";
						if(result!=null){
							configJSon=JsonUtil.objectToJson(result);
							SPUtils.put(context,SPUtils.CONFIG_JSON_DATA,configJSon);
						}
						// 保存下时间
						SPUtils.put(conntext2,SPUtils.LASTUPDATETIME,System.currentTimeMillis()+""); 
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					
				}
			});
		}
	}
}