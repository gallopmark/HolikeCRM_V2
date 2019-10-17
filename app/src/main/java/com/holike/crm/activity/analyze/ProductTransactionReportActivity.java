package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.ProductTradingBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.ProductTransactionDealerFragment;
import com.holike.crm.fragment.analyze.ProductTransactionDefaultFragment;
import com.holike.crm.presenter.fragment.ProductTradingPresenter;
import com.holike.crm.view.fragment.ProductTradingView;

/**
 * 成品交易报表
 */
public class ProductTransactionReportActivity extends MyFragmentActivity<ProductTradingPresenter, ProductTradingView> implements ProductTradingView {

    @Override
    protected ProductTradingPresenter attachPresenter() {
        return new ProductTradingPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_product_trading;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item5_title));
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
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(ProductTradingBean productTradingBean) {
        dismissLoading();
        if (TextUtils.equals(productTradingBean.getIsDealer(), "1")) {
            startFragment(null, ProductTransactionDealerFragment.newInstance(productTradingBean), false);
        } else {
            startFragment(null, ProductTransactionDefaultFragment.newInstance(productTradingBean), false);
        }
    }
}
