package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.bean.PerformanceBean;
import com.holike.crm.bean.staticbean.JumpBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.PerformanceFragment;
import com.holike.crm.fragment.analyze.PerformancePersonalFragment;
import com.holike.crm.presenter.fragment.PerformancePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.PerformanceView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 业绩报表
 */
public class PerformanceActivity extends MyFragmentActivity<PerformancePresenter, PerformanceView> implements PerformanceView {

    @Override
    protected PerformancePresenter attachPresenter() {
        return new PerformancePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_performance;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item8_title));
        setLeft(getString(R.string.report_title));
        getData();
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
    public void getData() {
        mPresenter.getData(null, null, null);
        showLoading();
        getIntentData();
    }

    public String getIntentData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            setLeft(getString(R.string.homepage));
            return getString(R.string.homepage);
        }
        return getString(R.string.report_title);
    }

    /**
     * 获取业绩数据成功
     *
     * @param performanceBean
     */
    @Override
    public void getDataSuccess(PerformanceBean performanceBean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.PERFORMANCE_BEAN, performanceBean);
        if(getIntentData().equals(getString(R.string.homepage))) {
            JumpBean.setJumpBack(getString(R.string.homepage));
        }else {
            JumpBean.setJumpBack(getString(R.string.report_title));
        }
        if (performanceBean.getIsDealer().equals("1")) {
            startFragment(params, new PerformancePersonalFragment(), false);
        } else {
            startFragment(params, new PerformanceFragment(), false);
        }
    }



    /**
     * 获取橱柜数据成功
     */
    @Override
    public void getCupboardDataSuccess(CupboardBean bean) {
        dismissLoading();
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

}
