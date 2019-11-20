package com.holike.crm.activity.customer;


import android.content.Intent;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.activity.customer.helper.CustomerEditHelper;
import com.holike.crm.base.BaseFragment;
import com.holike.crm.bean.ActivityPoliceBean;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by pony 2019/7/5
 * Copyright (c) 2019 holike
 * 新建客户、编辑客户
 */
public class CustomerEditActivity extends GeneralCustomerActivity implements CustomerEditHelper.CustomerEditCallback,
        GeneralCustomerPresenter.OnQueryShopGroupListener, GeneralCustomerPresenter.OnReceivingCustomerListener, GeneralCustomerPresenter.OnDistributionMsgPushListener, GeneralCustomerPresenter.OnActivationCallback {

    private CustomerEditHelper mHelper;//13900011111

    @Override
    protected GeneralCustomerPresenter attachPresenter() {
        return new GeneralCustomerPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_customer_edit;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        setTitle(getString(R.string.receive_deposit_add_customer));
        mHelper = new CustomerEditHelper(this, this);
    }

    /*获取活动优惠政策*/
    @Override
    public void onRequestActivityPolicy(String dealerId) {
        mPresenter.getActivityPolice(dealerId);
    }

    /*获取业务字典*/
    @Override
    public void onRequestSysCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    /*请求当前登录的用户信息*/
    @Override
    public void onRequestUserInfo() {
        showLoading();
        mPresenter.getUserInfo();
    }

    @Override
    public void onQueryShopGroup(String shopId) {
        showLoading();
        mPresenter.getShopGroupByUser(shopId, this);
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

    @OnClick(R.id.tvSave)
    public void onViewClicked() {
        mHelper.onSave();
    }

    @Override
    public void onReceivingCustomer(String personalId, String houseId, String shopId, String groupId) {
        showLoading();
        mPresenter.receiveHouse(personalId, houseId, shopId, groupId, SharedPreferencesUtils.getUserId(), this);
    }

    /*激活客户*/
    @Override
    public void onActivationCustomer(String personalId, String houseId, String shopId, String groupId) {
        showLoading();
        mPresenter.activationCustomer(personalId, houseId, shopId, groupId, this);
    }

    /*重新分配客户*/
    @Override
    public void onDistributionMsgPush(String requestBody) {
        showLoading();
        mPresenter.distributionMsgPush(requestBody, this);
    }

    @Override
    public void onRequired(CharSequence failed) {
        showShortToast(failed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSave(boolean isEdit, String body) {
        showLoading();
        if (!isEdit) {
            mPresenter.createCustomer(body);
        } else {
            mPresenter.alterCustomer(body);
        }
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

    @SuppressWarnings("unchecked")
    @Override
    public void onSuccess(Object object) {
        dismissLoading();
        if (object instanceof SysCodeItemBean) {
            mHelper.setSysCodeItemBean((SysCodeItemBean) object);
        } else if (object instanceof CurrentUserBean) {
            mHelper.setCurrentUserBean((CurrentUserBean) object);
        } else if (object instanceof List) {
            mHelper.setActivityPoliceBean((List<ActivityPoliceBean>) object);
        } else if (object instanceof String) { //创建或者修改客户成功
            mHelper.onSaveResult((String) object);
        }
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
        mHelper.onDestroy();
        super.onDestroy();
    }

    /**
     * @param isEdit 是否是编辑房屋
     */
    public static void editCustomer(BaseFragment<?, ?> fragment, boolean isEdit, Bundle bundle) {
        if (fragment.getActivity() == null) return;
        Intent intent = new Intent(fragment.getActivity(), CustomerEditActivity.class);
        intent.putExtra("isEdit", isEdit);
        fragment.openActivityForResult(intent, bundle);
    }
}
