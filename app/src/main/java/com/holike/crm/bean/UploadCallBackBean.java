package com.holike.crm.bean;

import java.util.Map;

/**
 * Created by wqj on 2018/6/5.
 * 上传图片返回结果
 */

public class UploadCallBackBean {
    /**
     * msg : 文件上传成功！
     * issuccess : 0
     * adress : [{"9be30f7cf16241dc92ec533ab483632d":"upload/0upload_20180608133316283.jpg"}]
     * historyIds : ["9be30f7cf16241dc92ec533ab483632d"]
     */

    private String msg;
    private String issuccess;
    private Map<String, String> adress;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIssuccess() {
        return issuccess;
    }

    public void setIssuccess(String issuccess) {
        this.issuccess = issuccess;
    }

    public Map<String, String> getAdress() {
        return adress;
    }

    public void setAdress(Map<String, String> adress) {
        this.adress = adress;
    }
}
