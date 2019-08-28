package com.holike.crm.activity.analyze;

import android.app.Dialog;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.NewRetailFragment;

/**
 * Created by wqj on 2018/5/31.
 * 新零售
 */

public class NewRetailActivity extends MyFragmentActivity {
    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_new_retail;
    }

    @Override
    protected void init() {
        super.init();
        startFragment(new NewRetailFragment());
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }
}
