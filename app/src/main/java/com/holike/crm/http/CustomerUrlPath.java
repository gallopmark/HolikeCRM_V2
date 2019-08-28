package com.holike.crm.http;

import com.holike.crm.BuildConfig;

/**
 * Created by gallop on 2019/7/19.
 * Copyright holike possess 2019.
 * 新版客户管理接口 客户管理v2.0
 */
public class CustomerUrlPath {

    /*v2.0版本客户管理接口*/
    private static final String CUSTOMER_MANAGER_V2 = BuildConfig.API_CUSTOMER_MANAGER;

    /*获取客户列表*/
    public static final String URL_CUSTOMER_LIST = BuildConfig.API_HOST + "/member/searchCustomerList";

    /*获取客户状态列表*/
    public static final String URL_CUSTOMER_STATUS_LIST = BuildConfig.API_HOST + "/member/getCustomerList2";

    /*客户详情*/
    public static final String URL_GET_CUSTOMER_DETAIL = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/getCustomerDetailInfo";

    /*客户管理-图片展示*/
    public static final String URL_SHOW_IMAGE = CUSTOMER_MANAGER_V2 + "/file/showImg/";

    /*获取业务字典 GET请求*/
    public static final String URL_GET_SYSTEM_CODE_ITEM = CUSTOMER_MANAGER_V2 + "/sysCodeItem/getItem";

    /*发布留言*/
    public static final String URL_PUBLISH_MESSAGE = CUSTOMER_MANAGER_V2 + "/customerMessageBoard/saveMessage";

    /*创建客户*/
    public static final String URL_CREATE_CUSTOMER = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/createPersonalHouse";

    public static final String URL_ALTER_CUSTOMER = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/updatePersonalInfo";
    /*上传图片*/
    public static final String URL_UPLOAD_IMAGE = CUSTOMER_MANAGER_V2 + "/file/uploadImgs";

    /*分配导购*/
    public static final String URL_ASSIGN_GUIDE = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/assignGuide";

    /*分配设计师*/
    public static final String URL_ASSIGN_DESIGNER = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/assignDesigner";

    /*分配门店*/
    public static final String URL_ASSIGN_SHOP = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/assignShop";

    /*获取省份*/
    public static final String URL_REGION_PROVINCE = CUSTOMER_MANAGER_V2 + "/msgRegion/getMasRegionProvince";

    /*获取区县*/
    public static final String URL_REGION_CHILD = CUSTOMER_MANAGER_V2 + "/msgRegion/getMasRegionCityOrDisTrict";

    /*客户管理-修改房屋信息*/
    public static final String URL_UPDATE_HOUSE_INFO = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/updateHouseInfo";

    /*客户管理-获取门店导购-设计师-业务员*/
    public static final String URL_GET_SHOP_USER = CUSTOMER_MANAGER_V2 + "/sysUser/getShopRoleUser";

    /*获取门店分组信息*/
    public static final String URL_GET_SHOP_GROUP = CUSTOMER_MANAGER_V2 + "/sysPartnerShopGroup/getGroupInfo";

    /*获取门店下的导购人员*/
    public static final String URL_GET_SHOP_GUIDE = CUSTOMER_MANAGER_V2 + "/sysUser/getShopGuide";

    /*获取分组导购*/
    public static final String URL_GET_GROUP_GUIDE = CUSTOMER_MANAGER_V2 + "/sysUser/getGroupGuide";

    public static final String URL_APPOINT_MEASURE = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/appointMeasure";

    /*客户管理-获取当前登录用户信息*/
    public static final String URL_GET_USER_INFO = CUSTOMER_MANAGER_V2 + "/sysUser/getUserInfo";

    /*客户管理-获取经销商活动优惠政策*/
    public static final String URL_GET_ACTIVITY_POLICE = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/getdealerActivityPolicy";

    /*客户管理-客户电话-微信号重复检查*/
    public static final String URL_CHECK_PHONE_WX = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/checkContect";

    /*客户管理-地址重复检查*/
    public static final String URL_CHECK_ADDRESS = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/checkAddress";

    /*客户管理-新增房屋*/
    public static final String URL_ADD_HOUSE_INFO = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/addHouseInfo";

    /*客户管理-获取经销商下所有设计师*/
    public static final String URL_GET_DEALER_DESIGNER = CUSTOMER_MANAGER_V2 + "/sysUser/getDealerDesigner";

    /*保存量尺结果-方案图片记录*/
    public static final String URL_SAVE_HOUSE_SCHEME_IMG = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/saveCustomerHouseSchemeImg";

    /*客户管理-删除量尺，方案图片记录*/
    public static final String URL_DELETE_SCHEME_IMG = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/deleteSchemeImg";

    /*客户管理-上传方案记录*/
    public static final String URL_UPLOAD_PLAN = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/uploadPlan";

    /*客户管理-量尺结果保存*/
    public static final String URL_FINISH_MEASURE = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/finishMeasure";

    /*客户管理-上传安装图-安装效果图*/
    public static final String URL_SAVE_INSTALL_DRAWING_IMG = CUSTOMER_MANAGER_V2 + "/customerInstallInfo/saveInstallInfoImg";

    /*上传安装图纸备注信息*/
    public static final String URL_SAVE_INSTALL_DRAWING = CUSTOMER_MANAGER_V2 + "/customerInstallInfo/saveInstallImgInfo";

    /*客户管理-删除安装图，效果图片记录*/
    public static final String URL_DELETE_INSTALL_IMG = CUSTOMER_MANAGER_V2 + "/customerInstallInfo/deleteInstallImg";

    /*客户管理-流失房屋*/
    public static final String URL_LEAVE_HOUSE = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/leaveHouse";

    public static final String URL_SUPERVISOR_ROUNDS = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/reviewHousePlan";
    /*客户管理-安装完成*/
    public static final String URL_SAVE_INSTALL_USER = CUSTOMER_MANAGER_V2 + "/customerInstallUserInfo/saveInstallUserInfo";

    /*客户管理-获取经销商安装工*/
    public static final String URL_GET_DEALER_INSTALLER = CUSTOMER_MANAGER_V2 + "/sysUser/getDealerInstaller";

    /*客户管理-预约安装*/
    public static final String URL_SAVE_UNINSTALL = CUSTOMER_MANAGER_V2 + "/customerInstallInfo/saveInstallInfo";

    /*客户管理-获取门店收款人*/
    public static final String URL_GET_SHOP_PAYMENT = CUSTOMER_MANAGER_V2 + "/sysUser/getShopPaymentUser";

    /*客户管理-收取订金，尾款，退款*/
    public static final String URL_SAVE_PAYMENT = CUSTOMER_MANAGER_V2 + "/customerPayment/savePayment";

    /*客户管理-收取订金，尾款，合同款图片记录*/
    public static final String URL_SAVE_PAYMENT_IMG = CUSTOMER_MANAGER_V2 + "/customerPaymentImg/saveCustomerPaymentImg";

    /*合同登记*/
    public static final String URL_SAVE_HOUSE_CONTRACT = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/saveHousecontract";

    /*客户管理-添加通话记录*/
    public static final String URL_SAVE_PHONE_RECORD = CUSTOMER_MANAGER_V2 + "/customerPhoneRecord/savePhoneRecord";

    /*客户管理-同意/拒绝分配*/
    public static final String URL_REDISTRIBUTE = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/redistributeCustomer";

    /*客户管理-公海客户-领取客户*/
    public static final String URL_RECEIVE_HOUSE = CUSTOMER_MANAGER_V2 + "/customerHouseInfo/receiveHouse";

    /*客户管理-公海历史记录(返回参数与客户详情一致)当房屋信息highSeasHisytoryFlag='Y'时存在历史记录*/
    public static final String URL_GET_HIGH_SEAS_HISTORY = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/getHighSeasHistory";

    /*客户管理-推送系统消息(重新分配客户)*/
    public static final String URL_REDISTRIBUTION_PUSH = CUSTOMER_MANAGER_V2 + "/customerPersonalInfo/redistributionMsgPush";
}
