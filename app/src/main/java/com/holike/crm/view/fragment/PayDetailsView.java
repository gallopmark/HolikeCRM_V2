package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.UploadByRelationBean;

public interface PayDetailsView extends BaseView {
    void onSuccess(String stateCode);

    void onFail(String errorMsg);

    void onRelationSuccess(UploadByRelationBean bean,int loopIndex);
}
