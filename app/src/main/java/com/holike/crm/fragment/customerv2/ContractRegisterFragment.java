package com.holike.crm.fragment.customerv2;

import android.content.Intent;


import com.holike.crm.R;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.fragment.customerv2.helper.ContractRegisterHelper;

import java.util.List;
import java.util.Map;


/**
 * Created by pony on 2019/7/23.
 * Copyright holike possess 2019.
 * 合同登记
 */
public class ContractRegisterFragment extends GeneralCustomerFragment implements ContractRegisterHelper.OnHelperCallback {

    private ContractRegisterHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_contractregister;
    }

    @Override
    protected void init() {
        mHelper = new ContractRegisterHelper(this, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    /*客户管理-获取门店签约人*/
    @Override
    public void onQueryContractUsers(String shopId) {
        showLoading();
        mPresenter.getShopContractUsers(shopId);
    }

    @Override
    public void onRequired(CharSequence text) {
        showShortToast(text);
    }

    @Override
    public void onSaved(Map<String, Object> params, List<String> imagePaths) {
        showLoading();
        mPresenter.saveContractRegister(mContext, params, imagePaths);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object == null) {
            return;
        }
        if (object instanceof List) {
            mHelper.setPaymentUserList((List<ShopRoleUserBean.UserBean>) object);
        } else {
            setResultOk(object);
        }
    }
}
