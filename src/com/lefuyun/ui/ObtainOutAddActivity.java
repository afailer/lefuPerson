package com.lefuyun.ui;

import java.util.Date;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OlderEgress;
import com.lefuyun.util.StringUtils;
import com.lefuyun.widget.pickerview.TimePickerView;
import com.lefuyun.widget.pickerview.TimePickerView.OnTimeSelectListener;

public class ObtainOutAddActivity extends BaseActivity {
	
	private static final int OUT_STATE = 1;
	private static final int BACK_STATE = 2;
	
	private long oldPeopleId;
	
	private String format = "yyyy-MM-dd HH:mm";
	
	private int mCurrentState;
	// 预计离院时间、 预计返回时间
	private TextView mOutTimeView, mBackTimeView;
	private EditText mReasonView, mNameView;
	private TextView mConfirmBtn, mCancelBtn;
	// 时间选择控件
	private TimePickerView mTimePickerView;
	
	private Date mOutTime, mBackTime;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_obtain_out_add;
	}

	@Override
	protected void initView() {
		setActionBarTitle("外出请假");
		mOutTimeView = (TextView) findViewById(R.id.start_time_obtain_permission_activity);
		mBackTimeView = (TextView) findViewById(R.id.end_time_obtain_permission_activity);
		mOutTimeView.setOnClickListener(this);
		mBackTimeView.setOnClickListener(this);
		mReasonView = (EditText) findViewById(R.id.reason_obtain_permission_activity);
		mNameView = (EditText) findViewById(R.id.name_obtain_permission_activity);
		mConfirmBtn = (TextView) findViewById(R.id.add_obtain_permission_activity);
		mConfirmBtn.setOnClickListener(this);
		mCancelBtn = (TextView) findViewById(R.id.cancel_obtain_permission_activity);
		mCancelBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		oldPeopleId = intent.getLongExtra("oldPeopleId", 0);
		// 时间选择器
		mTimePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
		// 设置开始时间
		mTimePickerView.setStartYear(StringUtils.getCurrentDate()[0]);
		// 控制时间范围
		mTimePickerView.setTime(new Date());
		mTimePickerView.setCyclic(false);
		mTimePickerView.setCancelable(true);
		// 点击确认的时候默认不关闭控件
		mTimePickerView.isConfirmCloseView(false);
		mTimePickerView.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				String dateString = StringUtils.getFormatData(date.getTime(), format);
				if(mCurrentState == OUT_STATE) {
					// 选择离院时间
					if(date.getTime() + 60000 < new Date().getTime()) {
						showToast("时间已过,请重新选择");
						mTimePickerView.setTime(new Date());
						return;
					}
					mOutTime = date;
					mOutTimeView.setText(dateString);
					if(mBackTime != null && mBackTime.getTime() <= mOutTime.getTime()) {
						// 重新选择的离院时间大于返回时间
						mBackTimeView.setText("");
						mBackTime = null;
					}
				}else if(mCurrentState == BACK_STATE) {
					if(mOutTime == null) {
						showToast("请选择预计离院时间");
						mTimePickerView.dismiss();
						return;
					}
					if(date.getTime() <= mOutTime.getTime()) {
						showToast("预计返回时间早于离院时间");
						return;
					}
					// 选择预计返回时间
					mBackTime = date;
					mBackTimeView.setText(dateString);
				}
				mTimePickerView.dismiss();
			}
		});
	}
	/**
	 * 创建一条请假记录
	 */
	private void addObtainOut() {
		// 校验信息
		if(mOutTime == null) {
			showToast("预计离院时间不能为空");
			return;
		}
		if(mBackTime == null) {
			showToast("预计返回时间不能为空");
			return;
		}
		String reason = mReasonView.getText().toString().trim();
		if(StringUtils.isEmpty(reason)) {
			showToast("请假事由不能为空");
			return;
		}
		String name = mNameView.getText().toString().trim();
		if(StringUtils.isEmpty(name)) {
			showToast("接送家属不能为空");
			return;
		}
		showWaitDialog();
		OlderEgress egress = new OlderEgress();
		egress.setOld_people_id(oldPeopleId);
		egress.setExpected_leave_dt(mOutTime.getTime());
		egress.setExpected_return_dt(mBackTime.getTime());
		egress.setLeave_state(OlderEgress.LEAVE_STATE_APPLY);
		egress.setParty_signature(name);
		egress.setLeave_reason(reason);
		LefuApi.addOBtainOut(egress, new RequestCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				hideWaitDialog();
				showToast("请假提交成功");
				finish();
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				hideWaitDialog();
				showToast(e.getMessage());
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_time_obtain_permission_activity:
			// 选择离院时间
			mTimePickerView.setTitle("预计离院时间");
			mCurrentState = OUT_STATE;
			if(mOutTime != null) {
				mTimePickerView.setTime(mOutTime);
			}
			mTimePickerView.show();
			break;
		case R.id.end_time_obtain_permission_activity:
			// 选择预计返回时间
			mTimePickerView.setTitle("预计返回时间");
			mCurrentState = BACK_STATE;
			if(mBackTime != null) {
				mTimePickerView.setTime(mBackTime);
			}
			mTimePickerView.show();
			break;
		case R.id.add_obtain_permission_activity:
			// 提交请假内容
			addObtainOut();
			break;
		case R.id.cancel_obtain_permission_activity:
			// 取消按钮点击事件
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
