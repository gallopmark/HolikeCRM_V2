package com.holike.crm.bean;

public class NoMoreBean implements MultiItem{
    private String text;

    public NoMoreBean() {
    }

    public NoMoreBean(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getItemType() {
        return 2;
    }
}
