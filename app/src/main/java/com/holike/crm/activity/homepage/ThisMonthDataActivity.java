package com.holike.crm.activity.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.monthdata.BossMonthDataFragment;
import com.holike.crm.fragment.monthdata.FinanceMonthDataFragment;
import com.holike.crm.fragment.monthdata.InstallManagerMonthDataFragment;

/**
 * Created by pony on 2019/8/12.
 * Copyright holike possess 2019.
 * 本月数据
 */
public class ThisMonthDataActivity extends MyFragmentActivity {
    public static final String TYPE_BOSS = "boss"; //老板
    public static final String TYPE_INSTALL_MANAGER = "install-manager"; //安装经理
    public static final String TYPE_FINANCE = "finance"; //财务

    //v3.0
    public static final String TYPE_DESIGN_MANAGER = "design-manager"; //设计经理
    public static final String TYPE_PERSONAL_PERFORMANCE = "personal-performance"; //个人绩效

    public static void start(BaseActivity<?, ?> activity, String type) {
        start(activity, type, null);
    }

    public static void start(BaseActivity<?, ?> activity, String type, @Nullable Bundle options) {
        Intent intent = new Intent(activity, ThisMonthDataActivity.class);
        intent.setType(type);
        if (options != null) {
            intent.putExtras(options);
        }
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
    protected void init(Bundle savedInstanceState) {
        String type = getIntent().getType();
        if (TextUtils.equals(type, TYPE_BOSS)) {   //老板、店长本月数据
            startFragment(new BossMonthDataFragment());
        } else if (TextUtils.equals(type, TYPE_INSTALL_MANAGER)) { //安装经理本月数据
            startFragment(new InstallManagerMonthDataFragment());
        } else if (TextUtils.equals(type, TYPE_FINANCE)) { //财务本月数据
            startFragment(new FinanceMonthDataFragment());
        }
        //v3.0
//        else {
//            if (TextUtils.equals(type, TYPE_DESIGN_MANAGER)) {  //设计经理员工绩效 v3.0
//                startFragment(new DesignManagerMonthDataFragment());
//            } else if (TextUtils.equals(type, TYPE_PERSONAL_PERFORMANCE)) {
//                startFragment(new PersonalPerformanceFragment(), getIntent().getExtras(), false);
//            }
//        }

    }
}
