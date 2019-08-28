package com.holike.crm.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gallop on 2019/8/9.
 * Copyright holike possess 2019.
 * 员工信息修改成功后返回的信息
 */
public class EmployeeEditResultBean {
    public String msg;
    public ResultBean result;

    @Nullable
    public ResultBean getResult() {
        return result;
    }

    public static class ResultBean {
        List<RoleDataBean> roleArr;
        List<DistributionStoreBean> shopArr;
        List<RoleDataBean.AuthInfoBean> authInfo;

        @NonNull
        public List<DistributionStoreBean> getShopArr() {
            return shopArr == null ? new ArrayList<>() : shopArr;
        }

        @NonNull
        public List<RoleDataBean> getRoleArr() {
            return roleArr == null ? new ArrayList<>() : roleArr;
        }

        @NonNull
        public List<RoleDataBean.AuthInfoBean> getAuthInfo() {
            return authInfo == null ? new ArrayList<>() : authInfo;
        }
    }

}
