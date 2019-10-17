package com.holike.crm.fragment.monthdata;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthDataBossBean;
import com.holike.crm.dialog.MonthDataTipsDialog;
import com.holike.crm.presenter.fragment.MonthDataBossPresenter;
import com.holike.crm.view.fragment.MonthDataBossView;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gallop on 2019/8/12.
 * Copyright holike possess 2019.
 * 老板本月数据
 */
public class BossMonthDataFragment extends MyFragment<MonthDataBossPresenter,
        MonthDataBossView> implements MonthDataHelper.Callback, MonthDataBossView {
    @BindView(R.id.tv_time)
    TextView mTimeTextView;
    @BindView(R.id.ll_content)
    LinearLayout mContentLayout;
    private BossMonthDataHelper mHelper;

    @Override
    protected MonthDataBossPresenter attachPresenter() {
        return new MonthDataBossPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_month_data_boss;
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.homepage_month_data));
        mHelper = new BossMonthDataHelper(this, this);
    }

    @Override
    public void onQuery(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getMonthData(type, cityCode, startDate, endDate);
    }

    @OnClick({R.id.tv_description})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_description) {
            new MonthDataTipsDialog(mContext).show();
        }
    }

    @Override
    public void onSuccess(MonthDataBossBean bean) {
        dismissLoading();
        hasData();
        if (mContentLayout.getVisibility() != View.VISIBLE) {
            mContentLayout.setVisibility(View.VISIBLE);
        }
        setRightMenu(R.string.report_select_date);
        mTimeTextView.setText(bean.timeDetail);
        mHelper.onSuccess(bean);
    }


    @Override
    protected void clickRightMenu(String menuText, View actionView) {
        mHelper.onSelectCalendar();
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
