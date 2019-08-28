package com.holike.crm.presenter.activity;

import android.app.Activity;
import android.text.TextUtils;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.model.activity.MainModel;
import com.holike.crm.util.Constants;
import com.holike.crm.util.IOUtil;
import com.holike.crm.util.PackageUtil;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.view.activity.MainView;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wqj on 2017/11/27.
 * 主页presenter
 */

public class MainPresenter extends BasePresenter<MainView, MainModel> {

    /**
     * 检测系统版本更新
     */
    public void checkVersion() {
        model.checkVersion(new MainModel.checkListener() {
            @Override
            public void success(UpdateBean updateBean) {
                setUpdateType(updateBean);
                if (updateBean.getType() != 0) {
                    if (getView() != null)
                        getView().hasNewVersion(updateBean);
                }
                SharedPreferencesUtils.saveString(Constants.UPDATE_BEAN, updateBean.toString());
            }

            @Override
            public void failed(String reult) {
                if (getView() != null) getView().onFailure();
            }
        });
    }

    /**
     * 设置版本更新类型
     *
     * @param updateBean
     */
    public static void setUpdateType(UpdateBean updateBean) {
        int localVersion = PackageUtil.getVersionCode(IOUtil.getCachePath() + "/" + "CRM.apk");     //本地安装包版本
        int netVersion = Integer.parseInt(updateBean.getVersion());                                                      //线上版本
        int currentVersion = PackageUtil.getVersionCode();                                                               //当前版本
        if (netVersion > currentVersion || localVersion > currentVersion) {
            if (netVersion > localVersion) {
                updateBean.setType(UpdateBean.TYPE_DOWNLOAD);
            } else {
                updateBean.setType(UpdateBean.TYPE_INSTALL);
            }
        }
    }

    /**
     * 设置别名
     */
    public static void setAlias() {
        if (!TextUtils.isEmpty(SharedPreferencesUtils.getString(Constants.USER_ID))) {
            JPushInterface.setAlias(MyApplication.getInstance(), 0, SharedPreferencesUtils.getString(Constants.USER_ID).replace("-", "_"));
        }
//        JPushInterface.resumePush(activity.getApplicationContext());
    }

}
