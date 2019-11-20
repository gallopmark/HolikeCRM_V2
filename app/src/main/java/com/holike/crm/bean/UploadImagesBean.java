package com.holike.crm.bean;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pony on 2019/7/29.
 * Copyright holike possess 2019.
 * 图片上传成功返回
 */
public class UploadImagesBean {
    public String code;
    List<String> data;
    public String msg;

    @NonNull
    public List<String> getData() {
        return data == null ? new ArrayList<>() : data;
    }
}
