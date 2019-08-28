package com.holike.crm.activity.homepage2;

import android.content.Intent;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.homepagev2.BossMonthDataFragment;
import com.holike.crm.fragment.homepagev2.FinanceMonthDataFragment;
import com.holike.crm.fragment.homepagev2.InstallManagerMonthDataFragment;

/**
 * Created by gallop on 2019/8/12.
 * Copyright holike possess 2019.
 * 本月数据
 */
public class MonthDataActivity extends MyFragmentActivity {
    public static final String TYPE_BOSS = "boss";
    public static final String TYPE_INSTALL_MANAGER = "install_manager";
    public static final String TYPE_FINANCE = "finance";

    public static void start(BaseActivity<?, ?> activity, String type) {
        Intent intent = new Intent(activity, MonthDataActivity.class);
        intent.setType(type);
        activity.openActivity(intent);
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_month_data;
    }

    @Override
    protected void init() {
        String type = getIntent().getType();
        if (TextUtils.equals(type, TYPE_BOSS)) {   //老板、店长本月数据
            startFragment(new BossMonthDataFragment());
        } else if (TextUtils.equals(type, TYPE_INSTALL_MANAGER)) { //安装经理本月数据
            startFragment(new InstallManagerMonthDataFragment());
        } else if (TextUtils.equals(type, TYPE_FINANCE)) { //财务本月数据
            startFragment(new FinanceMonthDataFragment());
        }
    }
}
