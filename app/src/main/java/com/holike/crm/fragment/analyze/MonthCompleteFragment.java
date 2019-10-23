package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.util.Constants;
import com.holike.crm.util.ParseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pony.xcode.chart.BarChartView;
import pony.xcode.chart.data.BarChartData;

/**
 * Created by wqj on 2018/4/18.
 * 各月完成率
 */

public class MonthCompleteFragment extends MyFragment {
//    @BindView(R.id.monthCompleteView)
//    MonthCompleteChartView monthCompleteChartView;

    @BindView(R.id.barChartView)
    BarChartView mBarChartView;

    public static MonthCompleteFragment newInstance(String title, List<MonthCompleteBean> dataList) {
        IntentValue.getInstance().put(Constants.MONTH_COMPLETE, dataList);
        MonthCompleteFragment fragment = new MonthCompleteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_transparent);
        setTitleBackground(R.color.bg_transparent);
        setTitle(getString(R.string.translate_report_month_complete));
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString(Constants.TITLE);
            setTitle(getString(R.string.translate_report_month_complete) + (TextUtils.isEmpty(title) ? "" : "—" + title));
        }
        List<MonthCompleteBean> dataList = (List<MonthCompleteBean>) IntentValue.getInstance().get(Constants.MONTH_COMPLETE);
        if (dataList != null && !dataList.isEmpty()) {
            List<BarChartData> chartDataList = new ArrayList<>();
            String[] xAxisTextArray = new String[dataList.size()];
            String[] subXAxisTextArray = new String[dataList.size()];
            for (int i = 0; i < dataList.size(); i++) {
                MonthCompleteBean bean = dataList.get(i);
                float value = ParseUtils.parseFloat(bean.getDepositPercent().replace("%", ""));
                chartDataList.add(new BarChartData("", value, value > 0 ? value + "%" : " "));
                xAxisTextArray[i] = TextUtils.isEmpty(bean.getMonth()) ? "" : bean.getMonth();
                subXAxisTextArray[i] = TextUtils.isEmpty(bean.getDay()) ? "" : bean.getDay().replace("-", "\n至\n");
            }
            mBarChartView.withData(chartDataList, xAxisTextArray, "%")
                    .withSubXAxisTextArray(subXAxisTextArray)
                    .barValueUnitEdge(false)
                    .start();
        }
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_month_complete;
    }

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected void back() {
        finishFragment(0, Constants.RESULT_CODE_REFRESH_ORDER_REPORT, null);
    }

    @Override
    public boolean onBackPressed() {
        finishFragment(0, Constants.RESULT_CODE_REFRESH_ORDER_REPORT, null);
        return true;
    }
}
