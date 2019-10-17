package com.holike.crm.presenter.activity;

import android.text.TextUtils;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.UpdateBean;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.activity.MainModel;
import com.holike.crm.util.AppUtils;
import com.holike.crm.util.Constants;
import com.holike.crm.util.IOUtil;
import com.holike.crm.util.PackageUtil;
import com.holike.crm.util.ParseUtils;
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
        if (getModel() != null) {
            getModel().checkVersion(new RequestCallBack<UpdateBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) getView().onFailure();
                }

                @Override
                public void onSuccess(UpdateBean updateBean) {
                    IntentValue.getInstance().setUpdateBean(updateBean);  //缓存值，我的tab会用到
                    if (getView() != null) {
                        long currentVersion = AppUtils.getVersionCode(); //当前版本
                        long serviceVersion = ParseUtils.parseLong(updateBean.getVersion());
                        getView().onGetVersion(updateBean, serviceVersion > currentVersion);
                    }
                }
            });
        }
    }

    /**
     * 设置版本更新类型
     *
     * @param updateBean
     */
    public static void setUpdateType(UpdateBean updateBean) {
        int localVersion = PackageUtil.getVersionCode(IOUtil.getCachePath() + "/" + "CRM.apk");     //本地安装包版本
        int netVersion = ParseUtils.parseInt(updateBean.getVersion());                                                      //线上版本
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
