package com.holike.crm.bean;

import java.io.Serializable;

public class EventRefreshBean implements Serializable {
    public String needRefresh;



    public EventRefreshBean(String needRefresh) {
        this.needRefresh = needRefresh;
    }
}
