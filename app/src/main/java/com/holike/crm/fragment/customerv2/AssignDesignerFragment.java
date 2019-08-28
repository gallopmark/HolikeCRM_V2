package com.holike.crm.fragment.customerv2;

import android.app.Activity;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.fragment.customerv2.helper.AssignDesignerHelper;

/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 * 分配设计师
 */
public class AssignDesignerFragment extends GeneralCustomerFragment implements AssignDesignerHelper.AssignDesignerCallback {

    private AssignDesignerHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_assigndesigner;
    }

    @Override
    protected void init() {
        mHelper = new AssignDesignerHelper(this, this);
    }

    @Override
    public void onQueryUserInfo() {
        showLoading();
        mPresenter.getUserInfo();
    }

    @Override
    public void onQueryDesigner(String shopId) {
        showLoading();
        mPresenter.getShopRoleUser(shopId);
    }

    @Override
    public void onSave(String body) {
        showLoading();
        mPresenter.assignDesigner(body);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object == null) return;
        if (object instanceof CurrentUserBean) {
            mHelper.onGetCurrentUser((CurrentUserBean) object);
        } else if (object instanceof ShopRoleUserBean) {
            mHelper.onGetDesigner(((ShopRoleUserBean) object).getDesigner());
        } else {
            showShortToast((String) object);
            BaseActivity<?, ?> activity = (BaseActivity<?, ?>) mContext;
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }

}
