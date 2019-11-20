package com.holike.crm.activity.report;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.fragment.report.business.ColorsPerformanceFragment;
import com.holike.crm.fragment.report.business.HomeDeliveryFragment;
import com.holike.crm.fragment.report.business.SeriesPerformanceFragment;

/**
 * Created by pony on 2019/10/22.
 * Copyright holike possess 2019.
 * 生意内参 v3.0报表- 经销商-系列、花色、宅配业绩
 */
public class DealerMultiPerformanceActivity extends BaseActivity {

    /*传入类型，决定跳转的fragment*/

    /*打开系列报表*/
    public static void renderSeries(BaseActivity<?, ?> activity) {
        start(activity, "1");
    }

    /*打开花色报表*/
    public static void renderColors(BaseActivity<?, ?> activity) {
        start(activity, "2");
    }

    /*打开宅配报表*/
    public static void renderHomeDelivery(BaseActivity<?, ?> activity) {
        start(activity, "3");
    }

    private static void start(BaseActivity<?, ?> activity, String dimensionOf) {
        Intent intent = new Intent(activity, DealerMultiPerformanceActivity.class);
        intent.putExtra("dimensionOf", dimensionOf);
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
        String dimensionOf = getIntent().getStringExtra("dimensionOf");
        if (TextUtils.equals(dimensionOf, "1")) {
            startFragment(new SeriesPerformanceFragment());
        } else if (TextUtils.equals(dimensionOf, "2")) {
            startFragment(new ColorsPerformanceFragment());
        } else if (TextUtils.equals(dimensionOf, "3")) {
            startFragment(new HomeDeliveryFragment());
        }
    }
}
