package com.holike.crm.fragment.homepage;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;

/**
 * Created by wqj on 2018/7/18.
 * 如何获取订单号
 */

public class FeedbackGetOrderFragment extends MyFragment {
    @Override
    protected int setContentViewId() {
        return R.layout.fragment_feedback_getorder;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.feedback_getorder));
    }
}
