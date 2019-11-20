package com.holike.crm.bean;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/14.
 * Copyright holike possess 2019.
 * 经销商所有门店和设计师信息
 */
public class DealerInfoBean {

    public String dealerId;
    public String shopId;
    public String shopName;
    public String statusCode;
    List<UserBean> userList;

    @NonNull
    public List<UserBean> getUserList() {
        return userList == null ? new ArrayList<>() : userList;
    }

    public static class UserBean {
        public String stationId;
        public String userId;
        public String userName;
    }
}
