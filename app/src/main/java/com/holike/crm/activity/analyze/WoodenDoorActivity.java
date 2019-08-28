package com.holike.crm.activity.analyze;

import android.app.Dialog;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.WoodenDoorBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.WoodenDoorDealerFragment;
import com.holike.crm.fragment.analyze.WoodenDoorPersonalFragment;
import com.holike.crm.presenter.activity.WoodenDoorPresenter;
import com.holike.crm.view.activity.WoodenDoorView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    protected void init() {
        super.init();
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
        Map<String, Serializable> params = new HashMap<>();
        params.put("bean", bean);
        if (bean.isDealer()) {
            startFragment(params, new WoodenDoorDealerFragment(), false);
        } else {
            startFragment(params, new WoodenDoorPersonalFragment(), false);
        }
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        showShortToast(failReason);
    }
}
