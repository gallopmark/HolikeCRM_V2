package com.holike.crm.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MonthDataDesignBean {

    public String shopDetail; //描述
    public String timeDetail; //时间
    @SuppressWarnings("WeakerAccess")
    List<ArrBean> arr;

    public List<ArrBean> getArr() {
        return arr == null ? new ArrayList<>() : arr;
    }

    public static class ArrBean {
        public String scaleCount; //量尺数
        public String picCount; //出图数
        public String type; //类型
        public String cityCode; //城市代码
        public String contractTotal; //成交总金额
        public String name; //名字
        public String receivables; //已收款
        public String orders; //下单数
        public String contractCount; //签约数
        String isClick; //是否可以点击

        public boolean isClickable() {
            return TextUtils.equals(isClick, "1");
        }
    }
}
