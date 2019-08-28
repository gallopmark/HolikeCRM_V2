package com.holike.crm.activity.homepage;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.homepage.ReceivingScanFragment;

public class ReceivingScanActivity extends MyFragmentActivity {
    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_fragment_depend;
    }

    @Override
    protected void init() {
        super.init();
        startFragment(new ReceivingScanFragment());
    }
}
