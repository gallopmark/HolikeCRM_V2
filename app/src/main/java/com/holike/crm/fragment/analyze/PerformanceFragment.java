package com.holike.crm.fragment.analyze;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.grallopmark.tablayout.SlidingTabLayout;
import com.grallopmark.tablayout.listener.OnTabSelectListener;
import com.holike.crm.R;
import com.holike.crm.base.MyFragment;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.bean.PerformanceBean;
import com.holike.crm.bean.staticbean.JumpBean;
import com.holike.crm.presenter.fragment.DealerRankPresenter;
import com.holike.crm.presenter.fragment.PerformancePresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.view.fragment.PerformanceView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wqj on 2018/5/16.
 * 业绩报表
 */

public class PerformanceFragment extends MyFragment<PerformancePresenter, PerformanceView> implements PerformanceView {
    @BindView(R.id.tab_performance_type)
    SlidingTabLayout tabType;
    @BindView(R.id.vp_performance)
    ViewPager vp;
    @BindView(R.id.tv_performance_table_time)
    TextView tvTableTime;
    @BindView(R.id.tv_table_header_today)
    TextView tvToday;
    @BindView(R.id.rv_performance)
    RecyclerView rv;

    protected List<String> tabTitles;
    protected String cityCode;
    protected String type;
    protected String time;
    protected String title;
    protected Bundle bundle;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_performance;
    }

    @Override
    protected PerformancePresenter attachPresenter() {
        return new PerformancePresenter();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar(R.color.bg_state_bar);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        bundle = getArguments();
        if (bundle != null) {
            cityCode = bundle.getString(Constants.CITY_CODE);
            time = bundle.getString(Constants.TIME) == null ? "0" : bundle.getString(Constants.TIME);
            type = bundle.getString(Constants.TYPE);
            title = bundle.getString(Constants.TITLE);
            if (cityCode == null) {
                if (JumpBean.getJumpBack() != null && JumpBean.getJumpBack().equals(getString(R.string.homepage))) {
                    setLeft(getString(R.string.homepage));
                } else {
                    setLeft(getString(R.string.report_title));
                }
            }
            getBean();
        }
        setTitle();
    }

    protected void setTitle() {
        setTitle(TextUtils.isEmpty(title) ? getString(R.string.report_item8_title) : getString(R.string.report_item8_title) + "—" + title);
    }

    /**
     * 获取bean参数
     */
    protected void getBean() {
        PerformanceBean bean = (PerformanceBean) bundle.getSerializable(Constants.PERFORMANCE_BEAN);
        if (bean != null) {
            getDataSuccess(bean);
        } else {
            getData();
        }
    }

    /**
     * 获取数据
     */
    @Override
    public void getData() {
        mPresenter.getData(cityCode, time, type);
        showLoading();
    }

    /**
     * 获取数据失败
     */
    @Override
    public void getDataFailed(String failed) {
        dismissLoading();
        showShortToast(failed);
    }

    /**
     * 获取数据成功
     */
    @Override
    public void getDataSuccess(PerformanceBean performanceBean) {
        dismissLoading();
        tvToday.setText(time.equals("-1") ? getString(R.string.report_table_growth) : getString(R.string.report_table_today));
        initTab(performanceBean.getSelectData());
        tvTableTime.setText(performanceBean.getTimeData());
        showList(performanceBean.getPercentData());
    }

    /**
     * 获取橱柜数据成功
     */
    @Override
    public void getCupboardDataSuccess(CupboardBean bean) {
        dismissLoading();
    }

    /**
     * 显示数据列表
     */
    protected void showList(List<PerformanceBean.PercentDataBean> list) {
        rv.setAdapter(new CommonAdapter<PerformanceBean.PercentDataBean>(mContext, list) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_rv_performance_report;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, PerformanceBean.PercentDataBean percentDataBean, int position) {
                LinearLayout ll = holder.obtainView(R.id.ll_item_rv_performance_report);
                TextView tvArea = holder.obtainView(R.id.tv_item_rv_performance_report_area);
                TextView tvName = holder.obtainView(R.id.tv_item_rv_performance_report_name);
                TextView tvTarget = holder.obtainView(R.id.tv_item_rv_performance_report_target);
                TextView tvComplete = holder.obtainView(R.id.tv_item_rv_performance_report_complete);
                TextView tvCompletePercen = holder.obtainView(R.id.tv_item_rv_performance_report_complete_percen);
                TextView tvRank = holder.obtainView(R.id.tv_item_rv_performance_report_rank);
                TextView tvGrowth = holder.obtainView(R.id.tv_item_rv_performance_report_growth);
                ll.setBackgroundResource(position % 2 == 0 ? R.color.bg_item_order_report : R.color.color_while);
                tvArea.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsClick() == 1 ? R.color.textColor5 : R.color.textColor8));
                tvName.setTextColor(ContextCompat.getColor(mContext, percentDataBean.getIsChange() == 1 ? R.color.textColor17 : R.color.textColor8));
                tvArea.setText(TextUtils.isEmpty(percentDataBean.getArea()) ? "-" : percentDataBean.getArea());
                tvName.setText(TextUtils.isEmpty(percentDataBean.getName()) ? "-" : percentDataBean.getName());
                tvTarget.setText(TextUtils.isEmpty(percentDataBean.getCountsTarget()) ? "-" : percentDataBean.getCountsTarget());
                tvCompletePercen.setText(TextUtils.isEmpty(percentDataBean.getPercentComplete()) ? "-" : percentDataBean.getPercentComplete());
                tvComplete.setText(TextUtils.isEmpty(percentDataBean.getCountsComplete()) ? "-" : percentDataBean.getCountsComplete());
                tvRank.setText(TextUtils.isEmpty(percentDataBean.getRank()) ? "-" : percentDataBean.getRank());
                if (TextUtils.equals(time, "-1")) {
                    tvGrowth.setText(TextUtils.isEmpty(percentDataBean.getPercentThan()) ? "-" : percentDataBean.getPercentThan());
                } else {
                    tvGrowth.setText(TextUtils.isEmpty(percentDataBean.getCountsTodayComplete()) ? "-" : percentDataBean.getCountsTodayComplete());
                }
//                tvGrowth.setText(time.equals("-1") ? percentDataBean.getPercentThan() : percentDataBean.getCountsTodayComplete());
                tvArea.setOnClickListener(v -> {
                    if (percentDataBean.getIsClick() == 1) {
                        Map<String, Serializable> params = new HashMap<>();
                        params.put(Constants.CITY_CODE, percentDataBean.getCityCode());
                        params.put(Constants.TIME, time);
                        params.put(Constants.TYPE, String.valueOf(percentDataBean.getType()));
                        params.put(Constants.TITLE, percentDataBean.getArea());
                        openArea(params);
                    }
                });
            }
        });
    }

    /**
     * 选择地区
     */
    protected void openArea(Map<String, Serializable> params) {
        startFragment(params, new PerformanceFragment());
    }

    /**
     * 切换月份
     */
    protected void initTab(final List<PerformanceBean.SelectDataBean> selectDataBeans) {
        if (tabTitles == null) {
            tabTitles = new ArrayList<>();
            for (PerformanceBean.SelectDataBean bean : selectDataBeans) {
                tabTitles.add(bean.getName());
            }
            vp.setAdapter(DealerRankPresenter.getPagerAdapter(tabTitles.size()));
//            PerformancePresenter.setTabWidth(tabType, tabTitles.size());
            tabType.setupViewPager(vp, tabTitles.toArray(new String[0]));
            tabType.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    selectTab(selectDataBeans.get(position));
                }

                @Override
                public void onTabReselect(int position) {
                }
            });
            setSelectTab(selectDataBeans);
        }
    }

    protected void setSelectTab(List<PerformanceBean.SelectDataBean> selectDataBeans) {
        tabType.setCurrentTab(mPresenter.getSelectPosition(time, selectDataBeans, 1));
    }

    protected void selectTab(PerformanceBean.SelectDataBean selectDataBean) {
        time = String.valueOf(selectDataBean.getSelectTime());
        getData();
    }
}
