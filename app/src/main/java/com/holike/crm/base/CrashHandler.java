package com.holike.crm.base;


import com.holike.crm.util.LogCat;

/**
 * Created by wqj on 2017/12/19.
 * 自定义Carsh防止解除presenter和view的绑定发生崩溃
 */
@Deprecated
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private static CrashHandler mCrashHandler = new CrashHandler();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return mCrashHandler;
    }

    public void init() {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex.getCause() != null && ex.getCause().toString().contains(BasePresenter.DEATTACH_EXCEPTION)) {//防止解除presenter和view的绑定发生崩溃
            LogCat.i(TAG, ex.toString());
        } else if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        }
    }
}
