package com.lefuyun.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lefuyun.R;
import com.lefuyun.base.adapter.ImportantDetailAdapter;
import com.lefuyun.bean.ImportantNotice;
import com.lefuyun.widget.RefreshLayout;

@SuppressLint("ValidFragment")
public class ImportantHistoryDetailFragment extends Fragment{
	RefreshLayout list;
	ImportantDetailAdapter adapter;
	List<ImportantNotice> importants=new ArrayList<ImportantNotice>();
	int type;
	String url;
	
	public static ImportantHistoryDetailFragment getImportantHistoryDetailFragmentByType(int type,String url){
		return new ImportantHistoryDetailFragment(type, url);
	}
	
	public ImportantHistoryDetailFragment(int type,String url) {
		this.type=type;
		this.url=url;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_important_detail_notice, null);
			list=(RefreshLayout) view.findViewById(R.id.list);
			adapter=new ImportantDetailAdapter(getActivity(), importants, R.layout.important_notice_item);
			
		return view;
	}
}