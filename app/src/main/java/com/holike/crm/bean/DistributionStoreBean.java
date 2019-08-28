package com.holike.crm.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqj on 2018/9/19.
 * 门店数据
 */
public class DistributionStoreBean implements Serializable {

    @SerializedName("status_code")
    public String statusCode;
    public String shopId;
    public String shopName;

    private int isSelect;
    private String status;
    public List<Group> groupList;

    public DistributionStoreBean(String shopId, String shopName) {
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId == null ? "" : shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public boolean isSelected() {
        return isSelect == 1;
    }

    public String getStatus() {
        return status;
    }

    public String getShowText() {
        return shopName + "\u3000" + (TextUtils.isEmpty(status) ? "" : status);
    }

    @NonNull
    public List<Group> getGroupList() {
        return groupList == null ? new ArrayList<>() : groupList;
    }

    /*门店下的组织是否已经全选*/
    public boolean isGroupSelectAll() {
        if (getGroupList().isEmpty()) return true;
        for (Group group : getGroupList()) {
            if (!group.isSelected()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof DistributionStoreBean) {
            return shopId.equals(((DistributionStoreBean) obj).shopId);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 + (!TextUtils.isEmpty(shopId) ? shopId.hashCode() : 0);
    }

    public static class Group {
        @SerializedName("group_id")
        public String groupId;
        @SerializedName("group_name")
        public String groupName;
        String isSelect; //接口数据，1已选，0未选

        public void setIsSelect(String isSelect) {
            this.isSelect = isSelect;
        }

        public boolean isSelected() {
            return TextUtils.equals(isSelect, "1");
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof Group) {
                return groupId.equals(((Group) obj).groupId);
            } else {
                return false;
            }
        }

    }
}
