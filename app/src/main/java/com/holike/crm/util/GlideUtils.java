package com.holike.crm.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.gallopmark.imagepicker.utils.ImageUtil;

/*glide图片加载器*/
public class GlideUtils {

    public static void load(Context context, String url, ImageView ivTarget) {
        Glide.with(context).load(url).into(ivTarget);
    }

    public static void load(Activity activity, String url, ImageView ivTarget) {
        Glide.with(activity).load(url).into(ivTarget);
    }

    public static void load(Fragment fragment, String url, ImageView ivTarget) {
        Glide.with(fragment).load(url).into(ivTarget);
    }

    public static void load(Context context, String url, @DrawableRes int resourceId, ImageView ivTarget) {
        Glide.with(context).load(url).apply(new RequestOptions().placeholder(resourceId)).into(ivTarget);
    }

    public static void load(Activity activity, String url, @DrawableRes int placeResourceId, ImageView ivTarget) {
        Glide.with(activity).load(url).apply(new RequestOptions().placeholder(placeResourceId)).into(ivTarget);
    }

    public static void load(Fragment fragment, String url, @DrawableRes int placeResourceId, ImageView ivTarget) {
        Glide.with(fragment).load(url).apply(new RequestOptions().placeholder(placeResourceId)).into(ivTarget);
    }

    public static void load(Context context, String url, @DrawableRes int placeResourceId, @DrawableRes int errorResourceId, ImageView ivTarget) {
        Glide.with(context).load(url).apply(new RequestOptions().placeholder(placeResourceId).error(errorResourceId)).into(ivTarget);
    }

    public static void load(Activity activity, String url, @DrawableRes int resourceId, @DrawableRes int errorResourceId, ImageView ivTarget) {
        Glide.with(activity).load(url).apply(new RequestOptions().placeholder(resourceId).error(errorResourceId)).into(ivTarget);
    }

    public static void load(Fragment fragment, String url, @DrawableRes int resourceId, @DrawableRes int errorResourceId, ImageView ivTarget) {
        Glide.with(fragment).load(url).apply(new RequestOptions().placeholder(resourceId).error(errorResourceId)).into(ivTarget);
    }

    /*查看大图处理*/
    public static void imagePreview(Context context, String url, ImageView imageView) {
        Glide.with(context).asBitmap().load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource == null) return;
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        Bitmap newBitmap = resource;
                        if (width > 8192 || height > 8192) {
                            newBitmap = ImageUtil.zoomBitmap(resource, 8192, 8192);
                        }
                        setBitmap(imageView, newBitmap);
                    }
                });
    }

    private static void setBitmap(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        if (bitmap != null) {
            int bw = bitmap.getWidth();
            int bh = bitmap.getHeight();
            int vw = imageView.getWidth();
            int vh = imageView.getHeight();
            if (bw != 0 && bh != 0 && vw != 0 && vh != 0) {
                if (1.0f * bh / bw > 1.0f * vh / vw) {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
        }
    }
}
