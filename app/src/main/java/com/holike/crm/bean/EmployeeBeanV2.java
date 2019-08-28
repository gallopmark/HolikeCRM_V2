package com.holike.crm.bean;

import android.text.TextUtils;


public class EmployeeBeanV2 implements MultiItem {
    public String userId;
    public String name;
    public String isBoss;
    public String phone;
    public String status;
    public String roleName;
    public String shopName;

    /*名字为三个字以上  截取最后两个字 比如张学友->学友*/
    public String getShortName() {
        if (TextUtils.isEmpty(name)) return "";
        if (name.trim().length() > 2) {
            return name.substring(name.length() - 2);
        }
        return name;
    }

    /*是否有效*/
    public boolean isValid() {
        return TextUtils.equals(status, "1");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof EmployeeBeanV2) {
            return userId.equals(((EmployeeBeanV2) obj).userId);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 + (!TextUtils.isEmpty(userId) ? userId.hashCode() : 0);
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
