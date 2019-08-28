package com.holike.crm.bean;


import com.holike.crm.http.ProgressResponseBody;
import com.holike.crm.util.IOUtil;
import com.holike.crm.util.MD5Util;

import java.io.Serializable;

/**
 * Created by wqj on 2017/11/20.
 * 下载文件请求参数bean
 */

public class DownloadFileBean implements Serializable {
    private String url;         //下载文件地址
    private String path = IOUtil.getCachePath();           //保存文件的文件夹,默认下载到cache文件夹中
    private String fileName;    //保存的文件名
    private ProgressResponseBody.ProgressListener progressListener;//下载进度回调监听


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ProgressResponseBody.ProgressListener getProgressListener() {
        return progressListener;
    }

    public void setProgressListener(ProgressResponseBody.ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public DownloadFileBean(String url) {
        this.url = url;
        this.fileName = MD5Util.urlToMD5(url);
    }

    public DownloadFileBean(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

}
