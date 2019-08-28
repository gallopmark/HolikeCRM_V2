package com.holike.crm.presenter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.holike.crm.activity.customer.CustomerStatusListActivity;
import com.holike.crm.activity.homepage.CustomerStateListActivity;
import com.holike.crm.activity.mine.FeedbackActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.HomepageBean;
import com.holike.crm.model.fragment.HomePageModel;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.fragment.HomePageView;

/**
 * Created by wqj on 2018/2/24.
 * 首页
 */

public class HomePagePresenter extends BasePresenter<HomePageView, HomePageModel> {

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

    /*
     * 售后体验反馈
     */
//    public void feedback(Activity activity, HomepageBean homepageBean) {
//        if (activity != null && homepageBean != null) {
//            MyApplication.getInstance().put(FeedbackActivity.FEEDBACK_QUESTIONS, homepageBean);
//            Intent intent = new Intent(activity, FeedbackActivity.class);
//            activity.startActivity(intent);
//        }
//    }

    /**
     * 客户状态列表页
     */
    public void customerStateList(Context context, String state, boolean isBoss) {
        Intent intent = new Intent(context, CustomerStateListActivity.class); //v2.0 ->CustomerStatusListActivity
        intent.putExtra("stateName", state);
        intent.putExtra(Constants.IS_BOSS, isBoss);
        context.startActivity(intent);
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

    public static boolean isNewMsg() {
        try {
            return Integer.parseInt(getMsgNum()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
