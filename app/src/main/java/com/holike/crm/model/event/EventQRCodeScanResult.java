package com.holike.crm.model.event;


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
}
