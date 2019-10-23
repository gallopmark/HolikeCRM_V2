package com.holike.crm.view;

import com.holike.crm.base.BaseView;

public interface SettingsView extends BaseView {

    void onSetupSuccess(String id, String message);

    void onSetupFailure(String failReason);
}
