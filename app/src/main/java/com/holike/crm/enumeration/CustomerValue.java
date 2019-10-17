package com.holike.crm.enumeration;

/**
 * Created by gallop on 2019/7/11.
 * Copyright holike possess 2019.
 */
public class CustomerValue {

    public static final String ADD_V1 = "添加客户";
    public static final String ADD_V2 = "新建客户";
    public static final String POTENTIAL = "潜在客户";
    public static final String STAY_MEASURE = "待量尺客户";
    public static final String STAY_DRAWING = "待出图客户";
    public static final String STAY_ROUNDS = "待查房客户";
    @Deprecated
    public static final String STAY_REMEASURE = "待复尺客户";
    public static final String STAY_SIGN = "待签约客户";
    public static final String STAY_COLLECT_AMOUNT = "待收全款客户";
    @Deprecated
    public static final String STAY_ORDER = "待下单客户";
    public static final String ORDER = "订单客户";
    public static final String ORDER_PLACED = "已下单客户";
    public static final String STAY_INSTALL = "待安装客户";
    public static final String INSTALLED = "已安装客户";
    public static final String HIGH_SEAS = "公海客户";
    public static final String DEPOSIT_RECEIVED = "已收订金客户";
    public static final String FEEDBACK = "售后反馈";
    public static final String FEEDBACK_RECORD = "反馈记录";
    public static final String RECEIPT_SCAN = "收货扫码";

    /*根据以下类型 跳转不同fragment*/
    public static final String TYPE_ASSIGN_GUIDE = "type_assign_guide"; //分配导购
    public static final String TYPE_ASSIGN_DESIGNER = "type_assign_designer"; //分配设计师
    public static final String TYPE_CALL_LOGS = "type_call_logs"; //通话记录
    public static final String TYPE_BEEN_LOST = "type_been_lost"; //已流失
    public static final String TYPE_MEASURE_RESULT = "type_measure_result"; //量尺结果
    public static final String TYPE_UPLOAD_PLAN = "type_upload_plan"; //上传方案
    public static final String TYPE_INVALID_RETURN = "type_invalid_return"; //无效退回
    public static final String TYPE_UNMEASURED = "type_unmeasured"; //预约量尺
    public static final String TYPE_ROUNDS = "type_rounds"; //主管查房
    public static final String TYPE_RECEIPT = "type_receipt"; //收款
    public static final String TYPE_CONTRACT_REGISTER = "type_contract_register";//合同登记
    public static final String TYPE_UNINSTALL = "type_uninstall"; //预约安装
    public static final String TYPE_INSTALL_DRAWING = "type_install_drawing"; //上传安装图纸
    public static final String TYPE_INSTALLED = "type_installed"; //安装完成

    public static final String HOUSE_ID = "houseId"; //房屋id bundle传值
    public static final String PERSONAL_ID = "personalId"; //客户id bundle传值
    public static final String HIGH_SEAS_HOUSE_FLAG = "isHighSeasHouse"; //是否是公海房屋 boolean

    public static final String EVENT_TYPE_ALTER_CUSTOMER = "event.type.ALTER.CUSTOMER";  //修改客户信息
    public static final String EVENT_TYPE_ADD_CUSTOMER = "event.type.ADD.CUSTOMER"; //新增客户信息
    public static final String EVENT_TYPE_RECEIVE_HOUSE = "event.type.RECEIVE.HOUSE"; //领取房屋
    public static final String EVENT_TYPE_CONFIRM_LOST_HOUSE = "event.type.CONFIRM.LOST.HOUSE"; //确认流失房屋
    public static final String EVENT_TYPE_LOST_HOUSE = "event.type.LOST_HOUSE"; //流失房屋

    public static final int RESULT_CODE_EDIT_CUSTOMER = 1001; //新增或修改客户信息成功返回码
    public static final int RESULT_CODE_LOST_HOUSE = 1002; //流失房屋成功返回码
    public static final int RESULT_CODE_RECEIVE_HOUSE = 1003; //领取房屋成功

    public static final int RESULT_CODE_ACTIVATION = 1006; //激活客户成功返回码
    public static final int RESULT_CODE_HIGH_SEAS = 1007; //领取公海客户成功
}
