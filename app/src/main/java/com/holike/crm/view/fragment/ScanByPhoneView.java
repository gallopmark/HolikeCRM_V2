package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.model.event.EventQRCodeScanResult;

import java.util.List;

public interface ScanByPhoneView extends BaseView {
    void onSuccess(String code, List<EventQRCodeScanResult> results);

    void onFail(String reason);

    void onDelayDone();
}
