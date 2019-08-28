package com.holike.crm.model.customer;

import android.content.Context;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.ActivityPoliceBean;
import com.holike.crm.bean.CurrentUserBean;
import com.holike.crm.bean.CustomerListBeanV2;
import com.holike.crm.bean.CustomerManagerV2Bean;
import com.holike.crm.bean.DealerInfoBean;
import com.holike.crm.bean.ShopGroupBean;
import com.holike.crm.bean.ShopRoleUserBean;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.bean.TypeIdBean;
import com.holike.crm.http.CallbackHelper;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.http.UploadHelper;
import com.holike.crm.http.UrlPath;
import com.holike.crm.util.Constants;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by gallop on 2019/7/16.
 * Copyright holike possess 2019.
 * 客户管理页面 model
 */
public class GeneralCustomerModel extends BaseModel {

    /*获取业务字典*/
    public void getSystemCodeItems(RequestCallBack<SysCodeItemBean> callBack) {
        get(CustomerUrlPath.URL_GET_SYSTEM_CODE_ITEM, callBack);
    }

    /*获取当前登录用户信息*/
    public void getUserInfo(RequestCallBack<CurrentUserBean> callBack) {
        getByTimeout(CustomerUrlPath.URL_GET_USER_INFO, 60, callBack);
    }

    /*获取活动优惠政策*/
    public void getActivityPolice(String dealerId, RequestCallBack<List<ActivityPoliceBean>> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("dealerId", dealerId);
        get(CustomerUrlPath.URL_GET_ACTIVITY_POLICE, params, callBack);
    }

    /*创建客户*/
    public void createCustomer(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_CREATE_CUSTOMER, body, callBack);
    }

    /*修改客户信息*/
    public void alterCustomer(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_ALTER_CUSTOMER, body, callBack);
    }

    /**
     * 获取客户列表
     */
    public void getCustomerList(String content, String customerStatusId, String customerTypeId,
                                String orderBy, Date startDate, Date endDate,
                                int pageNo, int pageSize, RequestCallBack<CustomerListBeanV2> callBack) {
        String startTime = "";
        if (startDate != null) {
            startTime = TimeUtil.dateToStamp(startDate, false);
        }
        String endTime = "";
        if (endDate != null) {
            endTime = TimeUtil.dateToStamp(endDate, true);
        }
        Map<String, String> params = ParamHelper.Customer.customerList(content, customerStatusId, customerTypeId,
                orderBy, startTime, endTime, pageNo, pageSize);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        postByTimeout(CustomerUrlPath.URL_CUSTOMER_LIST, header, params, 60, callBack);
    }

    /*获取客户状态列表*/
    public void getCustomerStatusList(String statusMove, String earnestStatus, String intentionStatus,
                                      String customerStatus, String tailStatus, String seaStatus,
                                      String orderBy, int pageNo, int pageSize,
                                      RequestCallBack<?> callback) {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        Map<String, String> params = ParamHelper.Customer.customerStatusList(statusMove, earnestStatus, intentionStatus,
                customerStatus, tailStatus, seaStatus, orderBy, pageNo, pageSize);
        post(CustomerUrlPath.URL_CUSTOMER_STATUS_LIST, header, params, callback);
    }

    /**
     * 获取类型id
     */
    public void getTypeId(RequestCallBack<TypeIdBean> callBack) {
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID));
        post(UrlPath.URL_GET_TYPE_ID, header, null, callBack);
    }

    /*删除客户*/
    public void deleteCustomer(String houseId, final RequestCallBack<String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("houseId", houseId);
        post(UrlPath.URL_DELETE_CUSTOMER, null, params, callback);
    }

    /*分配门店*/
    public void assignShop(String houseId, String shopId, String groupId, String guideId, RequestCallBack<String> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("houseId", ParamHelper.noNullWrap(houseId));
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        params.put("groupId", ParamHelper.noNullWrap(groupId));
        params.put("guideId", ParamHelper.noNullWrap(guideId));
        String body = ParamHelper.toBody(params);
        postByBody(CustomerUrlPath.URL_ASSIGN_SHOP, body, callBack);
    }

    /*客户管理-添加通话记录*/
    public void savePhoneRecord(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_SAVE_PHONE_RECORD, null, body, callBack);
    }

    /*客户详细信息查询*/
    @Deprecated
    public void doRequest(String personalId, RequestCallBack<CustomerManagerV2Bean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("personalId", personalId);
        Map<String, String> header = new HashMap<>();
        header.put(Constants.USER_ID, SharedPreferencesUtils.getString(Constants.USER_ID, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        postByTimeout(UrlPath.URL_COMSTOMER_INFO, header, params, 60, callBack);
    }

    public void doGet(String personalId, RequestCallBack<CustomerManagerV2Bean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("personalId", personalId);
        params.put("pageNo", "1");
        params.put("pageSize", "200");
        getByTimeout(CustomerUrlPath.URL_GET_CUSTOMER_DETAIL, null, params, 60, callBack);
    }

    /**
     * 发布留言
     *
     * @param houseId 房屋id
     * @param message 留言记录
     */
    public void publishMessage(String houseId, String message, RequestCallBack<?> callBack) {
        String body = ParamHelper.Customer.publishMessage(houseId, message);
        postByBody(CustomerUrlPath.URL_PUBLISH_MESSAGE, body, callBack);
    }

    /*分配导购*/
    public void assignGuide(String body, final RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_ASSIGN_GUIDE, body, callBack);
    }

    /*分配设计师*/
    public void assignDesigner(String params, final RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_ASSIGN_DESIGNER, params, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                callBack.onFailed(failReason);
            }

            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(MyJsonParser.getShowMessage(result));
            }
        });
    }

    /*预约量尺*/
    public void appointMeasure(String params, final RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_APPOINT_MEASURE, params, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                callBack.onFailed(failReason);
            }

            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(MyJsonParser.getShowMessage(result));
            }
        });
    }

    /*获取门店导购-设计师-业务员*/
    public void getShopRoleUser(String shopId, RequestCallBack<ShopRoleUserBean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        get(CustomerUrlPath.URL_GET_SHOP_USER, params, callBack);
    }

    /*获取门店分组信息*/
    public void getShopGroup(String shopId, RequestCallBack<List<ShopGroupBean>> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        get(CustomerUrlPath.URL_GET_SHOP_GROUP, params, callBack);
    }

    /*获取门店导购*/
    public void getShopGuide(String shopId, RequestCallBack<List<ShopRoleUserBean.UserBean>> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", ParamHelper.noNullWrap(shopId));
        get(CustomerUrlPath.URL_GET_SHOP_GUIDE, params, callBack);
    }

    /*获取分组导购*/
    public void getGroupGuide(String groupId, RequestCallBack<List<ShopRoleUserBean.UserBean>> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("groupId", ParamHelper.noNullWrap(groupId));
        get(CustomerUrlPath.URL_GET_GROUP_GUIDE, params, callBack);
    }

    /*新增房屋信息*/
    public void addHouseInfo(String body, final RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_ADD_HOUSE_INFO, body, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                callBack.onFailed(failReason);
            }

            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(MyJsonParser.getShowMessage(result));
            }
        });
    }

    /*修改房屋信息*/
    public void updateHouseInfo(String body, final RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_UPDATE_HOUSE_INFO, body, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                callBack.onFailed(failReason);
            }

            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(MyJsonParser.getShowMessage(result));
            }
        });
    }

    /*获取经销商下所有设计师*/
    public void getDealerDesigner(final RequestCallBack<?> callBack) {
        getByTimeout(CustomerUrlPath.URL_GET_DEALER_DESIGNER, 60, callBack);
    }

    /*量尺结果保存*/
    public void saveMeasureResult(Context context, Map<String, Object> params, List<String> imagePaths,
                                  final RequestCallBack<String> callBack) {
        addDisposable(CallbackHelper.deliveryResult(UploadHelper.doUpload(context,
                imagePaths, MyJsonParser.fromBeanToJson(params), new UploadHelper.OnUploadProcessListener() {
                    @Override
                    public String toBody(List<String> uploadResourceIds) {
                        params.put("uploadResourceId", uploadResourceIds);
                        return MyJsonParser.fromBeanToJson(params);
                    }

                    @Override
                    public String doOnNextUrl() {
                        return CustomerUrlPath.URL_FINISH_MEASURE;
                    }
                }), callBack));
    }

    /*上传方案*/
    public void uploadPlan(Context context, List<String> removedImages, String houseId, String bookOrderDate, String product,
                           String series, String style, String remark,
                           List<String> images, final RequestCallBack<String> callBack) {
        Map<String, Object> params = ParamHelper.Customer.uploadPlan(removedImages, houseId, bookOrderDate,
                product, series, style, remark);
        String body = MyJsonParser.fromBeanToJson(params);
        addDisposable(CallbackHelper.deliveryResult(UploadHelper.doUpload(context, images, body,
                new UploadHelper.OnUploadProcessListener() {
                    @Override
                    public String toBody(List<String> uploadResourceIds) {
                        params.put("uploadResourceId", uploadResourceIds);
                        return MyJsonParser.fromBeanToJson(params);
                    }

                    @Override
                    public String doOnNextUrl() {
                        return CustomerUrlPath.URL_UPLOAD_PLAN;
                    }
                }), callBack));
    }

    /*删除量尺或方案图片（每次只删除一张）*/
    @Deprecated
    public void deleteSchemeImage(String schemeImgId, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_DELETE_SCHEME_IMG, ParamHelper.Customer.deleteSchemeImg(schemeImgId), callBack);
    }

    /*上传安装图纸*/
    public void uploadInstallDrawing(Context context, List<String> removedImages, final String houseId, final String installId,
                                     final String remark, final List<String> images,
                                     RequestCallBack<String> callBack) {
        final Map<String, Object> params = ParamHelper.Customer.uploadInstallDrawing(removedImages, houseId, installId, remark);
        String body = MyJsonParser.fromBeanToJson(params);
        addDisposable(CallbackHelper.deliveryResult(UploadHelper.doUpload(context, images, body, new UploadHelper.OnUploadProcessListener() {
            @Override
            public String toBody(List<String> uploadResourceIds) {
                params.put("uploadResourceId", uploadResourceIds);
                return MyJsonParser.fromBeanToJson(params);
            }

            @Override
            public String doOnNextUrl() {
                return CustomerUrlPath.URL_SAVE_INSTALL_DRAWING;
            }
        }), callBack));
    }

    /*客户管理-删除安装图，效果图片记录*/
    @Deprecated
    public void deleteInstallImage(String schemeImgId, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_DELETE_INSTALL_IMG, ParamHelper.Customer.deleteSchemeImg(schemeImgId), callBack);
    }

    /*客户管理-流失房屋*/
    public void levelHouse(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_LEAVE_HOUSE, body, callBack);
    }

    /*客户管理-获取经销商安装工*/
    public void getDealerInstaller(RequestCallBack<List<DealerInfoBean.UserBean>> callBack) {
        Map<String, String> params = ParamHelper.general();
        params.put("dealerId", SharedPreferencesUtils.getDealerId());
        getByTimeout(CustomerUrlPath.URL_GET_DEALER_INSTALLER, null, params, 60, callBack);
    }

    /*客户管理-主管查房*/
    public void supervisorRounds(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_SUPERVISOR_ROUNDS, body, callBack);
    }

    /*客户管理-预约安装*/
    public void saveUninstall(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_SAVE_UNINSTALL, body, callBack);
    }

    /*客户管理-安装完成*/
    public void finishInstall(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_SAVE_INSTALL_USER, body, callBack);
    }

    /*客户管理-获取门店收款人*/
    public void getShopPaymentUsers(String shopId, RequestCallBack<List<ShopRoleUserBean.UserBean>> callBack) {
        Map<String, String> params = ParamHelper.general();
        params.put("shopId", shopId);
        getByTimeout(CustomerUrlPath.URL_GET_SHOP_PAYMENT, null, params, 60, callBack);
    }

    /*收款*/
    public void savePayment(Context context, final Map<String, Object> params, List<String> imagePaths, RequestCallBack<String> callBack) {
        addDisposable(CallbackHelper.deliveryResult(UploadHelper.doUpload(context, imagePaths,
                MyJsonParser.fromBeanToJson(params), new UploadHelper.OnUploadProcessListener() {
                    @Override
                    public String toBody(List<String> uploadResourceIds) {
                        params.put("uploadResourceId", uploadResourceIds);
                        return MyJsonParser.fromBeanToJson(params);
                    }

                    @Override
                    public String doOnNextUrl() {
                        return CustomerUrlPath.URL_SAVE_PAYMENT;
                    }
                }), callBack));
    }

    /*客户管理-保存合同登记*/
    public void contractRegister(Context context, Map<String, Object> params, List<String> imagePaths, RequestCallBack<String> callBack) {
        addDisposable(CallbackHelper.deliveryResult(UploadHelper.doUpload(context, imagePaths, MyJsonParser.fromBeanToJson(params), new UploadHelper.OnUploadProcessListener() {
            @Override
            public String toBody(List<String> uploadResourceIds) {
                params.put("uploadResourceId", uploadResourceIds);
                return MyJsonParser.fromBeanToJson(params);
            }

            @Override
            public String doOnNextUrl() {
                return CustomerUrlPath.URL_SAVE_HOUSE_CONTRACT;
            }
        }), callBack));
    }

    /*客户管理-公海客户-领取客户*/
    public void receiveHouse(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_RECEIVE_HOUSE, body, callBack);
    }

    /*客户管理-公海历史记录*/
    public void getHighSeasHistory(String personalId, String houseId, RequestCallBack<CustomerManagerV2Bean> callBack) {
        Map<String, String> params = new HashMap<>();
        params.put("personalId", personalId);
        params.put("houseId", houseId);
        params.put("pageNo", "1");
        params.put("pageSize", "300");
        getByTimeout(CustomerUrlPath.URL_GET_HIGH_SEAS_HISTORY, null, params, 60, callBack);
    }

    /*客户管理-推送系统消息(重新分配客户)*/
    public void distributionMsgPush(String body, RequestCallBack<String> callBack) {
        postByBody(CustomerUrlPath.URL_REDISTRIBUTION_PUSH, body, callBack);
    }
}
