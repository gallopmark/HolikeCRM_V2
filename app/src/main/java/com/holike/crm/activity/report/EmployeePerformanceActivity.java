package com.holike.crm.activity.report;

import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.employee.EmployeePerformanceFragment;

/**
 * Created by pony on 2019/10/30.
 * Version v3.0 app报表
 * 员工绩效
 */
public class EmployeePerformanceActivity extends MyFragmentActivity {
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
        startFragment(new EmployeePerformanceFragment());
    }
}
