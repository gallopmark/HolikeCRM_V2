//package com.holike.crm.imageloader;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.bumptech.glide.request.RequestOptions;
//import com.gallopmark.banner.loader.ImageLoader;
//import com.holike.crm.R;
//import com.holike.crm.util.DensityUtil;
//
///**
// * Created by wqj on 2018/1/6.
// * Banner的图片加载器
// */
//
//public class BannerImageLoader extends ImageLoader {
//    private int type;
//
//    public BannerImageLoader() {
//    }
//
//    public BannerImageLoader(int type) {
//        this.type = type;
//    }
//
//    @Override
//    public void displayImage(Context context, Object path, ImageView imageView) {
//        switch (type) {
//            case 0:
//                Glide.with(context)                             //配置上下文
//                        .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                        .apply(new RequestOptions()
//                                .error(R.color.color_while) //设置错误图片
//                                .placeholder(R.color.color_while)//设置占位图片
//                        .diskCacheStrategy(DiskCacheStrategy.ALL))  //缓存全尺寸
//                        .into(imageView);
//                break;
//            case 1:
//                Glide.with(context)                             //配置上下文
//                        .load(path)      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                        .apply(new RequestOptions()
//                                .error(R.color.color_while) //设置错误图片
//                                .placeholder(R.color.color_while)//设置占位图片
//                                .transform(new RoundedCorners(DensityUtil.dp2px(10)))
////                                .transform(new GlideRoundTransform(context,10)) //圆角
//                                .diskCacheStrategy(DiskCacheStrategy.ALL))  //缓存全尺寸
//                        .into(imageView);
//                break;
//        }
//    }
//
//}
