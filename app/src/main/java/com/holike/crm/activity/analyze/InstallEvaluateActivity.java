package com.holike.crm.activity.analyze;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.base.MyFragmentActivity;
import com.holike.crm.presenter.activity.InstallEvaluatePresenter;
import com.holike.crm.view.activity.InstallEvaluateView;

/**
 * 安装服务评价表
 */
public class InstallEvaluateActivity extends MyFragmentActivity<InstallEvaluatePresenter, InstallEvaluateView> implements InstallEvaluateView {

    @Override
    protected InstallEvaluatePresenter attachPresenter() {
        return new InstallEvaluatePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_install_evaluate;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.report_item7_title));
        setLeft(getString(R.string.back));
        setRightMenu(getString(R.string.report_select_date));
    }

}
