package com.lefuyun.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;

public class ContactUsActivity extends BaseActivity {

	ListView mListView;
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_contact_us;
	}

	@Override
	protected void initView() {
		setActionBarTitle("联系我们");
		TextView version =(TextView)findViewById(R.id.contact_version);
		version.setText("当前版本：v"+getVersion());
		mListView=(ListView) findViewById(R.id.contact_list);
		List<LefuContact> datas=new ArrayList<ContactUsActivity.LefuContact>();
		datas.add(new LefuContact("北京总部", "010-87587331", "01087587331"));
		datas.add(new LefuContact("山东办事处", "0531-88272678", "053188272678"));
		datas.add(new LefuContact("成都办事处", "028-84446522", "02884446522"));
		ContactAdapter adapter=new ContactAdapter(getApplicationContext(), datas, R.layout.contact_item);
		mListView.setAdapter(adapter);
	}
	public String getVersion() {
		String version = null;
		PackageManager pm = (PackageManager) getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			version = "未知";
		}
		return version;
	}
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

		@Override
	public void onRightViewClick(View view) {
		// TODO Auto-generated method stub
		super.onRightViewClick(view);
	}
	/*@Override
	protected boolean hasRightTextColor() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	protected String getRightTextColor() {
		// TODO Auto-generated method stub
		return "#06BEBD";
	}
	@Override
	protected String getActionBarRightText() {
		// TODO Auto-generated method stub
		return "提交";
	}*/
	@Override
	protected boolean hasActionBar() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected boolean hasBackButton() {
		// TODO Auto-generated method stub
		return true;
	}
	class LefuContact{
		private String locName;
		private String telStr;
		private String telNum;
		
		public LefuContact(String locName, String telStr, String telNum) {
			super();
			this.locName = locName;
			this.telStr = telStr;
			this.telNum = telNum;
		}
		public String getLocName() {
			return locName;
		}
		public void setLocName(String locName) {
			this.locName = locName;
		}
		public String getTelStr() {
			return telStr;
		}
		public void setTelStr(String telStr) {
			this.telStr = telStr;
		}
		public String getTelNum() {
			return telNum;
		}
		public void setTelNum(String telNum) {
			this.telNum = telNum;
		}
	}
	class ContactAdapter extends CommonAdapter<LefuContact>{

		public ContactAdapter(Context context, List<LefuContact> datas,
				int layoutId) {
			super(context, datas, layoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder holder, final LefuContact t, int position) {
			// TODO Auto-generated method stub
			holder.setText(R.id.contact_item_where, t.getLocName()).
			setText(R.id.contact_item_phone, t.getTelStr());
			holder.getView(R.id.contact_us).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(Intent.ACTION_DIAL);
					Uri data = Uri.parse("tel:" + t.getTelNum());
					intent.setData(data);
					startActivity(intent);
				}
			});
		}
		
	}
}
