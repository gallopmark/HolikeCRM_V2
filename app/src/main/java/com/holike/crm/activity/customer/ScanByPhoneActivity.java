package com.holike.crm.activity.customer;

import android.content.Intent;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.base.SystemTintHelper;
import com.holike.crm.fragment.customer.ScanByPhoneFragment;
import com.holike.crm.model.event.EventCurrentResult;

/**
 * Created by gallop on 2019/10/23.
 * Copyright holike possess 2019.
 * 手机直接扫描
 */
public class ScanByPhoneActivity extends MyFragmentActivity {

    public static void open(BaseActivity<?, ?> activity, EventCurrentResult result) {
        if (result != null) {
            IntentValue.getInstance().put("currentResult", result);
        }
        activity.openActivity(new Intent(activity, ScanByPhoneActivity.class));
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_common;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        startFragment(new ScanByPhoneFragment());
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }
}
