package com.lefuyun.base;

import com.lefuyun.util.StringUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 登录页面和注册页面的基类
 * 能够进行手机号码的格式化显示 如: 182 6666 6666
 */
public abstract class BaseCheckPhoneActivity extends BaseActivity implements TextWatcher{
	

	/**
	 * 获取填写手机号的控件
	 * @return
	 */
	protected EditText getPhoneEditText() {
		return null;
	}
	/**
	 * 检验手机号是否为空,或者手机号是否为11位
	 * @param phone
	 * @return
	 */
	protected boolean checkPhoneLegal(String phone) {
		if(StringUtils.isEmpty(phone)) {
			showToast("手机号不能为空");
			return false;
		}
		if(phone.length() != 11) {
			showToast("手机号格式不正确");
			return false;
		}
		return true;
	}
	
	/**
	 * 格式化显示手机号 如: 182 6666 6666
	 */
	@Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s == null || s.length() == 0) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            if(getPhoneEditText() != null) {
            	getPhoneEditText().setText(sb.toString());
                getPhoneEditText().setSelection(index);
            }
        }
    }

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}

	@Override
	public void afterTextChanged(Editable s) {}
}
