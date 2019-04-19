package com.lefuyun.util;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.text.Html;

import com.lefuyun.R;
import com.lefuyun.widget.WaitDialog;
import com.lefuyun.widget.dialog.ConfirmDialogFragment;

public class DialogHelper {
	
	public static WaitDialog getWaitDialog(Activity activity, int message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog_waiting);
			dialog.setMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialog;
	}

	public static WaitDialog getWaitDialog(Activity activity, String message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog_waiting);
			dialog.setMessage(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dialog;
	}
	
	public static ConfirmDialogFragment getConfirmDialog(FragmentActivity activity, String title, 
			String message, ConfirmDialogFragment.OnClickListener l) {
		ConfirmDialogFragment cdf = new ConfirmDialogFragment();
		cdf.setTitle(title);
		cdf.setMessage(Html.fromHtml(message).toString());
		cdf.setOnClickListener(l);
		cdf.show(activity.getSupportFragmentManager(), "");
		return cdf;
	}
	
}
