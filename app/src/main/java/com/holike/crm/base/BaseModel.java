package com.holike.crm.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.holike.crm.http.CallbackHelper;
import com.holike.crm.http.HttpClient;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.MultipartUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import top.zibin.luban.Luban;

/**
 * Created by wqj on 2017/10/17.
 * 所有model的实现接口
 */

public abstract class BaseModel {
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public <T> void get(String url, RequestCallBack<T> callBack) {
        get(url, null, callBack);
    }

    /**
     * 带参数的get请求
     */
    public <T> void get(String url, @Nullable Map<String, String> params, RequestCallBack<T> callBack) {
        get(url, null, params, callBack);
    }

    /**
     * 带请求头和参数的get请求
     */
    public <T> void get(String url, @Nullable Map<String, String> header, @Nullable Map<String, String> params, RequestCallBack<T> callBack) {
        addDisposable(HttpClient.getInstance().get(url, header, params, callBack));
    }

    public <T> void getByTimeout(String url, int timeout, RequestCallBack<T> callBack) {
        getByTimeout(url, null, timeout, callBack);
    }

    public <T> void getByTimeout(String url, @Nullable Map<String, String> header, int timeout, RequestCallBack<T> callBack) {
        getByTimeout(url, header, null, timeout, callBack);
    }

    public <T> void getByTimeout(String url, @Nullable Map<String, String> header, @Nullable Map<String, String> params,
                                 int timeout, RequestCallBack<T> callBack) {
        addDisposable(MyHttpClient.getByTimeout(url, header, params, timeout, callBack));
    }

    /**
     * 只带url的post请求
     */
    public <T> void post(String url, RequestCallBack<T> callBack) {
        post(url, null, callBack);
    }

    /**
     * 带参数的post请求
     */
    public <T> void post(String url, @Nullable Map<String, String> params, RequestCallBack<T> callBack) {
        post(url, null, params, callBack);
    }

    public <T> void post(String url, @Nullable Map<String, String> header, @Nullable Map<String, String> params, RequestCallBack<T> callBack) {
        addDisposable(HttpClient.getInstance().post(url, header, params, callBack));
    }

    public <T> void postByTimeout(String url, int timeout, RequestCallBack<T> callBack) {
        postByTimeout(url, null, timeout, callBack);
    }

    public <T> void postByTimeout(String url, @Nullable Map<String, String> params, int timeout, RequestCallBack<T> callBack) {
        postByTimeout(url, null, params, timeout, callBack);
    }

    public <T> void postByTimeout(String url, @Nullable Map<String, String> header, @Nullable Map<String, String> params, int timeout, RequestCallBack<T> callBack) {
        addDisposable(MyHttpClient.postByTimeout(url, header, params, timeout, callBack));
    }

    public <T> void postByBody(String url, @Nullable String params, RequestCallBack<T> callBack) {
        postByBody(url, null, params, callBack);
    }

    public <T> void postByBody(String url, @Nullable Map<String, String> header, @Nullable String params, RequestCallBack<T> callBack) {
        addDisposable(MyHttpClient.postByBody(url, header, params, callBack));
    }

    public <T> void postBodyTimeout(String url, @Nullable String params, int timeout, RequestCallBack<T> callBack) {
        postBodyTimeout(url, null, params, timeout, callBack);
    }

    public <T> void postBodyTimeout(String url, @Nullable Map<String, String> header, @Nullable String params, int timeout, RequestCallBack<T> callBack) {
        addDisposable(MyHttpClient.postByBodyTimeout(url, header, params, timeout, callBack));
    }

    /*直接返回json字符串*/
    public void postByBodyString(String url, String params, RequestCallBack<String> callBack) {
        postByBodyString(url, null, params, callBack);
    }

    public void postByBodyString(String url, @Nullable Map<String, String> header, String params, RequestCallBack<String> callBack) {
        addDisposable(MyHttpClient.postByBodyString(url, header, params, callBack));
    }

    public void postByBodyString(String url, String params, int timeout, RequestCallBack<String> callBack) {
        postByBodyString(url, null, params, timeout, callBack);
    }

    public void postByBodyString(String url, @Nullable Map<String, String> header, String params, int timeout, RequestCallBack<String> callBack) {
        addDisposable(MyHttpClient.postByBodyString(url, header, params, timeout, callBack));
    }

    public <T> void upload(final Context context, String url, @NonNull List<String> filePaths, RequestCallBack<T> callBack) {
        if (filePaths.isEmpty()) {
            callBack.onFailed("请选择需要上传的图片");
            return;
        }
        addDisposable(Observable.just(filePaths).subscribeOn(Schedulers.io())
                .map(paths -> {
                    List<String> compressImgPaths = new ArrayList<>();
                    for (String path : paths) {
                        compressImgPaths.add(compressImg(context, path));
                    }
                    return compressImgPaths;
                }).flatMap((Function<List<String>, ObservableSource<String>>) paths -> {
                    List<MultipartBody.Part> parts = MultipartUtils.pathsToMultipartBodyParts("file", paths);
                    return HttpClient.getInstance().getUploadObservable(url, null, null, parts);
                }).subscribe(s -> {
                    LogCat.e("上传图片成功", s);
                    CallbackHelper.onDeliverySuccess(s, callBack);
                }, throwable -> {
                    LogCat.e("上传图片失败", throwable.getMessage());
                    CallbackHelper.onDeliveryFailure(throwable, callBack);
                }));
    }

    public <T> void uploadWithBody(final Context context, String url, @NonNull List<String> filePaths, RequestCallBack<T> callBack) {
        if (filePaths.isEmpty()) {
            callBack.onFailed("请选择需要上传的图片");
            return;
        }
        addDisposable(Observable.just(filePaths).subscribeOn(Schedulers.io())
                .map(paths -> {
                    List<String> compressImgPaths = new ArrayList<>();
                    for (String path : paths) {
                        compressImgPaths.add(compressImg(context, path));
                    }
                    return compressImgPaths;
                }).flatMap((Function<List<String>, ObservableSource<String>>) paths -> {
                    MultipartBody multipartBody = MultipartUtils.pathsToMultipartBody("file", paths);
                    return HttpClient.getInstance().getUploadObservable(url, multipartBody);
                }).subscribe(s -> {
                    LogCat.e("上传图片成功", s);
                    CallbackHelper.onDeliverySuccess(s, callBack);
                }, throwable -> {
                    LogCat.e("上传图片失败", throwable.getMessage());
                    CallbackHelper.onDeliveryFailure(throwable, callBack);
                }));
    }

    /**
     * 压缩图片
     */
    public String compressImg(Context context, String imgPath) {
        try {
            return Luban.with(context).load(imgPath).get(imgPath).getAbsolutePath();
        } catch (Exception e) {
            return imgPath;
        }
    }

    public void addDisposable(@Nullable Disposable disposable) {
        if (disposable != null) {
            mDisposable.add(disposable);
        }
    }

    public void removeDisposable(@Nullable Disposable disposable) {
        if (disposable != null) {
            mDisposable.remove(disposable);
        }
    }

    public void dispose() {
        mDisposable.dispose();
    }
}
