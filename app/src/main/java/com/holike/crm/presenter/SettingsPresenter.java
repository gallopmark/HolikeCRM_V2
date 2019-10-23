package com.holike.crm.presenter;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.SettingsModel;
import com.holike.crm.view.SettingsView;

public class SettingsPresenter extends BasePresenter<SettingsView, SettingsModel> {

    public void setRule(String id, String param2) {
        if (getModel() != null) {
            getModel().setRule(id, param2, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onSetupFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSetupSuccess(MyJsonParser.getResultAsString(result), MyJsonParser.getMsgAsString(result));
                    }
                }
            });
        }
    }
}
