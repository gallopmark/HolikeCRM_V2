package com.holike.crm.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * createBy	创建人	string	@mock=13200001
 * createTime	创建时间	string	@mock=2018-02-26 10:53:43
 * gender	性别	string	@mock=男
 * phone	手机号	string	@mock=13617871390
 * staffId	工号	string	@mock=132001-27
 * status	状态	string	@mock=有效
 * updateBy	更新人	string	@mock=13200001-0027
 * updateTime	更新时间	string	@mock=2019-04-19 16:06:01
 * userId	员工id	string	@mock=13200001-0027
 * userName	员工名称
 */
public class EmployeeBean {
    private String createBy;
    private String createTime;
    private String userId; //员工id
    private String name; //名字
    private String phone;   //手机
    private String status;  //1有效 0无效

    private String staffId; //工号
    private String gender; //性别

    private String updateBy;
    private String updateTime;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*名字为三个字以上  截取最后两个字 比如张学友->学友*/
    public String getShortName() {
        if (TextUtils.isEmpty(name)) return "";
        if (name.trim().length() > 2) {
            return name.substring(name.length() - 2);
        }
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof EmployeeBean) {
            return userId.equals(((EmployeeBean) obj).userId);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 + (!TextUtils.isEmpty(userId) ? userId.hashCode() : 0);
    }
}
