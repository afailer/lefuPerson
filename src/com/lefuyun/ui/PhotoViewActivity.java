package com.lefuyun.ui;

import uk.co.senab.photoview.PhotoView;

import com.lefuyun.R;
import com.lefuyun.R.layout;
import com.lefuyun.R.menu;
import com.lefuyun.api.remote.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PhotoViewActivity extends Activity {
	PhotoView photoView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view);
		photoView=(PhotoView) findViewById(R.id.photoView);
		String path = getIntent().getStringExtra("path");
		ImageLoader.loadImg(path, photoView);
	}
}
