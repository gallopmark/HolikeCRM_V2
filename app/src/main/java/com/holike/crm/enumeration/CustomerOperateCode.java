package com.holike.crm.enumeration;

/**
 * Created by pony on 2019/11/5.
 * Version v3.0 app报表
 * 客户操作流程状态码
 */
public class CustomerOperateCode {
    public static final String CODE_GUIDE = "01"; //分配导购
    public static final String CODE_UNMEASURED = "04"; //预约量尺
    public static final String CODE_DESIGNER = "05";   //分配设计师
    public static final String CODE_MEASURED = "06";   //量尺结果
    public static final String CODE_UPLOAD_PLAN = "07";    //上传方案
    public static final String CODE_ROUNDS = "08"; //主管查房
    public static final String CODE_CONTRACT = "09";   //合同登记
    public static final String CODE_ORDER = "11";  //生成订单
    public static final String CODE_LOSE = "12";   //已流失
    public static final String CODE_UNINSTALL = "15";  //预约安装
    public static final String CODE_INSTALLED = "16";  //安装完成
    public static final String CODE_INSTALL_DRAWING = "17";    //上传安装图纸
    public static final String CODE_MESSAGE_BOARD = "18";  //留言记录
    public static final String CODE_RECEIPT = "19";    //收款
    public static final String CODE_CONFIRM_LOSE = "21";   //确认流失
    public static final String CODE_INVALID_RETURN = "23";  //（线上引流）无效退回
}
