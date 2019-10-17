package com.holike.crm.presenter.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.model.fragment.EmployeeModel;
import com.holike.crm.view.activity.EmployeeResetPasswordView;

public class EmployeeResetPasswordPresenter extends BasePresenter<EmployeeResetPasswordView, EmployeeModel> {

    public void watcher(EditText mPwdEditText, EditText mPwdTwiceEditText,
                        final ImageView mClearImageView,
                        final ImageView mClearImageView2,
                        final TextView mSaveTextView) {
        mPwdEditText.addTextChangedListener(new FillWatcher(mPwdTwiceEditText, mClearImageView, mSaveTextView));
        mPwdTwiceEditText.addTextChangedListener(new FillWatcher(mPwdEditText, mClearImageView2, mSaveTextView));
        mClearImageView.setOnClickListener(v -> mPwdEditText.setText(""));
        mClearImageView2.setOnClickListener(v -> mPwdTwiceEditText.setText(""));
    }

    private class FillWatcher implements TextWatcher {

        private EditText target;
        private ImageView mClearImageView;
        private TextView mSaveTextView;

        private FillWatcher(EditText target, ImageView mClearImageView, TextView mSaveTextView) {
            this.target = target;
            this.mClearImageView = mClearImageView;
            this.mSaveTextView = mSaveTextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString())) {
                mClearImageView.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(target.getText().toString())) {
                    mSaveTextView.setEnabled(true);
                } else {
                    mSaveTextView.setEnabled(false);
                }
            } else {
                mClearImageView.setVisibility(View.GONE);
                mSaveTextView.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void onShowPassword(EditText editText, ImageView mEyeImageView) {
        mEyeImageView.setOnClickListener(v -> {
            if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mEyeImageView.setImageResource(R.drawable.eye_open);
            } else {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mEyeImageView.setImageResource(R.drawable.eye_close);
            }
            editText.setSelection(editText.getText().length());
        });
    }

    public void onResetPassword(String userId, String newPassword) {
        if (getModel() != null) {
            getModel().resetPassword(userId, newPassword, new EmployeeModel.OnResetPasswordCallback() {
                @Override
                public void onResetStart() {
                    if (getView() != null) getView().onShowLoading();
                }

                @Override
                public void onSuccess() {
                    if (getView() != null) getView().onResetSuccess();
                }

                @Override
                public void onFailure(String message) {
                    if (getView() != null) getView().onResetFailure(message);
                }

                @Override
                public void onResetComplete() {
                    if (getView() != null) getView().onHideLoading();
                }
            });
        }
    }
}
