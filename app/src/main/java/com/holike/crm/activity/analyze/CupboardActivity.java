package com.holike.crm.activity.analyze;

import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.fragment.analyze.CupboardPerformanceFragment;
import com.holike.crm.fragment.analyze.CupboardPerformanceDealerFragment;

/**
 * Created by wqj on 2018/5/24.
 * 橱柜业绩报表
 */
public class CupboardActivity extends PerformanceActivity {
    @Override
    protected void init(Bundle savedInstanceState) {
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item9_title));
        fragmentManager = getSupportFragmentManager();
        mPresenter.getCupboardData(null, null, null);
        showLoading();
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getCupboardDataSuccess(CupboardBean bean) {
        super.getCupboardDataSuccess(bean);
        if (TextUtils.equals(bean.getIsDealer(), "1")) {
            startFragment(null, CupboardPerformanceDealerFragment.newInstance(bean), false);
        } else {
            startFragment(null, CupboardPerformanceFragment.newInstance(bean), false);
        }
    }
}
