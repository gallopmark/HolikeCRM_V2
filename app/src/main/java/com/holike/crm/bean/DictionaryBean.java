package com.holike.crm.bean;

import android.text.TextUtils;

/**
 * Created by gallop on 2019/7/23.
 * Copyright holike possess 2019.
 */
public class DictionaryBean {
    public String id;
    public String name;

    public DictionaryBean() {
    }

    public DictionaryBean(String id, String name) {
        this.id = id;
        this.name = name;
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
