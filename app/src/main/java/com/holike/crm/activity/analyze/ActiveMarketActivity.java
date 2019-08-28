package com.holike.crm.activity.analyze;


import android.app.Dialog;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.ActiveMarketFragment;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销数据报表
 */

public class ActiveMarketActivity extends MyFragmentActivity {
    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_active_market;
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }

    @Override
    protected void init() {
        super.init();
        startFragment(new ActiveMarketFragment());
    }
}
