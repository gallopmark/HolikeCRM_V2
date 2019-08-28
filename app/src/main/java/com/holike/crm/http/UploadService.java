package com.holike.crm.http;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by wqj on 2018/6/4.
 * 上传文件
 */

public interface UploadService {
    @Multipart
    @POST()
    Observable<String> upLoad(@Url() String url, @Part List<MultipartBody.Part> parts);

    @Multipart
    @POST()
    Observable<String> upLoad(@Url() String url, @HeaderMap Map<String, String> header, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST()
    Observable<String> upLoad(@Url() String url, @HeaderMap Map<String, String> header, @Part("relationId") RequestBody relationId, @Part List<MultipartBody.Part> parts);

    @Multipart
    @POST()
    Observable<String> upLoadHeader(@Url() String url, @HeaderMap Map<String, String> header, @Part List<MultipartBody.Part> parts);

    @Multipart
    @POST()
    Observable<String> upLoadParam(@Url() String url, @QueryMap Map<String, String> param, @Part List<MultipartBody.Part> parts);

    @Multipart
    @POST()
    Observable<String> upLoad(@Url() String url, @HeaderMap Map<String, String> header, @QueryMap Map<String, String> param, @Part List<MultipartBody.Part> parts);

    /*以body方式上传图片*/
    @POST()
    Observable<String> upload(@Url String url, @Body MultipartBody multipartBody);

}
