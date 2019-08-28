package com.holike.crm.presenter.fragment;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.holike.crm.activity.main.MainActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.bean.UserInfoBean;
import com.holike.crm.model.fragment.MineModel;
import com.holike.crm.presenter.activity.MainPresenter;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.fragment.MineView;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wqj on 2018/2/24.
 * 我的
 */

public class MinePresenter extends BasePresenter<MineView, MineModel> {

    /**
     * 检测系统版本更新
     */
    public void checkVersion(final boolean isDownload) {
        String updateJson = SharedPreferencesUtils.getString(Constants.UPDATE_BEAN);
        UpdateBean updateBean = new Gson().fromJson(updateJson, UpdateBean.class);
        if (updateBean != null) {
            MainPresenter.setUpdateType(updateBean);
            if (updateBean.getType() != 0) {
                if (getView() != null)
                    getView().hasNewVersion(updateBean, isDownload);
            } else {
                if (getView() != null)
                    getView().isLastVersion();
            }
        }
    }

    /**
     * 退出登录
     *
     * @param activity
     */
    public static void logout(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("logout", true);
        activity.startActivity(intent);
        MyApplication.getInstance().resetSystem();
        JPushInterface.deleteAlias(activity.getApplicationContext(), 0);
        SharedPreferencesUtils.clear(); //退出登录清空本地数据
//        SharedPreferencesUtils.saveString(Constants.CLI_ID, null);
//        SharedPreferencesUtils.saveString(Constants.USER_ID, null);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        model.getUserInfo(new MineModel.GetUserInfoListener() {
            @Override
            public void success(UserInfoBean userInfoBean) {
                if (getView() != null)
                    getView().getUserInfoSuccess(userInfoBean);
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getUserInfoFailed(failed);
            }
        });
    }
}
