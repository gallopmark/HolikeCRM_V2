package com.holike.crm.fragment.monthdata;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthDataInstallManagerBean;
import com.holike.crm.presenter.fragment.MonthDataInstallManagerPresenter;
import com.holike.crm.view.fragment.MonthDataBossInstallManagerView;

import java.util.Date;

import butterknife.BindView;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 * 安装经理本月数据
 */
public class InstallManagerMonthDataFragment extends MyFragment<MonthDataInstallManagerPresenter,
        MonthDataBossInstallManagerView> implements MonthDataBossInstallManagerView, MonthDataHelper.Callback {

    @BindView(R.id.tv_time)
    TextView mTimeTextView;
    @BindView(R.id.ll_content)
    LinearLayout mContentLayout;
    private InstallManagerMonthDataHelper mHelper;

    public static void open(BaseActivity<?, ?> activity) {
        Intent intent = new Intent(activity, InstallManagerMonthDataFragment.class);
        activity.openActivity(intent);
    }

    @Override
    protected MonthDataInstallManagerPresenter attachPresenter() {
        return new MonthDataInstallManagerPresenter();
    }

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_month_data_installmanager;
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.homepage_month_data));
        mHelper = new InstallManagerMonthDataHelper(this, this);
    }

    @Override
    public void onQuery(String type, String cityCode, Date startDate, Date endDate) {
        showLoading();
        mPresenter.getMonthData(type, cityCode, startDate, endDate);
    }

    @Override
    protected void clickRightMenu(String text,View actionView) {
        mHelper.onSelectCalendar();
    }

    @Override
    public void onSuccess(MonthDataInstallManagerBean bean) {
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
}
