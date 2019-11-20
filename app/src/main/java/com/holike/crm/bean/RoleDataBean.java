package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/8/7.
 * Copyright holike possess 2019.
 * 角色权限
 */
public class RoleDataBean {
    @SerializedName("role_name")
    public String roleName;  //角色名
    @SerializedName("role_code")
    public String roleCode; //角色代码
    String isSelect; //是否已选
    private List<RoleDataBean.Action> actionList;

    public RoleDataBean(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public boolean isSelected() {
        return TextUtils.equals(isSelect, "1");
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    @NonNull
    public List<RoleDataBean.Action> getActionList() {
        return actionList == null ? new ArrayList<>() : actionList;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof RoleDataBean) {
            return TextUtils.equals(roleCode, (((RoleDataBean) obj).roleCode));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 + (!TextUtils.isEmpty(roleCode) ? roleCode.hashCode() : 0);
    }

    public static class Action {
        public String actionId;  //动作id
    }

    public static class AuthInfoBean implements MultiItem, Serializable {
        public String pName;
        List<PArrBean> pArr2;

        @Override
        public int getItemType() {
            return 1;
        }

        public List<PArrBean> getAuthData() {
            return pArr2 == null ? new ArrayList<>() : pArr2;
        }

        public static class PArrBean implements MultiItem, Serializable {
            public String actionId;
            private int isSelect;
            public String actionName;

            private boolean isHeader;
            private boolean showLine; //不是接口返回的字段

            public PArrBean(String actionName, boolean isHeader) {
                this.actionName = actionName;
                this.isHeader = isHeader;
            }

            public void setIsSelect(int isSelect) {
                this.isSelect = isSelect;
            }

            public int getIsSelect() {
                return isSelect;
            }

            public boolean isSelect() {
                return isSelect == 1;
            }

            public String getActionName() {
                return actionName;
            }

            public boolean isShowLine() {
                return showLine;
            }

            public void setShowLine(boolean showLine) {
                this.showLine = showLine;
            }

            public boolean isHeader() {
                return isHeader;
            }

            @Override
            public int getItemType() {
                return 2;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (obj == this) return true;
                if (obj instanceof PArrBean) {
                    return TextUtils.equals(actionId, (((PArrBean) obj).actionId));
                } else {
                    return false;
                }
            }

            @Override
            public int hashCode() {
                return 31 + (!TextUtils.isEmpty(actionId) ? actionId.hashCode() : 0);
            }
        }
    }
}
