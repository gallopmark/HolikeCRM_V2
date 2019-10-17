package com.holike.crm.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by gallop on 2019/7/25.
 * Copyright holike possess 2019.
 */
public abstract class IImageSelectHelper {
    protected Context mContext;
    protected SelectImageHelper mImageHelper;

    public IImageSelectHelper(Fragment fragment) {
        this.mContext = fragment.getContext();
        mImageHelper = SelectImageHelper.with(fragment);
    }

    public IImageSelectHelper(Activity activity) {
        this.mContext = activity;
        mImageHelper = SelectImageHelper.with(activity);
    }

    protected void setupSelectImages(RecyclerView recyclerView, @StringRes int stringId) {
        setupSelectImages(recyclerView, stringId, false);
    }

    protected void setupSelectImages(RecyclerView recyclerView, @StringRes int stringId, boolean isMust) {
        mImageHelper.tipsText(mContext.getString(stringId))
                .required(isMust)
                .maxSize(9).spanCount(3).attachRecyclerView(recyclerView);
    }

    protected void setupSelectImages(RecyclerView recyclerView, @StringRes int stringId, boolean isMust, int size) {
        mImageHelper.tipsText(mContext.getString(stringId))
                .required(isMust)
                .maxSize(size).spanCount(3).attachRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mImageHelper.onActivityResult(requestCode, resultCode, data);
    }
}
