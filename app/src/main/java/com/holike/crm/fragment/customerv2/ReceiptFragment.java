package com.holike.crm.fragment.customerv2;

import android.content.Intent;

import com.holike.crm.R;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.fragment.customerv2.helper.ReceiptHelper;

import java.util.List;
import java.util.Map;


/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 * 收款
 */
public class ReceiptFragment extends GeneralCustomerFragment implements ReceiptHelper.OnHelperCallback {
    private ReceiptHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_receipt;
    }

    @Override
    protected void init() {
        mHelper = new ReceiptHelper(this, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onQuerySystemCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    @Override
    public void onQueryPaymentUsers(String shopId) {
        showLoading();
        mPresenter.getShopPaymentUsers(shopId);
    }

    @Override
    public void onRequired(CharSequence tips) {
        showShortToast(tips);
    }

    @Override
    public void onSaved(Map<String, Object> params, List<String> imagePaths) {
        showLoading();
        mPresenter.savePayment(mContext, params, imagePaths);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object == null) return;
        if (object instanceof SysCodeItemBean) {
            mHelper.setSystemCode((SysCodeItemBean) object);
        } else if (object instanceof List) {
            mHelper.setPaymentUserList((List<ShopRoleUserBean.UserBean>) object);
        } else {
            setResultOk(object);
        }
    }
}
