package com.holike.crm.fragment.customerv2;


import com.holike.crm.R;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.fragment.customerv2.helper.InvalidReturnHelper;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;

/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 * 无效退回
 */
public class InvalidReturnFragment extends GeneralCustomerFragment
        implements InvalidReturnHelper.InvalidReturnCallback {

    private InvalidReturnHelper mHelper;

    @Override
    protected GeneralCustomerPresenter attachPresenter() {
        return new GeneralCustomerPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_invalidreturn;
    }

    @Override
    protected void init() {
        mHelper = new InvalidReturnHelper(this, this);
    }

    @Override
    public void onQuerySystemCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    @Override
    public void onSave(String body) {
        showLoading();
        mPresenter.invalidReturn(body);
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
