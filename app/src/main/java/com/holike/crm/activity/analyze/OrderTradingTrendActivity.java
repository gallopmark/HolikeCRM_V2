package com.holike.crm.activity.analyze;

import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.fragment.analyze.OrderTradingTrendFragment;

/**
 * 订单交易趋势
 */
public class OrderTradingTrendActivity extends MyFragmentActivity {

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_order_trading_trend;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        startFragment(new OrderTradingTrendFragment());
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

}
