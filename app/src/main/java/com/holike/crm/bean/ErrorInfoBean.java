package com.holike.crm.bean;

import android.text.TextUtils;

import com.holike.crm.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by gallop on 2019/8/13.
 * Copyright holike possess 2019.
 */
public class ErrorInfoBean {

    public String statusTime;
    public String depositFlag;
    public String status;
    public String houseId;
//
    public String getStatusTime() {
//        if (TextUtils.isEmpty(errorInfo)) {
//            return "";
//        }
//        try {
//            String statusTime = errorInfo.substring(errorInfo.indexOf("=") + 1, errorInfo.indexOf(","));
//            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(statusTime);
//            return TimeUtil.timeMillsFormat(date);
//        } catch (Exception e) {
//            return "";
//        }
        if (TextUtils.isEmpty(statusTime)) {
            return "";
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(statusTime);
            return TimeUtil.timeMillsFormat(date);
        } catch (Exception e) {
            return statusTime;
        }
    }

    public String getStatus() {
//        if (TextUtils.isEmpty(errorInfo)) {
//            return "";
//        }
//        try {
//            return errorInfo.substring(errorInfo.lastIndexOf("=") + 1, errorInfo.length() - 1);
//        } catch (Exception e) {
//            return "";
//        }
        return status;
    }

    public boolean isChargeDeposit() {
//        if (TextUtils.isEmpty(errorInfo)) {
//            return false;
//        }
//        try {
//            int index = errorInfo.lastIndexOf(",");
//            String depositFlag = errorInfo.substring(index - 1, index);
//            return TextUtils.equals(depositFlag, "Y") || TextUtils.equals(depositFlag, "y");
//        } catch (Exception e) {
//            return false;
//        }
        if (TextUtils.isEmpty(depositFlag)) return false;
        return TextUtils.equals(depositFlag, "Y") || TextUtils.equals(depositFlag, "y");
    }

}
