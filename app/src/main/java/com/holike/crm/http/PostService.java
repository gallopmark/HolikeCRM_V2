package com.holike.crm.http;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by wqj on 2017/11/24.
 * post请求
 */

public interface PostService {
    @POST
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> post(@Url String url);

    @POST
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> postHeader(@Url String url, @HeaderMap Map<String, String> header);

    @POST
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> postParams(@Url String url, @FieldMap Map<String, String> param);

    @POST
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> postHeaderParams(@Url String url, @HeaderMap Map<String, String> header, @FieldMap Map<String, String> params);
}
