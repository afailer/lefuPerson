package com.lefuyun.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.lefuyun.R;
import com.lefuyun.base.adapter.CommonAdapter;
import com.lefuyun.base.adapter.ViewHolder;
import com.lefuyun.bean.Organization;
import com.lefuyun.ui.CurrentCitiesMapActivity;
import com.lefuyun.ui.OrgActivity;
import com.lefuyun.ui.ReserveOrganization;

public class OrganizationFragmentAdapter extends CommonAdapter<Organization> {
	
	private Context mContext;

	public OrganizationFragmentAdapter(Context context, List<Organization> datas,
			int layoutId) {
		super(context, datas, layoutId);
		mContext = context;
	}

	@Override
	public void convert(ViewHolder holder, final Organization t,int position) {
		holder.setText(R.id.name_item_organization_fragment, t.getAgency_name())
		.setText(R.id.remark_item_organization_fragment, t.getRemark())
		.setText(R.id.bed_total_item_organization_fragment, t.getBed_total() + "")
		.setText(R.id.residual_bed_item_organization_fragment, t.getAgency_property_text() + "")
		.setText(R.id.location_item_organization_fragment, t.getRegion_name())
		.setImageByUrl(R.id.image_item_organization_fragment, t.getExterior_pic());
		
		holder.getView(R.id.event_item_organization_fragment)
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转机构活动页面
				Intent intent = new Intent(mContext, OrgActivity.class);
				intent.putExtra("id", t.getAgency_id());
				intent.putExtra("name", t.getAgency_name());
				mContext.startActivity(intent);
			}
		});
		holder.getView(R.id.apply_item_organization_fragment)
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转机构预约页面
				Intent intent = new Intent(mContext, ReserveOrganization.class);
				intent.putExtra("organization", t);
				mContext.startActivity(intent);
				
			}
		});
		holder.getView(R.id.skip_map_item_organization_fragment)
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转到机构所在地图上页面
				Intent intent = new Intent(mContext, CurrentCitiesMapActivity.class);
				intent.putExtra("organization", t);
				mContext.startActivity(intent);
				
			}
		});
	}
}
