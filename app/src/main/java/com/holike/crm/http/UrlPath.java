package com.holike.crm.http;


import com.holike.crm.BuildConfig;


/*接口地址*/
public class UrlPath {
    private static final String HOST = BuildConfig.API_HOST;

    private static final String HOST_CM = BuildConfig.API_HOST_CM;

    private static final String HOST_CM_SHORT = BuildConfig.API_HOST_CM_SHORT;

    /*登录*/
    public static final String URL_LOGIN = HOST + "/user_system/crmLogin";

    /*查询账号*/
    public static final String URL_CHECK_ACCOUNT = HOST + "/user_system/searchAccount";

    /*广告页图片*/
    public static final String URL_GET_AD = HOST + "/user_system/advertisement";

    /*首页数据*/
    public static final String URL_HOMEPAGE_DATA = HOST + "/member/crmIndex";

    /*获取客户选择条件数据*/
    @Deprecated
    public static final String URL_CUSTOMER_SELECT = HOST + "/member/getType";

    /*获取订单中心条件数据*/
    public static final String URL_ORDER_CENTER_SELECT = HOST + "/member/getType";

    /*获取客户管理选择条件数据*/
    public static final String URL_GET_TYPE_ID = HOST_CM + "/mobile/queryalldicttype";

    /*获取所属人数据*/
    public static final String URL_GET_ASSOCIATE = HOST_CM + "/message/queryPer";

    /*上传文件*/
    public static final String URL_UPLOAD_FILE = HOST_CM + "/fileUpload/uploadnew";

    /*新增/编辑客户或新增*/
    public static final String URL_ADD_DEPOSIT = HOST_CM + "/mobile/createuserinfo";

    /* 客户状态列表*/
    public static final String URL_CUSTOMER_STATE_LIST = HOST + "/member/getCustomerList";

    /*获取客户列表*/
    public static final String URL_GET_CUSTOMER_LIST = HOST_CM + "/mobile/queryuserinfo";

    /*删除客户*/
    public static final String URL_DELETE_CUSTOMER = HOST_CM + "/message/delHouse";

    /*客户详情信息*/
    public static final String URL_COMSTOMER_INFO = HOST_CM + "/message/queryuserdetailinfo";

    /*查询未交定金客户信息*/
    public static final String URL_COLLECT_DEPOSIT_LIST = HOST_CM + "/mobile/queryNotDeposit";

    /*获取分配导购*/
    public static final String URL_GET_DIVIDE_GUIDE = HOST_CM + "/trailsman/queryGuide";

    /*获取分配设计师*/
    public static final String URL_GET_DIVIDE_DESIGER = HOST_CM + "/trailsman/queryAllotSty";

    /*获取收款记录*/
    public static final String URL_GET_COLLECTION = HOST_CM + "/trailsman/queryMoneyMessage";

    /*分配导购/分配设计师/添加沟通记录/预约量房/上传量房结果/收取订金/店长查房/上传方案/签约/
    上传复尺结果/预约安装/安装完成/收尾款/已流失*/
    public static final String URL_WORK_FLOW = HOST_CM + "/trailsman/customermgr";

    /*编辑客户*/
    public static final String URL_EDIT_CUSTOMER = HOST_CM + "/message/updataUser";
    /*编辑房屋*/
    public static final String URL_EDIT_HOUSE = HOST_CM + "/mobile/updataHouse";

    /*撤销*/
    public static final String URL_CANCEL = HOST_CM + "/rule/cancel";

    /*获取上传图片参数*/
    public static final String URL_UPLOAD_PARAM = HOST + "/qiniu/get_param";

    /*获取订单列表*/
    public static final String URL_GET_ORDER_LIST = HOST + "/member/searchOrder";

    /*获取订单详情*/
    public static final String URL_GET_ORDER_DETAILS = HOST + "/member/getOrderInfo";

    /*获取消息/公告列表*/
    public static final String URL_GET_MESSAGE_LIST = HOST + "/member/getMessageList";

    /*获取公告详情*/
    public static final String URL_GET_MESSAGE_INFO = HOST + "/member/getMessageInfo";

    /*获取查看报表权限*/
    public static final String URL_GET_REPORT_PERMISSIONS = HOST + "/member/getNewPowerInfo";

    /*订单交易对趋势*/
    public static final String URL_GET_DEPOSIT = HOST + "/member/getDepositList";

    /*当天订单详情*/
    public static final String URL_GET_DEPOSIT_INFO = HOST + "/member/getDepositInfo";

    /*订单交易报表*/
    public static final String URL_GET_DEPOSIT_REPORT = HOST + "/member/getBusinessList";

    /*订单交易报表-各月完成率*/
    public static final String URL_GET_MONTH_COMPLETE = HOST + "/member/completeList";

    /*订单交易报表-填写目标*/
    public static final String URL_WRITE_DEPOSIT_REPORT_TARGET = HOST + "/member/targetList";

    /*订单交易报表-保存目标*/
    public static final String URL_SAVE_DEPOSIT_REPORT_TARGET = HOST + "/member/targetSubmit";

    /*订单转化报表*/
    public static final String URL_GET_TRANSLATE_REPORT = HOST + "/member/getPercentList";

    /*个人签单排行*/
    public static final String URL_GET_ORDER_RANKINF_REPORT = HOST + "/member/rankList";

    /*成品交易报表*/
    public static final String URL_GET_PRODUCT_TRADING_REPORT = HOST + "/member/productList";

    /*成品交易报表-各月完成率*/
    public static final String URL_GET_PRODUCT_COMPLETE = HOST + "/member/completeProductList";

    /*建店报表*/
    public static final String URL_GET_BUILD_SHOP_REPORT = HOST + "/member/shopList";

    /*业绩报表*/
    public static final String URL_GET_PERFORMANCE_REPORT = HOST + "/member/achievementList";

    /*橱柜业绩报表*/
    public static final String URL_GET_CUP_BOARD_REPORT = HOST + "/member/cupboardList";

    /*原态板占比报表*/
    public static final String URL_GET_ORIGNAL_BOARD_REPORT = HOST + "/member/boardList";

    /*经销商排行*/
    public static final String URL_GET_DEALER_RANK_REPORT = HOST + "/member/achievementRank";

    /*终端大检查*/
    public static final String URL_GET_TERMINAL_CHECK_REPORT = HOST + "/member/terminal";

    /*新零售*/
    public static final String URL_GET_NEW_RETAIL_REPORT = HOST + "/member/retail";

    /*拉网*/
    public static final String URL_NET_REPORT = HOST + "/member/nettingList";

    /*拉网明细*/
    public static final String URL_NET_DETAIL_REPORT = HOST + "/member/nettingDeatilList";

    /*月度pk*/
    public static final String URL_MONTH_PK_REPORT = HOST + "/member/pkList";

    /*主动营销*/
    public static final String URL_ACTIVE_MARKET_REPORT = HOST + "/member/activeData";

    /*主动营销排行*/
    public static final String URL_ACTIVE_MARKET_RANK_REPORT = HOST + "/member/activeRank";

    /*主动营销-填写城市*/
    public static final String URL_ACTIVE_MARKET_REPORT_WRITE_CITY = HOST + "/member/activeRecord";

    /*主动营销-保存城市*/
    public static final String URL_ACTIVE_MARKET_REPORT_SAVE_CITY = HOST + "/member/addRecord";

    /*主动营销-删除城市*/
    public static final String URL_ACTIVE_MARKET_REPORT_DELETE_CITY = HOST + "/member/deleteRecord";

    /*木门业绩报表*/
    public static final String URL_WOODEN_DOOR = HOST + "/member/achievementListForDoor";
    /*评价报表*/
    public static final String URL_EVALUATION_REPORT = HOST + "/member/evaluateList";

    /*获取个人信息*/
    public static final String URL_GET_USERINFO = HOST + "/member/getCrmUserInfo";

    /*修改密码*/
    public static final String URL_CHANGE_PASSWORD = HOST + "/member/modifyPassword";

    /*检测版本更新*/
    public static final String URL_CHECK_VERSION = HOST + "/user_system/crmVersionInfo";

    /*售后体验反馈*/
    public static final String URL_FEEDBACK = HOST + "/member/submitQuestion";

    /*反馈记录*/
    public static final String URL_FEEDBACK_RECORD = HOST + "/member/getQuestionList";

    /*分配门店*/
    public static final String URL_DISTRIBUTION_STORE = HOST_CM + "/message/queryShopBydealerId";

    /*提交门店*/
    public static final String URL_UPDATE_HOUSE_SHOP = HOST_CM + "/message/updateHouseShopId";

    /*分配导购*/
    public static final String URL_QUERY_PER_SHOP = HOST_CM + "/message/queryPer";

    /*获取消失的数据*/
    @Deprecated
    public static final String URL_NEW_ITEM = HOST + "/member/getNewItem";

    /*验证货物包装号*/
    public static final String URL_GET_CODE_INFO = HOST + "/member/getCodeInfo";

    /*验证货物包装号*/
    public static final String URL_SUBMIT_CODE_INFO = HOST + "/member/submitCodeInfo";

    /*收发货清单*/
    public static final String URL_RECEIVE_DELIVERY_MANIFEST = HOST + "/member/selectReceiptAndDeliveryListDate";

    /*物流信息*/
    public static final String URL_GET_LOGISTICS_INFO = HOST + "/member/getLogisticsInfo";

    /*空间清单*/
    public static final String URL_GET_PRODUCT_LIST = HOST + "/member/getProductList";

    /*报价清单*/
    public static final String URL_QUERY_QUOTE_INFO = HOST + "/member/queryQuoteInfo";

    /*产品信息*/
    public static final String URL_QUERY_ORDER_BOM_INFO = HOST + "/member/queryorderBomInfo";

    /*操作日志*/
    public static final String URL_SELECT_LOG_BY_SPACEID = HOST + "/member/selectLogBySpaceId";

    /*信贷查询*/
    public static final String URL_CREDIT_CHECKING_DEALER = HOST + "/member/creditCheckDealer";

    /*账单列表*/
    public static final String URL_QUERY_DEALER_DETAIL = HOST + "/member/querydealerdetail";

    /*在线申报信息*/
    public static final String URL_COMPLAIN_LIST = HOST + "/member/complainList";

    /*在线申报信息*/
    public static final String URL_PAY_LIST = HOST + "/member/payList";

    /*在线申报提交修改撤回删除 和在线申报&打款信息确认的新增*/
    public static final String URL_EDIT_STATUS = HOST + "/member/editStatus";

    /*打款信息确认-提交撤回上传凭证*/
    public static final String URL_CONFIRM = HOST + "/member/confirm";

    /*上传图片*/
    public static final String URL_UPLOAD_FILE_BANK = HOST_CM_SHORT + "/bank/api/upload/uploadFile";

    /*删除图片*/
    public static final String URL_GET_DELET_IMG = HOST_CM_SHORT + "/bank/api/upload/delete/";

    /*显示图片*/
    public static final String URL_GET_PATH_IMG = HOST_CM_SHORT + "/bank/api/upload/file/";

    /*拎包入住*/
    public static final String URL_NEW_RETAIL = HOST + "/member/newRetail";

    /*线上引流报表*/
    public static final String URL_ONLINE_DRAINAGE_REPORT = HOST + "/member/drainageList";

    /*员工列表*/
    public static final String URL_EMPLOYEE_LIST = HOST + "/member/getUserList";

    /*获取员工信息*/
    public static final String URL_EMPLOYEE_DATA = HOST + "/member/getUserData";

    /*新增修改员工*/
    public static final String URL_EMPLOYEE_EDIT = HOST + "/member/modifyUser";

    /*修改密码*/
    public static final String URL_EMPLOYEE_EDIT_PASSWORD = HOST + "/member/modifyUserPassword";

    /*老板本月数据*/
    public static final String URL_BOSS_MONTH_DATA = HOST + "/member/shopownerData";

    /*安装经理本月数据*/
    public static final String URL_INSTALL_MANAGER_MONTH_DATA = HOST + "/member/installData";

    /*本月数据-财务*/
    public static final String URL_FINANCE_MONTH_DATA = HOST + "/member/financeData";

    /*新增编辑规则*/
    public static final String URL_SETTINGS_RULE = HOST + "/member/editRule";

    /*v3.0*/
    /*员工个人排行榜*/
    public static final String URL_EMPLOYEE_RANKING = HOST + "/forms/rank";
    public static final String URL_EVALUATE_TYPE = HOST + "/forms/getEvaluateType";
    public static final String URL_DESIGN_MONTH_DATA = HOST + "/forms/designerData"; //本月数据-设计经理
    public static final String URL_PERSONAL_PERFORMANCE = HOST + "/forms/formsAfterSale"; //个人绩效
    public static final String URL_GET_TARGET = HOST + "/forms/getTarget"; //获取目标
    public static final String URL_SET_TARGET = HOST + "/forms/setTarget"; //设置目标
    public static final String URL_BUSINESS_OBJECTIVES = HOST + "/forms/getManagement"; //经营目标首页数据
    public static final String URL_POWER_INFO = HOST + "/forms/getPowerInfo4";  //3.0获取分析图标
    public static final String URL_CUSTOMER_CONVERSION = HOST + "/forms/getChange"; //客户转化率报表
    public static final String URL_DEALER_FACTORY_PERFORMANCE = HOST + "/forms/getDealerFactoryPerformance"; //经销商-出厂业绩
    public static final String URL_MARKET_FACTORY_PERFORMANCE = HOST + "/forms/getMarketFactoryPerformance"; //营销人员-出厂业绩
    public static final String URL_SHOP_ANALYSIS = HOST + "/forms/storeList"; //门店分析
    public static final String URL_ONLINE_DRAINAGE = HOST + "/forms/drainageData"; //线上引流
    public static final String URL_SHEET_ANALYSIS = HOST + "/forms/getSheetAnalysis"; //板材分析
    public static final String URL_HOME_DOLL_CHANNEL = HOST + "/forms/getHomeChannel"; //家装渠道-营销人员
    public static final String URL_DEALER_HOME_DOLL_CHANNEL = HOST + "/forms/getDealerHomeChannel"; //家装渠道-经销商
    public static final String URL_DEALER_MULTI_PERFORMANCE = HOST + "/forms/getDealerColorAndSeriesDetail"; //花色/系列 业绩详情-经销商
}
