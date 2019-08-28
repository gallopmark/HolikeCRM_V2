package com.holike.crm.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by wqj on 2017/11/24.
 * 下载请求
 */

public interface DownloadService {
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
