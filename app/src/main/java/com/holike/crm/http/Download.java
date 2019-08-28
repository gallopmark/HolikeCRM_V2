package com.holike.crm.http;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.holike.crm.bean.DownloadFileBean;
import com.holike.crm.util.IOUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by wqj on 2017/11/15.
 * 下载文件类
 */

public class Download {
    private Call<ResponseBody> currentCall;
    private DownloadService service;
    private ProgressResponseBody.ProgressListener progressListener;//下载进度回调监听

    public Download() {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(HttpClient.BASE_URL);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS).addNetworkInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response orginalResponse = chain.proceed(chain.request());
                return orginalResponse.newBuilder().body(new ProgressResponseBody(orginalResponse.body(), progressListener)).build();
            }
        }).build();
        service = builder.client(client).build().create(DownloadService.class);
    }

    /**
     * 取消当前下载
     */
    public void cancel() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }


    /**
     * 下载单个文件
     *
     * @param downloadFileBean
     * @param listener
     */
    public void downloadFile(DownloadFileBean downloadFileBean, DownloadListener listener) {
        List<DownloadFileBean> downloadFileBeans = new ArrayList<>();
        downloadFileBeans.add(downloadFileBean);
        downloadFiles(downloadFileBeans, listener);
    }

    /**
     * 下载多文件
     *
     * @param downloadFileBeans
     * @param listener
     */
    @SuppressLint("CheckResult")
    public void downloadFiles(final List<DownloadFileBean> downloadFileBeans, final DownloadListener listener) {
        Observable.just(downloadFileBeans).subscribeOn(Schedulers.io()).doOnNext(new Consumer<List<DownloadFileBean>>() {
            @Override
            public void accept(List<DownloadFileBean> downloadFileBeans) throws Exception {
                for (DownloadFileBean downloadFileBean : downloadFileBeans) {
                    if (!TextUtils.isEmpty(downloadFileBean.getUrl())) {
                        downFile(downloadFileBean);
                        if (currentCall.isCanceled()) {//取消下载
                            break;
                        }
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<DownloadFileBean>>() {
            @Override
            public void accept(List<DownloadFileBean> strings) throws Exception {
                if (currentCall.isCanceled()) {
                    listener.failed();
                } else {
                    listener.success();
                }
            }
        });
    }

    public interface DownloadListener {
        void success();

        void failed();
    }

    /**
     * 断点+进度条下载
     *
     * @param downloadFileBean 下载的文件参数
     */
    public void downFile(final DownloadFileBean downloadFileBean) {
        progressListener = downloadFileBean.getProgressListener();
        currentCall = service.downloadFile(downloadFileBean.getUrl());
        try {
            Response<ResponseBody> response = currentCall.execute();
            ResponseBody body = response.body();
            if (body != null) {
                IOUtil.saveFile(body.byteStream(), downloadFileBean.getPath(), downloadFileBean.getFileName());
            }
        } catch (IOException e) {
            currentCall.cancel();
            e.printStackTrace();
        }
    }
}
