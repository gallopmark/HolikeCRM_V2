package com.holike.crm.activity.report;

import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.fragment.main.ReportV3Fragment;

/**
 * Created by pony on 2019/11/20.
 * Version v3.0 app报表
 */
public class TestActivity extends BaseActivity {
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
        setStatusBarColor(R.color.colorAccent);
        startFragment(new ReportV3Fragment());
    }
}
