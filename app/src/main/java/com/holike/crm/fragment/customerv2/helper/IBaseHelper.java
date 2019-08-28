package com.holike.crm.fragment.customerv2.helper;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.holike.crm.helper.SelectImageHelper;

/**
 * Created by gallop on 2019/7/25.
 * Copyright holike possess 2019.
 */
abstract class IBaseHelper {
    protected Context mContext;
    protected SelectImageHelper mImageHelper;

    IBaseHelper(Fragment fragment) {
        this.mContext = fragment.getContext();
        mImageHelper = SelectImageHelper.with(fragment);
    }

    public void setupSelectImages(RecyclerView recyclerView, @StringRes int stringId) {
        setupSelectImages(recyclerView, stringId, false);
    }

    public void setupSelectImages(RecyclerView recyclerView, @StringRes int stringId, boolean isMust) {
        mImageHelper.tipsText(mContext.getString(stringId))
                .required(isMust)
                .maxSize(9).spanCount(3).attachRecyclerView(recyclerView);
    }

    public void setupSelectImages(RecyclerView recyclerView, @StringRes int stringId, boolean isMust, int size) {
        mImageHelper.tipsText(mContext.getString(stringId))
                .required(isMust)
                .maxSize(size).spanCount(3).attachRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mImageHelper.onActivityResult(requestCode, resultCode, data);
    }
}
