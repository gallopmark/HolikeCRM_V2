package com.holike.crm.activity.customer.helper;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;
import com.holike.crm.util.GlideUtils;

import java.util.List;

/**
 * Created by pony on 2019/8/2.
 * Copyright holike possess 2019.
 */
public class CustomerImagePreviewHelper {

    private BaseActivity<?, ?> mActivity;

    public CustomerImagePreviewHelper(BaseActivity<?, ?> activity) {
        this.mActivity = activity;
    }

    public void setImageData(RecyclerView recyclerView, final List<String> images) {
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, mActivity.getResources().getDimensionPixelSize(R.dimen.dp_10), true, 0));
        if (images != null && !images.isEmpty()) {
            ImageGridAdapter adapter = new ImageGridAdapter(mActivity, images);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener((a, holder, view, position) -> {
                PhotoViewActivity.openImage(mActivity, position, images);
            });
        }
    }

    class ImageGridAdapter extends CommonAdapter<String> {

        ImageGridAdapter(Context context, List<String> mDatas) {
            super(context, mDatas);
        }

        @Override
        protected int bindView(int viewType) {
            return R.layout.item_square_image;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, String url, int position) {
            GlideUtils.load(mContext, url, R.drawable.loading_photo, holder.obtainView(R.id.iv_image));
        }
    }
}
