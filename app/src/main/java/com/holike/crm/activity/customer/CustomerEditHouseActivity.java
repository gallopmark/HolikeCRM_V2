package com.holike.crm.activity.customer;


import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerEditHouseHelper;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;

import java.util.List;


/**
 * Created by gallop on 2019/7/31.
 * Copyright holike possess 2019.
 * 客户添加房屋或编辑房屋
 */
public class CustomerEditHouseActivity extends GeneralCustomerActivity implements CustomerEditHouseHelper.CustomerEditHouseCallback, GeneralCustomerPresenter.OnQueryShopGroupListener {

    private CustomerEditHouseHelper mHelper;

    @Override
    protected GeneralCustomerPresenter attachPresenter() {
        return new GeneralCustomerPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_addhouse;
    }

    @Override
    protected void init() {
        mHelper = new CustomerEditHouseHelper(this, this);
    }

    @Override
    public void onQuerySysCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    /*查询当前用户登录信息*/
    @Override
    public void onQueryUserInfo() {
        showLoading();
        mPresenter.getUserInfo();
    }

    @Override
    public void onQueryShopGroup(String shopId) {
        showLoading();
        mPresenter.getShopGroup(shopId,this);
    }

    /*保存房屋信息*/
    @Override
    public void onSave(boolean isEditHouse, String body) {
        showLoading();
        if (!isEditHouse) { //新增房屋信息
            mPresenter.addHouseInfo(body);
        } else {    //修改房屋信息
            mPresenter.updateHouseInfo(body);
        }
    }

    @Override
    protected void onDestroy() {
        mHelper.onDetached();
        super.onDestroy();
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object instanceof SysCodeItemBean) {
            mHelper.setSysCode((SysCodeItemBean) object);
        } else if (object instanceof CurrentUserBean) {
            mHelper.setCurrentUser((CurrentUserBean) object);
        } else {  //新增房屋或修改房屋成功
            setResultOk(object);
        }
    }

    @Override
    public void onQuerySuccess(List<ShopGroupBean> result) {
        dismissLoading();
        mHelper.setShopGroupArray(result);
    }

    @Override
    public void onQueryFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }
}
