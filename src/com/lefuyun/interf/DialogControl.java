package com.lefuyun.interf;

import com.lefuyun.widget.WaitDialog;

public interface DialogControl {

	public abstract void hideWaitDialog();

	public abstract WaitDialog showWaitDialog();

	public abstract WaitDialog showWaitDialog(int resid);

	public abstract WaitDialog showWaitDialog(String text);
}
