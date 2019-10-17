package com.holike.crm.fragment.monthdata;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthDataFinanceBean;
import com.holike.crm.presenter.fragment.MonthDataFinancePresenter;
import com.holike.crm.view.fragment.MonthDataFinanceView;

import java.util.Date;

import butterknife.BindView;

/**
 * Created by gallop on 2019/8/12.
 * Copyright holike possess 2019.
 * 财务本月数据
 */
public class FinanceMonthDataFragment extends MyFragment<MonthDataFinancePresenter,
        MonthDataFinanceView> implements MonthDataFinanceView, MonthDataHelper.Callback {

    @BindView(R.id.tv_time)
    TextView mTimeTextView;
    @BindView(R.id.ll_content)
    LinearLayout mContentLayout;
    private FinanceMonthDataHelper mHelper;

    @Override
    protected MonthDataFinancePresenter attachPresenter() {
        return new MonthDataFinancePresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_month_data_finance;
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.homepage_month_data));
        mHelper = new FinanceMonthDataHelper(this, this);
    }

    @Override
    public void onQuery(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getMonthData(type, cityCode, startDate, endDate);
    }

    @Override
    protected void clickRightMenu(String text, View actionView) {
        mHelper.onSelectCalendar();
    }

    @Override
    public void onSuccess(MonthDataFinanceBean bean) {
        dismissLoading();
        hasData();
        if (mContentLayout.getVisibility() != View.VISIBLE) {
            mContentLayout.setVisibility(View.VISIBLE);
        }
        setRightMenu(R.string.report_select_date);
        mTimeTextView.setText(bean.timeData);
        mHelper.onSuccess(bean);
    }

    @Override
    public void onFailure(String failReason) {
        dismissLoading();
        mContentLayout.setVisibility(View.GONE);
        noNetwork(failReason);
    }

    @Override
    public void reload() {
        mHelper.onQuery();
    }

    @Override
    public void onDestroyView() {
        mHelper.onDestroy();
        super.onDestroyView();
    }
}
