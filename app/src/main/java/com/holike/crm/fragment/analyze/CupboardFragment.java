package com.holike.crm.fragment.analyze;

import com.holike.crm.R;
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

public class CupboardFragment extends PerformanceFragment {
    @Override
    protected void init() {
        super.init();
        setStatusBar();
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
        CupboardBean bean = (CupboardBean) bundle.getSerializable(Constants.CUPBOARD_BEAN);
        if (bean != null) {
            getCupboardDataSuccess(bean);
        } else {
            getData();
        }
    }

    /**
     * 获取橱柜数据成功
     *
     * @param bean
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
     *
     * @param selectDataBeans
     */
    @Override
    protected void setSelectTab(List<PerformanceBean.SelectDataBean> selectDataBeans) {
        tabType.setCurrentTab(mPresenter.getSelectPosition(time, selectDataBeans, 2));
    }

    /**
     * 切换导航
     *
     * @param selectDataBean
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
     *
     * @param params
     */
    @Override
    protected void openArea(Map<String, Serializable> params) {
        startFragment(params, new CupboardFragment());
    }
}
