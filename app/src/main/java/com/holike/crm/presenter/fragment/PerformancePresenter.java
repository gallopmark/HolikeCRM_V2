package com.holike.crm.presenter.fragment;

import com.grallopmark.tablayout.SlidingTabLayout;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.CupboardBean;
import com.holike.crm.bean.PerformanceBean;
import com.holike.crm.http.UrlPath;
import com.holike.crm.model.fragment.PerformanceModel;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.view.fragment.PerformanceView;

import java.util.List;

/**
 * Created by wqj on 2018/5/16.
 * 业绩报表
 */

public class PerformancePresenter extends BasePresenter<PerformanceView, PerformanceModel> {

    /**
     * 获取业绩报表数据
     *
     * @param cityCode
     * @param type
     */
    public void getData(String cityCode, String time, String type) {
        model.getData(cityCode == null ? "" : cityCode, time == null ? "0" : time, type == null ? "" : type, UrlPath.URL_GET_PERFORMANCE_REPORT, new PerformanceModel.GetDataListener() {
            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }

            @Override
            public void success(PerformanceBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }
        });
    }

    /**
     * 获取橱柜报表数据
     *
     * @param cityCode
     * @param time
     * @param type
     */
    public void getCupboardData(String cityCode, String time, String type) {
        model.getCupboardData(cityCode == null ? "" : cityCode, time == null ? "0" : time, type == null ? "" : type, UrlPath.URL_GET_CUP_BOARD_REPORT, new PerformanceModel.GetCupboardListener() {
            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }

            @Override
            public void success(CupboardBean bean) {
                if (getView() != null)
                    getView().getCupboardDataSuccess(bean);
            }
        });
    }

    /**
     * 获取选择位置
     *
     * @param time
     * @param selectDataBeans
     * @param type            1:业绩报表  2：橱柜
     * @return
     */

    public int getSelectPosition(String time, List<PerformanceBean.SelectDataBean> selectDataBeans, int type) {
        if (time == null) {
            return 1;
        } else {
            for (int i = 0, size = selectDataBeans.size(); i < size; i++) {
                PerformanceBean.SelectDataBean bean = selectDataBeans.get(i);
                if (type == 1) {
                    if (String.valueOf(bean.getSelectTime()).equals(time)) {
                        return i;
                    }
                } else {
                    if (bean.getTime().equals(time)) {
                        return i;
                    }
                }
            }
        }
        return 1;
    }

    /**
     * 设置导航子项宽度
     *
     * @param tabType
     * @param size
     */
    public static void setTabWidth(SlidingTabLayout tabType, int size) {
        int tabWidth;
        if (size == 0) {
            tabWidth = MyApplication.getInstance().screenWidth;
        } else {
            tabWidth = MyApplication.getInstance().screenWidth / size;
        }
        tabType.setTabWidth(tabWidth < tabType.getTabWidth() ? DensityUtil.px2dp(tabType.getTabWidth()) : DensityUtil.px2dp(tabWidth));
    }
}
