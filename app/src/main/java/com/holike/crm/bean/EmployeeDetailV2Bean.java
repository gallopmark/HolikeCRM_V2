package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/7.
 * Copyright holike possess 2019.
 */
public class EmployeeDetailV2Bean {

    private String isBoss; //1是经销商老板 0不是
    private List<DistributionStoreBean> shopInfo;
    private List<RoleDataBean.AuthInfoBean> authInfo;
    @SerializedName("userinfo")
    private InfoBean userInfo;
    private List<RoleDataBean> roleData;

    @Nullable
    public InfoBean getUserInfo() {
        return userInfo;
    }

    public boolean isBoss() {
        return TextUtils.equals(isBoss, "1");
    }

    public void setRoleData(List<RoleDataBean> roleData) {
        this.roleData = roleData;
    }

    @NonNull
    public List<RoleDataBean> getRoleData() {
        return roleData == null ? new ArrayList<>() : roleData;
    }

    public void setShopInfo(List<DistributionStoreBean> shopInfo) {
        this.shopInfo = shopInfo;
    }

    @NonNull
    public List<DistributionStoreBean> getShopInfo() {
        return shopInfo == null ? new ArrayList<>() : shopInfo;
    }

    public void setAuthInfo(List<RoleDataBean.AuthInfoBean> authInfo) {
        this.authInfo = authInfo;
    }

    @NonNull
    public List<RoleDataBean.AuthInfoBean> getAuthInfo() {
        return authInfo == null ? new ArrayList<>() : authInfo;
    }

    /*基础信息*/
    public static class InfoBean implements Serializable {
        public String createBy;
        public String createTime;
        public String userId; //员工id
        public String phone;   //手机
        public String status;  //1有效 0无效
        public String status2;
        public String staffId; //工号
        public String gender; //性别
        public String gender2; //类型

        public String updateBy;
        public String updateTime;
        public String userName;

        /*是否是先生*/
        public boolean isMr() {
            return TextUtils.equals(gender2, "2");
        }

        /*是否是女士*/
        public boolean isMs() {
            return TextUtils.equals(gender2, "1");
        }

        /*是否有效*/
        public boolean isValid() {
            return TextUtils.equals(status2, "1");
        }
    }
}
