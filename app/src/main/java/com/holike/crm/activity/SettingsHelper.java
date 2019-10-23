package com.holike.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.widget.SwitchCompat;

import com.holike.crm.R;
import com.holike.crm.base.ActivityHelper;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;

class SettingsHelper extends ActivityHelper {

    private Callback mCallback;
    private String mId;
    private String mParam2;
    private SwitchCompat mSwitchButton;
    private boolean mOldCheckStatus, mStatusChanged; //statusChanged 状态是否发生了改变（即开关按钮状态是否发生了改变）

    SettingsHelper(BaseActivity<?, ?> activity, Callback callback) {
        super(activity);
        this.mCallback = callback;
        initIntentValue(activity.getIntent());
        initView();
    }

    private void initIntentValue(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mId = intent.getStringExtra("id");
            mParam2 = intent.getStringExtra("param2");
        }
    }

    private void initView() {
        mSwitchButton = obtainView(R.id.switchButton);
        //-1没有设置过规则或1已经设置 默认为打开状态
        mOldCheckStatus = TextUtils.equals(mParam2, "-1") || TextUtils.equals(mParam2, "1") || TextUtils.isEmpty(mId);
        mSwitchButton.setChecked(mOldCheckStatus);
        mSwitchButton.setOnClickListener(view -> {
            mStatusChanged = true;
            mCallback.openOrClose(mId, mSwitchButton.isChecked() ? "1" : "0");
        });
    }

    /*设置成功，接口返回id*/
    void onSettingSuccess(String id, String message) {
        mActivity.showLongToast(message);
        this.mId = id;
        mOldCheckStatus = mSwitchButton.isChecked();
        mParam2 = mSwitchButton.isChecked() ? "1" : "0";
    }

    //设置失败
    void onSettingsFailure(String failReason) {
        mActivity.showLongToast(failReason);
        mSwitchButton.setChecked(mOldCheckStatus);
        mParam2 = mSwitchButton.isChecked() ? "1" : "0";
    }

    /*把结果返回到上一级页面（考虑到上一级页面是多层Fragment，所以不用setResult）*/
    void setResult() {
        if (mStatusChanged) {
            MessageEvent event = new MessageEvent();
            Bundle bundle = new Bundle();
            bundle.putString("id", mId);
            bundle.putString("param2", mParam2);
            event.setArguments(bundle);
            RxBus.getInstance().post(event);
        }
    }

    interface Callback {
        void openOrClose(String id, String param2);
    }
}
