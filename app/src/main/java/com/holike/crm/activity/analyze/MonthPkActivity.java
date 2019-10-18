package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.MonthPkBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.MonthPkFragment;
import com.holike.crm.fragment.analyze.MonthPkPersonalFragment;
import com.holike.crm.presenter.activity.MonthPkPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.MonthPkView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/6/7.
 * 月度PK
 */

public class MonthPkActivity extends MyFragmentActivity<MonthPkPresenter, MonthPkView> implements MonthPkView {
    @Override
    protected MonthPkPresenter attachPresenter() {
        return new MonthPkPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_month_pk;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item15_title));
        showLoading();
        mPresenter.getData(null);
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }

    /**
     * 月度PK
     *
     * @param bean
     */
    @Override
    public void openMonthPk(MonthPkBean bean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.MONTH_PK, bean);
        startFragment(params, new MonthPkFragment(), false);
    }

    /**
     * 月度PK-中心经理
     */
    @Override
    public void openMonthPkPersonal(MonthPkBean bean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.MONTH_PK, bean);
        startFragment(params, new MonthPkPersonalFragment(), false);
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDatafailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
