package com.holike.crm.presenter.fragment;

import android.content.Context;

import androidx.annotation.NonNull;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.base.IntentValue;
import com.holike.crm.bean.ActivityPoliceBean;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.bean.CustomerOnlineLogBean;
import com.holike.crm.bean.DealerInfoBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.model.customer.GeneralCustomerModel;
import com.holike.crm.view.fragment.GeneralCustomerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pony on 2019/8/1.
 * Copyright holike possess 2019.
 * 客户管理
 */
public class GeneralCustomerPresenter extends BasePresenter<GeneralCustomerView, GeneralCustomerModel> {

    /*获取业务字典*/
    public void getSystemCode() {
        if (getModel() != null) {
            getModel().getSystemCodeItems(new RequestCallBack<SysCodeItemBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(SysCodeItemBean itemBean) {
                    if (getView() != null) {
                        IntentValue.getInstance().setSystemCode(itemBean);
                        getView().onSuccess(itemBean);
                    }
                }
            });
        }
    }

    /*获取当前登录用户信息*/
    public void getUserInfo() {
        if (getModel() != null) {
            getModel().getUserInfo(new RequestCallBack<CurrentUserBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(CurrentUserBean bean) {
                    IntentValue.getInstance().setCurrentUserInfo(bean);
                    if (getView() != null) {
                        getView().onSuccess(bean);
                    }
                }
            });
        }
    }

    /*get请求*/
    public void getCustomerDetail(String personalId, boolean isHighSeasHouse) {
//        personalId = "C2019070000747"; //测试公海客户
        if (getModel() != null) {
            getModel().doGet(personalId, isHighSeasHouse, new RequestCallBack<CustomerManagerV2Bean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null)
                        getView().onFailure(failReason);
                }

                @Override
                public void onSuccess(CustomerManagerV2Bean bean) {
                    if (getView() != null)
                        getView().onSuccess(bean);
                }
            });
        }
    }

//    /*已改为get请求*/
//    @Deprecated
//    public void request(String personalId) {
//        if (getModel() != null) {
//            getModel().doRequest(personalId, new RequestCallBack<CustomerManagerV2Bean>() {
//                @Override
//                public void onFailed(String failReason) {
//                    if (getView() != null)
//                        getView().onFailure(failReason);
//                }
//
//                @Override
//                public void onSuccess(CustomerManagerV2Bean bean) {
//                    if (getView() != null)
//                        getView().onSuccess(bean);
//                }
//            });
//        }
//    }

    /*发布留言*/
    public void publishMessage(String houseId, String message) {
        if (getModel() != null) {
            getModel().publishMessage(houseId, message, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*公海客户领取房屋*/
    public void receiveHouse(String houseId, String shopId, String groupId, String salesId) {
        if (getModel() != null) {
            String body = ParamHelper.Customer.receiveHouse(houseId, shopId, groupId, salesId);
            getModel().receiveHouse(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*客户管理-确认流失房屋*/
    public void confirmLostHouse(String houseId) {
        if (getModel() != null) {
            getModel().confirmLoseHouse(houseId, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*获取活动优惠政策*/
    public void getActivityPolice(String dealerId) {
        if (getModel() != null) {
            getModel().getActivityPolice(dealerId, new RequestCallBack<List<ActivityPoliceBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<ActivityPoliceBean> list) {
                    if (getView() != null) {
                        getView().onSuccess(list);
                    }
                }
            });
        }
    }

    /*获取活动优惠政策*/
    public void getActivityPolice(String dealerId, final OnQueryActivityPoliceCallback callback) {
        if (getModel() != null) {
            getModel().getActivityPolice(dealerId, new RequestCallBack<List<ActivityPoliceBean>>() {
                @Override
                public void onFailed(String failReason) {
                    callback.onQueryActivityPoliceFailure(failReason);
                }

                @Override
                public void onSuccess(List<ActivityPoliceBean> list) {
                    callback.onQueryActivityPolice(list);
                }
            });
        }
    }

    public interface OnQueryActivityPoliceCallback {
        void onQueryActivityPoliceFailure(String failReason);

        void onQueryActivityPolice(List<ActivityPoliceBean> dataList);
    }

    /*客户管理-获取门店导购-设计师-业务员*/
    public void getShopRoleUser(String shopId) {
        if (getModel() != null) {
            getModel().getShopRoleUser(shopId, new RequestCallBack<ShopRoleUserBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(ShopRoleUserBean result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*获取门店分组信息*/
    public void getShopGroup(String shopId) {
        if (getModel() != null) {
            getModel().getShopGroup(shopId, new RequestCallBack<List<ShopGroupBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<ShopGroupBean> result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    public void getShopGroup(String shopId, final OnQueryShopGroupListener listener) {
        if (getModel() != null) {
            getModel().getShopGroup(shopId, new RequestCallBack<List<ShopGroupBean>>() {
                @Override
                public void onFailed(String failReason) {
                    listener.onQueryFailure(failReason);
                }

                @Override
                public void onSuccess(List<ShopGroupBean> result) {
                    listener.onQuerySuccess(result);
                }
            });
        }
    }

    public void getShopGroupByUser(String shopId, final OnQueryShopGroupListener listener) {
        if (getModel() != null) {
            if (getModel() != null) {
                getModel().getShopGroupByUser(shopId, new RequestCallBack<List<ShopGroupBean>>() {
                    @Override
                    public void onFailed(String failReason) {
                        listener.onQueryFailure(failReason);
                    }

                    @Override
                    public void onSuccess(List<ShopGroupBean> result) {
                        listener.onQuerySuccess(result);
                    }
                });
            }
        }
    }

    public interface OnQueryShopGroupListener {
        void onQuerySuccess(List<ShopGroupBean> result);

        void onQueryFailure(String failReason);
    }

    /*获取门店导购*/
    public void getShopGuide(String shopId, final OnQueryRoleUserCallback callBack) {
        if (getModel() != null) {
            getModel().getShopGuide(shopId, new RequestCallBack<List<ShopRoleUserBean.UserBean>>() {
                @Override
                public void onFailed(String failReason) {
                    callBack.onFailure(failReason);
                }

                @Override
                public void onSuccess(List<ShopRoleUserBean.UserBean> result) {
                    callBack.onSuccess(result);
                }
            });
        }
    }

    /*获取导购人员回调*/
    public interface OnQueryRoleUserCallback {
        void onSuccess(List<ShopRoleUserBean.UserBean> list);

        void onFailure(String failReason);
    }

    /*获取分组导购*/
    public void getGroupGuide(String groupId, final OnQueryRoleUserCallback callBack) {
        if (getModel() != null) {
            getModel().getGroupGuide(groupId, new RequestCallBack<List<ShopRoleUserBean.UserBean>>() {
                @Override
                public void onFailed(String failReason) {
                    callBack.onFailure(failReason);
                }

                @Override
                public void onSuccess(List<ShopRoleUserBean.UserBean> result) {
                    callBack.onSuccess(result);
                }
            });
        }
    }

    /*获取量尺人员*/
    public void getMeasurePerson(String shopId, @NonNull final OnQueryRoleUserCallback callback) {
        if (getModel() != null) {
            getModel().getMeasurePerson(shopId, new RequestCallBack<List<ShopRoleUserBean.UserBean>>() {
                @Override
                public void onFailed(String failReason) {
                    callback.onFailure(failReason);
                }

                @Override
                public void onSuccess(List<ShopRoleUserBean.UserBean> list) {
                    callback.onSuccess(list);
                }
            });
        }
    }

    /*新建客户*/
    public void createCustomer(String body) {
        if (getModel() != null) {
            getModel().createCustomer(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String json) {
                    if (getView() != null)
                        getView().onSuccess(json);
                }
            });
        }
    }

    /*修改客户信息*/
    public void alterCustomer(String body) {
        if (getModel() != null) {
            getModel().alterCustomer(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String json) {
                    if (getView() != null)
                        getView().onSuccess(json);
                }
            });
        }
    }

    /*分配导购*/
    public void assignGuide(String body) {
        if (getModel() != null) {
            getModel().assignGuide(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*分配设计师*/
    public void assignDesigner(String params) {
        if (getModel() != null) {
            getModel().assignDesigner(params, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*预约量房*/
    public void appointMeasure(String body) {
        if (getModel() != null) {
            getModel().appointMeasure(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*新增房屋*/
    public void addHouseInfo(String body) {
        if (getModel() != null) {
            getModel().addHouseInfo(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*修改房屋信息*/
    public void updateHouseInfo(String body) {
        if (getModel() != null) {
            getModel().updateHouseInfo(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*获取经销商下所有设计师*/
    @Deprecated
    public void getDealerDesigner() {
        if (getModel() != null) {
            getModel().getDealerDesigner(new RequestCallBack<List<DealerInfoBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<DealerInfoBean> list) {
                    if (getView() != null) {
                        getView().onSuccess(list);
                    }
                }
            });
        }
    }

    /*量尺结果保存*/
    public void saveMeasureResult(Context context, Map<String, Object> params, List<String> imagePaths) {
        if (getModel() != null) {
            getModel().saveMeasureResult(context, params, imagePaths, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*上传方案记录*/
    public void uploadPlan(Context context, List<String> removedImages, String houseId,
                           String bookOrderDate, String product,
                           String series, String style, String remark,
                           List<String> images) {
        if (getModel() != null) {
            getModel().uploadPlan(context, removedImages, houseId, bookOrderDate, product,
                    series, style, remark, images,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailed(String failReason) {
                            if (getView() != null) {
                                getView().onFailure(failReason);
                            }
                        }

                        @Override
                        public void onSuccess(String result) {
                            if (getView() != null) {
                                getView().onSuccess(MyJsonParser.getShowMessage(result));
                            }
                        }
                    });
        }
    }

    /*删除方案图片*/
//    @Deprecated
//    public void deleteSchemeImage(String schemeImgId) {
//        if (getModel() != null) {
//            getModel().deleteSchemeImage(schemeImgId, new RequestCallBack<String>() {
//                @Override
//                public void onFailed(String failReason) {
//                    if (getView() != null) {
//                        getView().onFailure(failReason);
//                    }
//                }
//
//                @Override
//                public void onSuccess(String result) {
//                    if (getView() != null) {
//                        getView().onSuccess(MyJsonParser.getShowMessage(result));
//                    }
//                }
//            });
//        }
//    }

    /*上传安装图纸*/
    public void uploadInstallDrawing(Context context, List<String> removedImages, String houseId, String installId,
                                     String remark, List<String> imagePaths) {
        if (getModel() != null) {
            getModel().uploadInstallDrawing(context, removedImages, houseId, installId, remark, imagePaths, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*客户管理-删除安装图，效果图片记录*/
//    @Deprecated
//    public void deleteInstallImage(String installImgId) {
//        if (getModel() != null) {
//            getModel().deleteInstallImage(installImgId, new RequestCallBack<String>() {
//                @Override
//                public void onFailed(String failReason) {
//                    if (getView() != null) {
//                        getView().onFailure(failReason);
//                    }
//                }
//
//                @Override
//                public void onSuccess(String result) {
//                    if (getView() != null) {
//                        getView().onSuccess(MyJsonParser.getShowMessage(result));
//                    }
//                }
//            });
//        }
//    }

    /*客户管理-流失房屋*/
    public void leaveHouse(String body) {
        if (getModel() != null) {
            getModel().levelHouse(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*客户管理-获取经销商安装工*/
    @Deprecated
    public void getDealerInstaller() {
        if (getModel() != null) {
            getModel().getDealerInstaller(new RequestCallBack<List<DealerInfoBean.UserBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<DealerInfoBean.UserBean> result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*客户管理-获取门店安装工*/
    public void getShopInstallers(String shopId) {
        if (getModel() != null) {
            getModel().getShopInstallers(shopId, new RequestCallBack<List<DealerInfoBean.UserBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<DealerInfoBean.UserBean> result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*客户管理-主管查房*/
    public void supervisorRounds(String body) {
        if (getModel() != null) {
            getModel().supervisorRounds(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*预约安装*/
    public void saveUninstall(String body) {
        if (getModel() != null) {
            getModel().saveUninstall(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*客户管理-安装完成*/
    public void finishInstall(Context context, final Map<String, Object> params, List<String> imagePaths) {
        if (getModel() != null) {
            getModel().finishInstall(context, params, imagePaths, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*客户管理-获取门店收款人*/
    public void getShopPaymentUsers(String shopId) {
        if (getModel() != null) {
            getModel().getShopPaymentUsers(shopId, new RequestCallBack<List<ShopRoleUserBean.UserBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<ShopRoleUserBean.UserBean> result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*客户管理-获取门店签约人*/
    public void getShopContractUsers(String shopId) {
        if (getModel() != null) {
            getModel().getShopContractUsers(shopId, new RequestCallBack<List<ShopRoleUserBean.UserBean>>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(List<ShopRoleUserBean.UserBean> result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*收款*/
    public void savePayment(Context context, Map<String, Object> params, List<String> imagePaths) {
        if (getModel() != null) {
            getModel().savePayment(context, params, imagePaths, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*客户管理-合同登记*/
    public void saveContractRegister(Context context, Map<String, Object> params, List<String> imagePaths) {
        if (getModel() != null) {
            getModel().contractRegister(context, params, imagePaths, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*获取房屋公海历史记录*/
    public void getHighSeasHistory(String personalId, String houseId) {
        if (getModel() != null) {
            getModel().getHighSeasHistory(personalId, houseId, new RequestCallBack<CustomerManagerV2Bean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(CustomerManagerV2Bean result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }

    /*公海客户领取房屋*/
    public void receiveHouse(final String personalId, final String houseId, String shopId,
                             String groupId, String salesId, final OnReceivingCustomerListener listener) {
        if (getModel() != null) {
            String body = ParamHelper.Customer.receiveHouse(houseId, shopId, groupId, salesId);
            getModel().receiveHouse(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    listener.onReceivingFailure(failReason);
                }

                @Override
                public void onSuccess(String result) {
                    listener.onReceivingSuccess(MyJsonParser.getShowMessage(result), personalId, houseId);
                }
            });
        }
    }

    public interface OnReceivingCustomerListener {
        void onReceivingFailure(String failReason);

        void onReceivingSuccess(String message, String personalId, String hosueId);
    }

    /*客户管理-推送系统消息(重新分配客户)*/
    public void distributionMsgPush(String body, final OnDistributionMsgPushListener listener) {
        if (getModel() != null) {
            getModel().distributionMsgPush(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    listener.onMsgPushFailure(failReason);
                }

                @Override
                public void onSuccess(String result) {
                    listener.onMsgPushSuccess(MyJsonParser.getShowMessage(result));
                }
            });
        }
    }

    public interface OnDistributionMsgPushListener {
        void onMsgPushFailure(String failReason);

        void onMsgPushSuccess(String message);
    }

    public void invalidReturn(String body) {
        if (getModel() != null) {
            getModel().invalidReturn(body, new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(String result) {
                    if (getView() != null) {
                        getView().onSuccess(MyJsonParser.getShowMessage(result));
                    }
                }
            });
        }
    }

    /*客户管理-激活线上客户*/
    public void activationCustomer(final String personalId, final String houseId, String
            shopId, String groupId, final OnActivationCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("houseId", ParamHelper.noNullWrap(houseId));
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        params.put("groupId", ParamHelper.noNullWrap(groupId));
        if (getModel() != null) {
            getModel().activationCustomer(ParamHelper.toBody(params), new RequestCallBack<String>() {
                @Override
                public void onFailed(String failReason) {
                    callback.onActivationFailure(failReason);
                }

                @Override
                public void onSuccess(String result) {
                    callback.onActivationSuccess(MyJsonParser.getShowMessage(result), personalId, houseId);
                }
            });
        }
    }

    public interface OnActivationCallback {
        void onActivationFailure(String failReason);

        void onActivationSuccess(String message, String personalId, String houseId);
    }

    /**
     * 线上记录
     *
     * @param customerId 客户id
     */
    public void getCustomerOnlineLog(String customerId) {
        if (getModel() != null) {
            getModel().getCustomerOnlineLog(customerId, new RequestCallBack<CustomerOnlineLogBean>() {
                @Override
                public void onFailed(String failReason) {
                    if (getView() != null) {
                        getView().onFailure(failReason);
                    }
                }

                @Override
                public void onSuccess(CustomerOnlineLogBean result) {
                    if (getView() != null) {
                        getView().onSuccess(result);
                    }
                }
            });
        }
    }
}
