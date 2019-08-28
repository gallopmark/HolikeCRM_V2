package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.FeedbackRecordBean;

import java.util.List;

/**
 * Created by wqj on 2018/7/17.
 * 反馈记录
 */

public interface FeedbackRecordView extends BaseView {
    void refresh(boolean showLoading);

    void loadmore();

    void getRecordSuccess(List<FeedbackRecordBean> list);

    void getRecordFailed(String failed);

    void noRecord();

    void loadAll();

    void loadComplete();
}
