package com.holike.crm.activity.analyze;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.analyze.WeekReportFragment;
import com.holike.crm.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单交易趋势
 */
public class WeekReportActivity extends MyFragmentActivity {
    public final static int TYPE_WEEK_REPORT = 1;
    public final static int TYPT_MONTH_REPORT = 2;
    public final static int TYPE_ORDER_REPORT = 3;
    public final static int TYPT_DEPOSIT_REPORT = 4;


    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_week_report;
    }


    @Override
    protected void init() {
        super.init();
        Map<String, Integer> params = new HashMap<>();
        params.put(Constants.TYPE, TYPE_WEEK_REPORT);
        startFragment(params, new WeekReportFragment(), false);
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

}
