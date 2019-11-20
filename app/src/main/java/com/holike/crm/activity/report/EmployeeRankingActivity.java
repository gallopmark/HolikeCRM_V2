package com.holike.crm.activity.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.bean.EmployeeRankingBean;
import com.holike.crm.fragment.employee.EmployeeRankDealerFragment;
import com.holike.crm.fragment.employee.EmployeeRankInternalFragment;
import com.holike.crm.presenter.GeneralReportPresenter;
import com.holike.crm.view.GeneralReportView;
import com.holike.crm.view.fragment.GeneralCustomerView;

import butterknife.BindView;

/**
 * Created by pony on 2019/10/23.
 * Copyright holike possess 2019.
 * 员工个人排行榜
 */
public class EmployeeRankingActivity extends BaseActivity<GeneralReportPresenter, GeneralCustomerView> implements GeneralReportView {
    @BindView(R.id.fl_fragment_main)
    FrameLayout mContainerLayout;

    @Override
    protected GeneralReportPresenter attachPresenter() {
        return new GeneralReportPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_employee_ranking;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_ranking_board));
        initData();
    }

    private void initData() {
        showLoading();
        mPresenter.getEmployeeRanking(null, null, null, null, "1");
    }

    @Override
    public void onSuccess(Object obj) {
        requestCompleted();
        EmployeeRankingBean bean = (EmployeeRankingBean) obj;
        if (bean.isDealer()) {
            startFragment(EmployeeRankDealerFragment.newInstance(bean));
        } else {
            startFragment(EmployeeRankInternalFragment.newInstance(bean));
        }
    }

    @Override
    public void onFailure(String failReason) {
        requestCompleted();
        LayoutInflater.from(this).inflate(R.layout.include_empty_page, mContainerLayout, true);
        noNetwork(failReason);
    }

    private void requestCompleted() {
        dismissLoading();
        mContainerLayout.removeAllViews();
    }

    @Override
    public void reload() {
        initData();
    }
}
