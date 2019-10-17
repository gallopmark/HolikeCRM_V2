package com.holike.crm.activity.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.OnRequestPermissionsCallback;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.enumeration.CustomerValue;
import com.holike.crm.http.CustomerUrlPath;
import com.holike.crm.http.MyHttpClient;
import com.holike.crm.http.MyJsonParser;
import com.holike.crm.http.ParamHelper;
import com.holike.crm.http.RequestCallBack;
import com.holike.crm.rxbus.MessageEvent;
import com.holike.crm.rxbus.RxBus;
import com.holike.crm.util.AppUtils;
import com.holike.crm.util.LogCat;
import com.holike.crm.util.SharedPreferencesUtils;
import com.holike.crm.util.TimeUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by gallop on 2019/8/26.
 * Copyright holike possess 2019.
 */
class MainHelper implements OnRequestPermissionsCallback {

    private BaseActivity<?, ?> mActivity;
    /*监听通话过程*/
    private TelephonyManager mTelephonyManager;
    private CompositeDisposable mDisposables;
    //    private String mCallPhoneNumber, mPersonalId, mHouseId;
    private String mPersonalId, mHouseId;
    private String mRequestBody;
    private Handler mHandler;
    private SimplePhoneListener mPhoneStateListener;

    //    private long mFirstCallTime;//点击拨号时候的时间
    private boolean mCalled = false;//是否拨打过电话

    private boolean mCallLogPermissionGranted; //读取通话记录权限是否已被注册
    private boolean mCallFinished; //是否是通话结束

    MainHelper(BaseActivity<?, ?> activity, Handler handler) {
        this.mActivity = activity;
        this.mHandler = handler;
        mDisposables = new CompositeDisposable();
        telephonyListen(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCallLogPermissionGranted = mActivity.isPermissionGranted(Manifest.permission.READ_CALL_LOG);
        } else {
            mCallLogPermissionGranted = true;
        }
    }

    private void requestCallLogPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mActivity.requestPermission(Manifest.permission.READ_CALL_LOG, this);
    }

    /*监听通话过程*/
    private void telephonyListen(Context context) {
        mTelephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new SimplePhoneListener();
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mDisposables.add(RxBus.getInstance().toObservable(MessageEvent.class).subscribe(event -> {
            Bundle bundle = event.getArguments();
            if (bundle != null) {
//                mCallPhoneNumber = bundle.getString("phoneNumber");
                mPersonalId = bundle.getString(CustomerValue.PERSONAL_ID);
                mHouseId = bundle.getString(CustomerValue.HOUSE_ID);
//                mHandler.postDelayed(() -> onCallFinish(123), 5000);
            }
        }));
    }

    @Override
    public void onGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }
        mCallLogPermissionGranted = isAllGranted;
        actionCallLog();
    }

    @Override
    public void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit) {
        mCallLogPermissionGranted = false;
        whenCallLogPermissionDenied(isProhibit);
    }

    private void whenCallLogPermissionDenied(final boolean isProhibit) {
        if (mCallFinished) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity);
            builder.title(R.string.dialog_title_default);
            if (isProhibit) {
                builder.message(R.string.tips_dismiss_callLogs_prohibit);
            } else {
                builder.message(R.string.tips_dismiss_callLogs);
            }
            builder.negativeButton(R.string.cancel, null)
                    .positiveButton(R.string.confirm, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        if (isProhibit) {
                            AppUtils.openSettings(mActivity);
                        } else {
                            requestCallLogPermission();
                        }
                    }).show();
        } else {
            mActivity.showShortToast(mActivity.getString(R.string.tips_callLog_permission_denied));
        }
    }

    private class SimplePhoneListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲
                    LogCat.e("电话处于休闲状态中...");
                    if (mCalled) {
                        //通话时长
                        mCallFinished = true;
                        mHandler.postDelayed(MainHelper.this::onCallFinish, 1000);
                        mCalled = false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    LogCat.e("电话处于响铃中...");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    LogCat.e("电话处于通话中...");
                    mCalled = true;
                    break;
                default:
                    break;
            }
        }
    }

    void onRestart() {
        mCallLogPermissionGranted = mActivity.isPermissionGranted(Manifest.permission.READ_CALL_LOG);
        actionCallLog();
    }

    /*获取最新的一条通话记录*/
    private void onCallFinish() {
        if (!mCallLogPermissionGranted) {
            requestCallLogPermission();
        } else {
            actionCallLog();
        }
    }

    private void actionCallLog() {
        if (mCallLogPermissionGranted && mCallFinished) {
            queryLatestCallLog();
            mCallFinished = false;
        }
    }

    /*查找最新一条通话记录*/
    @SuppressLint("MissingPermission")
    private void queryLatestCallLog() {
        Cursor cursor = null;
        try {
            Uri uri = CallLog.Calls.CONTENT_URI;
            ContentResolver resolver = mActivity.getContentResolver();
            cursor = resolver.query(uri, new String[]{CallLog.Calls.CACHED_NAME // 通话记录的联系人
                            , CallLog.Calls.NUMBER // 通话记录的电话号码
                            , CallLog.Calls.DATE// 通话记录的日期
                            , CallLog.Calls.DURATION// 通话时长
                            , CallLog.Calls.TYPE} // 通话类型}}
                    , null, null, CallLog.Calls.DEFAULT_SORT_ORDER + " LIMIT 0,1");
            if (cursor != null && cursor.moveToNext()) {
//                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
//                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
//                    long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)); // 通话时长
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //通话类型
                if ((type == CallLog.Calls.INCOMING_TYPE || type == CallLog.Calls.OUTGOING_TYPE) && duration > 0) {
                    LogCat.e("duration", String.valueOf(duration));  //单位：秒
                    onCallFinish(duration);
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 通话结束
     *
     * @param second 通话秒数
     */
    private void onCallFinish(int second) {
        if (!TextUtils.isEmpty(mPersonalId) && !TextUtils.isEmpty(mHouseId)) {
            String talkTime = TimeUtil.getTime(second);
            String dialPersonId = SharedPreferencesUtils.getUserId();
            mRequestBody = ParamHelper.Customer.savePhoneRecord(mPersonalId, mHouseId, dialPersonId, "", talkTime, "");
            savePhoneRecord();
//        mCallPhoneNumber = null;
            mPersonalId = null;
            //        mHouseId = null;
        }
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
        if (mTelephonyManager != null && mPhoneStateListener != null) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
            mPhoneStateListener = null;
            mTelephonyManager = null;
        }
    }
}
