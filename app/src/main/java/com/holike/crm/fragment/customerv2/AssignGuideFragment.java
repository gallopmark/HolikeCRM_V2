package com.holike.crm.fragment.customerv2;


import com.holike.crm.R;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.fragment.customerv2.helper.AssignGuideHelper;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;

import java.util.List;
import java.util.Map;


/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 * 分配导购
 */
public class AssignGuideFragment extends GeneralCustomerFragment
        implements AssignGuideHelper.AssignGuideCallback, GeneralCustomerPresenter.OnQueryGuideCallback {
    private AssignGuideHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_assignguide;
    }

    @Override
    protected void init() {
        mHelper = new AssignGuideHelper(this, this);
    }

    @Override
    public void onQueryUserInfo() {
        showLoading();
        mPresenter.getUserInfo();
    }

    /*获取门店分组信息*/
    @Override
    public void onQueryShopGroup(String shopId) {
        showLoading();
        mPresenter.getShopGroup(shopId);
    }

    /*获取门店下的导购人员，前提是门店下没有分组信息*/
    @Override
    public void onQueryShopGuide(String shopId) {
        showLoading();
        mPresenter.getShopGuide(shopId, this);
    }

    /*获取分组导购*/
    @Override
    public void onQueryGroupGuide(String groupId) {
        showLoading();
        mPresenter.getGroupGuide(groupId, this);
    }

    /*客户管理-获取门店-业务员*/
    @Override
    public void onQuerySalesman(String shopId) {
        showLoading();
        mPresenter.getShopRoleUser(shopId);
    }

    @Override
    public void onSave(String body) {
        showLoading();
        mPresenter.assignGuide(body);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object != null) {
            if (object instanceof CurrentUserBean) {
                mHelper.setCurrentUserInfo((CurrentUserBean) object);
            } else if (object instanceof ShopRoleUserBean) {
                mHelper.setSalesmanList((ShopRoleUserBean) object);
            } else if (object instanceof List) {
                mHelper.setShopGroup((List<ShopGroupBean>) object);
            } else {
                setResultOk(object);
            }
        }
    }

    @Override
    public void onSuccess(List<ShopRoleUserBean.UserBean> list) {
        dismissLoading();
        mHelper.setGuideList(list);
    }
}
