package com.holike.crm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.holike.crm.bean.DownloadFileBean;
import com.holike.crm.http.Download;
import com.holike.crm.http.ProgressResponseBody;
import com.holike.crm.util.NotificationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.holike.crm.service.UpdateService.DOWNLOADFILEBEAN;


/**
 * 下载文件服务父类
 */
public abstract class DownLoadService extends Service {
    protected List<DownloadFileBean> downloadFileBeans;
    protected Download download;
    protected long startTime;
    private NotificationUtils notificationUtils;

    public DownLoadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Serializable serializable = intent.getSerializableExtra(DOWNLOADFILEBEAN);
        if (serializable instanceof DownloadFileBean) {
            downloadFileBeans = new ArrayList<>();
            downloadFileBeans.add((DownloadFileBean) serializable);
        } else {
            downloadFileBeans = (List<DownloadFileBean>) serializable;
        }
        download = new Download();
        for (final DownloadFileBean downloadFileBean : downloadFileBeans) {
            ProgressResponseBody.ProgressListener progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void onProgress(long progress, long total, boolean done) {
                    showNotify(downloadFileBean.hashCode(), (int) (progress * 100 / total));
                }
            };
            downloadFileBean.setProgressListener(progressListener);
            download.downloadFile(downloadFileBean, setDownloadListener());
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 取消下载
     */
    public void cancel() {
        if (download != null) {
            download.cancel();
        }
    }

    abstract Download.DownloadListener setDownloadListener();

    protected boolean isShowNotify() {
        return false;
    }

    public void showNotify(final int notifyId, int progress) {
        if (isShowNotify()) {
            long time = System.currentTimeMillis() - startTime;
            if (time >= 2000) {
                startTime = System.currentTimeMillis();

                sendDownloadProgressBroadcast(notifyId, progress, false);
            }
        }
    }

    public void sendDownloadProgressBroadcast(int notifyId, int progress, boolean cancel) {
        if (notificationUtils == null) {
            notificationUtils = new NotificationUtils(this);
        }
        if (cancel) {
            notificationUtils.cancel(notifyId);
        } else {
            notificationUtils.sendNotification(notifyId, "下载", "下载" + progress + "%", progress);
        }
    }

    void cancelNotification(){
        notificationUtils.cancelAll();
    }
}
