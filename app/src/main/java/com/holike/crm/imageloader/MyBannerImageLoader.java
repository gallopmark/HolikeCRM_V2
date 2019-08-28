package com.holike.crm.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.holike.crm.R;
import com.xcode.banner.loader.ImageLoaderImpl;

public class MyBannerImageLoader extends ImageLoaderImpl {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)                             //配置上下文
                .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .apply(new RequestOptions()
                        .error(R.color.color_while) //设置错误图片
                        .placeholder(R.color.color_while)//设置占位图片
                        .diskCacheStrategy(DiskCacheStrategy.ALL))  //缓存全尺寸
                .into(imageView);
        Glide.with(context).load(path).into(imageView);
    }
}
