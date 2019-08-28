package com.holike.crm.view.fragment;

import com.holike.crm.base.BaseView;
import com.holike.crm.bean.ReceiptDeliveryManifestBean;

import java.util.List;

public interface ReceiptDeliveryManifestView extends BaseView {
    void onInitTopSuccess(List<ReceiptDeliveryManifestBean> s);
    void onInitSideSuccess(List<ReceiptDeliveryManifestBean> s);
    void onInitContentSuccess(List<ReceiptDeliveryManifestBean> s);

    void onFail(String errorMsg);

    void onTopBarSelect(int position, ReceiptDeliveryManifestBean bean);
    void onSerialSelect(int position,List<ReceiptDeliveryManifestBean.PackingListBean.PackingDataBean>  bean);
}
