package com.holike.crm.receiver.push;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.holike.crm.activity.homepage.MessageV2Activity;
import com.holike.crm.activity.main.MainActivity;
import com.holike.crm.base.MyApplication;
import com.holike.crm.bean.PushMsgBean;
import com.holike.crm.util.Constants;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.NotificationUtils;

import java.io.Serializable;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wqj on 2017/10/17.
 * 极光推送
 */

public class JPushReceiver extends BroadcastReceiver {
//    private static final String TAG = "JPushReceiver";
    public final static String DATE = "date";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int notifyId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                String msg = getMsg(bundle);
                notify(context, msg);
                LogCat.d("notifyId=" + notifyId);
            }
        }
    }

    /**
     * 获取推送的消息体
     */
    private static String getMsg(Bundle bundle) {
        String msg = null;
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
            }
        }
        return msg;
    }

    /**
     * 推送通知
     */
    private void notify(Context context, String msg) {
        try {
            PushMsgBean bean = new Gson().fromJson(msg, PushMsgBean.class);
            sendUpdateBroadcast(Constants.ACTION_UNREAD_MESSAGE, "1");
            String type = bean.getType();
            if (TextUtils.equals(type, "12")) {
                new NotificationUtils(context).sendNotification(bean.getTitle(), bean.getContent(),
                        getPendingIntent(context, MessageV2Activity.class, Constants.PUSH_TYPE_NOTIFY), true, true);
            } else {

            }
//            switch (Integer.parseInt(bean.getType())) {
//                case 12:
//
//                    break;
//
//            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 发送广播
     */
    public static void sendUpdateBroadcast(String action, Object o) {
        if (action != null && o != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            if (o instanceof String) {
                intent.putExtra(DATE, (String) o);
            } else if (o instanceof Serializable) {
                intent.putExtra(DATE, (Serializable) o);
            } else {
                return;
            }
            MyApplication.getInstance().sendBroadcast(intent);
        }
    }

    /**
     * 获取要打开的页面
     */
    private static PendingIntent getPendingIntent(Context context, Class<?> activity, int pushType) {
        Intent resultIntent = new Intent(context, activity);
        resultIntent.putExtra(Constants.PUSH_TYPE, pushType);
        Intent[] intents = new Intent[]{resultIntent};
        if (MyApplication.getInstance().isExit()) {//若应用未启动，先打开首页再进入指定页
            Intent mainIntent = new Intent(context, MainActivity.class);
            intents = new Intent[]{mainIntent, resultIntent};
        }
        return PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}