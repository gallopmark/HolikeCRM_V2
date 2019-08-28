package com.holike.crm.activity.analyze;

import android.app.Dialog;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.BuildStoreFragment;

/**
 * 建店数据报表
 */
public class BuildStoreActivity extends MyFragmentActivity {

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_build_store;
    }

    @Override
    protected void init() {
        super.init();
        startFragment(new BuildStoreFragment());
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }
}
