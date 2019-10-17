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


/**
 * Created by Administrator on 2016/12/8 0008.
 * 请求框架，请求头带版本(version)默认参数
 */

public class MyHttpClient {

    /**
     * 只带url的get请求
     */
    public static <T> Disposable get(String url, RequestCallBack<T> callBack) {
        return get(url, null, callBack);
    }

    /**
     * 带参数的get请求
     */
    public static <T> Disposable get(String url, @Nullable Map<String, String> params, RequestCallBack<T> callBack) {
        return get(url, null, params, callBack);
    }

    /**
     * 带请求头和参数的get请求
     */
    public static <T> Disposable get(String url, @Nullable Map<String, String> header, @Nullable Map<String, String> params, RequestCallBack<T> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE2, ""));
        return HttpClient.getInstance().get(url, header, params, callBack);
    }

    public static <T> Disposable getByTimeout(String url, int timeout, RequestCallBack<T> callBack) {
        return getByTimeout(url, null, timeout, callBack);
    }

    public static <T> Disposable getByTimeout(String url, @Nullable Map<String, String> header, int timeout, RequestCallBack<T> callBack) {
        return getByTimeout(url, header, null, timeout, callBack);
    }

    public static <T> Disposable getByTimeout(String url, @Nullable Map<String, String> header, @Nullable Map<String, String> params,
                                              int timeout, RequestCallBack<T> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
//        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE, ""));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE2, ""));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(timeout, TimeUnit.SECONDS).connectTimeout(timeout, TimeUnit.SECONDS);
        //----开启builder日志 等级BODY----
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        ob.addInterceptor(loggingInterceptor);
        //-------
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HttpClient.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        GetService service = retrofit.create(GetService.class);
        Observable<String> call;
        if (params == null) {
            call = service.getHeader(url, header);
            LogCat.v_request("GET url:" + url + "\nheader:" + header.toString());
        } else {
            call = service.getHeaderParams(url, header, params);
            LogCat.v_request("GET url:" + url + "\nheader:" + header.toString() + "\nparams:" + params);
        }
        //        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        return CallbackHelper.deliveryResult(call, callBack);
    }

    /**
     * 只带url的post请求
     */
    public static Disposable post(String url, RequestCallBack<?> callBack) {
        return post(url, null, null, callBack);
    }

    /**
     * 带参数的post请求
     */
    public static Disposable post(String url, Map<String, String> params, RequestCallBack<?> callBack) {
        return post(url, null, params, callBack);
    }

    /**
     * 带cliid请求头和参数的post请求
     */
    public static Disposable postByCliId(String url, Map<String, String> header, Map<String, String> params, RequestCallBack<?> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE, ""));
        return post(url, header, params, callBack);
    }

    /**
     * 带请求头和参数的post请求
     */
    public static Disposable post(String url, Map<String, String> header, Map<String, String> params, RequestCallBack<?> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE, ""));
        return HttpClient.getInstance().post(url, header, params, callBack);
    }

    /**
     * 带参数的body post请求
     */
    public static <T> Disposable postByBody(String url, String params, RequestCallBack<T> callBack) {
        return postByBody(url, null, params, callBack);
    }

    /**
     * 带请求头和参数的body post请求
     */
    public static <T> Disposable postByBody(String url, Map<String, String> header, String params, RequestCallBack<T> callBack) {
        return HttpClient.getInstance().postByBody(url, header, params, callBack);
    }

    /*直接返回json字符串*/
    public static Disposable postByBodyString(String url, String params, RequestCallBack<String> callBack) {
        return postByBodyString(url, null, params, callBack);
    }

    public static Disposable postByBodyString(String url, @Nullable Map<String, String> header, String params, RequestCallBack<String> callBack) {
        return HttpClient.getInstance().postByBodyString(url, header, params, callBack);
    }

    public static Disposable postByBodyString(String url, String params, int timeout, RequestCallBack<String> callBack) {
        return postByBodyString(url, null, params, timeout, callBack);
    }

    public static Disposable postByBodyString(String url, @Nullable Map<String, String> header, String params, int timeout, RequestCallBack<String> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE2, ""));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(timeout, TimeUnit.SECONDS).connectTimeout(timeout, TimeUnit.SECONDS);
        //----开启builder日志 等级BODY----
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        ob.addInterceptor(loggingInterceptor);
        //-------
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HttpClient.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        BodyService service = retrofit.create(BodyService.class);
        Observable<String> call;
        LogCat.v_request(url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), params);
        call = service.post(url, header, body);
        LogCat.v_request("POST url:" + url + "\nheader:" + header.toString() + "\nparams:" + params);
        return CallbackHelper.processResult(call, callBack);
    }

    public static <T> Disposable postByBodyTimeout(String url, Map<String, String> header, String params,
                                                   int timeout, RequestCallBack<T> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE2, ""));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(timeout, TimeUnit.SECONDS).connectTimeout(timeout, TimeUnit.SECONDS);
        //----开启builder日志 等级BODY----
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        ob.addInterceptor(loggingInterceptor);
        //-------
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HttpClient.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        BodyService service = retrofit.create(BodyService.class);
        Observable<String> call;
        LogCat.v_request(url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), params);
        call = service.post(url, header, body);
        LogCat.v_request("POST url:" + url + "\nheader:" + header.toString() + "\nparams:" + params);
        //        currentCallBack = callBack;
//        callBacks.add(currentCallBack);
        return CallbackHelper.deliveryResult(call, callBack);
    }

    /**
     * 带cliid请求头和参数的body post请求
     */
    public static Disposable postByBodyWithId(String url, Map<String, String> header, String params, RequestCallBack<?> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        return postByBody(url, header, params, callBack);
    }

    /**
     * 可设置超时时间请求
     */
    public static Disposable postByTimeout(String url, @Nullable Map<String, String> header, @Nullable Map<String, String> params, int timeout, RequestCallBack<?> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.CLI_ID, SharedPreferencesUtils.getString(Constants.CLI_ID));
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE, ""));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(timeout, TimeUnit.SECONDS).connectTimeout(timeout, TimeUnit.SECONDS);
        //----开启builder日志 等级BODY----
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        ob.addInterceptor(loggingInterceptor);
        //-------
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HttpClient.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        PostService service = retrofit.create(PostService.class);
        Observable<String> call;
        if (params == null) {
            LogCat.v_request("POST url:" + url + "\nheader:" + header.toString());
            call = service.postHeader(url, header);
        } else {
            LogCat.v_request("POST url:" + url + "\nheader:" + header.toString() + "\nparams:" + params.toString());
            call = service.postHeaderParams(url, header, params);
        }
        return CallbackHelper.deliveryResult(call, callBack);
//        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
    }

    public static Disposable upload(String url, Map<String, String> header, Map<String, String> params, List<MultipartBody.Part> parts, RequestCallBack<?> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        return HttpClient.getInstance().upload(url, header, params, parts, callBack);
    }

    public static Disposable upload(String url, Map<String, String> header, String relationId, List<MultipartBody.Part> parts, RequestCallBack<?> callBack) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(Constants.VERSION, String.valueOf(PackageUtil.getVersionCode()));
        header.put(Constants.CLIENT, Constants.ANDROID);
        header.put(Constants.COOKIE, SharedPreferencesUtils.getString(Constants.COOKIE));
        return HttpClient.getInstance().upload(url, header, relationId, parts, callBack);
    }


}
