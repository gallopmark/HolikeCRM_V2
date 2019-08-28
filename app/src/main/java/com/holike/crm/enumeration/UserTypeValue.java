package com.holike.crm.enumeration;

/**
 * Created by gallop on 2019/7/12.
 * Copyright holike possess 2019.
 * 用户类型
 */
public class UserTypeValue {
    public static final String NO_PERMISSION = "0";  //无权限
    public static final String MARKETING = "1";  //营销人员
    /*2.0以后使用 INSTALLER_V2*/
    @Deprecated
    public static final String INSTALLER = "2";  //安装工 2.0以上用 INSTALLER_V2

    @Deprecated
    public static final String GUIDE_SALESMAN = "3";   //导购设计师  2.0以下

    @Deprecated
    public static final String BOSS = "4"; //店长老板

    public static final String WAREHOUSE = "5"; //仓库、仓管

    public static final String INSTALLER_V2 = "6"; //安装工

    public static final String INSTALL_MANAGER = "61"; //安装经理

    public static final String FINANCE = "7";//财务

    public static final String DESIGNER = "8"; //设计师/经理

    public static final String GUIDE_SALESMAN_V2 = "9"; //导购/业务

    public static final String BOSS_V2 = "10"; //店长老板 V2.0版本

    public static final String AFTER_SALE = "11"; //售后

}
