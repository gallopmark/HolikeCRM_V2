package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;

/**
 * Created by wqj on 2018/7/16.
 * 售后体验反馈
 */

public interface FeedbackView extends BaseView {
    void saveSuccess(String success);

    void saveFailed(String failed);

    void stateChange();

}
