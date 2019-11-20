package com.holike.crm.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gallopmark.imagepicker.model.ImagePicker;
import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.activity.main.PhotoViewActivity;
import com.holike.crm.bean.MultiItem;
import com.holike.crm.itemdecoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pony on 2019/7/19.
 * Copyright holike possess 2019.
 * 选择图片展示
 */
public class SelectImageHelper {

    private Activity mActivity;
    private Fragment mFragment;
    private static final int TYPE_ADD = 0;
    private static final int TYPE_IMAGE = 1;
    private CharSequence mTipsText;
    private boolean mIsMust; //是否必选
    private int mMaxSize;
    private List<MultiItem> mItems = new ArrayList<>();
    private int mSpanCount = 3;
    private SelectImageAdapter mImageAdapter;
    private OnImageOptionsListener mListener;
    private AddImageItem mAddItem;

    public static SelectImageHelper with(Activity activity) {
        return new SelectImageHelper(activity);
    }

    public static SelectImageHelper with(Fragment fragment) {
        return new SelectImageHelper(fragment);
    }

    private SelectImageHelper(Fragment fragment) {
        this.mFragment = fragment;
    }

    private SelectImageHelper(Activity activity) {
        this.mActivity = activity;
    }

    public SelectImageHelper tipsText(@NonNull CharSequence tipsText) {
        this.mTipsText = tipsText;
        return this;
    }

    public SelectImageHelper required(boolean isMust) {
        this.mIsMust = isMust;
        return this;
    }

    public SelectImageHelper maxSize(int mMaxSize) {
        this.mMaxSize = mMaxSize;
        return this;
    }

    public SelectImageHelper spanCount(int spanCount) {
        this.mSpanCount = spanCount;
        return this;
    }

    public SelectImageHelper imageOptionsListener(OnImageOptionsListener listener) {
        this.mListener = listener;
        return this;
    }

    public void attachRecyclerView(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new GridLayoutManager(context, mSpanCount));
        if (recyclerView.getItemDecorationCount() == 0) {
            int space = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(mSpanCount, space, false, 0));
        }
        mAddItem = new AddImageItem(mTipsText, mIsMust);
        mItems.add(mAddItem);
        mImageAdapter = new SelectImageAdapter(context, mItems);
        recyclerView.setAdapter(mImageAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImagePicker.DEFAULT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //获取选择器返回的数据
            List<String> images = null;
            ArrayList<String> list = data.getStringArrayListExtra(ImagePicker.SELECT_RESULT);
            if (list != null && !list.isEmpty()) {
                images = new ArrayList<>(list);
            }
            if (images != null) {
                setImagePaths(images);
            }
        }
    }

    public void setImagePaths(List<String> imagePaths) {
        if (mAddItem == null) {
            throw new IllegalArgumentException("you must attach recyclerView");
        }
        mItems.remove(mAddItem); //移除最后一个 “添加图片标识”
        if (mItems.size() >= mMaxSize) {  //超过限定的数量
            return;
        }
        for (String path : imagePaths) {
            mItems.add(new ImageItem(path));
        }
        if (mItems.size() < mMaxSize) {
            mItems.add(mAddItem);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    public List<String> getSelectedImages() {
        List<String> images = new ArrayList<>();
        for (MultiItem item : mItems) {
            if (item.getItemType() == TYPE_IMAGE) {
                images.add(((ImageItem) item).path);
            }
        }
        return images;
    }

    class SelectImageAdapter extends CommonAdapter<MultiItem> {

        SelectImageAdapter(Context context, List<MultiItem> mDatas) {
            super(context, mDatas);
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).getItemType();
        }

        @Override
        protected int bindView(int viewType) {
            if (viewType == TYPE_ADD) {
                return R.layout.item_imageunselected;
            }
            return R.layout.item_imageselected;
        }

        @Override
        public void onBindHolder(RecyclerHolder holder, MultiItem item, int position) {
            if (holder.getItemViewType() == TYPE_ADD) {
                AddImageItem addItem = (AddImageItem) item;
                if (getItemCount() == 1) {
                    holder.setText(R.id.tv_tips, addItem.tips);
                    holder.setVisibility(R.id.tv_must, addItem.isMust);
                } else {
                    holder.setVisibility(R.id.tv_must, false);
                    holder.setText(R.id.tv_tips, (getItemCount() - 1) + "/" + mMaxSize);
                }
                holder.itemView.setOnClickListener(view -> startImagePicker(mMaxSize - getItemCount() + 1));
            } else {
                final ImageItem imageItem = (ImageItem) item;
                ImageView ivPicture = holder.obtainView(R.id.iv_picture);
                Glide.with(mContext).load(imageItem.path).apply(new RequestOptions().error(0).placeholder(R.drawable.loading_photo).centerCrop()).into(ivPicture);
                ivPicture.setOnClickListener(view -> {
                    List<String> images = new ArrayList<>();
                    for (MultiItem multiItem : mItems) {
                        if (multiItem.getItemType() == TYPE_IMAGE) {
                            images.add(((ImageItem) multiItem).path);
                        }
                    }
                    if (mActivity != null) {
                        PhotoViewActivity.openImage(mActivity, position, images);
                    } else {
                        PhotoViewActivity.openImage(mFragment, position, images);
                    }
                });
                holder.setOnClickListener(R.id.tv_delete, view -> {
                    if (mListener != null) {
                        mListener.onDelete(position, imageItem.path);
                    } else {
                        remove(position);
                    }
                });
            }
        }
    }

    /*选择图片*/
    private void startImagePicker(int remainCount) {
        if (mActivity != null) {
            ImagePicker.builder().maxSelectCount(remainCount).start(mActivity);
        } else {
            ImagePicker.builder().maxSelectCount(remainCount).start(mFragment);
        }
    }

    public void remove(int position) {
        if (position < 0 || position > mItems.size()) {
            return;
        }
        mItems.remove(position);
        if (mItems.size() < mMaxSize) {
            mItems.remove(mAddItem);
            mItems.add(mAddItem);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    class AddImageItem implements MultiItem {
        CharSequence tips;
        boolean isMust;

        AddImageItem(CharSequence tips, boolean isMust) {
            this.tips = tips;
            this.isMust = isMust;
        }

        @Override
        public int getItemType() {
            return TYPE_ADD;
        }
    }

    class ImageItem implements MultiItem {
        String path;

        ImageItem(String path) {
            this.path = path;
        }

        @Override
        public int getItemType() {
            return TYPE_IMAGE;
        }
    }

    public interface OnImageOptionsListener {
        void onDelete(int position, String path);
    }
}
