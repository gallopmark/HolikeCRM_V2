package com.holike.crm.bean;

import android.text.TextUtils;

/**
 * Created by gallop on 2019/8/13.
 * Copyright holike possess 2019.
 * 活动优惠政策
 */
public class ActivityPoliceBean {
    public String id;
    public String dealerId;
    public String activityPolicy;
    public String recordStatus;
    public String updateDate;
    public String createBy;
    public String versionNumber;
    public String ruleId;
    public String createDate;
    public String updateBy;

    public ActivityPoliceBean(String activityPolicy) {
        this.activityPolicy = activityPolicy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof ActivityPoliceBean) {
            return TextUtils.equals(id, ((ActivityPoliceBean) obj).id) || TextUtils.equals(activityPolicy, ((ActivityPoliceBean) obj).activityPolicy);
        } else {
            return false;
        }
    }
}
