package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2017/10/12.
 * 版本更新bean
 */

public class UpdateBean implements Serializable {
    public static final int TYPE_DOWNLOAD = 1;
    public static final int TYPE_INSTALL = 2;

    /**
     * actualversion : 1.0.0
     * updatepath : baidu.com
     * updatetips : 有新版本更新啦
     * detailArray: ["有新版本更新啦"]
     * version : 100
     */

    private String actualversion;
    private String updatepath;
    private String updatetips;
    private String version;
    private List<String> detailArray;
    private int type;

    public String getActualversion() {
        return actualversion;
    }

    public void setActualversion(String actualversion) {
        this.actualversion = actualversion;
    }

    public String getUpdatepath() {
        return updatepath;
    }

    public void setUpdatepath(String updatepath) {
        this.updatepath = updatepath;
    }

    public String getUpdatetips() {
        return updatetips;
    }

    public void setUpdatetips(String updatetips) {
        this.updatetips = updatetips;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getDetailArray() {
        return detailArray;
    }

    public void setDetailArray(List<String> detailArray) {
        this.detailArray = detailArray;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("actualversion='").append(actualversion).append('\'');
        sb.append(", updatepath='").append(updatepath).append('\'');
        sb.append(", updatetips='").append(updatetips).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", detailArray=").append(detailArray);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
