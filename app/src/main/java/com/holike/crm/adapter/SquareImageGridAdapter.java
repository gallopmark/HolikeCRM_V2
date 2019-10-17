package com.holike.crm.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;

import java.util.List;

/**
 * Created by gallop on 2019/9/23.
 * Copyright holike possess 2019.
 * 图片列表适配器 方形图片
 */
public class SquareImageGridAdapter extends CommonAdapter<String> {

    public SquareImageGridAdapter(Context context, List<String> mDatas) {
        super(context, mDatas);
    }

    @Override
    protected int bindView(int viewType) {
        return R.layout.item_square_image;
    }

    @Override
    public void onBindHolder(RecyclerHolder holder, String url, int position) {
        Glide.with(mContext).load(url).apply(new RequestOptions()
                .placeholder(R.drawable.loading_photo)
                .error(0).centerCrop()).into((ImageView) holder.obtainView(R.id.iv_image));
        holder.itemView.setOnClickListener(view -> PhotoViewActivity.openImage(mContext, position, mDatas));
    }
}
