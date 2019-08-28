package com.holike.crm.activity.analyze;

import android.app.Dialog;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.dialog.LoadingTipDialog;
import com.holike.crm.fragment.analyze.OriginalBoardFragment;
import com.holike.crm.fragment.analyze.OriginalBoardPersonalFragment;
import com.holike.crm.presenter.fragment.OriginalBoardPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.OriginalBoardView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    protected void init() {
        super.init();
        setStatusBarColor(R.color.bg_state_bar);
        setTitle(getString(R.string.report_item10_title));
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
        showLoading();
        mPresenter.getData(null, null, null, null, null);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(OriginalBoardBean bean) {
        dismissLoading();
        Map<String, Serializable> params = new HashMap<>();
        params.put(Constants.ORIGINAL_BOARD_BEAN, bean);
        if (TextUtils.equals(bean.getIsDealer(), "1")) {
            startFragment(params, new OriginalBoardPersonalFragment(), false);
        } else {
            startFragment(params, new OriginalBoardFragment(), false);
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
