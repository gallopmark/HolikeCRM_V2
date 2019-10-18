package com.holike.crm.activity.analyze;

import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.staticbean.JumpBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.FastLiveFragment;

public class FastLiveActivity extends MyFragmentActivity {

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_order_report;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        JumpBean.setJumpBack(getIntentData());
        startFragment(new FastLiveFragment());
    }


    public String getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return getString(R.string.homepage);
        }
        return getString(R.string.fast_live);
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    public LoadingTipDialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }
}
