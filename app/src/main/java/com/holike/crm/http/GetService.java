package com.holike.crm.http;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by wqj on 2017/11/24.
 * get请求
 */

public interface GetService {
    @GET
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> get(@Url String url);

    @GET
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> getHeader(@Url String url, @HeaderMap Map<String, String> header);

    @GET
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> getParams(@Url String url, @QueryMap Map<String, String> params);

    @GET
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> getHeaderParams(@Url String url, @HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);
}
