package com.lefuyun.ui;

import java.util.Hashtable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lefuyun.R;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.OldPeople;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;

public class SuccessBuildZxingCodeActivity extends BaseActivity {
	
	private OldPeople mCurrentOldPeople;
	
	private TextView mShareBtn;
	private ImageView mZxingCodeView;
	
	// 二维码的宽和高
	private int mZxingWidthAndHeight;
	private Bitmap mZxingCode;
	

	@Override
	protected int getLayoutId() {
		return R.layout.activity_success_build_zxing_code;
	}

	@Override
	protected void initView() {
		setActionBarTitle("二维码");
		mZxingCodeView = (ImageView) findViewById(R.id.img_success_build_zxing_code_activity);
		mShareBtn = (TextView) findViewById(R.id.share_success_build_zxing_code_activity);
		mShareBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mCurrentOldPeople = (OldPeople) intent.getSerializableExtra("oldPeople");
		String number = StringUtils.randomBuildNumber(mCurrentOldPeople.getId());
		// 初始化二维码的宽和高
		mZxingWidthAndHeight = (int) getResources().getDimension(R.dimen.zxing_width_height);
		mZxingCode = buildZxingCode(number, mZxingWidthAndHeight, mZxingWidthAndHeight);
		if(mZxingCode == null) {
			showToast("二维码生成失败");
			return;
		}
		mZxingCodeView.setImageBitmap(mZxingCode);
	}
	
	/**
	 *  * 生成一个二维码图像
	 * @param str 传入的字符串
	 * @param QR_WIDTH 
	 * 				宽度（像素值px）
	 * @param QR_HEIGHT 
	 * 				高度（像素值px）
	 * @return 返回图片的bitmap
	 */
	private Bitmap buildZxingCode(String str, int QR_WIDTH, int QR_HEIGHT) {
		try {
			// 判断str合法性  
	        if (str == null || "".equals(str) || str.length() < 1) {  
	            return null;  
	        }  
	        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
	        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
	        // 图像数据转换，使用了矩阵转换  
	        BitMatrix bitMatrix = new QRCodeWriter().encode(str,  
	                BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);  
	        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];  
	        // 下面这里按照二维码的算法，逐个生成二维码的图片，  
	        // 两个for循环是图片横列扫描的结果  
	        for (int y = 0; y < QR_HEIGHT; y++) {  
	            for (int x = 0; x < QR_WIDTH; x++) {  
	                if (bitMatrix.get(x, y)) {  
	                    pixels[y * QR_WIDTH + x] = 0xff000000;  
	                } else {  
	                    pixels[y * QR_WIDTH + x] = 0xffffffff;  
	                }  
	            }  
	        }  
	        // 生成二维码图片的格式，使用ARGB_8888  
	        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,  
	                Bitmap.Config.ARGB_8888);  
	        bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);  
	        // 显示到一个ImageView上面  
	        // sweepIV.setImageBitmap(bitmap);  
	        return bitmap;
		}catch (WriterException e) {  
            TLog.log("生成二维码错误 : " + e.getMessage());  
            return null;
        }
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.share_success_build_zxing_code_activity) {
			// 分享当前二维码
			String url = LefuApi.getAbsoluteApiUrl("lefuyun/socialPeopleCtr/toShareOldPeople");
			url = url + "?oid=" + mCurrentOldPeople.getId();
			String message = "扫一扫二维码,关注您的家人" + mCurrentOldPeople.getElderly_name();
			LefuApi.sharePage(this, " ", message, "", url, false);
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
