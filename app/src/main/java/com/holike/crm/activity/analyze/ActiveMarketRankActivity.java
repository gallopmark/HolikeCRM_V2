package com.holike.crm.activity.analyze;

import android.app.Dialog;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.ActiveMarketRankBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.ActiveMarketRankFragment;
import com.holike.crm.fragment.analyze.ActiveMarketRankPersonalFragment;
import com.holike.crm.presenter.fragment.ActiveMarketRankPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.ActiveMarketRankView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wqj on 2018/6/21.
 * 主动营销排行榜
 */

public class ActiveMarketRankActivity extends MyFragmentActivity<ActiveMarketRankPresenter, ActiveMarketRankView> implements ActiveMarketRankView {
    @Override
    protected int setContentViewId() {
        return R.layout.activity_active_market_rank;
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingTipDialog(this);
    }

    @Override
    protected ActiveMarketRankPresenter attachPresenter() {
        return new ActiveMarketRankPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_item17_title));
        setLeft(getString(R.string.report_title));
        setStatusBarColor(R.color.bg_state_bar);
        getData();
    }

    @Override
    public void getData() {
        showLoading();
        mPresenter.getData(null, null);
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(ActiveMarketRankBean bean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.ACTIVE_MARKET_RANK_BEAN, bean);
        if (bean.getIsActive() == 1) {
            startFragment(params, new ActiveMarketRankPersonalFragment(), false);
        } else {
            startFragment(params, new ActiveMarketRankFragment(), false);
        }
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
