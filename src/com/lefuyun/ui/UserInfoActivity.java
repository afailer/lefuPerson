package com.lefuyun.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lefuyun.AppContext;
import com.lefuyun.R;
import com.lefuyun.api.common.RequestCallback;
import com.lefuyun.api.http.exception.ApiHttpException;
import com.lefuyun.api.remote.ImageLoader;
import com.lefuyun.api.remote.LefuApi;
import com.lefuyun.base.BaseActivity;
import com.lefuyun.bean.Portrait;
import com.lefuyun.bean.User;
import com.lefuyun.util.ImageUtils;
import com.lefuyun.util.StringUtils;
import com.lefuyun.util.TLog;
import com.lefuyun.widget.CircleImageView;
import com.lefuyun.widget.dialog.ActionSheetDialog;
import com.lefuyun.widget.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.lefuyun.widget.dialog.ActionSheetDialog.SheetItemColor;
import com.lefuyun.widget.pickerview.TimePickerView;
import com.lefuyun.widget.pickerview.TimePickerView.OnTimeSelectListener;
import com.lefuyun.widget.togglebutton.ToggleButton;
import com.lefuyun.widget.togglebutton.ToggleButton.OnToggleChanged;

public class UserInfoActivity extends BaseActivity {
	
	private TextView mMaleView, mFemaleView;
	private ToggleButton mToggleButton;
	
	private CircleImageView mUserImg;
	
	private EditText mUserNameView;
	
	// 日期
	private TextView mDate;
	// 修改按钮
	private TextView mUpdateBtn;
	
	private User mUser;

	private boolean isAnimator;
	
	// 时间选择控件
	private TimePickerView mTimePickerView;
	// 用户性别,默认是男为14,女是15
	private int gender = 14;
	
	
	private final int PIC_FROM_CAMERA = 1;
	private final int PIC_FROM_LOCALPHOTO = 0;
	private final int PIC_FROM_CROP = 2;
	
	private Uri origUri;
	@SuppressWarnings("unused")
	private String theLarge;
	private String protraitPath;
	private File protraitFile;
	private Uri cropUri;
	private Bitmap protraitBitmap;
	// 裁剪图片的大小
	private final static int CROP = 200;
	// 是否有数据更新
	private boolean isUpdate;
	
	private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/lefuyun/Portrait/";

	@Override
	protected int getLayoutId() {
		return R.layout.activity_user_info;
	}

	@Override
	protected void initView() {
		setActionBarTitle("个人资料");
		mMaleView = (TextView) findViewById(R.id.male_user_info_activity);
		mFemaleView = (TextView) findViewById(R.id.female_user_info_activity);
		mToggleButton = (ToggleButton) findViewById(R.id.togglebutton_user_info_activity);
		mToggleButton.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(boolean on) {
				if(on) {
					// 选择女
					gender = 15;
					mMaleView.setVisibility(View.INVISIBLE);
					mFemaleView.setVisibility(View.VISIBLE);
				}else {
					// 选择男
					gender = 14;
					mMaleView.setVisibility(View.VISIBLE);
					mFemaleView.setVisibility(View.INVISIBLE);
				}
				
			}
		});
		
		mUserNameView = (EditText) findViewById(R.id.name_user_info_activity);
		
		mDate = (TextView) findViewById(R.id.date_user_info_activity);
		mDate.setOnClickListener(this);
		// 修改按钮
		mUpdateBtn = (TextView) findViewById(R.id.update_user_info_activity);
		mUpdateBtn.setOnClickListener(this);
		// 用户头像
		mUserImg = (CircleImageView) findViewById(R.id.img_user_info_activity);
		mUserImg.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		isAnimator = intent.getBooleanExtra("isAnimator", false);
		mUser = (User) intent.getSerializableExtra("user");
		// 时间选择器
		mTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		// 控制时间范围
		mTimePickerView.setStartYear(1910);
		mTimePickerView.setTime(new Date());
		mTimePickerView.setCyclic(false);
		mTimePickerView.setCancelable(true);
		mTimePickerView.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				String dateString = StringUtils.getDateString2(date);
				mDate.setText(dateString);
			}
		});
		
		if(mUser.getGender() == 14) {
			mToggleButton.setToggleOff();
			mMaleView.setVisibility(View.VISIBLE);
			mFemaleView.setVisibility(View.INVISIBLE);
			gender = 14;
		}else {
			mToggleButton.setToggleOn();
			mMaleView.setVisibility(View.INVISIBLE);
			mFemaleView.setVisibility(View.VISIBLE);
			gender = 15;
		}
		ImageLoader.loadImg(mUser.getIcon(), mUserImg);
		mUserNameView.setText(mUser.getUser_name());
		mUserNameView.setSelection(mUserNameView.length());
		mDate.setText(mUser.getBirthday_dt() > 0 ? 
				StringUtils.getFormatData(mUser.getBirthday_dt(), "yyyy-MM-dd") : "");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_user_info_activity:
			mTimePickerView.setTitle("选择出生日期");
			mTimePickerView.show();
			break;
		case R.id.update_user_info_activity:
			// 更新用户信息
			updateUserInfo(false, null);
			break;
		case R.id.img_user_info_activity:
			// 修改用户头像
			getUserImg();
			break;

		default:
			break;
		}
	}
	/**
	 * 更新用户信息
	 * @param flag false : 更新用户信息    ; true : 只更新用户头像
	 * @param icon
	 */
	private void updateUserInfo(final boolean flag, String icon) {
		if(flag) {
			mUser.setIcon(icon);
		}else {
			mUser.setGender(gender);
			mUser.setUser_name(mUserNameView.getText().toString().trim());
			mUser.setBirthday_dt(StringUtils.getFormatTime(mDate.getText().toString(), "yyyy-MM-dd"));
		}
		LefuApi.updateUserInfo(mUser, flag, new RequestCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				TLog.log("用户信息修改成功 == " + result);
				isUpdate = true;
				if(flag) {
					showToast("头像上传成功");
					hideWaitDialog();
				}else {
					showToast("修改成功");
					finish();
				}
			}
			
			@Override
			public void onFailure(ApiHttpException e) {
				TLog.log("用户信息修改失败 == " + e.getMessage());
				showToast("信息修改失败");
			}
		});
	}
	/**
	 * 获取用户选择的头像
	 */
	private void getUserImg() {
		new ActionSheetDialog(this)
		.builder()
		.setTitle("选项")
		.setCancelable(true)
		.setCanceledOnTouchOutside(true)
		.addSheetItem("拍摄照片", SheetItemColor.Red,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						doHandlerPhoto(PIC_FROM_CAMERA);
					}
				})
		.addSheetItem("选取本地", SheetItemColor.Red,
		new OnSheetItemClickListener() {
			@Override
			public void onClick(int which) {
				doHandlerPhoto(PIC_FROM_LOCALPHOTO);
			}
		}).show();
	}
	
	@SuppressLint("SimpleDateFormat")
	private void doHandlerPhoto(int type) {
		// 判断当前获取图片的类型
		if (type == PIC_FROM_LOCALPHOTO) {
			// 从本地获取图片
			Intent intent;
	        if (Build.VERSION.SDK_INT < 19) {
	            intent = new Intent();
	            intent.setAction(Intent.ACTION_GET_CONTENT);
	            intent.setType("image/*");
	            startActivityForResult(Intent.createChooser(intent, "选择图片"),
	            		PIC_FROM_LOCALPHOTO);
	        } else {
	            intent = new Intent(Intent.ACTION_PICK,
	                    Images.Media.EXTERNAL_CONTENT_URI);
	            intent.setType("image/*");
	            startActivityForResult(Intent.createChooser(intent, "选择图片"),
	            		PIC_FROM_LOCALPHOTO);
	        }
		} else {
			Intent intent;
	        // 判断是否挂载了SD卡
	        String savePath = "";
	        String storageState = Environment.getExternalStorageState();
	        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
	            savePath = Environment.getExternalStorageDirectory()
	                    .getAbsolutePath() + "/lefuyun/Camera/";
	            File savedir = new File(savePath);
	            if (!savedir.exists()) {
	                savedir.mkdirs();
	            }
	        }

	        // 没有挂载SD卡，无法保存文件
	        if (StringUtils.isEmpty(savePath)) {
	        	showToast("无法保存照片，请检查SD卡是否挂载");
	            return;
	        }

	        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
	                .format(new Date());
	        String fileName = "lefu_" + timeStamp + ".jpg";// 照片命名
	        File out = new File(savePath, fileName);
	        Uri uri = Uri.fromFile(out);
	        origUri = uri;

	        theLarge = savePath + fileName;// 该照片的绝对路径

	        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	        startActivityForResult(intent, PIC_FROM_CAMERA);
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
            return;
		switch (requestCode) {
		case PIC_FROM_CAMERA: 
			// 拍照
			startActionCrop(origUri);
			break;
		case PIC_FROM_LOCALPHOTO:
			// 本地获取的图片
			startActionCrop(data.getData());
			break;
		case PIC_FROM_CROP:
			// 裁剪后的图片
			uploadNewPhoto();
			break;
		}
	}
	
	/**
     * 上传新照片
     */
    private void uploadNewPhoto() {
        showWaitDialog("正在上传头像...");

        // 获取头像缩略图
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, CROP, CROP);
        } else {
            showToast("图像不存在，上传失败");
        }
        if (protraitBitmap != null) {
        	mUserImg.setImageBitmap(protraitBitmap);
        	LefuApi.updatePortrait(protraitFile, new RequestCallback<Portrait>() {
				
				@Override
				public void onSuccess(Portrait result) {
					TLog.log("上传图片成功 ..." + result);
					updateUserInfo(true, result.getFileUrl());
				}
				
				@Override
				public void onFailure(ApiHttpException e) {
					showToast("更换头像失败");
					hideWaitDialog();
				}
			});
        }
    }
	
	/**
     * 拍照后裁剪
     * 
     * @param data
     *            原始图片
     * @param output
     *            裁剪后图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent, PIC_FROM_CROP);
    }
    
    /**
     *  裁剪头像的绝对路径
     * @param uri
     * @return
     */
    @SuppressLint("SimpleDateFormat")
	private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
        	showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "lefu" + mUser.getUser_id() + "_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }
    
    /**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	private String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}
	
	@Override
	protected boolean hasActionBar() {
		return true;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	public void finish() {
		if(isUpdate) {
			Intent data = new Intent();
			data.putExtra("user", mUser);
			setResult(AppContext.UPDATE_USERINFO_SUCCESS, data);
		}
		super.finish();
		// 设置当前activity关闭时的动画
		if(isAnimator) {
			overridePendingTransition(R.animator.slide_left_in, R.animator.slide_right_out);
		}
	}

}
