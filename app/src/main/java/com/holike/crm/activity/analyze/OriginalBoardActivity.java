package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.OriginalBoardRatioDefaultFragment;
import com.holike.crm.fragment.analyze.OriginalBoardRatioDealerFragment;
import com.holike.crm.presenter.fragment.OriginalBoardPresenter;
import com.holike.crm.view.fragment.OriginalBoardView;


/**
 * 原态板占比
 */
public class OriginalBoardActivity extends MyFragmentActivity<OriginalBoardPresenter, OriginalBoardView> implements OriginalBoardView {

    @Override
    protected OriginalBoardPresenter attachPresenter() {
        return new OriginalBoardPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_original_board;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item10_title));
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
        showLoading();
        mPresenter.getData(null, null, null, null, null);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(OriginalBoardBean bean) {
        dismissLoading();
        if (TextUtils.equals(bean.getIsDealer(), "1")) {
            startFragment(null, OriginalBoardRatioDealerFragment.newInstance(bean), false);
        } else {
            startFragment(null, OriginalBoardRatioDefaultFragment.newInstance(bean), false);
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
