package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.TranslateReportBean;
import com.holike.crm.model.fragment.TranslateReportModel;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.view.fragment.TranslateReportView;

/**
 * Created by wqj on 2018/4/12.
 * 订金转化报表
 */

public class TranslateReportPresenter extends BasePresenter<TranslateReportView, TranslateReportModel> {

    /**
     * 获取数据
     *
     * @param cityCode
     * @param startTime
     * @param endTime
     * @param type
     */
    public void getData(String cityCode, String startTime, String endTime, String type) {
        model.getData(cityCode == null ? "" : cityCode, startTime == null ? "" : String.valueOf(ParseUtils.parseLong(startTime)), endTime == null ? "" : String.valueOf(ParseUtils.parseLong(endTime) ), type == null ? "" : type, new TranslateReportModel.GetDataListener() {//时间先转化成秒
            @Override
            public void success(TranslateReportBean bean) {
                if (getView() != null)
                    getView().getDataSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getDataFailed(failed);
            }
        });
    }
}
