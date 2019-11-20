package com.holike.crm.model.event;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.holike.crm.bean.DictionaryBean;

import java.io.Serializable;

public class EventQRCodeScanResult implements Serializable {
    String result;

    public EventQRCodeScanResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof EventQRCodeScanResult) {
            return TextUtils.equals(result, ((EventQRCodeScanResult) obj).result);
        } else {
            return false;
        }
    }
}
