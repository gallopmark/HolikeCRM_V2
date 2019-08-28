package com.holike.crm.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.holike.crm.util.LogCat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gallop on 2019/7/15.
 * Copyright holike possess 2019.
 * 监听通话状态时长
 */
public class PhoneStateService extends Service {
    private TelephonyManager mTelephonyManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogCat.e("phoneStateService已启动");
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTelephonyManager.listen(new SimplePhoneListener(this), PhoneStateListener.LISTEN_CALL_STATE);
        return START_NOT_STICKY;
    }

    final class SimplePhoneListener extends PhoneStateListener {
        private final Context context;
        //获取本次通话的时间(单位:秒)
        private int time = 0;
        //判断是否正在通话
        private boolean isCalling;
        //控制循环是否结束
        boolean isFinish;
        private ExecutorService service;

        SimplePhoneListener(Context context) {
            this.context = context;
            service = Executors.newSingleThreadExecutor();
        }

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    LogCat.e("电话处于休闲状态中...");
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
                    LogCat.e("电话处于通话中...");
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
                    LogCat.e("电话处于响铃中...");
                    isFinish = false;
                    if (service.isShutdown()) {
                        service = Executors.newSingleThreadExecutor();
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
