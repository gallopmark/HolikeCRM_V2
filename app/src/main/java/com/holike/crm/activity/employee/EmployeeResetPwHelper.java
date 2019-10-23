package com.holike.crm.activity.employee;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.SimpleTextWatcher;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.KeyBoardUtil;


/**
 * Created by gallop on 2019/9/19.
 * Copyright holike possess 2019.
 * 员工重置密码帮助类
 */
class EmployeeResetPwHelper extends ActivityHelper {

    private String mUserId; //员工id，getIntent()获取
    private Callback mCallback;
    private EditText mNewPwEditText;
    private ImageView mClearImageView;
    private EditText mConfirmPwEditText;
    private ImageView mClearImageView2;
    private TextView mSaveTextView;

    EmployeeResetPwHelper(BaseActivity<?, ?> activity, Callback callback) {
        super(activity);
        this.mCallback = callback;
        Intent intent = mActivity.getIntent();
        mUserId = intent.getExtras() == null ? "" : intent.getExtras().getString("userId", "");
        initView();
    }

    private void initView() {
        mNewPwEditText = obtainView(R.id.et_new_pw);
        mClearImageView = obtainView(R.id.iv_clear_pw);
        ImageView eyeImageView = obtainView(R.id.iv_eye_pw);
        mConfirmPwEditText = obtainView(R.id.et_confirm_pw);
        mClearImageView2 = obtainView(R.id.iv_clear_confirm_pw);
        ImageView eyeImageView2 = obtainView(R.id.iv_eye_confirm_pw);
        mSaveTextView = obtainView(R.id.tvSave);
        mSaveTextView.setEnabled(false);
        addTextWatcher();
        switchPassword(mNewPwEditText, eyeImageView);
        switchPassword(mConfirmPwEditText, eyeImageView2);
        mClearImageView.setOnClickListener(view -> clearText(mNewPwEditText));
        mClearImageView2.setOnClickListener(view -> clearText(mConfirmPwEditText));
        mSaveTextView.setOnClickListener(view -> onSave());
    }

    private void addTextWatcher() {
        mNewPwEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (TextUtils.isEmpty(cs)) {
                    mSaveTextView.setEnabled(false);
                    mClearImageView.setVisibility(View.GONE);
                } else {
                    mClearImageView.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(getText(mConfirmPwEditText))) {
                        mSaveTextView.setEnabled(true);
                    }
                }
            }
        });
        mConfirmPwEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (TextUtils.isEmpty(cs)) {
                    mSaveTextView.setEnabled(false);
                    mClearImageView2.setVisibility(View.GONE);
                } else {
                    mClearImageView2.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(getText(mNewPwEditText))) {
                        mSaveTextView.setEnabled(true);
                    } else {
                        mSaveTextView.setEnabled(false);
                    }
                }
            }
        });
    }

    /*控制密码可见性*/
    private void switchPassword(EditText editText, ImageView eyeImageView) {
        eyeImageView.setOnClickListener(v -> {
            if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                eyeImageView.setImageResource(R.drawable.eye_open);
            } else {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eyeImageView.setImageResource(R.drawable.eye_close);
            }
            editText.setSelection(editText.getText().length());
        });
    }

    /*点击保存后*/
    private void onSave() {
        KeyBoardUtil.hideSoftInput(mActivity);
        String password = getText(mNewPwEditText);
        String confirmPassword = getText(mConfirmPwEditText);
        if (!CheckUtils.isPassword(password)) { //密码是否与指定正则匹配
            mActivity.showShortToast(R.string.hint_password_regex_tips);
        } else {
            if (!TextUtils.equals(password, confirmPassword)) {  //两次输入的密码不一致
                mActivity.showShortToast(R.string.reset_password_error_tips);
            } else {
                mCallback.onSave(mUserId, password);
            }
        }
    }

    interface Callback {
        void onSave(String userId, String newPassword);
    }
}
