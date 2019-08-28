package com.holike.crm.presenter.activity;

import android.text.TextUtils;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.customer.GeneralCustomerModel;
import com.holike.crm.view.activity.CustomerV2View;

import java.util.Date;

/**
 * Created by gallop 2019/7/5
 * Copyright (c) 2019 holike
 */
public class CustomerListPresenter extends BasePresenter<CustomerV2View, GeneralCustomerModel> {
    public void getCustomerList(String searchContent, String source, String status,
                                Date startDate, Date endDate, String orderBy,
                                int pageNo, int pageSize) {
        if (TextUtils.isEmpty(searchContent)) searchContent = "";
        if (TextUtils.isEmpty(source)) source = "";
        if (TextUtils.isEmpty(status)) status = "";
        if (getModel() != null) {
            getModel().getCustomerList(searchContent, status, source, orderBy, startDate, endDate, pageNo,
                    pageSize, new RequestCallBack<CustomerListBeanV2>() {
                        @Override
                        public void onFailed(String failReason) {
                            if (getView() != null) {
                                getView().getCustomerListFailed(failReason);
                            }
                        }

                        @Override
                        public void onSuccess(CustomerListBeanV2 bean) {
                            if (getView() != null) {
                                getView().getCustomerListSuccess(bean);
                            }
                        }
                    });
        }
    }

    public void getSystemCode() {
        if (getModel() != null) {
            getModel().getSystemCodeItems(new RequestCallBack<SysCodeItemBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onGetSystemCodeFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(SysCodeItemBean bean) {
                    if (getView() != null) {
                        getView().onGetSystemCodeSuccess(bean);
                    }
                }
            });
        }
    }

    /**
     * 获取客户选择条件数据
     */
//    public void getTypeId() {
//        if (getModel() != null) {
//            getModel().getTypeId(new RequestCallBack<TypeIdBean>() {
//                @Override
//                public void onFailed(String failReason) {
//                    if (getView() != null) {
//                        getView().onGetSystemCodeFailure(failReason);
//                    }
//                }
//
//                @Override
//                public void onSuccess(TypeIdBean bean) {
//                    if (getView() != null) {
//                        getView().onGetSystemCodeSuccess(bean);
//                    }
//                }
//            });
//        }
//    }

    /*删除客户*/
    public void deleteCustomer(String houseId, final int position) {
        if (getModel() != null) {
            getModel().deleteCustomer(houseId, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) getView().deleteCustomerFailure(failReason);
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) getView().deleteCustomerSuccess(result, position);
                }
            });
        }
    }

    public void saveCallRecord(String body) {
        if (getModel() != null) {
            getModel().savePhoneRecord(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onSavePhoneRecordFailure(body, failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSavePhoneRecordSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*分配门店*/
    public void assignShop(String houseId, String shopId, String groupId, String guideId) {
        if (getModel() != null) {
            getModel().assignShop(houseId, shopId, groupId, guideId, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().distributionShopFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().distributionShopSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }
}
