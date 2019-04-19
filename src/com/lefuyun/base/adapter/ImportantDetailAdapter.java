package com.lefuyun.base.adapter;

import java.util.List;

import android.content.Context;

import com.lefuyun.R;
import com.lefuyun.bean.ImportantNotice;

public class ImportantDetailAdapter extends CommonAdapter<ImportantNotice>{

	public ImportantDetailAdapter(Context context, List<ImportantNotice> datas,
			int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, ImportantNotice t,int position) {
		// TODO Auto-generated method stub
		holder.setText(R.id.important_title, t.getTitle());
		holder.setImageByUrl(R.id.important_pic, t.getImgUrl());
		holder.setText(R.id.important_content, t.getContent());
	}
	
}
