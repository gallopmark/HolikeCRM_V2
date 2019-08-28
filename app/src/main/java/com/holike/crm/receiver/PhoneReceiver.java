package com.holike.crm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gallop on 2019/7/15.
 * Copyright holike possess 2019.
 * 监听通话记录
 */
@Deprecated
public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (phoneManager != null) {
                phoneManager.listen(new PhoneListen(context), PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

    final class PhoneListen extends PhoneStateListener {
        private final Context context;
        //获取本次通话的时间(单位:秒)
        private int time = 0;
        //判断是否正在通话
        private  boolean isCalling;
        //控制循环是否结束
        boolean isFinish;
        private ExecutorService service;

        PhoneListen(Context context) {
            this.context = context;
            service = Executors.newSingleThreadExecutor();
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (isCalling) {
                        isCalling = false;
                        isFinish = true;
                        service.shutdown();
                        Toast.makeText(context, "本次通话" + time + "秒",
                                Toast.LENGTH_LONG).show();
                        time = 0;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    isCalling = true;
                    service.execute(() -> {
                        while (!isFinish) {
                            try {
                                Thread.sleep(1000);
                                time++;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    isFinish = false;
                    if (service.isShutdown()) {
                        service = Executors.newSingleThreadExecutor();
                    }
                    break;
            }
        }
    }
}
