package com.holike.crm.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by wqj on 2017/11/24.
 * body请求
 */

public interface BodyService {
    @POST
//    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> post(@Url String url, @Body RequestBody body);

    @POST
//    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<String> post(@Url String url, @HeaderMap Map<String, String> header, @Body RequestBody body);
}
