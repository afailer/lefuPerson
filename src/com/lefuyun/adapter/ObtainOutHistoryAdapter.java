package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;

import com.lefuyun.R;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.OlderEgress;
import com.lefuyun.util.StringUtils;

public class ObtainOutHistoryAdapter extends CommonAdapter<OlderEgress> {
	
	private String format = "yyyyMMdd";
	private String[] mObtainOutState;

	public ObtainOutHistoryAdapter(Context context, List<OlderEgress> datas,
			int layoutId) {
		super(context, datas, layoutId);
		mObtainOutState = context.getResources().getStringArray(R.array.obtain_out_state);
	}

	@Override
	public void convert(ViewHolder holder, OlderEgress t,int position) {
		long outTime = 0;
		long backTime = 0;
		if(t.getLeave_state() == OlderEgress.LEAVE_STATE_OUT) {
			// 出院状态
			outTime = t.getLeave_hospital_dt();
			backTime = t.getExpected_return_dt();
		}else if(t.getLeave_state() == OlderEgress.LEAVE_STATE_COMPLETE) {
			// 当前请假为已完成状态
			outTime = t.getLeave_hospital_dt();
			backTime = t.getReal_return_dt();
		}else {
			// 其他三个状态,只显示预计返回时间和预计离院时间
			outTime = t.getExpected_leave_dt();
			backTime = t.getExpected_return_dt();
		}
		String date = StringUtils.getFormatData(outTime, format) + " - " + 
				StringUtils.getFormatData(backTime, format);
		holder.setText(R.id.details_item_obtain_out_history_fragment, t.getLeave_reason())
		.setText(R.id.elder_item_obtain_out_history_fragment, t.getElderly_name())
		.setText(R.id.date_item_obtain_out_history_fragment, date)
		.setText(R.id.time_item_obtain_out_history_fragment, 
				StringUtils.calDateDifferent(outTime, backTime) + "天")
		.setText(R.id.state_item_obtain_out_history_fragment, mObtainOutState[t.getLeave_state() - 1]);
	}
	

}
