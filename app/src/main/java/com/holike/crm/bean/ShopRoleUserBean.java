package com.holike.crm.bean;

import androidx.annotation.NonNull;

import com.holike.crm.bean.internal.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/12.
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

    public static class UserBean implements Dictionary {
        public String stationId;
        public String userId;
        public String userName;

        @Override
        public String getId() {
            return userId;
        }

        @Override
        public String getName() {
            return userName;
        }
    }
}
