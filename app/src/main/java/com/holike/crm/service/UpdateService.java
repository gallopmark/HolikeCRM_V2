package com.holike.crm.service;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.DownloadFileBean;
import com.holike.crm.http.Download;
import com.holike.crm.util.AppUtils;

import java.io.File;

/**
 * 更新apk服务
 */
@Deprecated
public class UpdateService extends DownLoadService {
    public static final String DOWNLOADFILEBEAN = "downloadFileBean";

    @Override
    protected boolean isShowNotify() {
        return true;
    }

    @Override
    Download.DownloadListener setDownloadListener() {
        return new Download.DownloadListener() {
            @Override
            public void success() {
                for (DownloadFileBean downloadFileBean : downloadFileBeans) {
                    sendDownloadProgressBroadcast(downloadFileBean.hashCode(), 100, true);
                    install(AppUtils.getApkPath() + "/" + downloadFileBean.getFileName());
                    stop();
                }
            }

            @Override
            public void failed() {
                stop();
            }
        };
    }

    public void stop() {
        cancelNotification();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (downloadFileBeans != null) {
            downloadFileBeans.clear();
        }
        super.onDestroy();
    }

    /**
     * 安装apk
     *
     * @param path
     */
    public static void install(String path) {
        AppUtils.installApp(new File(path));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //先获取是否有安装未知来源应用的权限
//            boolean haveInstallPermission = MyApplication.getInstance().getPackageManager().canRequestPackageInstalls();
//            if (!haveInstallPermission) {//没有权限
//                new SimpleDialog(MyApplication.getInstance().getCurrentActivity()).setDate("提示", "8.0以上更新需要允许安装未知来源哦", "取消", "马上开启").setListener(new SimpleDialog.ClickListener() {
//                    @Override
//                    public void left() {
//                    }
//
//                    @RequiresApi(api = Build.VERSION_CODES.O)
//                    @Override
//                    public void right() {
//                        startInstallPermissionSettingActivity();
//                    }
//                }).show();
//                return;
//            }
//        }
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Activity activity = MyApplication.getInstance().getCurrentActivity();
//        File apkFile = new File(path);
//        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        MyApplication.getInstance().getCurrentActivity().startActivityForResult(intent, 10086);
    }
}
