package com.holike.crm.activity.analyze;

import com.holike.crm.R;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.fragment.analyze.CupboardFragment;
import com.holike.crm.fragment.analyze.CupboardPersonalFragment;
import com.holike.crm.util.Constants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/24.
 * 橱柜业绩报表
 */

public class CupboardActivity extends PerformanceActivity {
    @Override
    protected void init() {
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item9_title));
        setLeft(getString(R.string.report_title));
        fragmentManager = getSupportFragmentManager();
        mPresenter.getCupboardData(null, null, null);
        showLoading();
    }

    /**
     * 获取数据成功
     *
     * @param bean
     */
    @Override
    public void getCupboardDataSuccess(CupboardBean bean) {
        super.getCupboardDataSuccess(bean);
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.CUPBOARD_BEAN, bean);
        if (bean.getIsDealer().equals("1")) {
            startFragment(params, new CupboardPersonalFragment(), false);
        } else {
            startFragment(params, new CupboardFragment(), false);
        }
    }
}
