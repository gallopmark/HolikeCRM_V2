package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.WoodenDoorBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.WoodenDoorDealerFragment;
import com.holike.crm.fragment.analyze.WoodenDoorDefaultFragment;
import com.holike.crm.presenter.activity.WoodenDoorPresenter;
import com.holike.crm.view.activity.WoodenDoorView;


/**
 * Created by gallop on 2019/8/28.
 * Copyright holike possess 2019.
 * 木门业绩报表
 */
public class WoodenDoorActivity extends MyFragmentActivity<WoodenDoorPresenter, WoodenDoorView> implements WoodenDoorView {

    @Override
    protected WoodenDoorPresenter attachPresenter() {
        return new WoodenDoorPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_product_trading;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item18_title));
        getData();
    }

    private void getData() {
        showLoading();
        mPresenter.getData(null, null, null);
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }

    @Override
    public void onSuccess(WoodenDoorBean bean) {
        dismissLoading();
        if (bean.isDealer()) {
            startFragment(null, WoodenDoorDealerFragment.newInstance(bean), false);
        } else {
            startFragment(null, WoodenDoorDefaultFragment.newInstance(bean), false);
        }
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }
}
