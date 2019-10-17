package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.grallopmark.tablayout.CommonTabLayout;
import com.grallopmark.tablayout.SegmentTabLayout;
import com.grallopmark.tablayout.TabEntity;
import com.grallopmark.tablayout.listener.CustomTabEntity;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.DayDepositBean;
import com.holike.crm.bean.WeekDepositBean;
import com.holike.crm.presenter.fragment.WeekReportPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.NumberUtil;
import com.holike.crm.view.fragment.WeekReportView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import galloped.xcode.widget.TitleBar;
import pony.xcode.chart.BarChartView;
import pony.xcode.chart.data.BarChartData;

/**
 * Created by wqj on 2018/4/11.
 * 订单交易趋势
 */

public class OrderTradingTrendFragment extends MyFragment<WeekReportPresenter, WeekReportView> implements WeekReportView {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
//    @BindView(R.id.tv_customer_deposit_report_content)
//    TextView tvContent;
//    @BindView(R.id.tv_customer_deposit_report_data)
//    TextView tvData;
    @BindView(R.id.tv_week_report_table_title)
    TextView mTabTitleTextView;
    @BindView(R.id.barChartView)
    BarChartView mBarChartView;
    @BindView(R.id.tab_order)
    CommonTabLayout mOrderTabLayout;
    @BindView(R.id.tab_time)
    SegmentTabLayout mTimeTabLayout;
    private int mOrderTab = 0;
    private int mTimeType = 1;
    private WeekDepositBean mWeekDepositBean;

    @Override
    protected WeekReportPresenter attachPresenter() {
        return new WeekReportPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        mTitleBar.setNavigationOnClickListener(view -> finishFragment());
        initTab();
//        setLeft(getString(R.string.report_title));
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTimeType = bundle.getInt(Constants.TYPE);
        }
//        else {
//            setLeft(getString(R.string.report_title));
//        }
        initData();
//        if (money == null) {
//            initData();
//        } else {
//            showLoading();
//            mPresenter.getDepositInfo(time, String.valueOf(mTimeType), money);
//        }
    }

    private void initData() {
        showLoading();
        mPresenter.getDepositList(String.valueOf(mTimeType));
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_order_trading_trend;
    }


    private void initTab() {
        List<CustomTabEntity> orderTabList = new ArrayList<>();
        orderTabList.add(new TabEntity("订单数"));
        orderTabList.add(new TabEntity("订金额"));
        mOrderTabLayout.setTabData(orderTabList);
        mOrderTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mOrderTab = position;
                updateBarData();
                setTableTitle();
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mTimeTabLayout.setTabData(new String[]{"查看周", "查看月"});
        mTimeTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    mTimeType = 1;
                } else {
                    mTimeType = 2;
                }
                initData();
                setTableTitle();
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mTimeTabLayout.setCurrentTab(0);
    }

    /**
     * 设置表头标题
     */
    private void setTableTitle() {
        if (mOrderTab == 0) {
            if (mTimeType == 1) {
                mTabTitleTextView.setText(getString(R.string.report_week_order));
            } else {
                mTabTitleTextView.setText(getString(R.string.report_month_order));
            }
        } else {
            if (mTimeType == 1) {
                mTabTitleTextView.setText(getString(R.string.report_week_deposit));
            } else {
                mTabTitleTextView.setText(getString(R.string.report_month_deposit));
            }
        }
    }

    /**
     * 获取周/月数据成功
     */
    @Override
    public void getDepositListSuccess(WeekDepositBean weekDepositBean) {
        dismissLoading();
        this.mWeekDepositBean = weekDepositBean;
//        tvTitle.setText(getString(R.string.report_item1_title));
//        tvData.setText("¥" + weekDepositBean.getTotalMoney());
//        weekReportChartView.setWeekDepositBeans(weekDepositBean.getMoneyData(), mOrderType);
        updateBarData();
    }

    private void updateBarData() {
        if (mWeekDepositBean != null) {
            List<BarChartData> dataList = new ArrayList<>();
            for (WeekDepositBean.MoneyDataBean dataBean : mWeekDepositBean.getMoneyData()) {
                String xAxisText;
                if (TextUtils.isEmpty(dataBean.getMonth())) {
                    xAxisText = dataBean.getStartTime() + "\n至\n" + dataBean.getEndTime();
                } else {
                    xAxisText = dataBean.getMonth();
                }
                float value = mOrderTab == 0 ? dataBean.getCounts() : dataBean.getMoney();
                String description = mOrderTab == 0 ? String.valueOf(dataBean.getCounts()) : String.valueOf(dataBean.getMoney());
                dataList.add(new BarChartData(xAxisText, NumberUtil.doubleValue(value, 1), description));
            }
            updateBarChart(dataList);
        }
    }

    private void updateBarChart(List<BarChartData> dataList){
        mBarChartView.withData(dataList).start();
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
//        tvTitle.setText(getString(R.string.customer_deposit_report_title_info));
//        tvData.setText("¥" + money);
        List<BarChartData> dataList = new ArrayList<>();
        for (DayDepositBean dataBean : dayDepositBeans) {
            float value = dataBean.getMoney();
            String description = String.valueOf(dataBean.getMoney());
            dataList.add(new BarChartData(dataBean.getName(), NumberUtil.doubleValue(value, 1), description));
        }
        updateBarChart(dataList);
//        weekReportChartView.setDayDepositBeans(dayDepositBeans);
    }

    /**
     * 获取当天数据失败
     */
    @Override
    public void getDepositInfoFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

}
