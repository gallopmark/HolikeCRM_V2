package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.OnlineAttractReportFragment;

public class OnlineAttractReportActivity extends MyFragmentActivity {
    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_online_attract_report;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        startFragment(new OnlineAttractReportFragment());
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }
}
