package com.holike.crm.fragment.customerv2;

import android.content.Intent;

import com.holike.crm.R;
import com.holike.crm.fragment.customerv2.helper.UploadInstallDrawingHelper;

import java.util.List;


/**
 * Created by pony on 2019/7/30.
 * Copyright holike possess 2019.
 * 上传安装图纸
 */
public class UploadInstallDrawingFragment extends GeneralCustomerFragment implements UploadInstallDrawingHelper.UploadInstallDrawingCallback {
    private UploadInstallDrawingHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_installdrawing;
    }

    @Override
    protected void init() {
        mHelper = new UploadInstallDrawingHelper(this, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequired(String text) {
        showShortToast(text);
    }

    @Override
    public void onSaved(List<String> removedImages, String houseId, String installId, String installUserId, String remark, List<String> imagePaths) {
        showLoading();
        mPresenter.uploadInstallDrawing(mContext, removedImages, houseId, installId, remark, imagePaths);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        setResultOk(object);
    }
}
