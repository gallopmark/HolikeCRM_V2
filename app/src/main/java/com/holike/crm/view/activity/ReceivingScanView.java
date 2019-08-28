package com.holike.crm.view.activity;

import com.holike.crm.base.BaseView;
import com.holike.crm.model.event.EventQRCodeScanResult;

import java.util.List;

public interface ReceivingScanView extends BaseView {
    void onSuccess(String result);

    void onAddResultSuccess(List<EventQRCodeScanResult> results, boolean canClean);

    void onFail(String result);

    void onAddResultFail(int reason);

    void onScanGunSuccess(String scanResult);
}
