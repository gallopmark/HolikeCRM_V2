package com.holike.crm.fragment.customerv2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.holike.crm.R;
import com.holike.crm.activity.customer.CustomerEditActivity;
import com.holike.crm.activity.customer.CustomerEditHouseActivity;
import com.holike.crm.activity.customer.CustomerHighSeasHistoryActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.ActivityPoliceBean;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.fragment.customerv2.helper.CustomerManagerHelper;
import com.holike.crm.presenter.fragment.GeneralCustomerPresenter;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by gallop on 2019/7/16.
 * Copyright holike possess 2019.
 * 客户管理页面 v2.0版本
 */
public class CustomerManagerV2Fragment extends GeneralCustomerFragment
        implements CustomerManagerHelper.CustomerManagerCallback, GeneralCustomerPresenter.OnQueryActivityPoliceCallback {
    @BindView(R.id.ll_content)
    LinearLayout mContentLayout;
    private CustomerManagerHelper mHelper;
    private int mRequestType;
    private Handler mHandler;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_manager;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(mContext.getString(R.string.customer_manage_title));
//        setRightMenu(mContext.getString(R.string.history_record));
        mHelper = new CustomerManagerHelper(this, this);
        getActivityPolice();
    }

    @Override
    public void onGetCustomerDetail(String personalId, boolean isHighSeasHouse) {
        mRequestType = 1;
        showLoading();
        mPresenter.getCustomerDetail(personalId, isHighSeasHouse);
    }

    /*检查是否有活动优惠政策，主要是控制是否显示“活动优惠政策”字段*/
    private void getActivityPolice() {
        mPresenter.getActivityPolice(SharedPreferencesUtils.getDealerId(), this);
    }

    @Override
    public void onSuccess(Object object) {
        dismissLoading();
        if (object instanceof CustomerManagerV2Bean) {
            mContentLayout.setVisibility(View.VISIBLE);
            mHelper.onHttpResponse((CustomerManagerV2Bean) object);
        } else if (object instanceof String) {
            String result = (String) object;
            if (mRequestType == 2) {  //发布留言成功
                onPublishMessageSuccess(result);
            } else if (mRequestType == 3) { //领取房屋成功
                onReceiveHouseSuccess(result);
            } else if (mRequestType == 4) {  //确认流失成功
                onLoseHouseSuccess(result);
            }
        }
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        if (mRequestType == 1) { //请求客户详情出错
            mContentLayout.setVisibility(View.GONE);
            if (isNoAuth(failReason)) {
                noAuthority();
            } else {
                noNetwork(failReason);
            }
        } else {
            if (mRequestType == 2) {  //发布留言失败
                onPublishMessageFailure(failReason);
            } else if (mRequestType == 3) { //领取房屋失败
                onReceiveHouseFailure(failReason);
            } else if (mRequestType == 4) {  //确认流失失败
                onLoseHouseFailure(failReason);
            }
        }
    }

    @Override
    protected void reload() {
        mHelper.doRequest();
    }

    @Override
    public void onQueryActivityPoliceFailure(String failReason) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(this::getActivityPolice, 3000);
    }

    @Override
    public void onQueryActivityPolice(List<ActivityPoliceBean> dataList) {
        mHelper.setActivityPoliceEnabled(dataList.isEmpty());
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
        mRequestType = 2;
        mPresenter.publishMessage(houseId, content);
    }

    /*发布留言成功*/
    private void onPublishMessageSuccess(String result) {
        processResult(result, true);
    }

    /*发布留言失败*/
    private void onPublishMessageFailure(String failReason) {
        processResult(failReason, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mHelper.doRequest();
        } else if ((resultCode == CustomerValue.RESULT_CODE_ACTIVATION ||
                resultCode == CustomerValue.RESULT_CODE_HIGH_SEAS) && data != null) {
            ((BaseActivity<?, ?>) mContext).setResult(CustomerValue.RESULT_CODE_ACTIVATION, data);
            ((BaseActivity<?, ?>) mContext).finish();
        }
    }

    /*领取客户*/
    @Override
    public void doReceive(String houseId, String shopId, String groupId, String salesId) {
        mRequestType = 3;
        showLoading();
        mPresenter.receiveHouse(houseId, shopId, groupId, salesId);
    }

    /*领取房屋成功 刷新页面*/
    private void onReceiveHouseSuccess(String message) {
        RxBus.getInstance().post(new MessageEvent(CustomerValue.EVENT_TYPE_RECEIVE_HOUSE));
        ((BaseActivity<?, ?>) mContext).setResult(CustomerValue.RESULT_CODE_RECEIVE_HOUSE);
        dismissLoading();
        showShortToast(message);
        mHelper.onReceiveHouseSuccess();
    }

    private void onReceiveHouseFailure(String failReason) {
        processResult(failReason, false);
    }

    @Override
    public void onConfirmLoseHouse(String houseId) {
        mRequestType = 4;
        showLoading();
        mPresenter.confirmLostHouse(houseId);
    }

    /*确认流失成功后，返回上一级页面*/
    private void onLoseHouseSuccess(String message) {
        dismissLoading();
        showShortToast(message);
        RxBus.getInstance().post(new MessageEvent(CustomerValue.EVENT_TYPE_CONFIRM_LOST_HOUSE));
        ((BaseActivity<?, ?>) mContext).setResult(CustomerValue.RESULT_CODE_LOST_HOUSE);
        ((BaseActivity<?, ?>) mContext).finish();
    }

    private void onLoseHouseFailure(String failReason) {
        processResult(failReason, false);
    }

    private void processResult(String text, boolean isRefresh) {
        dismissLoading();
        showShortToast(text);
        if (isRefresh) {
            mHelper.doRequest();
        }
    }

    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }
}
