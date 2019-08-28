package com.holike.crm.bean;

/**
 * Created by wqj on 2017/10/17.
 * 推送消息bean
 */

public class PushMsgBean {

    /**
     * content : 我是内容
     * level : 17
     * type : 2
     * name : 设计师
     * title : 我是标题
     * diamond: 10
     */

    private String content;
    private String level;
    private String type;
    private String name;
    private String title;
    private String diamond;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }
}