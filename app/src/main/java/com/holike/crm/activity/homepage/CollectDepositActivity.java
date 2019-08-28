package com.holike.crm.activity.homepage;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.customer.CollectDepositFragment;

/**
 * Created by wqj on 2018/8/17.
 * 首页进入收取订金页
 */

public class CollectDepositActivity extends MyFragmentActivity {
    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_collect_deposit;
    }

    @Override
    protected void init() {
        super.init();
        startFragment(new CollectDepositFragment());
    }
}
