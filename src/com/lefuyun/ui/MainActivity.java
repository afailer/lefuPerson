package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.Json;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.Assess;
import com.lefuyun.bean.JpushNotificationDetailBean;
import com.lefuyun.bean.MainTab;
import com.lefuyun.bean.News;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.bean.OldPeopleAssess;
import com.lefuyun.bean.User;
import com.lefuyun.bean.Weather;
import com.lefuyun.fragment.LefuFragment;
import com.lefuyun.healthcondition.TimeBroadCastReciver;
import com.lefuyun.util.ConfigForSpeialOldUtils;
import com.lefuyun.util.JsonUtil;
import com.lefuyun.util.SPUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.util.UpdateManager;

public class MainActivity extends BaseActivity implements OnTabChangeListener{

	private FragmentTabHost mTabHost;
	private User mUser;
	private List<OldPeople> mOldPeopleLists;
	// 记录当前是否是双击  
    private static Boolean isExit = false;
    // 当前Tab的id
    private int mCurrentTabId;
    // 前一个Tab的id
    private int mBeforeTabId;
    // 请求网络的个数
    private int networkNum;
    
    private boolean isFromConcernElderlyPage;
    
    /*** 跳转过来的参数通知或者新闻*/
    String jumptype;
    /*** 通知传过来的通知详情类*/
    JpushNotificationDetailBean detailBean;
    /*** 通知传过来的新闻类*/
    News news;
    @Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@SuppressLint("NewApi")
	public void initView() {
		Intent intent = getIntent();
		mUser = (User) intent.getSerializableExtra("user");
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();
        if(mUser == null) {
        	// 用户没有登录直接跳转发现页面
        	mTabHost.setCurrentTab(1);
        	mCurrentTabId = 1;
        }else {
        	mTabHost.setCurrentTab(0);
        	mCurrentTabId = 0;
        	//发送请求配置的广播
        	Intent intent_config = new Intent(getApplicationContext(), TimeBroadCastReciver.class);
        	intent_config.setAction("com.lefu.updateConfig.action");
    		sendBroadcast(intent_config);
        }
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
        UpdateManager mUpdateManager = new UpdateManager(this, false);
		mUpdateManager.checkUpdate();
		
		jumptype=getIntent().getStringExtra("jumptype");
		TLog.log("jumptype", jumptype+"");
		detailBean=(JpushNotificationDetailBean) getIntent().getSerializableExtra("detailBean");
		if(detailBean!=null){
			TLog.log("detailBeanasqwdeqd", detailBean.toString());
		}
	    news=(News) getIntent().getSerializableExtra("news");
        if(news!=null){
        	TLog.log("news", news.toString());
        }
	}
	
	private void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(
                    mainTab.getResIcon());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            title.setText(getString(mainTab.getResName()));
            if(i == 1 && mUser == null) {
            	// 第二个默认被选中
            	title.setTextColor(Color.parseColor("#666666"));
            }else if(i == 0 && mUser != null) {
            	// 第一个默认被选中
            	title.setTextColor(Color.parseColor("#666666"));
            }else {
            	// 其他没有被选中的置默认颜色
            	title.setTextColor(Color.parseColor("#979797"));
            }
            tab.setIndicator(indicator);
            tab.setContent(new TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);
        }
    }

	@Override
	public void onTabChanged(String tabId) {
		// 监听Tab是否发生改变
		final int id = mTabHost.getCurrentTab();
		mBeforeTabId = mCurrentTabId;
		mCurrentTabId = id;
		final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            TextView title = (TextView) v.findViewById(R.id.tab_title);
            if (i == id) {
            	// 设置选中状态
                v.setSelected(true);
                title.setTextColor(Color.parseColor("#666666"));
            } else {
            	// 设置未选中状态
                v.setSelected(false);
                title.setTextColor(Color.parseColor("#979797"));
            }
        }
        if(id == 0) {
        	checkPageDataForSkip();
        }
	}
	

	@Override
	protected void initData() {
		networkNum = 0;
		if(mUser != null) {
			showWaitDialog();
			LefuApi.getBindingElders(new RequestCallback<List<OldPeople>>() {

				@Override
				public void onSuccess(List<OldPeople> result) {
					if(result == null) {
						mOldPeopleLists = new ArrayList<OldPeople>();
					}else {
						mOldPeopleLists = result;
					    //将老人保存到sp中
						SPUtils.put(MainActivity.this, SPUtils.OLDPEOPLE_JSON, JsonUtil.objectToJson(mOldPeopleLists));
					}
					initOldPeopleAssess(mOldPeopleLists, 0);
				}

				@Override
				public void onFailure(ApiHttpException e) {
					showToast("获取关注人失败");
					hideWaitDialog();
					mOldPeopleLists = new ArrayList<OldPeople>();
					initOldPeopleAssess(mOldPeopleLists, 0);
				}
			});
		}else {
			TLog.log("当前用户是游客身份");
		}
		
	}
	/**
	 * 加载老人综合评估信息
	 * @param list
	 * @param oldPeopleId
	 */
	private void initOldPeopleAssess(List<OldPeople> list, final long oldPeopleId) {
		for (OldPeople oldPeople : list) {
			LefuApi.getElderAssess(oldPeople.getId(), 
					new RequestCallback<OldPeopleAssess>() {
				
				@Override
				public void onSuccess(OldPeopleAssess result) {
					TLog.log("当前老人的配置信息" + result);
					// 转换老人信息
					swithOldPeopleInfo(result);
					// 统计网络完成请求
					statisticNetWork(oldPeopleId);
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					// 统计网络完成请求
					statisticNetWork(oldPeopleId);
				}
			});
		}
		if(list.size() == 0) {
			setLefuFragmentValue(list);
			mTabHost.setCurrentTab(1);
        	mCurrentTabId = 1;
        	hideWaitDialog();
		}
	}
	/**
	 * 统计网络的请求数
	 * @param oldPeopleId 需要跳转的老人id, 如果为0则不跳转
	 */
	private synchronized void statisticNetWork(long oldPeopleId) {
		networkNum++;
		if(mOldPeopleLists.size() == networkNum) {
			TLog.log("老人数据转换完成后 mOldPeopleLists == " + mOldPeopleLists);
			initWeather(oldPeopleId);
		}
	}
	/**
	 * 转换老人信息,即将信息整合到OldPeople
	 * @param o 老人综合评估信息
	 */
	private synchronized void swithOldPeopleInfo(OldPeopleAssess o) {
		for (OldPeople oldPeople : mOldPeopleLists) {
			if(oldPeople.getId() == o.getOld_people_id()) {
				oldPeople.setAssessId(o.getId());
				oldPeople.setScore(o.getScore());
				if(o.getType() != null) {
					Gson gson = Json.getGson();
					Assess assess = gson.fromJson(o.getType(), Assess.class);
					oldPeople.setSignData(assess.getSignData());
					oldPeople.setDailyLife(assess.getDailyLife());
					oldPeople.setDailyNurs(assess.getDailyNurs());
					oldPeople.setDrinkMeal(assess.getDrinkMeal());
				}
				break;
			}
		}
	}
	private void initWeather(final long oldPeopleId) {
		networkNum = 0;
		for (OldPeople oldPeople : mOldPeopleLists) {
			final OldPeople o = oldPeople;
			LefuApi.getWeather(oldPeople.getId(), new RequestCallback<Weather>() {
				
				@Override
				public void onSuccess(Weather result) {
					swithWeatherInfo(result, o.getId());
					statisticWeatherNetWork(oldPeopleId);
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					statisticWeatherNetWork(oldPeopleId);
				}
			});
		}
	}
	/**
	 * 统计网络的请求数
	 * @param oldPeopleId 需要跳转的老人id, 如果为0则不跳转
	 */
	private synchronized void statisticWeatherNetWork(long oldPeopleId) {
		networkNum++;
		if(mOldPeopleLists.size() == networkNum) {
			// 通知Fragment拿到了老人数据
			setLefuFragmentValue(mOldPeopleLists);
			if(oldPeopleId > 0) {
				showLefuFragment(oldPeopleId, false);
			}
			hideWaitDialog();
			//------------推送时，准备完老人数据跳转到通知或新闻页----------------------
    		TLog.log("jumptype", jumptype+"");
    		if("notice".equals(jumptype)){
    			if(detailBean!=null){
    				TLog.log("detailBean不为null", detailBean.toString());
    				Intent i = new Intent(MainActivity.this, ImportantNotifyForNotifyListActivity.class);  //通知详情界面
    				i.putExtra("detailBean", detailBean);
    				startActivity(i);
    			}
    		}
  	    	if("news".equals(jumptype)){
  				if(news!=null){
  					TLog.log("news不为null", news.toString());
  					Intent i = new Intent(this, NewsDetailsActivity.class);  //新闻详情界面
					i.putExtra("new", news);
					startActivity(i);
  				}
  			}
		}
	}
	/**
	 * 转换老人信息,即将信息整合到OldPeople
	 * @param o 老人综合评估信息
	 */
	private synchronized void swithWeatherInfo(Weather w, long id) {
		for (OldPeople oldPeople : mOldPeopleLists) {
			if(oldPeople.getId() == id) {
				oldPeople.setTem(w.getTem());
				oldPeople.setImg(w.getImg());
				oldPeople.setCode(w.getCode());
				oldPeople.setTxt(w.getTxt());
				oldPeople.setCity(w.getCity());
				break;
			}
		}
	}

	/**
	 * 将绑定老人信息传递给指定的fragment
	 * @param oldPeoples
	 */
	private void setLefuFragmentValue(List<OldPeople> oldPeoples) {
		LefuFragment lefuFragment = getLefuFragment();
	    if(lefuFragment != null) {
	    	lefuFragment.setOldPeopleList(oldPeoples);
	    	if(mUser == null) {
	    		lefuFragment.setUser(mUser);
	    	}
	    }
	}
	/**
	 * 获取LefuFragment的实例
	 * @return
	 */
	private LefuFragment getLefuFragment() {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		LefuFragment lefuFragment = null;
		if(fragments != null) {
		    for(Fragment fragment : fragments) {
		    	if(fragment instanceof LefuFragment) {
		    		lefuFragment = (LefuFragment) fragment;
		        	break;
		    	}
		    }
		}
		return lefuFragment;
	}

	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		TLog.log("mainActivity拿到返回信息");
		if(resultCode == AppContext.LOGIN_SUCCESS) {
			// 登录成功
			finish();
		}else if(resultCode == AppContext.BINDING_SUCCESS) {
			// 绑定老人成功要刷新数据
			if(data == null) {
				// 绑定成功,跳转的时候没有拿到指定的老人ID
				isFromConcernElderlyPage = true;
			}else {
				// 绑定老人成功时,选择返回.或者取消老人
				int num = data.getIntExtra("elderlyNum", -1);
				if(num == 0) {
					// 取消了所有的老人
					isFromConcernElderlyPage = false;
				}else {
					isFromConcernElderlyPage = true;
				}
			}
			mCurrentTabId = 1;
			mTabHost.setCurrentTab(0);
			refreshOldPeoples(0);
		}else if(resultCode == AppContext.BINDING_SUCCESS_SKIP) {
			// 绑定老人成功要刷新数据,并跳转到指定的老人
			isFromConcernElderlyPage = true;
			mTabHost.setCurrentTab(0);
			long id = data.getLongExtra("currentOldPeopleID", 0);
			refreshOldPeoples(id);
		}
	}
	
    /** 
     * 菜单、返回键响应 
     */  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_BACK) {    
               exitBy2Click();      //调用双击退出函数  
        }  
        return false;  
    }
    /**
     * 双击退出
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出 
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务 
      
        } else {
            finish();
            AppContext.getInstance().exit();
           // System.exit(0);
        }
    }
    /**
     * 获取当前用户对象
     * @return
     */
    public User getUser() {
    	return mUser;
    }
    /**
     * 修改用户信息
     * @param user
     */
    public void setUser(User user) {
    	mUser.setIcon(user.getIcon());
		mUser.setUser_name(user.getUser_name());
		mUser.setBirthday_dt(user.getBirthday_dt());
		mUser.setGender(user.getGender());
    }
    /**
     * 获取当前用户绑定的所有老人
     * @return
     */
    public List<OldPeople> getOldPeoples() {
    	return mOldPeopleLists;
    }
    /**
     * 获取绑定老人的个数
     * @return
     */
    public int getBindSize() {
    	if(mOldPeopleLists != null) {
    		return mOldPeopleLists.size();
    	}
    	return -1;
    }
    /**
     * 显示LefuFragment页面,并展示指定id的老人信息
     * @param oldPeopleId
     * @param isSwitch 是否切换到第一页面
     */
    public void showLefuFragment(long oldPeopleId, boolean isSwitch) {
    	LefuFragment lefuFragment = getLefuFragment();
    	if(lefuFragment != null) {
    		lefuFragment.setSelectElder(oldPeopleId);
    	}
    	if(isSwitch) {
    		mTabHost.setCurrentTab(0);
    	}
    }
    /**
     * 清除用户信息
     */
    public void clearUserInfo() {
    	// 将用户置null
    	mUser = null;
    	// 清除绑定的老人数据
    	mOldPeopleLists.clear();
    	// 发送修改的数据
    	setLefuFragmentValue(mOldPeopleLists);
    	// 清除保存的用户名以及密码
    	AppContext.clearUserInfo();
    }
    /**
     * 刷新老人数据
     */
    public void refreshOldPeoples(final long oldPeopleId) {
    	networkNum = 0;
    	if(mUser != null) {
			showWaitDialog();
			LefuApi.getBindingElders(new RequestCallback<List<OldPeople>>() {

				@Override
				public void onSuccess(List<OldPeople> result) {
					mOldPeopleLists.clear();
					if(result != null && result.size() > 0) {
						mOldPeopleLists.addAll(result);
						//将老人保存到sp中
						ConfigForSpeialOldUtils.oldPeoples_all = null;
						SPUtils.put(MainActivity.this, SPUtils.OLDPEOPLE_JSON, JsonUtil.objectToJson(mOldPeopleLists));
					}
					// 加载老人的评估信息
					initOldPeopleAssess(mOldPeopleLists, oldPeopleId);
				}

				@Override
				public void onFailure(ApiHttpException e) {
					showToast("更新关注人失败");
					hideWaitDialog();
					mOldPeopleLists.clear();
					// 加载老人的评估信息
					initOldPeopleAssess(mOldPeopleLists, 0);
				}
			});
		}else {
			TLog.log("当前用户是游客身份");
		}
    }
    /**
     * 通过当前数据的信息进行页面切换或者跳转
     */
    private void checkPageDataForSkip() {
    	TLog.log("LefuFragment页面进行页面切换校验");
		if(mUser == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 100);
			switchToBeforePage();
		}else {
			if(isFromConcernElderlyPage) {
				isFromConcernElderlyPage = false;
			}else {
				if(mOldPeopleLists != null && mOldPeopleLists.size() == 0) {
					Intent intent = new Intent(this, ConcernElderlyActivity.class);
					intent.putExtra("userId", mUser.getUser_id());
					startActivityForResult(intent, 100);
					switchToBeforePage();
				}
			}
		}
    }
    /**
     * 未登录时跳转到上一个页面
     */
    private void switchToBeforePage() {
    	mTabHost.setCurrentTab(mBeforeTabId);
    }
    
}
