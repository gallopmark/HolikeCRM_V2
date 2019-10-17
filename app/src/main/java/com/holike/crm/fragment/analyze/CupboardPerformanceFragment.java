package com.holike.crm.fragment.analyze;

import com.holike.crm.R;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.bean.PerformanceBean;
import com.holike.crm.util.Constants;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by wqj on 2018/5/24.
 * 橱柜业绩报表
 * （继承业绩报表）
 */

public class CupboardPerformanceFragment extends PerformanceFragment {
    public static CupboardPerformanceFragment newInstance(CupboardBean bean) {
        IntentValue.getInstance().put(Constants.CUPBOARD_BEAN, bean);
        return new CupboardPerformanceFragment();
    }

    @Override
    protected void init() {
        super.init();
        setStatusBar();
        CupboardBean bean = (CupboardBean) IntentValue.getInstance().removeBy(Constants.CUPBOARD_BEAN);
        if (bean != null) {
            getCupboardDataSuccess(bean);
        } else {
            getData();
        }
    }

    @Override
    protected void setTitle() {
        setTitle(title == null ? getString(R.string.report_item9_title) : getString(R.string.report_item9_title) + "—" + title);
    }

    /**
     * 获取bean参数
     */
    @Override
    protected void getBean() {

    }

    /**
     * 获取橱柜数据成功
     */
    @Override
    public void getCupboardDataSuccess(CupboardBean bean) {
        super.getCupboardDataSuccess(bean);
        tvToday.setText(getString(R.string.report_table_today));
        initTab(bean.getSelectData());
        tvTableTime.setText(bean.getTimeData());
        showList(bean.getPercentData());
    }

    /**
     * 设置当前选中tab
     */
    @Override
    protected void setSelectTab(List<PerformanceBean.SelectDataBean> selectDataBeans) {
        tabType.setCurrentTab(mPresenter.getSelectPosition(time, selectDataBeans, 2));
    }

    /**
     * 切换导航
     */
    @Override
    protected void selectTab(PerformanceBean.SelectDataBean selectDataBean) {
        time = String.valueOf(selectDataBean.getTime());
        getData();
    }

    /**
     * 获取数据
     */
    @Override
    public void getData() {
        showLoading();
        mPresenter.getCupboardData(cityCode, time, type);
    }

    /**
     * 选择地区
     */
    @Override
    protected void openArea(Map<String, Serializable> params) {
        startFragment(params, new CupboardPerformanceFragment());
    }
}
