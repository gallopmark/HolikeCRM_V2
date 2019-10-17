package com.holike.crm.presenter.fragment;

import android.app.Activity;
import android.content.Intent;

import com.holike.crm.activity.login.LoginActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.bean.UserInfoBean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.fragment.MineModel;
import com.holike.crm.util.AppUtils;
import com.holike.crm.util.ParseUtils;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.fragment.MineView;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wqj on 2018/2/24.
 * 我的
 */

public class MinePresenter extends BasePresenter<MineView, MineModel> {

    /*检测更新*/
    public void checkForUpdate() {
        UpdateBean updateBean = IntentValue.getInstance().getUpdateBean();
        if (updateBean != null) { //如果缓存中保存值 则取值（即在首页检测版本更新时将值存入了缓存）
            checkForUpdateSuccess(updateBean);
        } else { //否则请求版本检测
            if (getModel() != null) {
                getModel().checkVersion(new RequestCallBack<UpdateBean>() {
                    @Override
                    public void onFailed(String failReason) {
                        if (getView() != null) {
                            getView().checkForUpdateFailure(failReason);
                        }
                    }

                    @Override
                    public void onSuccess(UpdateBean bean) {
                        IntentValue.getInstance().setUpdateBean(bean);
                        checkForUpdateSuccess(bean);
                    }
                });
            }
        }
    }

    private void checkForUpdateSuccess(UpdateBean updateBean) {
        if (getView() != null) {
            long currentVersion = AppUtils.getVersionCode(); //当前版本
            long serviceVersion = ParseUtils.parseLong(updateBean.getVersion());
            getView().checkForUpdateSuccess(updateBean, serviceVersion > currentVersion);
        }
    }

    /*
     * 检测系统版本更新
     */
//    public void checkVersion(final boolean isDownload) {
//        String updateJson = SharedPreferencesUtils.getString(Constants.UPDATE_BEAN);
//        UpdateBean updateBean = new Gson().fromJson(updateJson, UpdateBean.class);
//        if (updateBean != null) {
//            MainPresenter.setUpdateType(updateBean);
//            if (updateBean.getType() != 0) {
//                if (getView() != null)
//                    getView().hasNewVersion(updateBean, isDownload);
//            } else {
//                if (getView() != null)
//                    getView().isLastVersion();
//            }
//        }
//    }

    /**
     * 退出登录
     */
    public static void logout(Activity activity) {
        JPushInterface.deleteAlias(activity.getApplicationContext(), 0);
        SharedPreferencesUtils.clear(); //退出登录清空本地数据
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("logout", true);
        activity.startActivity(intent);
        activity.finish();
        MyApplication.getInstance().resetSystem();
//        SharedPreferencesUtils.saveString(Constants.CLI_ID, null);
//        SharedPreferencesUtils.saveString(Constants.USER_ID, null);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        if (getModel() != null) {
            getModel().getUserInfo(new RequestCallBack<UserInfoBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null)
                        getView().getUserInfoFailed(failReason);
                }

                @Override
                public void onSuccess(UserInfoBean userInfoBean) {
                    if (getView() != null)
                        getView().getUserInfoSuccess(userInfoBean);
                }
            });
        }
    }
}
