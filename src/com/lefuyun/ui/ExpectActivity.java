package com.lefuyun.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;

/**
 * @author chenshichao
 * @date 2016-5-13
 * @description 敬请期待
 */
public class ExpectActivity extends BaseActivity {
	Button btn_ok;

	@Override
	public void onClick(View v) {

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_expect;
	}

	@Override
	protected void initView() {
		setActionBarTitle("敬请期待");
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected boolean hasActionBar() {
		return true;
	}

	@Override
	protected void initData() {

	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}
}
