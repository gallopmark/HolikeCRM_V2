package com.holike.crm.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wqj on 2018/3/5.
 * 公告详情bean
 */

public class MessageDetailsBean implements Serializable {
    /**
     * createDate : 2018-02-05
     * name : 王芳莉
     * pictureList : ["http://120.79.76.243/notice/noticeImg/关于卡普里系列DK床淘汰替换的通知_0001.jpg","http://120.79.76.243/notice/noticeImg/5.png"]
     * title : 关于卡普里系列DK床淘汰替换的通知
     */

    private String createDate;
    private String name;
    private String title;
    private List<String> pictureList;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }
}
