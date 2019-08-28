package com.holike.crm.bean;

/**
 * 验证二维码结果bean
 */
public class CodeInfoBean {

    String code;
    String msg;

    public CodeInfoBean(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
