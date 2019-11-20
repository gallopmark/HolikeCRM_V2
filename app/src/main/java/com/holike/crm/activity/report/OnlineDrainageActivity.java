package com.holike.crm.activity.report;


import android.os.Bundle;

import androidx.annotation.NonNull;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.OnlineDrainageBean;
import com.holike.crm.fragment.report.OnlineDrainageFragment;

/**
 * Created by pony on 2019/11/2.
 * Version v3.0 app报表
 * 线上引流报表
 */
public class OnlineDrainageActivity extends BaseActivity{

    public static void start(BaseActivity<?, ?> activity, String title) {
        start(activity, OnlineDrainageActivity.class, title);
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
        startFragment(new OnlineDrainageFragment());
    }
}
