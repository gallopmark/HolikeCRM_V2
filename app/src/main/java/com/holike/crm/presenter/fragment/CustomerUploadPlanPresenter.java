package com.holike.crm.presenter.fragment;

import android.content.Context;

import com.holike.crm.base.BasePresenter;
import com.holike.crm.model.fragment.CustomerOperateModel;
import com.holike.crm.view.fragment.CustomerUploadPlanView;

import java.util.List;

/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 */
public class CustomerUploadPlanPresenter extends BasePresenter<CustomerUploadPlanView, CustomerOperateModel> {

    public void uploadImages(Context context, List<String> images) {
        if (getModel() != null) {
            getModel().uploadImages(context, images);
        }
    }
}
