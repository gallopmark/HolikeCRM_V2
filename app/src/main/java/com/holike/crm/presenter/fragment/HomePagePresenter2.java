package com.holike.crm.presenter.fragment;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.model.fragment.HomePageModel;
import com.holike.crm.util.Constants;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.fragment.HomePageView2;

/**
 * Created by wqj on 2018/2/24.
 * 首页
 */

public class HomePagePresenter2 extends BasePresenter<HomePageView2, HomePageModel> {

    /**
     * 获取首页数据
     */
    public void getHomepageData() {
        model.getHomepageData(new HomePageModel.GetHomepageDataListener() {
            @Override
            public void success(HomepageBean bean) {
                if (getView() != null)
                    getView().getHomepageDataSuccess(bean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getHomepageDataFailed(failed);
            }
        });
    }

    /**
     * 保存为读消息数
     */
    public static void setMsgNum(String msgNum) {
        SharedPreferencesUtils.saveString(Constants.MSG_NUM, msgNum);
    }

    /**
     * 获取未读消息数
     */
    public static String getMsgNum() {
        return SharedPreferencesUtils.getString(Constants.MSG_NUM, "0");
    }

    public static int getMsgNum(String msgNum) {
        return ParseUtils.parseInt(msgNum);
    }

    public static boolean isNewMsg() {
        return ParseUtils.parseInt(getMsgNum()) > 0;
    }
}
