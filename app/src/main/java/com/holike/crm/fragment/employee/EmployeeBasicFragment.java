package com.holike.crm.fragment.employee;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.util.CheckUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Created by pony on 2019/8/6.
 * Copyright holike possess 2019.
 * 员工基础信息 新建员工
 */
public class EmployeeBasicFragment extends MyFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.mNameEditText)
    EditText mNameEditText;
    @BindView(R.id.mPhoneEditText)
    EditText mPhoneEditText;
    @BindView(R.id.cb_man)
    CheckBox mManCheckBox;
    @BindView(R.id.cb_women)
    CheckBox mWomenCheckbox;
    @BindView(R.id.mPwdEditText)
    EditText mPwdEditText;
    @BindView(R.id.mPwdTwiceEditText)
    EditText mPwdTwiceEditText;
    private String mGender; //性别

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_employee_edit_basic2;
    }

    @Override
    protected void init() {
        mManCheckBox.setOnCheckedChangeListener(this);
        mWomenCheckbox.setOnCheckedChangeListener(this);
        mPwdEditText.addTextChangedListener(new PasswordWatcher(mPwdEditText));
        mPwdTwiceEditText.addTextChangedListener(new PasswordWatcher(mPwdTwiceEditText));
    }

    class PasswordWatcher extends SimpleTextWatcher {
        EditText mEditText;

        PasswordWatcher(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String editable = mEditText.getText().toString();
            String regex = "[^a-zA-Z0-9]";  //只能输入字母、数字
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(editable);
            String source = m.replaceAll("").trim();    //删掉不是字母或数字的字符
            if (!editable.equals(source)) {
                mEditText.setText(source);  //设置EditText的字符
                mEditText.setSelection(source.length()); //因为删除了字符，要重写设置新的光标所在位置
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
        if (cb.getId() == R.id.cb_man) {
            if (isChecked) {
                mGender = "2";
                mWomenCheckbox.setChecked(false);
            } else {
                mGender = "3";
            }
        } else {
            if (isChecked) {
                mGender = "1";
                mManCheckBox.setChecked(false);
            } else {
                mGender = "3";
            }
        }
    }

    /*是否可以走下一部流程*/
    public boolean canGoNext() {
        String userName = mNameEditText.getText().toString();
        if (TextUtils.isEmpty(userName.trim())) {
            showShortToast(mNameEditText.getHint());
            return false;
        } else {
            String phone = mPhoneEditText.getText().toString();
//            if (TextUtils.isEmpty(phone)) {
//                showShortToast(mPhoneEditText.getHint());
//                return false;
//            } else {
            if (!TextUtils.isEmpty(phone) && !CheckUtils.isMobile(phone)) {
                showShortToast(mContext.getString(R.string.please_input_correct_phone));
                return false;
            } else {
                String password = mPwdEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showShortToast(mContext.getString(R.string.employee_twice_password_tips));
                    return false;
                } else {
                    if (!CheckUtils.isPassword(password)) {
                        showShortToast(mContext.getString(R.string.hint_password_regex_tips));
                        return false;
                    } else {
                        String password2 = mPwdTwiceEditText.getText().toString();
                        if (TextUtils.isEmpty(password2)) {
                            showShortToast(mContext.getString(R.string.employee_twice_password_empty_input));
                            return false;
                        } else {
                            if (!TextUtils.equals(password, password2)) {
                                showShortToast(mContext.getString(R.string.inconsistent_password_entered_twice));
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }
    }

    public Map<String, String> obtain() {
        Map<String, String> params = new HashMap<>();
        params.put("userName", mNameEditText.getText().toString().trim());
        params.put("phone", mPhoneEditText.getText().toString());
        params.put("gender", mGender);
        params.put("password", mPwdEditText.getText().toString());
        return params;
    }
}
