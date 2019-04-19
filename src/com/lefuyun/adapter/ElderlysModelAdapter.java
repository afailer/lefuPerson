package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lefuyun.R;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.ui.CurrentElderActivity;
import com.lefuyun.widget.HealthStateProcessBar;
import com.lefuyun.widget.HealthStateProcessBar.OnTargetClickListener;
/**
 *
 */
public class ElderlysModelAdapter extends PagerAdapter {
	
	private Context mContext;
	private List<OldPeople> mList;
	private LayoutInflater mInflater;
	
	
	public ElderlysModelAdapter(Context context,List<OldPeople> list) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		OldPeople oldPeople = mList.get(position);
		View content = mInflater.inflate(R.layout.item_model_fragment_lefu_elderlys, null);
		HealthStateProcessBar processBar = (HealthStateProcessBar) content.findViewById(R.id.processBar_item_model_elderlys);
		// 优秀>=95-<=100、良好>=80-<95、一般>=60-<80、较差 <60
		int score = oldPeople.getScore();
		if(score >= 95) {
			processBar.setHealthState("优秀");
		}else if(score >= 80) {
			processBar.setHealthState("良好");
		}else if(score >= 60) {
			processBar.setHealthState("一般");
		}else {
			processBar.setHealthState("较差");
		}
		processBar.setTitle("老 人 综 合 评 估");
		processBar.setMax(100);
		processBar.setCurrentValue(score);
		processBar.start();
		processBar.setOnClickListener(new OnTargetClickListener() {
			
			@Override
			public void onClick(View v) {
				skipCurrentElderActivity(mList.get(position));
			}
		});
		container.addView(content);
		return content;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	/**
	 * 跳转到老人页面
	 * @param oldPeople
	 */
	private void skipCurrentElderActivity(OldPeople oldPeople) {
		Intent intent = new Intent(mContext, CurrentElderActivity.class);
		intent.putExtra("oldPeople", oldPeople);
		mContext.startActivity(intent);
	}
}
