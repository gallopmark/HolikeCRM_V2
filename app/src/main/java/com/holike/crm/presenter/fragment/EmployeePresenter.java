package com.holike.crm.presenter.fragment;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.holike.crm.R;
import com.holike.crm.base.BaseModel;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.util.CheckUtils;
import com.holike.crm.util.KeyBoardUtil;
import com.holike.crm.view.fragment.EmployeeView;

public class EmployeePresenter extends BasePresenter<EmployeeView, BaseModel> {

    public void showStatusDialog(Context context, String checkText, OnStatusSelectListener listener) {
        if (context instanceof Activity) {
            KeyBoardUtil.hideSoftInput((Activity) context);
        }
        int checkItem = 0;
        String[] items = context.getResources().getStringArray(R.array.select_status);
        for (int i = 0; i < items.length; i++) {
            if (TextUtils.equals(items[i], checkText)) {
                checkItem = i;
                break;
            }
        }
        new AlertDialog.Builder(context).setSingleChoiceItems(items, checkItem, (dialogInterface, i) -> {
            if (listener != null) {
                listener.onStatusSelected(items[i]);
            }
            dialogInterface.dismiss();
        }).create().show();
    }

    public void showSexDialog(Context context, String checkText) {
        if (context instanceof Activity) {
            KeyBoardUtil.hideSoftInput((Activity) context);
        }
        int checkItem = 0;
        String[] items = context.getResources().getStringArray(R.array.select_sex);
        for (int i = 0; i < items.length; i++) {
            if (TextUtils.equals(items[i], checkText)) {
                checkItem = i;
                break;
            }
        }
        new AlertDialog.Builder(context).setSingleChoiceItems(items, checkItem, (dialogInterface, i) -> {
            if (getView() != null) {
                getView().onSexSelected(items[i]);
            }
            dialogInterface.dismiss();
        }).create().show();
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

    /*必填项是否所有已填并且（密码和确认密码一致）*/
    public boolean isContentFill(int viewId, String nameText, String phoneText, String pwdText, String pwdTwiceText) {
        boolean isFillName = !TextUtils.isEmpty(nameText);
        boolean isFillPhone = !TextUtils.isEmpty(phoneText);
        boolean isFillPws = !TextUtils.isEmpty(pwdText);
        boolean isFillTwicePws = !TextUtils.isEmpty(pwdTwiceText);
        if (viewId == R.id.mNameEditText) {
            return isFillPhone && CheckUtils.isMobile(phoneText) && isFillPws && isFillTwicePws;
        } else if (viewId == R.id.mPhoneEditText) {
            return isFillName && CheckUtils.isMobile(phoneText) && isFillPws && isFillTwicePws;
        } else if (viewId == R.id.mPwdEditText) {
            return isFillName && CheckUtils.isMobile(phoneText) && isFillPhone && isFillTwicePws; //&& TextUtils.equals(pwdText, pwdTwiceText)
        } else {
            return isFillName && CheckUtils.isMobile(phoneText) && isFillPhone && isFillPws; //&& TextUtils.equals(pwdText, pwdTwiceText)
        }
    }

    public interface OnStatusSelectListener {
        void onStatusSelected(String status);
    }
}
