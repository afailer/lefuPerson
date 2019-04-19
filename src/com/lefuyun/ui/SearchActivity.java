package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.adapter.SortAdapter;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.City;
import com.lefuyun.db.CityDao;
import com.lefuyun.fragment.ExploreFragment;
import com.lefuyun.util.CharacterParser;
import com.lefuyun.util.LogUtil;
import com.lefuyun.util.PinyinComparator;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.ClearEditText;
import com.lefuyun.widget.SideBar;

public class SearchActivity extends BaseActivity implements SectionIndexer{
	
	/**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator mComparator;
    
    private SortAdapter mAdapter;
	
	private ClearEditText mContent;
	private ListView mListView;
	private TextView mDialog;
	private SideBar mSideBar;
	
	private LinearLayout titleLayout;
	private TextView title;
	private TextView tvNofriends;
	
	private TextView mAllBtn;
	
	private List<City> mList;
	
	private List<City> mCurrentList;
	
	private CityDao mCityDao;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_search;
	}

	@Override
	protected void initView() {
		setActionBarTitle("切换城市");
		mCityDao = new CityDao(getApplicationContext());
		mContent = (ClearEditText) findViewById(R.id.filter_edit_search_activity);
		mListView = (ListView) findViewById(R.id.lv_search_activity);
		mDialog = (TextView) findViewById(R.id.dialog_search_activity);
		
		titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		title = (TextView) findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) findViewById(R.id.title_layout_no_match);
		
		mAllBtn = (TextView) findViewById(R.id.all_search_activity);
		mAllBtn.setOnClickListener(this);
		
		mSideBar = (SideBar) findViewById(R.id.sidebar_search_activity);
		mSideBar.setTextView(mDialog);
		mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

			@Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1){
                    mListView.setSelection(position);
                }
            }
        });
		
		//根据输入框输入值的改变来过滤搜索
		mContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	// 这个时候不需要挤压效果 就把他隐藏掉
				titleLayout.setVisibility(View.GONE);
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
	}

	@Override
	protected void initData() {
		//实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mComparator = new PinyinComparator();
        mCurrentList = new ArrayList<City>();
        TLog.log("城市列表从数据库拿到数据");
		setData(mCityDao.findCitysOfMain(0));
	}
	
	private void setData(List<City> result) {
		mCurrentList.clear();
		if(mList == null) {
			// 根据a-z进行排序源数据
			mList = result;
			filledData();
	 		Collections.sort(mList, mComparator);
	 		mCurrentList.addAll(mList);
	 		mAdapter = new SortAdapter(SearchActivity.this, mCurrentList);
	 		mListView.setAdapter(mAdapter);
	 		mListView.setOnScrollListener(new OnScrollListener() {
	 			@Override
	 			public void onScrollStateChanged(AbsListView view, int scrollState) {
	 			}

	 			@Override
	 			public void onScroll(AbsListView view, int firstVisibleItem,
	 					int visibleItemCount, int totalItemCount) {
	 				if(mList.size() > 0) {
	 					int section = getSectionForPosition(firstVisibleItem);
	 	 				int nextSection = getSectionForPosition(firstVisibleItem + 1);
	 	 				int nextSecPosition = getPositionForSection(+nextSection);
	 	 				if (firstVisibleItem != lastFirstVisibleItem) {
	 	 					MarginLayoutParams params = (MarginLayoutParams) titleLayout
	 	 							.getLayoutParams();
	 	 					params.topMargin = 0;
	 	 					titleLayout.setLayoutParams(params);
	 	 					title.setText(mList.get(
	 	 							getPositionForSection(section)).getSortLetters());
	 	 				}
	 	 				if (nextSecPosition == firstVisibleItem + 1) {
	 	 					View childView = view.getChildAt(0);
	 	 					if (childView != null) {
	 	 						int titleHeight = titleLayout.getHeight();
	 	 						int bottom = childView.getBottom();
	 	 						MarginLayoutParams params = (MarginLayoutParams) titleLayout
	 	 								.getLayoutParams();
	 	 						if (bottom < titleHeight) {
	 	 							float pushedDistance = bottom - titleHeight;
	 	 							params.topMargin = (int) pushedDistance;
	 	 							titleLayout.setLayoutParams(params);
	 	 						} else {
	 	 							if (params.topMargin != 0) {
	 	 								params.topMargin = 0;
	 	 								titleLayout.setLayoutParams(params);
	 	 							}
	 	 						}
	 	 					}
	 	 				}
	 	 				lastFirstVisibleItem = firstVisibleItem;
	 				}
	 			}
	 		});
	 		mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TLog.log("mList == " + mList);
					Intent intent = new Intent(SearchActivity.this, SearchSecondaryActivity.class);
					intent.putExtra("city", mCurrentList.get(position));
					startActivityForResult(intent, 100);
				}
			});
		}else {
			mList.clear();
			mList.addAll(result);
			filledData();
			mCurrentList.addAll(mList);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.e("searchActivity",requestCode+" "+resultCode);
		if(resultCode == 1000) {
			//setResult(1000, data);
			City city = (City) data.getSerializableExtra("city");
			finish();
			ExploreFragment.OnResult(city);
			//finish();
		}
		
	}
	
	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private void filledData() {
		
		for (City city : mList) {
			// 汉字转换成拼音
			String pinyin = mCharacterParser.getSelling(city.getRegion_name());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				city.setSortLetters(sortString.toUpperCase());
			} else {
				city.setSortLetters("#");
			}
		}
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<City> filterDateList = new ArrayList<City>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = mList;
			tvNofriends.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (City city : mList) {
				String name = city.getRegion_name();
				if (name.indexOf(filterStr.toString()) != -1
						|| mCharacterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(city);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, mComparator);
		mCurrentList.clear();
		mCurrentList.addAll(filterDateList);
		mAdapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ASCII值
	 */
	public int getSectionForPosition(int position) {
		if(mList != null && mList.size() > 0) {
			return mList.get(position).getSortLetters().charAt(0);
		}
		return -1;
	}

	/**
	 * 根据分类的首字母的Char ASCII值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < mList.size(); i++) {
			String sortStr = mList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all_search_activity:
			City city = new City();
			city.setId(0);
			city.setRegion_name("全部");
			ExploreFragment.OnResult(city);
			/*Intent intent = new Intent();
			intent.putExtra("city", city);
			setResult(1000, intent);*/
			finish();
			break;

		default:
			break;
		}
	}

}
