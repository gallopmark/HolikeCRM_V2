package com.holike.crm.fragment.customer;

import android.widget.LinearLayout;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;

import butterknife.BindView;

/**
 * 扫描枪说明
 */
public class ScanGunInstructionActivity extends MyFragmentActivity {

    @BindView(R.id.ll_parent)
    LinearLayout llParent;

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_scan_gun_instraction;
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.receiving_scan_way_1_how_to_use));
    }

}
