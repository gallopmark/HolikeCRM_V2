package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.OriginalBoardBean;
import com.holike.crm.model.fragment.OriginalBoardModel;
import com.holike.crm.view.fragment.OriginalBoardView;

/**
 * Created by wqj on 2018/5/25.
 * 原态板占比
 */

public class OriginalBoardPresenter extends BasePresenter<OriginalBoardView, OriginalBoardModel> {
    /**
     * 获取数据
     */
    public void getData(String cityCode, String startTime, String endTime, String time, String type) {
        model.getData(cityCode == null ? "" : cityCode, startTime == null ? "" : String.valueOf(Long.parseLong(startTime)), endTime == null ? "" : String.valueOf(Long.parseLong(endTime)), time == null ? "1" : time, type == null ? "" : type, new OriginalBoardModel.getDataListener() {
            @Override
            public void success(OriginalBoardBean bean) {
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
