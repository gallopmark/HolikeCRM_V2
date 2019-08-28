package com.holike.crm.bean;

import java.io.Serializable;

/**
 * Created by wqj on 2018/3/6.
 * 当天订金详情bean
 */

public class DayDepositBean implements Serializable {
    /**
     * money : 10000
     * name : 吴三
     */

    private float money;
    private String name;

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
