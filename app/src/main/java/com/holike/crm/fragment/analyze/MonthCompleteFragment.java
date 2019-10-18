package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import android.text.TextUtils;

import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthCompleteBean;
import com.holike.crm.customView.MonthCompleteChartView;
import com.holike.crm.util.Constants;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wqj on 2018/4/18.
 * 各月完成率
 */

public class MonthCompleteFragment extends MyFragment {
    @BindView(R.id.monthCompleteView)
    MonthCompleteChartView monthCompleteChartView;

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
            List<MonthCompleteBean> beans = (List<MonthCompleteBean>) bundle.getSerializable(Constants.MONTH_COMPLETE);
            setTitle(getString(R.string.translate_report_month_complete) + (TextUtils.isEmpty(title) ? "" : "—" + title));
            monthCompleteChartView.setBeans(beans);
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
