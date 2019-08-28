package com.holike.crm.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxLoopTask {
    private Disposable mDisposable;

    @SuppressLint("CheckResult")
    public void doLoopTask(int loopMS, final TaskRunnable taskRunnable) {
        mDisposable = Observable.interval(loopMS, TimeUnit.MILLISECONDS).
                subscribeOn(Schedulers.io()).
                subscribe(num -> new Handler(Looper.getMainLooper()).post(taskRunnable::loopTaskRun));
    }

    public void dispose() {
        mDisposable.dispose();
    }

    public interface TaskRunnable {
        void loopTaskRun();
    }
}
