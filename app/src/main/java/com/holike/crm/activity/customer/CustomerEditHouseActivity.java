package com.holike.crm.activity.customer;


import android.content.Intent;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerEditHouseHelper;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.List;


/**
 * Created by gallop on 2019/7/31.
 * Copyright holike possess 2019.
 * 客户添加房屋或编辑房屋
 */
public class CustomerEditHouseActivity extends GeneralCustomerActivity implements CustomerEditHouseHelper.CustomerEditHouseCallback,
        GeneralCustomerPresenter.OnQueryShopGroupListener, GeneralCustomerPresenter.OnReceivingCustomerListener,
        GeneralCustomerPresenter.OnDistributionMsgPushListener, GeneralCustomerPresenter.OnActivationCallback {

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
    protected void init(Bundle savedInstanceState) {
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
        mPresenter.getShopGroupByUser(shopId, this);
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
    public void onReceivingCustomer(String personalId, String houseId, String shopId, String groupId) {
        showLoading();
        mPresenter.receiveHouse(personalId, houseId, shopId, groupId, SharedPreferencesUtils.getUserId(), this);
    }

    /*激活客户*/
    @Override
    public void onActivationCustomer(String personalId, String houseId, String shopId, String groupId, String guideId) {
        showLoading();
        mPresenter.activationCustomer(personalId, houseId, shopId, groupId, guideId, this);
    }

    /*重新分配客户*/
    @Override
    public void onDistributionMsgPush(String requestBody) {
        showLoading();
        mPresenter.distributionMsgPush(requestBody, this);
    }

    /*领取客户失败*/
    @Override
    public void onReceivingFailure(String failReason) {
        onFailure(failReason);
    }

    /*领取客户成功*/
    @Override
    public void onReceivingSuccess(String message, String personalId, String houseId) {
        dismissLoading();
        showShortToast(message);
        RxBus.getInstance().post(new MessageEvent(CustomerValue.EVENT_TYPE_RECEIVE_HOUSE));
        Intent intent = new Intent();
        intent.putExtra(CustomerValue.PERSONAL_ID, personalId);
        intent.putExtra(CustomerValue.HOUSE_ID, houseId);
        setResult(CustomerValue.RESULT_CODE_HIGH_SEAS, intent);
        finish();
    }

    @Override
    public void onMsgPushFailure(String failReason) {
        onFailure(failReason);
    }

    @Override
    public void onMsgPushSuccess(String message) {
        dismissLoading();
        showShortToast(message);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object instanceof SysCodeItemBean) {
            mHelper.setSysCode((SysCodeItemBean) object);
        } else if (object instanceof CurrentUserBean) {
            mHelper.setCurrentUser((CurrentUserBean) object);
        } else if (object instanceof String) {  //新增房屋或修改房屋成功
            mHelper.onSaveResult((String) object);
        }
    }

    @Override
    public void onQuerySuccess(List<ShopGroupBean> result) {
        dismissLoading();
        mHelper.setShopGroupArray(result);
    }

    @Override
    public void onQueryFailure(String failReason) {
        onFailure(failReason);
    }

    @Override
    public void onActivationFailure(String failReason) {
        onFailure(failReason);
    }

    @Override
    public void onActivationSuccess(String message, String personalId, String houseId) {
        dismissLoading();
        showShortToast(message);
        Intent intent = new Intent();
        intent.putExtra(CustomerValue.PERSONAL_ID, personalId);
        intent.putExtra(CustomerValue.HOUSE_ID, houseId);
        setResult(CustomerValue.RESULT_CODE_ACTIVATION, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mHelper.onDetached();
        super.onDestroy();
    }

}
