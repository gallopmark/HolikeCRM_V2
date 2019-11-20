package com.holike.crm.fragment.customerv2;

import android.content.Intent;

import com.holike.crm.R;
import com.holike.crm.bean.SysCodeItemBean;
import com.holike.crm.fragment.customerv2.helper.UploadPlanHelper;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by pony on 2019/7/22.
 * Copyright holike possess 2019.
 * 上传方案
 */
public class UploadPlanFragment extends GeneralCustomerFragment
        implements UploadPlanHelper.UploadPlanCallback {

    private UploadPlanHelper mHelper;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_customer_uploadplan;
    }

    @Override
    protected void init() {
        mHelper = new UploadPlanHelper(this, this);
    }

    @OnClick(R.id.tvSave)
    public void onViewClicked() {
        mHelper.onSave();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestSystemCode() {
        showLoading();
        mPresenter.getSystemCode();
    }

    @Override
    public void onRequired(String tips) {
        showShortToast(tips);
    }

    @Override
    public void onSave(List<String> removedImages, String houseId, String bookOrderDate, String product,
                       String series, String style, String remark,
                       List<String> images) {
        showLoading();
        mPresenter.uploadPlan(mContext, removedImages, houseId, bookOrderDate, product, series, style, remark, images);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        if (object instanceof SysCodeItemBean) {
            mHelper.setSystemCode((SysCodeItemBean) object);
        } else {
            setResultOk(object);
        }
    }
}
