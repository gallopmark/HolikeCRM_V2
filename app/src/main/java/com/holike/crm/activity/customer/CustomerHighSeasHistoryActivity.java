package com.holike.crm.activity.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.activity.customer.helper.HighSeasHistoryHelper;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.enumeration.CustomerValue;

import butterknife.BindView;

/**
 * Created by gallop on 2019/7/30.
 * Copyright holike possess 2019.
 * 历史记录（被领取后客户管理历史记录）
 */
public class CustomerHighSeasHistoryActivity extends GeneralCustomerActivity {
    @BindView(R.id.ll_content_layout)
    LinearLayout mContentLayout;

    private HighSeasHistoryHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_historyrecord;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.history_record));
        mHelper = new HighSeasHistoryHelper(this);
        obtainBundleValue();
    }

    private void obtainBundleValue() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String personalId = bundle.getString(CustomerValue.PERSONAL_ID);
            String houseId = bundle.getString(CustomerValue.HOUSE_ID);
            getHighSeasHistory(personalId, houseId);
        } else {
            noResult();
        }
    }

    private void getHighSeasHistory(String personalId, String houseId) {
        showLoading();
        mPresenter.getHighSeasHistory(personalId, houseId);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object == null) {
            noResult();
        } else {
            CustomerManagerV2Bean bean = (CustomerManagerV2Bean) object;
            if (bean.getHouseInfoList().isEmpty()) {
                noResult();
            } else {
                mContentLayout.setVisibility(View.VISIBLE);
                mHelper.onHttpResponse(bean);
            }
        }
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mContentLayout.setVisibility(View.GONE);
        noNetwork(failReason);
    }
}
