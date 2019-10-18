package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.os.Bundle;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.DealerRankBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.DealerRankFragment;
import com.holike.crm.fragment.analyze.DealerRankPersonalFragment;
import com.holike.crm.presenter.fragment.DealerRankPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.DealerRankView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/5/28.
 * 经销商排行
 */

public class DealerRankActivity extends MyFragmentActivity<DealerRankPresenter, DealerRankView> implements DealerRankView {
    @Override
    protected DealerRankPresenter attachPresenter() {
        return new DealerRankPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_dealer_rank;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item11_title));
        getData(null);
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }

    @Override
    public void getData(String cityCode) {
        showLoading();
        mPresenter.getData(cityCode);
    }

    /**
     * 经销商排行榜-非经销商人员
     */
    @Override
    public void enterRank(DealerRankBean bean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.DEALER_RANK, bean);
        startFragment(params, new DealerRankFragment(), false);
    }

    /**
     * 经销商排行榜-经销商人员
     */
    @Override
    public void enterPersonal(DealerRankBean bean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.DEALER_RANK, bean);
        startFragment(params, new DealerRankPersonalFragment(), false);
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
