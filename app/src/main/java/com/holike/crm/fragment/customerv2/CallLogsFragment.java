package com.holike.crm.fragment.customerv2;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.fragment.customerv2.helper.CallLogsHelper;


/**
 * Created by pony on 2019/7/22.
 * Copyright holike possess 2019.
 * 通话记录
 */
public class CallLogsFragment extends MyFragment {

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_calllogs;
    }

    @Override
    protected void init() {
        CallLogsHelper helper = new CallLogsHelper(mContext);
        helper.setup(mContentView.findViewById(R.id.rv_call_logs));
    }

    @Override
    public void onDestroyView() {
        IntentValue.getInstance().remove("phoneRecord");
        super.onDestroyView();
    }
}
