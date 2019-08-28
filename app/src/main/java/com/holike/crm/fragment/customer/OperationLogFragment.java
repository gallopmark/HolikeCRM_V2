package com.holike.crm.fragment.customer;

import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.OpreationLogBean;
import com.holike.crm.presenter.fragment.OperationLogPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.OperationLogView;

import java.util.List;

import butterknife.BindView;

/**
 * 操作日志
 */
public class OperationLogFragment extends MyFragment<OperationLogPresenter, OperationLogView> implements OperationLogView {

    @BindView(R.id.rv_operation_log_center)
    RecyclerView rvCenter;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_operation_info;
    }

    @Override
    protected OperationLogPresenter attachPresenter() {
        return new OperationLogPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.operation_log_log));
        showLoading();
        if (getArguments() != null && getArguments().getSerializable(Constants.ORDER_ID) != null) {
            String orderId = (String) getArguments().getSerializable(Constants.ORDER_ID);
            mPresenter.getData(orderId);
        } else {
            onFail(getString(R.string.tips_no_data));
        }
    }

    @Override
    public void onSuccess(List<OpreationLogBean> bean) {
        dismissLoading();
        mPresenter.setContentAdapter(getActivity(),rvCenter,bean);
    }

    @Override
    public void onFail(String errorMsg) {
        noResult(getString(R.string.tips_no_corresponding_logistics_information));
        dismissLoading();
    }
}
