package com.holike.crm.bean;

import android.text.TextUtils;

/**
 * Created by pony on 2019/7/23.
 * Copyright holike possess 2019.
 * 字典实体类
 */
public class DictionaryBean {
    public String id;
    public String name;

    public String code;

    public DictionaryBean() {
    }

    public DictionaryBean(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public DictionaryBean(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof DictionaryBean) {
            return TextUtils.equals(id, ((DictionaryBean) obj).id);
        } else {
            return false;
        }
    }
}
