package com.holike.crm.fragment.customerv2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerEditActivity;
import com.holike.crm.activity.customer.CustomerEditHouseActivity;
import com.holike.crm.activity.customer.CustomerHighSeasHistoryActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.fragment.customerv2.helper.CustomerManagerHelper;
import com.holike.crm.presenter.fragment.CustomerManagerPresenter;
import com.holike.crm.view.fragment.CustomerManagerView;

import butterknife.BindView;

/**
 * Created by gallop on 2019/7/16.
 * Copyright holike possess 2019.
 * 客户管理页面 v2.0版本
 */
public class CustomerManagerV2Fragment extends MyFragment<CustomerManagerPresenter, CustomerManagerView>
        implements CustomerManagerView, CustomerManagerHelper.CustomerManagerCallback {
    @BindView(R.id.ll_content)
    LinearLayout mContentLayout;
    private CustomerManagerHelper mManagerHelper;

    @Override
    protected CustomerManagerPresenter attachPresenter() {
        return new CustomerManagerPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_manager;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(mContext.getString(R.string.customer_manage_title));
//        setRightMenu(mContext.getString(R.string.history_record));
        mManagerHelper = new CustomerManagerHelper(this, this);
        initData();
    }

    private void initData() {
        String personalId = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            personalId = bundle.getString(CustomerValue.PERSONAL_ID);
        }
        showLoading();
//        mPresenter.request(personalId);
        //personalId = "C2019070000747"; //测试公海客户
        mPresenter.getCustomerDetail(personalId);
    }

    @Override
    public void onSuccess(CustomerManagerV2Bean bean) {
        dismissLoading();
        mContentLayout.setVisibility(View.VISIBLE);
        mManagerHelper.onHttpResponse(bean);
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mContentLayout.setVisibility(View.GONE);
        dealWithFailed(failReason, true);
    }

    @Override
    protected void reload() {
        initData();
    }

    @Override
    public void onHighSeasHistory(String personalId, String houseId) {
        Bundle bundle = new Bundle();
        bundle.putString(CustomerValue.PERSONAL_ID, personalId);
        bundle.putString(CustomerValue.HOUSE_ID, houseId);
        startActivity(CustomerHighSeasHistoryActivity.class, bundle);
    }

    /*编辑客户信息*/
    @Override
    public void onEditInfo(Bundle bundle) {
        CustomerEditActivity.editCustomer(this, true, bundle);
    }

    /*新增或修改房屋信息*/
    @Override
    public void onEditHouse(Bundle bundle) {
        startActivity(CustomerEditHouseActivity.class, bundle);
    }

    /*发表留言*/
    @Override
    public void onPublishMessage(String houseId, String content) {
        mPresenter.publishMessage(houseId, content);
    }

    /*发布留言成功*/
    @Override
    public void onPublishMessageSuccess(String result) {
        showShortToast(result);
        initData();
    }

    /*发布留言失败*/
    @Override
    public void onPublishMessageFailure(String failReason) {
        showShortToast(failReason);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            initData();
        }
    }

    /*领取客户*/
    @Override
    public void doReceive(String houseId, String shopId, String groupId, String salesId) {
        showLoading();
        mPresenter.receiveHouse(houseId, shopId, groupId, salesId);
    }

    /*领取房屋成功 刷新页面*/
    @Override
    public void onReceiveHouseSuccess(String message) {
        showShortToast(message);
        initData();
    }

    @Override
    public void onReceiveHouseFailure(String failReason) {
        showShortToast(failReason);
    }
}
