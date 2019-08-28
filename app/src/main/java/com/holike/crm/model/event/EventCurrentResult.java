package com.holike.crm.model.event;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventCurrentResult implements Serializable {
    String result;
    int index;
    List<EventQRCodeScanResult > results = new ArrayList<>(0);

    public List<EventQRCodeScanResult> getResults() {
        return results;
    }

    public void setResults(List<EventQRCodeScanResult> results) {
        this.results = results;
    }

    public EventCurrentResult() {
        super();
    }

    public EventCurrentResult(String result, int index, List<EventQRCodeScanResult> results) {
        this.result = result;
        this.index = index;
        this.results = results;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
