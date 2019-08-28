package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.SegmentTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.activity.analyze.WeekReportActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.DayDepositBean;
import com.holike.crm.bean.WeekDepositBean;
import com.holike.crm.customView.WeekReportChartView;
import com.holike.crm.presenter.fragment.WeekReportPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.WeekReportView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/4/11.
 * 订单交易趋势
 */

public class WeekReportFragment extends MyFragment<WeekReportPresenter, WeekReportView> implements WeekReportView {
    @BindView(R.id.tv_customer_deposit_report_title)
    TextView tvTitle;
    @BindView(R.id.tv_customer_deposit_report_content)
    TextView tvContent;
    @BindView(R.id.tv_customer_deposit_report_data)
    TextView tvData;
    @BindView(R.id.tv_week_report_table_title)
    TextView tvTableTiele;
    @BindView(R.id.columnarChartView)
    WeekReportChartView weekReportChartView;
    @BindView(R.id.tab_week_report_order_type)
    CommonTabLayout tabOrderType;
    @BindView(R.id.tab_week_report_time_type)
    SegmentTabLayout tabTimeType;
    private int orderType = WeekReportActivity.TYPE_ORDER_REPORT;
    private int timeType = WeekReportActivity.TYPE_WEEK_REPORT;
    private String time;
    private String money;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private WeekDepositBean weekDepositBean;
    private String[] titleTimes = {"查看周", "查看月"};

    @Override
    protected WeekReportPresenter attachPresenter() {
        return new WeekReportPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        initTab();
        setLeft(getString(R.string.report_title));
        Bundle bundle = getArguments();
        if (bundle != null) {
            timeType = bundle.getInt(Constants.TYPE);
            time = bundle.getString(Constants.TIME);
            money = bundle.getString(Constants.MONEY);
        } else {
            setLeft(getString(R.string.report_title));
        }
        if (money == null) {
            mPresenter.getDepositList(String.valueOf(timeType));
            showLoading();
        } else {
            mPresenter.getDepositInfo(time, String.valueOf(timeType), money);
            showLoading();
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_week_report;
    }


    private void initTab() {
        mTabEntities.add(new TabEntity("订单数", 0, 0));
        mTabEntities.add(new TabEntity("订金额", 0, 0));
        tabOrderType.setTabData(mTabEntities);
        tabOrderType.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    orderType = WeekReportActivity.TYPE_ORDER_REPORT;
                } else {
                    orderType = WeekReportActivity.TYPT_DEPOSIT_REPORT;
                }
                weekReportChartView.setWeekDepositBeans(weekDepositBean.getMoneyData(), orderType);
                setTableTitle();
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        tabTimeType.setTabData(titleTimes);
        tabTimeType.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    timeType = WeekReportActivity.TYPE_WEEK_REPORT;
                } else {
                    timeType = WeekReportActivity.TYPT_MONTH_REPORT;
                }
                mPresenter.getDepositList(String.valueOf(timeType));
                showLoading();
                setTableTitle();
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        tabTimeType.setCurrentTab(0);
    }

    /**
     * 设置表头标题
     */
    private void setTableTitle() {
        if (orderType == WeekReportActivity.TYPE_ORDER_REPORT) {
            if (timeType == WeekReportActivity.TYPE_WEEK_REPORT) {
                tvTableTiele.setText(getString(R.string.report_week_order));
            } else {
                tvTableTiele.setText(getString(R.string.report_month_order));
            }
        } else {
            orderType = WeekReportActivity.TYPT_DEPOSIT_REPORT;
            if (timeType == WeekReportActivity.TYPE_WEEK_REPORT) {
                tvTableTiele.setText(getString(R.string.report_week_deposit));
            } else {
                tvTableTiele.setText(getString(R.string.report_month_deposit));
            }
        }
    }

    /**
     * 获取周/月数据成功
     *
     * @param weekDepositBean
     */
    @Override
    public void getDepositListSuccess(WeekDepositBean weekDepositBean) {
        dismissLoading();
        this.weekDepositBean = weekDepositBean;
        tvTitle.setText(getString(R.string.report_item1_title));
        tvData.setText("¥" + weekDepositBean.getTotalMoney());
        weekReportChartView.setWeekDepositBeans(weekDepositBean.getMoneyData(), orderType);
    }

    /**
     * 获取周/月数据失败
     */
    @Override
    public void getDepositListFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 获取当天数据成功
     */
    @Override
    public void getDepositInfoSuccess(List<DayDepositBean> dayDepositBeans, String time, String money) {
        dismissLoading();
        tvTitle.setText(getString(R.string.customer_deposit_report_title_info));
        tvData.setText("¥" + money);
        weekReportChartView.setDayDepositBeans(dayDepositBeans);
    }

    /**
     * 获取当天数据失败
     */
    @Override
    public void getDepositInfoFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    @OnClick({R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finishFragment();
                break;
        }
    }
}
