package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.ProductTradingBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.ProductPersonalFragment;
import com.holike.crm.fragment.analyze.ProductTradingFragment;
import com.holike.crm.presenter.fragment.ProductTradingPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.ProductTradingView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 成品交易报表
 */
public class ProductTradingActivity extends MyFragmentActivity<ProductTradingPresenter, ProductTradingView> implements ProductTradingView {

    @Override
    protected ProductTradingPresenter attachPresenter() {
        return new ProductTradingPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_product_trading;
    }

    @Override
    protected void init() {
        super.init();
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item5_title));
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
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.PRODUCT_TRADING_BEAN, productTradingBean);
        if (TextUtils.equals(productTradingBean.getIsDealer(), "1")) {
            startFragment(params, new ProductPersonalFragment(), false);
        } else {
            startFragment(params, new ProductTradingFragment(), false);
        }
    }
}
