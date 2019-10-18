package com.holike.crm.fragment.analyze;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SegmentTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.MonthPkBean;
import com.holike.crm.presenter.activity.MonthPkPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.activity.MonthPkView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wqj on 2018/6/7.
 * 月度PK
 */

public class MonthPkFragment extends MyFragment<MonthPkPresenter, MonthPkView> implements MonthPkView {
    @BindView(R.id.tab_month_pk_report_time_type)
    SegmentTabLayout tabTimeType;
    @BindView(R.id.tv_month_pk_table_time)
    TextView tvTableTime;
    @BindView(R.id.rv_month_pk)
    RecyclerView rv;

    private String[] titleTimes = {"A组", "B组"};


    @Override
    protected int setContentViewId() {
        return R.layout.fragment_month_pk;
    }

    @Override
    protected MonthPkPresenter attachPresenter() {
        return new MonthPkPresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        tabTimeType.setTabData(titleTimes);
        tabTimeType.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showLoading();
                mPresenter.getData(String.valueOf(position + 1));
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        tabTimeType.setCurrentTab(0);
        Bundle bundle = getArguments();
        if (bundle != null) {
            MonthPkBean bean = (MonthPkBean) bundle.getSerializable(Constants.MONTH_PK);
            if (bean != null) {
                showList(bean);
            }
        }
    }

    /**
     * 显示数据列表
     *
     * @param bean
     */
    private void showList(final MonthPkBean bean) {
        tvTableTime.setText(bean.getTimeData());
        rv.setAdapter(new CommonAdapter<MonthPkBean.PercentDataBean>(mContext, bean.getPercentData()) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_month_pk_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, MonthPkBean.PercentDataBean percentDataBean, int position) {
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_month_pk_report_area);
                TextView tvCenter = holder.obtainView(R.id.tv_item_rv_month_pk_report_center);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_month_pk_report_name);
                TextView tvCompleteYear = holder.obtainView(R.id.tv_item_rv_month_pk_report_complete_year);
                TextView tvCompleteMonth = holder.obtainView(R.id.tv_item_rv_month_pk_report_complete_month);
                TextView tvScore = holder.obtainView(R.id.tv_item_rv_month_pk_report_score);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_month_pk_report_rank);
                tvArea.setText(percentDataBean.getSales());
                tvCenter.setText(percentDataBean.getArea());
                tvName.setText(percentDataBean.getName());
                tvCompleteYear.setText(percentDataBean.getCountsYearComplete());
                tvCompleteMonth.setText(percentDataBean.getCountsMonthComplete());
                tvScore.setText(String.valueOf(percentDataBean.getPercent()));
                tvRank.setText(percentDataBean.getRank());
            }
        });
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finishFragment();
    }

    @Override
    public void openMonthPk(MonthPkBean bean) {
        dismissLoading();
        showList(bean);
    }

    @Override
    public void openMonthPkPersonal(MonthPkBean bean) {
        dismissLoading();
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDatafailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }
}
