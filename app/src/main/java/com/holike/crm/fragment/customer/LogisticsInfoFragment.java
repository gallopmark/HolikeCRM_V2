package com.holike.crm.fragment.customer;

import android.text.TextUtils;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.LogisticsInfoBean;
import com.holike.crm.presenter.fragment.LogisticsInfoPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.LogisticsInfoView;

import butterknife.BindView;

/**
 * 物流信息
 */
public class LogisticsInfoFragment extends MyFragment<LogisticsInfoPresenter, LogisticsInfoView> implements LogisticsInfoView {
    String orderId;
    @BindView(R.id.tv_logistics_line)
    TextView tvLogisticsLine;
    @BindView(R.id.tv_logistics_station)
    TextView tvLogisticsStation;
    @BindView(R.id.tv_logistics_frim)
    TextView tvLogisticsFirm;
    @BindView(R.id.tv_logistics_address)
    TextView tvLogisticsAddress;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_logistics_info;
    }

    @Override
    protected LogisticsInfoPresenter attachPresenter() {
        return new LogisticsInfoPresenter();
    }

    @Override
    protected void init() {
        super.init();
        showLoading();
        setTitle(getString(R.string.logistics_info));
        if (getArguments() != null) {
            orderId = (String) getArguments().getSerializable(Constants.ORDER_ID);
        }
        mPresenter.getData(orderId);
    }

    @Override
    public void onSuccess(LogisticsInfoBean bean) {
        dismissLoading();
        tvLogisticsAddress.setText(TextUtils.isEmpty(bean.getAddress()) ? "暂无信息" : bean.getAddress());
        tvLogisticsFirm.setText(TextUtils.isEmpty(bean.getFirm()) ? "暂无信息" : bean.getFirm());
        tvLogisticsStation.setText(TextUtils.isEmpty(bean.getStation()) ? "暂无信息" : bean.getStation());
        tvLogisticsLine.setText(TextUtils.isEmpty(bean.getLine()) ? "暂无信息" : bean.getLine());
    }

    @Override
    public void onFail(String msg) {
        dismissLoading();
        noResult(getString(R.string.tips_no_corresponding_operation_log));
    }

}
