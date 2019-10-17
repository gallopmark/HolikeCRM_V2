package com.holike.crm.http;


import androidx.annotation.Nullable;

import com.holike.crm.util.Constants;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.PackageUtil;
import com.holike.crm.util.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpClient {
    private volatile static HttpClient instance = null;
    static final String BASE_URL = "http://39.108.84.67:3333";
    private static final int DEFAULT_TIMEOUT = 20;

    private OkHttpClient mHttpClient;
    private Retrofit mRetrofit;
//    private List<RequestCallBack> callBacks;
//    private RequestCallBack currentCallBack;

    //构造方法私有
    private HttpClient() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //开启builder日志 等级BODY
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClientBuilder.addInterceptor(loggingInterceptor);
        //---

        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mHttpClient = builder.build();
//        callBacks = new ArrayList<>();

        mRetrofit = new Retrofit.Builder().client(mHttpClient).addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(BASE_URL).build();

    }

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }
//
//    //在访问HttpMethods时创建单例
//    private static class SingletonHolder {
//        private static final HttpClient INSTANCE = new HttpClient();
//    }
//
//    //获取单例
//    public static HttpClient getInstance() {
//        return SingletonHolder.INSTANCE;
//    }

    public OkHttpClient getHttpClient() {
        return mHttpClient;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /*get请求方式*/
    @Nullable
    public <T> Disposable get(String url, Map<String, String> header, Map<String, String> params, RequestCallBack<T> callBack) {
        if (url == null) {
            return null;
        }
        return CallbackHelper.deliveryResult(get(url, header, params), callBack);
    }

    public Observable<String> get(String url, Map<String, String> params) {
        return get(url, null, params);
    }

    public Observable<String> get(String url, @Nullable Map<String, String> header, Map<String, String> params) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
//        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE2, ""));
        GetService service = mRetrofit.create(GetService.class);
        Observable<String> call;
//        if (header == null) {
//            if (params == null) {
//                call = service.get(url);
//                LogCat.v_request("GET url:" + url);
//            } else {
//                call = service.getParams(url, params);
//                LogCat.v_request("GET url:" + url + "\nparams:" + params.toString());
//            }
//        } else {
        if (params == null) {
            call = service.getHeader(url, header);
            LogCat.v_request("GET url:" + url + "\nheader:" + header.toString());
        } else {
            call = service.getHeaderParams(url, header, params);
            LogCat.v_request("GET url:" + url + "\nheader:" + header.toString() + "\nparams:" + params);
        }
        return call;
    }

    /*post请求方式*/
    @Nullable
    public <T> Disposable post(String url, Map<String, String> header, Map<String, String> params, RequestCallBack<T> callBack) {
        if (url == null) {
            return null;
        }
        PostService service = mRetrofit.create(PostService.class);
        Observable<String> call;
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE, ""));
        if (params == null) {
            call = service.postHeader(url, header);
            LogCat.v_request("POST url:" + url + "\nheader:" + header.toString());
        } else {
            call = service.postHeaderParams(url, header, params);
            LogCat.v_request("POST url:" + url + "\nheader:" + header.toString() + "\nparams:" + params.toString());
        }
        //        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        return CallbackHelper.deliveryResult(call, callBack);
//        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
    }

    /*参数为body*/
    @Nullable
    public <T> Disposable postByBody(String url, Map<String, String> header, String params, RequestCallBack<T> callBack) {
        if (url == null || params == null) {
            return null;
        }
        Observable<String> call = postByBody(url, header, params);
//        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        return CallbackHelper.deliveryResult(call, callBack);
//        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
    }

    public Observable<String> postByBody(String url, @Nullable Map<String, String> header, String params) {
        BodyService service = mRetrofit.create(BodyService.class);
        Observable<String> call;
        LogCat.v_request(url);
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE2, ""));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), params);
        call = service.post(url, header, body);
        LogCat.v_request("header:" + header.toString() + "\nparams:" + params);
        //        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        return call;
//        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
    }

    /*不对json进行处理直接返回*/
    @Nullable
    Disposable postByBodyString(String url, Map<String, String> header, String params, RequestCallBack<String> callBack) {
        if (url == null || params == null) {
            return null;
        }
        Observable<String> call = postByBody(url, header, params);
//        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        return CallbackHelper.processResult(call, callBack);
//        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
    }

    /*上传文件*/
    @Nullable
    public Disposable upload(String url, Map<String, String> header, Map<String, String> params, List<MultipartBody.Part> parts, RequestCallBack<?> callBack) {
        if (url == null) {
            return null;
        }
//        UploadService service = mRetrofit.create(UploadService.class);
        Observable<String> call = getUploadObservable(url, header, params, parts);
//        if (header == null) {
//            if (params == null) {
//                call = service.upLoad(url, parts);
//                LogCat.v_request("UPLOAD url:" + url + "\nparts:" + parts.toString());
//            } else {
//                call = service.upLoadParam(url, params, parts);
//                LogCat.v_request("UPLOAD url:" + url + "\nparams:" + params.toString() + "\nparts:" + parts.toString());
//            }
//        } else {
//            if (params == null) {
//                call = service.upLoadHeader(url, header, parts);
//                LogCat.v_request("UPLOAD url:" + url + "\nheader:" + header.toString() + "\nparts:" + parts.toString());
//            } else {
//                call = service.upLoad(url, header, params, parts);
//                LogCat.v_request("UPLOAD url:" + url + "\nheader:" + header.toString() + "\nparams:" + params + "\nparts:" + parts.toString());
//            }
//        }
//        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        return CallbackHelper.deliveryResult(call, callBack);
//        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
    }

    public Observable<String> getUploadObservable(String url, MultipartBody multipartBody) {
        UploadService service = mRetrofit.create(UploadService.class);
        return service.upload(url, multipartBody);
    }

    public Observable<String> getUploadObservable(String url, Map<String, String> header, Map<String, String> params, List<MultipartBody.Part> parts) {
        UploadService service = mRetrofit.create(UploadService.class);
        Observable<String> call;
        if (header == null) {
            if (params == null) {
                call = service.upLoad(url, parts);
                LogCat.v_request("UPLOAD url:" + url + "\nparts:" + parts.toString());
            } else {
                call = service.upLoadParam(url, params, parts);
                LogCat.v_request("UPLOAD url:" + url + "\nparams:" + params.toString() + "\nparts:" + parts.toString());
            }
        } else {
            if (params == null) {
                call = service.upLoadHeader(url, header, parts);
                LogCat.v_request("UPLOAD url:" + url + "\nheader:" + header.toString() + "\nparts:" + parts.toString());
            } else {
                call = service.upLoad(url, header, params, parts);
                LogCat.v_request("UPLOAD url:" + url + "\nheader:" + header.toString() + "\nparams:" + params + "\nparts:" + parts.toString());
            }
        }
        return call;
    }

    /*上传文件*/
    @Nullable
    public Disposable upload(String url, Map<String, String> header, String relationId, List<MultipartBody.Part> parts, RequestCallBack<?> callBack) {
        if (url == null) {
            return null;
        }
        UploadService service = mRetrofit.create(UploadService.class);
        Observable<String> call = service.upLoad(url, header, toRequestBody(relationId), parts);
//        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        LogCat.v_request("UPLOAD url:" + url + "\nheader:" + header.toString() + "\nparts:" + parts.toString());
        return CallbackHelper.deliveryResult(call, callBack);
//        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
//        LogCat.v_request("UPLOAD url:" + url + "\nheader:" + header.toString() + "\nparts:" + parts.toString());
    }

    private static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

//    /**
//     * 取消当前请求
//     */
//    public void cancel() {
//        if (currentCallBack != null) {
//            currentCallBack.cancel();
//        }
//    }
//
//    /**
//     * 取消全部请求
//     */
//    public void cancelAll() {
//        if (callBacks != null && callBacks.size() > 0) {
//            for (RequestCallBack callBack : callBacks) {
//                if (callBack != null) {
//                    callBack.cancel();
//                }
//            }
//            callBacks.clear();
//        }
//    }
//
//    /**
//     * 返回成功移除响应
//     */
//    public void remove(RequestCallBack callBack) {
//        if (callBacks != null && callBacks.size() > 0) {
//            callBacks.remove(callBack);
//        }
//    }
}
