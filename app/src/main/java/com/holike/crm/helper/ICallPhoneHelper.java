package com.holike.crm.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.base.OnRequestPermissionsCallback;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.AppUtils;

/**
 * Created by gallop on 2019/8/30.
 * Copyright holike possess 2019.
 * 拨打客户电话帮助类
 */
public class ICallPhoneHelper implements OnRequestPermissionsCallback {

    private BaseActivity<?, ?> mActivity;
    private String mPersonalId, mHouseId, mPhoneNumber;

    private ICallPhoneHelper(BaseActivity<?, ?> activity) {
        this.mActivity = activity;
    }

    public static ICallPhoneHelper with(BaseActivity<?, ?> activity) {
        return new ICallPhoneHelper(activity);
    }

    public void requestCallPhone(String personalId, String houseId, String phoneNumber) {
        onPreCallPhone(phoneNumber, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            onCallPhone(personalId, houseId, phoneNumber);
        });
    }

    private void onPreCallPhone(String phoneNumber, DialogInterface.OnClickListener onConfirmListener) {
        new MaterialDialog.Builder(mActivity)
                .title(R.string.tips_call)
                .message(phoneNumber)
                .negativeButton(R.string.cancel, null)
                .positiveButton(R.string.call, onConfirmListener).show();
    }

    private void onCallPhone(String personalId, String houseId, String phoneNumber) {
        boolean isCallPermissionGranted = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        if (isCallPermissionGranted) {
            onActionCall(personalId, houseId, phoneNumber);
        } else {
            mPersonalId = personalId;
            mHouseId = houseId;
            mPhoneNumber = phoneNumber;
            /*由于要查看通话记录，所以要申请查看通话记录的权限*/
            mActivity.requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG}, this);
//            mActivity.requestPermission(Manifest.permission.CALL_PHONE, this);
        }
    }

    @SuppressLint("MissingPermission")
    private void onActionCall(String personalId, String houseId, String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            mActivity.startActivity(intent);
            MessageEvent event = new MessageEvent();
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", phoneNumber);
            bundle.putString(CustomerValue.PERSONAL_ID, personalId);
            bundle.putString(CustomerValue.HOUSE_ID, houseId);
            event.setArguments(bundle);
            RxBus.getInstance().post(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onActionCall(mPersonalId, mHouseId, mPhoneNumber);
        }
    }

    @Override
    public void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit) {
        if (permissions.length > 0 && ContextCompat.checkSelfPermission(mActivity, permissions[0])
                != PackageManager.PERMISSION_GRANTED && isProhibit) {
            onCallPermissionDenied();
        }
    }

    /*用户点击禁止通话权限“不再提示”*/
    private void onCallPermissionDenied() {
        new MaterialDialog.Builder(mActivity)
                .title(R.string.dialog_title_default)
                .message(R.string.tips_dismiss_callPhone)
                .negativeButton(R.string.cancel, null)
                .positiveButton(R.string.confirm, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    AppUtils.openSettings(mActivity);
                }).show();
    }
}
