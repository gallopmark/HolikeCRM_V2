package com.holike.crm.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.holike.crm.base.BaseActivity;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by gallop on 2019/8/26.
 * Copyright holike possess 2019.
 */
class MainHelper {
    /*监听通话过程*/
    private TelephonyManager mTelephonyManager;
    private CompositeDisposable mDisposables;
//    private String mCallPhoneNumber, mPersonalId, mHouseId;
    private String mPersonalId;
    private String mRequestBody;
    private Handler mHandler;

    MainHelper(BaseActivity<?, ?> activity, Handler handler) {
        this.mHandler = handler;
        mDisposables = new CompositeDisposable();
        telephonyListen(activity);
    }

    /*监听通话过程*/
    private void telephonyListen(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(new SimplePhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
        mDisposables.add(RxBus.getInstance().toObservable(MessageEvent.class).subscribe(event -> {
            Bundle bundle = event.getArguments();
            if (bundle != null) {
//                mCallPhoneNumber = bundle.getString("phoneNumber");
                mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID);
//                mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
//                mHandler.postDelayed(() -> onCallFinish(123), 5000);
            }
        }));
    }

    class SimplePhoneListener extends PhoneStateListener {
        //获取本次通话的时间(单位:秒)
        private int time = 0;
        //判断是否正在通话
        private boolean isCalling;
        //控制循环是否结束
        boolean isFinish;
        private ExecutorService service;

        SimplePhoneListener() {
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
                        LogCat.e("call", "本次通话" + time + "秒");
                        onCallFinish(time);
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

    /**
     * 通话结束
     *
     * @param second 通话秒数
     */
    private void onCallFinish(int second) {
//        if (!TextUtils.isEmpty(mCallPhoneNumber)) {
        if(!TextUtils.isEmpty(mPersonalId)){
            String talkTime = TimeUtil.getTime(second);
            String dialPersonId = SharedPreferencesUtils.getUserId();
            mRequestBody = ParamHelper.Customer.savePhoneRecord(mPersonalId, "", dialPersonId, "", talkTime, "");
            savePhoneRecord();
//        mCallPhoneNumber = null;
            mPersonalId = null;
            //        mHouseId = null;
        }
//        }
    }

    /*保存通话记录*/
    private void savePhoneRecord() {
        mDisposables.add(MyHttpClient.postByBody(CustomerUrlPath.URL_SAVE_PHONE_RECORD, null, mRequestBody, new RequestCallBack<String>() {
            @Override
            public void onFailed(String failReason) {
                LogCat.e("添加通话记录失败:" + (TextUtils.isEmpty(failReason) ? "" : failReason));
                mHandler.postDelayed(retryRun, 3000);
            }

            @Override
            public void onSuccess(String result) {
                LogCat.e(MyJsonParser.getShowMessage(result));
                mHandler.removeCallbacks(retryRun);
            }
        }));
    }

    private Runnable retryRun = this::savePhoneRecord;

    void deDeath() {
        mDisposables.dispose();
        mHandler.removeCallbacks(retryRun);
        mTelephonyManager.listen(null, PhoneStateListener.LISTEN_NONE);
    }
}
