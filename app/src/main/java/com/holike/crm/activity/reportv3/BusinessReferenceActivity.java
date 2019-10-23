package com.holike.crm.activity.reportv3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.fragment.reportv3.business.ColorsPerformanceFragment;
import com.holike.crm.fragment.reportv3.business.HomeDeliveryFragment;
import com.holike.crm.fragment.reportv3.business.SeriesPerformanceFragment;

/**
 * Created by gallop on 2019/10/22.
 * Copyright holike possess 2019.
 * 生意内参
 */
public class BusinessReferenceActivity extends BaseActivity {

    /*传入类型，决定跳转的fragment*/
    public static final String TYPE_HOME_DELIVERY = "home-delivery";  //宅配业绩
    public static final String TYPE_SERIES = "series"; //系列
    public static final String TYPE_COLOR = "color"; //花色

    public static void start(BaseActivity<?, ?> activity, String type) {
        Intent intent = new Intent(activity, BusinessReferenceActivity.class);
        intent.setType(type);
        activity.openActivity(intent);
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
        String type = getIntent().getType();
        if (TextUtils.equals(type, TYPE_HOME_DELIVERY)) {
            startFragment(new HomeDeliveryFragment());
        } else if (TextUtils.equals(type, TYPE_SERIES)) {
            startFragment(new SeriesPerformanceFragment());
        } else if (TextUtils.equals(type, TYPE_COLOR)) {
            startFragment(new ColorsPerformanceFragment());
        }
    }
}
