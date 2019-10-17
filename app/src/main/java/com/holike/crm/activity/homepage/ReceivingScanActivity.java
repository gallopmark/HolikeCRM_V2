package com.holike.crm.activity.homepage;

import android.os.Bundle;

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
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        startFragment(new ReceivingScanFragment());
    }
}
