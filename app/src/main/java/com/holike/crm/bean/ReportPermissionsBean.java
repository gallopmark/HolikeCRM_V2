package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/3/13.
 */

public class ReportPermissionsBean implements Serializable {

    /**
     * imageUrl : https://file.holike.com/072af120b5a0ee7ce54e0925331abe54.jpg
     * title : 订单交易趋势
     * type : 1
     */

    private String imageUrl;
    private String title;
    private String type;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        try {
            return Integer.parseInt(type);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
