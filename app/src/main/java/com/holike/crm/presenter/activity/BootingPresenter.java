package com.holike.crm.presenter.activity;

import android.text.TextUtils;
import android.util.Log;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.model.activity.BootingModel;
import com.holike.crm.view.activity.BootingView;

import java.net.URLDecoder;

/**
 * Created by wqj on 2018/4/13.
 * 启动页
 */

public class BootingPresenter extends BasePresenter<BootingView, BootingModel> {

    /**
     * 获取广告图片
     */
    public void getAd() {
        model.getAd(new BootingModel.GetAdListener() {
            @Override
            public void success(String result) {
                String url = MyJsonParser.getResultAsString(result);
                if (TextUtils.isEmpty(url)) {
                    if (getView() != null)
                        getView().getAdFailed("");
                } else {
                    if (getView() != null)
                        getView().getAdSuccess(URLDecoder.decode(url));
                }
            }

            @Override
            public void failed(String failed) {
                if (getView() != null)
                    getView().getAdFailed(failed);
            }
        });
    }
}
