package com.lefuyun.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.lefuyun.R;
import com.lefuyun.util.LogUtil;

public class ConfirmDialogFragment extends DialogFragment implements OnClickListener{
	
	private String mTitle;
	private String message;
	private OnClickListener mListener;
	public boolean flag=true;
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();  
		alertDialog.show();  
		Window window = alertDialog.getWindow();  
		window.setContentView(R.layout.dialog_fragment_confirm);
		TextView tv_title = (TextView) window.findViewById(R.id.title_confirm_dialog_fragment);  
		tv_title.setText(mTitle);  
		TextView tv_message = (TextView) window.findViewById(R.id.message_confirm_dialog_fragment);  
		tv_message.setText(message); 
		TextView negative = (TextView) window.findViewById(R.id.negative_confirm_dialog_fragment);  
		LogUtil.i("negative", negative+"");
		TextView positive = (TextView) window.findViewById(R.id.positive_confirm_dialog_fragment);
		negative.setOnClickListener(this);
		positive.setOnClickListener(this);
		if(!flag){
			negative.setVisibility(View.GONE);
			tv_title.setVisibility(View.GONE);
			this.setCancelable(false);
		}
        return alertDialog;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.negative_confirm_dialog_fragment:
			// 取消按钮点击
			if(mListener != null) {
				mListener.onNegativeClick(v);
			}
			dismiss();
			break;
		case R.id.positive_confirm_dialog_fragment:
			// 确定按钮点击
			if(mListener != null) {
				mListener.onPositiveClick(v);
			}
			dismiss();
			break;

		default:
			break;
		}
		
	}
	public void setOnClickListener(@NonNull OnClickListener l) {
		mListener = l;
	}
	/**
	 * 点击事件接口
	 */
	public interface OnClickListener {
		/**
		 * 取消按钮点击事件
		 * @param v
		 */
		public void onNegativeClick(View v);
		/**
		 * 确认按钮点击事件
		 * @param v
		 */
		public void onPositiveClick(View v);
	}
}
