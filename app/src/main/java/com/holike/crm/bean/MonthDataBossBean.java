package com.holike.crm.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 * 老板本月数据
 */
public class MonthDataBossBean {

    List<ArrBean> arr;
    public String shopDetail;
    public String timeDetail;
    public String userName;
    public String isShop; //1小组数据 2员工数据 3员工收款数据
    List<ArrBean2> arr2;
    List<ArrBean3> arr3;


    public List<ArrBean> getArr() {
        return arr == null ? new ArrayList<>() : arr;
    }

    public List<ArrBean2> getArr2() {
        return arr2 == null ? new ArrayList<>() : arr2;
    }

    public List<ArrBean3> getArr3() {
        return arr3 == null ? new ArrayList<>() : arr3;
    }

    public static class ArrBean {
        public String area; //地区
        public String scaleCount; //量尺数
        public String installed;    //安装数
        public String newCount; //新建客户数
        public String picCount; //出图数
        public String enterPercent; //进店成交率
        public String cityCode; //城市代码
        public String prescaleCount;    //预约量尺数
        public String contractTotal;    //成交总金额
        public String type; //类型
        public String firstSuccess; //一次安装完成率
        public String isClick;  //是否可点击 1可点击
        String isClick2;
        public String seaCustomer;  //公海客户数
        public String ordersCustomer;   //订金客户数
        public String scalePercent; //量尺成交率
        public String contractCount;    //签约数
        public String Satisfied;    //客户满意度
        public String are;  //安装平方数
        public String name; //名字
        public String receivables;  //已收款
        public String orders;   //下单数
        public String conversation; //通话次数

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }

        public boolean isClickable2() { //收款是否可以点击
            return TextUtils.equals(isClick2, "1");
        }
    }

    public static class ArrBean2 {
        public String tag; //角色名
        public String type; //1导购 2业务员 3设计师 4 安装工
        List<ArrBean> list;

        public List<ArrBean> getList() {
            return list == null ? new ArrayList<>() : list;
        }
    }

    public static class ArrBean3 {
        public String money; //收款金额
        public String type; //收款类型
        @SerializedName("pay_time")
        public String payTime; //收款时间
        public String name; //收款人
    }
}
