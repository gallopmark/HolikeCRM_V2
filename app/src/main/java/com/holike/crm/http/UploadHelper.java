package com.holike.crm.http;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.holike.crm.bean.UploadImagesBean;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.MultipartUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import top.zibin.luban.Luban;

/**
 * Created by gallop on 2019/8/20.
 * Copyright holike possess 2019.
 * 上传图片帮助类
 */
public class UploadHelper {
    /**
     * 压缩图片
     */
    private static String compressImg(Context context, String imgPath) {
        try {
            return Luban.with(context).load(imgPath).get(imgPath).getAbsolutePath();
        } catch (Exception e) {
            return imgPath;
        }
    }

    @Deprecated
    public static Observable<String> uploadImages(Context context, @Nullable List<String> imagePaths, final String body,
                                                  @NonNull final OnStepListener listener) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            return HttpClient.getInstance().postByBody(listener.doOnNext(), null, body);
        } else {
            return Observable.just(imagePaths).map(paths -> { //此步骤用于压缩图片处理
                List<String> compressImgPaths = new ArrayList<>();
                for (String path : paths) {
                    compressImgPaths.add(compressImg(context, path));
                }
                return compressImgPaths;
            }).flatMap((Function<List<String>, Observable<String>>) compressImgPaths -> {   //此步骤批量上传图片
                if (compressImgPaths.isEmpty()) return null;
                MultipartBody multipartBody = MultipartUtils.pathsToMultipartBody("file", compressImgPaths);
                return HttpClient.getInstance().getUploadObservable(CustomerUrlPath.URL_UPLOAD_IMAGE, multipartBody);
            }).map(json -> {
                int code = MyJsonParser.getCode(json);
                LogCat.e("上传图片返回", json);
                if (code == 0) {//上传图片成功
                    UploadImagesBean bean = MyJsonParser.fromJson(json, UploadImagesBean.class);
                    if (bean != null && !bean.getData().isEmpty()) {
                        return listener.onUploadSuccess(bean.getData(), json);
                    } else {
                        return json;
                    }
                }
                return json;
            }).flatMap((Function<String, ObservableSource<String>>) resources -> {
                if (MyJsonParser.hasCode(resources)) {  //上传失败 跳过此步骤
                    LogCat.e("上传图片失败", resources);
                    return Observable.just(resources);
                }
                return HttpClient.getInstance().postByBody(listener.saveImages(), null, resources);
            }).flatMap((Function<String, ObservableSource<String>>) json -> {
                int code = MyJsonParser.getCode(json);
                if (code == 0) { //进行下一步保存
                    if (listener.doOnNext() == null) return Observable.just(json);
                    return HttpClient.getInstance().postByBody(listener.doOnNext(), null, body);
                }
                return Observable.just(json);
            });
        }
    }

    public static Observable<String> doUpload(final Context context, final @Nullable List<String> imagePaths,
                                              final String body, final OnUploadProcessListener listener) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            return Observable.just(body).flatMap((Function<String, ObservableSource<String>>)
                    requestBody -> HttpClient.getInstance().postByBody(listener.doOnNextUrl(), null, requestBody));
        } else {
            return Observable.just(imagePaths).map(paths -> {
                //此步骤用于压缩图片处理
                List<String> compressImgPaths = new ArrayList<>();
                for (String path : paths) {
                    compressImgPaths.add(compressImg(context, path));
                }
                return compressImgPaths;
            }).flatMap((Function<List<String>, ObservableSource<String>>) compressImgPaths -> {
                //此步骤批量上传图片
                if (compressImgPaths.isEmpty()) return null;
                MultipartBody multipartBody = MultipartUtils.pathsToMultipartBody("file", compressImgPaths);
                return HttpClient.getInstance().getUploadObservable(CustomerUrlPath.URL_UPLOAD_IMAGE, multipartBody);
            }).map(json -> {
                int code = MyJsonParser.getCode(json);
                LogCat.e("上传图片返回", json);
                if (code == 0) {//上传图片成功
                    UploadImagesBean bean = MyJsonParser.fromJson(json, UploadImagesBean.class);
                    if (bean != null && !bean.getData().isEmpty()) {
                        return listener.toBody(bean.getData());
                    } else {
                        return json;
                    }
                }
                return json;
            }).flatMap((Function<String, ObservableSource<String>>) requestBody -> {
                if (MyJsonParser.hasCode(requestBody)) {  //上传失败 跳过此步骤
                    LogCat.e("上传图片失败", requestBody);
                    return Observable.just(requestBody);
                }
                return HttpClient.getInstance().postByBody(listener.doOnNextUrl(), null, requestBody);
            });
        }
    }

    public interface OnUploadProcessListener {
        /*图片上传成功，进行下一步数据封装,即将接口返回的图片集合包裹到请求体里*/
        String toBody(List<String> uploadResourceIds);

        String doOnNextUrl();
    }

    public interface OnStepListener {
        String onUploadSuccess(List<String> resourceIds, String json);

        //Url
        String saveImages();

        //Url
        String doOnNext();
    }
}
