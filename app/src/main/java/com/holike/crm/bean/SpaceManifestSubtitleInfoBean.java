package com.holike.crm.bean;

public class SpaceManifestSubtitleInfoBean {
    String name;
    String content;

    public SpaceManifestSubtitleInfoBean() {
        super();
    }

    public SpaceManifestSubtitleInfoBean(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
