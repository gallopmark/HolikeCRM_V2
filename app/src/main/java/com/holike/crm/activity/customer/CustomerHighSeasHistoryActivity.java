package com.holike.crm.activity.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.holike.crm.R;
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

    private String mPersonalId = "", mHouseId = "";
    private HighSeasHistoryHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_historyrecord;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle(getString(R.string.history_record));
        mHelper = new HighSeasHistoryHelper(this);
        obtainBundleValue();
        getHighSeasHistory();
    }

    private void obtainBundleValue() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID, "");
            mHouseId = bundle.getString(CustomerValue.HOUSE_ID, "");
        }
    }

    private void getHighSeasHistory() {
        showLoading();
        mPresenter.getHighSeasHistory(mPersonalId, mHouseId);
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

    @Override
    public void reload() {
        getHighSeasHistory();
    }
}
