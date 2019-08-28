package com.holike.crm.util;

/**
 * Created by wqj on 2017/11/23.
 * 常量类
 */

public class Constants {
    public final static int UPDATE_DIALOG_TIME = 60 * 1000;//多久弹出版本更新弹窗
    public final static String VERSION = "version";
    public final static String CLIENT = "client";
    public final static String ANDROID = "android";

    public final static int DEFAULT_PAGE = 0;
    /*sharePreference*/
    public final static String CLI_ID = "cliId";
    public final static String USER_ID = "userId";
    public final static String USER_PSW = "userPsw";
    public final static String COOKIE = "Cookie";
    public final static String COOKIE2 = "Cookie2";
    public final static String CHECK_VERSION_TIME = "check_version_time";//上次检测版本更新时间
    public final static String UPDATE_BEAN = "updateBean";//版本更新bean
    public final static String MSG_NUM = "msg_num";//未读消息数


    /*resultCode*/
    public final static int RESULT_CODE_CHECK_ACCOUNT = 10001;
    public final static int RESULT_CODE_EDIT_SUCCESS = 10003;
    public final static int RESULT_CODE_ADD_SUCCESS = 10004;
    public final static int RESULT_CODE_REFRESH_ORDER_REPORT = 10005;
    public final static int RESULT_CODE_DEALER_ID = 10006;
    public final static int RESULT_CODE_OPERATE_SUCCESS = 10007;
    public final static int RESULT_CODE_ADD_HOUSE_SUCCESS = 10008;

    /*intent参数名*/
    public final static String CHECK_ACCOUNT_LOGIN_ACCOUNT = "LoginAccount";
    public final static String TYPE_ID = "typeId";
    public final static String MESSAGE_ID = "messageId";
    public final static String ORDER_ID = "orderId";
    public final static String PAGE_TYPE = "pageType";
    public final static String BILL_LIST_BEAN = "billListBean";
    public final static String BILL_DETAIL = "bill_detail";
    public final static String PHOTO_VIEW_POSITION = "phoneview_position";
    public final static String PHONE_VIEW_IMAGES = "photoview_images";
    public final static String BRANK_DATA = "brank_data";
    public final static String BILL_LIST_SHOW_ALL = "show_all";
    public final static String DETAILS_CREDIT = "details_credit";
    public final static String DETAILS_CREDIT_TYPE = "details_credit";
    public final static String PAY_LIST = "pay_list";
    public final static String ONLINE_DECLARATION = "online_declaration";
    public final static String IS_EARNEST = "isEarnest";
    public final static String PERSONAL_ID = "personalId";
    public final static String HOUSE_ID = "houseId";
    public final static String HOUSE_BEAN = "house_bean";
    public final static String PERSONAL_BEAN = "personal_bean";
    public final static String HOUSE_BEAN_URL = "url";
    public final static String HOUSE_BEAN_APP_URL = "appurl";
    public final static String IS_BOSS = "isBoss";
    public final static String PUSH_TYPE = "push_type";
    public final static String HOUSE_TYPE = "house_type";
    public final static String EDIT_HOUSE = "edit_house";
    public final static String ADD_HOUSE = "add_house";

    /*Bundle参数*/
    public final static String CITY_CODE = "cityCode";
    public final static String START_TIME = "startTime";
    public final static String END_TIME = "endTime";
    public final static String TYPE = "type";
    public final static String TITLE = "title";
    public final static String TIME = "time";
    public final static String MONEY = "money";
    public final static String BACK = "back";
    public final static String DEALER_RANK = "dealerRank";
    public final static String NET_DETAIL = "netDetail";
    public final static String MONTH_COMPLETE = "month_complete";
    public final static String MONTH_PK = "month_pk";
    public final static String PRODUCT_TRADING_BEAN = "productTradingBean";
    public final static String PERFORMANCE_BEAN = "performanceBean";
    public final static String CUPBOARD_BEAN = "cupBoardBean";
    public final static String ORIGINAL_BOARD_BEAN = "originalBoardBean";
    public final static String WRITE_CITY_BEAN = "writeCityBean";
    public final static String DEALER_ID = "dealerId";
    public final static String ACTIVE_MARKET_RANK_BEAN = "activeMarketRankBean";
    public final static String CUSTOMER_STATUS = "customerStatus";
    public final static String HOUSE_INFO = "houseInfo";
    public final static String COLLECT_DEPOSIT_LIST_BEAN = "collectDepositListBean";
    public final static String LIST_DETAILS_BEAN = "listDetailsBean";
    public final static String CREDIT_TYPE = "credit_type";

    /*evnetBus 事件名*/
    public final static String EVENT_REFRESH = "onRefresh";

    /*广播参数*/
    public final static String ACTION_UNREAD_MESSAGE = "action_unread_message";//更新消息未读数 ;

    /*push类型*/
    public final static int PUSH_TYPE_NOTIFY = 1;

    /*重复*/
    public final static String SCAN_REPEAT = "scan_repeat";// ;
    public final static String SCAN_REPEAT_INPUTTED = "scan_repeat_inputted";//;
}
