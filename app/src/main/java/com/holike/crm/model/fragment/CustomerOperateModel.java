package com.holike.crm.model.fragment;

import android.content.Context;

import com.holike.crm.base.BaseModel;
import com.holike.crm.bean.UploadImagesBean;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.RequestCallBack;

import java.util.List;

/**
 * Created by pony on 2019/7/22.
 * Copyright holike possess 2019.
 */
public class CustomerOperateModel extends BaseModel {
    /*上传方案图片*/
    public void uploadImages(Context context, List<String> paths) {
        upload(context, CustomerUrlPath.URL_UPLOAD_IMAGE, paths, new RequestCallBack<UploadImagesBean>() {
            @Override
            public void onFailed(String failReason) {

            }

            @Override
            public void onSuccess(UploadImagesBean bean) {

            }
        });
    }
}
