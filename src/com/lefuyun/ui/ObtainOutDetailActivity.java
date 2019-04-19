package com.lefuyun.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OlderEgress;
import com.lefuyun.util.StringUtils;

public class ObtainOutDetailActivity extends BaseActivity {
	
	private String format = "yyyy-MM-dd HH:mm:ss";
	
	// 预计离院时间、 预计返回时间、 实际离院时间 、 实际返回时间
	private TextView mOutTime, mBackTime, mRealOutTime, mRealBackTime;
	// 实际离院时间和实际返回时间的容器
	private RelativeLayout mRealOutContainer, mRealBackContainer;
	// 离院原因和接送家属
	private TextView mReason, mName;
	// 关闭按钮
	private Button mCloseBtn;
	// 外出注意事项
	private TextView mNotesView;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_obtain_out_detail;
	}

	@Override
	protected void initView() {
		setActionBarTitle("请假详情");
		mOutTime = (TextView) findViewById(R.id.start_time_obtain_detail_activity);
		mBackTime = (TextView) findViewById(R.id.end_time_obtain_detail_activity);
		mRealOutTime = (TextView) findViewById(R.id.real_start_time_obtain_detail_activity);
		mRealBackTime = (TextView) findViewById(R.id.real_end_time_obtain_detail_activity);
		
		mRealOutContainer = (RelativeLayout) findViewById(R.id.container_real_start_time_obtain_detail_activity);
		mRealBackContainer = (RelativeLayout) findViewById(R.id.container_real_end_time_obtain_detail_activity);
		
		mName = (TextView) findViewById(R.id.name_obtain_detail_activity);
		mReason = (TextView) findViewById(R.id.reason_obtain_detail_activity);
		
		mCloseBtn = (Button) findViewById(R.id.close_obtain_detail_activity);
		mCloseBtn.setOnClickListener(this);
		
		mNotesView = (TextView) findViewById(R.id.notes_obtain_detail_activity);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		OlderEgress egress = (OlderEgress) intent.getSerializableExtra("olderEgress");
		if(egress.getLeave_state() == OlderEgress.LEAVE_STATE_OUT) {
			// 出院状态, 显示3个时间状态,出去实际返回时间
			mRealBackContainer.setVisibility(View.GONE);
			mRealOutTime.setText(StringUtils.getFormatData(
					egress.getLeave_hospital_dt(), format));
		}else if(egress.getLeave_state() == OlderEgress.LEAVE_STATE_COMPLETE) {
			// 当前请假为已完成状态,显示四个时间
			mRealOutTime.setText(StringUtils.getFormatData(
					egress.getLeave_hospital_dt(), format));
			mRealBackTime.setText(StringUtils.getFormatData(
					egress.getReal_return_dt(), format));
		}else {
			// 其他三个状态,只显示预计返回时间和预计离院时间
			mRealOutContainer.setVisibility(View.GONE);
			mRealBackContainer.setVisibility(View.GONE);
		}
		mOutTime.setText(StringUtils.getFormatData(
				egress.getExpected_leave_dt(), format)); 
		mBackTime.setText(StringUtils.getFormatData(
				egress.getExpected_return_dt(), format));
		mReason.setText(egress.getLeave_reason());
		mNotesView.setText(egress.getNotes_matters());
		mName.setText(egress.getParty_signature());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_obtain_detail_activity:
			// 提交请假内容
			finish();
			break;

		default:
			break;
		}

	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
}
