package com.lefuyun.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.StringUtils;

public class ProposalActivity extends BaseActivity {
	
	private OldPeople mOldPeople;
	
	private EditText mContent;
	private TextView mConfirm;
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_proposal;
	}

	@Override
	protected void initView() {
		setActionBarTitle("建议簿");
		mContent = (EditText) findViewById(R.id.content_proposal_activity);
		mConfirm = (TextView) findViewById(R.id.confirm_proposal_activity);
		mConfirm.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_proposal_activity:
			String content = mContent.getText().toString().trim();
			if(StringUtils.isEmpty(content)) {
				showToast("建议内容不能为空");
				return;
			}
			LefuApi.addAdvice(mOldPeople.getAgency_id(), content, new RequestCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					showToast("感谢您的建议");
					finish();
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					showToast("建议提交失败");
				}
			});
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
