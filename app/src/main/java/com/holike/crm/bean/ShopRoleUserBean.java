package com.holike.crm.bean;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/12.
 * Copyright holike possess 2019.
 */
public class ShopRoleUserBean {

    private List<InnerBean> promoter;
    private List<InnerBean> designer;
    private List<InnerBean> guide;

    public List<InnerBean> getPromoter() {
        return promoter == null ? new ArrayList<>() : promoter;
    }

    public List<InnerBean> getDesigner() {
        return designer == null ? new ArrayList<>() : designer;
    }

    public List<InnerBean> getGuide() {
        return guide == null ? new ArrayList<>() : guide;
    }

    public static class InnerBean {
        public String dealerId;
        public String shopId;
        public String shopName;
        public String statusCode;
        List<GroupBean> groupList;
        List<UserBean> userList;

        @NonNull
        public List<GroupBean> getGroupList() {
            return groupList == null ? new ArrayList<>() : groupList;
        }

        @NonNull
        public List<UserBean> getUserList() {
            return userList == null ? new ArrayList<>() : userList;
        }
    }

    public static class GroupBean {
        public String groupId;
        public String groupName;
        public String shopId;
        List<UserBean> userList;

        public List<UserBean> getUserList() {
            return userList == null ? new ArrayList<>() : userList;
        }
    }

    public static class UserBean {
        public String stationId;
        public String userId;
        public String userName;
    }
}
