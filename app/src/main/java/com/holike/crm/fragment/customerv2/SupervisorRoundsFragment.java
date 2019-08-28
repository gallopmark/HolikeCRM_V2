package com.holike.crm.fragment.customerv2;

import com.holike.crm.R;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.fragment.customerv2.helper.SupervisorRoundsHelper;


/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 * 主管查房
 */
public class SupervisorRoundsFragment extends GeneralCustomerFragment implements SupervisorRoundsHelper.Callback {

    private SupervisorRoundsHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_supervisorrounds;
    }

    @Override
    protected void init() {
        mHelper = new SupervisorRoundsHelper(this, this);
    }

    @Override
    public void onQuerySystemCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    @Override
    public void onSaved(String body) {
        showLoading();
        mPresenter.supervisorRounds(body);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object instanceof SysCodeItemBean) {
            mHelper.setSystemCode((SysCodeItemBean) object);
        } else {
            setResultOk(object);
        }
    }
}
