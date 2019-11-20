package com.holike.crm.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.holike.crm.R;
import com.holike.crm.customView.AppToastCompat;
import com.holike.crm.util.AppUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * Created by pony on 2019/9/9.
 * Copyright holike possess 2019.
 * 版本更新服务
 */
public class VersionUpdateService extends Service {
    private static final int NOTIFICATION_ID = 0x0001;
    private static final String CHANNEL = "holike";
    private static final String CHANNEL_NAME = "holikeCRM";
    private static final String ACTION_CLICK = "ACTION_CLICK";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private String mUrl;
    private boolean mError = false;

    public static final int REQUEST_CODE_APP_INSTALL = 12;
    public static final String ACTION_INSTALL = "ACTION_INSTALL";


    public static void start(Context context, String url) {
        Intent intent = new Intent(context, VersionUpdateService.class);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), ACTION_CLICK) && mError) {
                startDownload();
                mError = false;
            } else if (TextUtils.equals(intent.getAction(), ACTION_INSTALL)) {
                if (AppUtils.canInstallApk()) {
                    installApk(AppUtils.getTargetApkPath());
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setupNotification();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CLICK);
        filter.addAction(ACTION_INSTALL);
        registerReceiver(mReceiver, filter);
    }

    @SuppressWarnings("deprecation")
    private void setupNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL + NOTIFICATION_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            String Description = "This is my channel";
            channel.setDescription(Description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(false);
            channel.enableVibration(false);
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder = new NotificationCompat.Builder(this, CHANNEL + NOTIFICATION_ID);
        } else {
            mBuilder = new NotificationCompat.Builder(this);
        }
        Intent intentClick = new Intent(ACTION_CLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentClick, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.version_update_notification_wait))
                .setProgress(100, 0, true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
        } else {
            mUrl = intent.getStringExtra("url");
            if (TextUtils.isEmpty(mUrl)) {
                stopSelf();
            } else {
                startDownload();
            }
        }
        return START_NOT_STICKY;
    }

    /*可以从首页进来，也可以从我的tab进来，所以需要标识是否正在更新，否则会导致更新错乱*/
    private void startDownload() {
        String apkPath = AppUtils.getTargetApkPath();
        if (!TextUtils.isEmpty(apkPath) && new File(apkPath).exists()) {  //下载记录已存在
            checkAndInstall(apkPath);
        } else {
            AppToastCompat.makeText(this, R.string.version_update_notification_wait, Toast.LENGTH_LONG).show();
            updateNotification();
            FileDownloader.getImpl().create(mUrl).setPath(apkPath, false)
                    .setForceReDownload(true)
                    .setListener(mDownloadListener)
                    .start();
        }
    }

    private final FileDownloadListener mDownloadListener = new FileDownloadSampleListener() {
        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            updateNotification();
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            mBuilder.setProgress(totalBytes, soFarBytes, false);
            updateNotification();
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            String savePath = task.getTargetFilePath();
            checkAndInstall(savePath);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            mError = true;
            mBuilder.setContentText(getString(R.string.version_update_pause));
            updateNotification();
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            mError = true;
            mBuilder.setContentText(getString(R.string.version_update_error));
            updateNotification();
        }
    };

    private void updateNotification() {
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void checkAndInstall(String apkPath) {
        mNotificationManager.cancel(NOTIFICATION_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (AppUtils.canInstallApk()) {
                installApk(apkPath);
            } else {
                AppUtils.startUnknownAppSourceSetting(REQUEST_CODE_APP_INSTALL);
            }
        } else {
            installApk(apkPath);
        }
    }

    /*调用系统安装apk并停止当前服务*/
    private void installApk(String apkPath) { //data/user/0/com.holike.crm/files/apk/1.9.4_higher_CRM.apk
        AppUtils.installApp(apkPath);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        FileDownloader.getImpl().pause(mDownloadListener);
        mNotificationManager.cancel(NOTIFICATION_ID);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
