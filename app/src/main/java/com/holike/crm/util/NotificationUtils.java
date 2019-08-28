package com.holike.crm.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.holike.crm.R;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by wqj on 2018/3/17.
 * 通知提醒
 */

public class NotificationUtils {

    private NotificationManager manager;
    private static final String CHANNEL = "holikeCrm_";
    private static final String CHANNEL_NAME = "holikeCrm_channel";
    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    /**
     * 8.0以上通知设置
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(int chanelId) {
        NotificationChannel channel = new NotificationChannel(CHANNEL + chanelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        String Description = "This is my channel";
        channel.setDescription(Description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.setShowBadge(false);
        switch (chanelId) {
            case 1://有声音、震动
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0, 500, 500, 500});
                break;
            case 2://有声音，没震动
                channel.enableVibration(false);
                break;
            case 3://没声音、有震动
                channel.setSound(null, null);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0, 500, 500, 500});
                break;
            case 4://没声音、没震动
                channel.enableVibration(false);
                channel.setSound(null, null);
                break;
        }
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @SuppressWarnings("deprecation")
    public NotificationCompat.Builder getNotification(String title, String content, int progress, PendingIntent pendingIntent, boolean isOpenSound, boolean isOpenShock) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {//8.0以上
            if (isOpenSound) {
                if (isOpenShock) {//有声音、震动
                    createNotificationChannel(1);
                    builder = new NotificationCompat.Builder(context, CHANNEL + 1);
                } else {//有声音，没震动
                    createNotificationChannel(2);
                    builder = new NotificationCompat.Builder(context, CHANNEL + 2);
                }
            } else {
                if (isOpenShock) {//没声音、有震动
                    createNotificationChannel(3);
                    builder = new NotificationCompat.Builder(context, CHANNEL + 3);
                } else {//没声音、没震动
                    createNotificationChannel(4);
                    builder = new NotificationCompat.Builder(context, CHANNEL + 4);
                }
            }
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder.setContentTitle(title).setContentText(content).setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true);
        if (isOpenShock && isOpenSound) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        } else if (isOpenSound) {
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
        } else if (isOpenShock) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        }
        if (progress >= 0) {
            builder.setProgress(100, progress, false);
        }
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        return builder;
    }

    public void sendNotification(String title, String content, PendingIntent pendingIntent, boolean isOpenSound, boolean isOpenShock) {
        sendNotification((int) System.currentTimeMillis(), title, content, -1, pendingIntent, isOpenSound, isOpenShock);
    }

    public void sendNotification(int id, String title, String content, int progress) {
        sendNotification(id, title, content, progress, null, false, false);
    }

    public void sendNotification(int id, String title, String content, int progress, PendingIntent pendingIntent, boolean isOpenSound, boolean isOpenShock) {
        Notification notification = getNotification(title, content, progress, pendingIntent, isOpenSound, isOpenShock).build();
        getManager().notify(id, notification);
    }

    public void cancel(int id) {
        getManager().cancel(id);
    }
}