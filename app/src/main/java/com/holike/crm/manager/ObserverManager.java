package com.holike.crm.manager;

import com.holike.crm.util.LogCat;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ObserverManager {

    private CompositeDisposable compositeDisposable;

    public ObserverManager() {
        compositeDisposable = new CompositeDisposable();
    }

    public static ObserverManager getInstance() {
        return ObserverManagerHolder.instance;
    }

    static class ObserverManagerHolder {
        static ObserverManager instance = new ObserverManager();
    }

    public void doTask(final CallBack callBack) {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
            }

            @Override
            public void onNext(String data) {
            }

            @Override
            public void onError(Throwable t) {
                LogCat.e(t);
                if (callBack != null)
                    callBack.onError(t);
            }

            @Override
            public void onComplete() {
                callBack.onComplete();
            }
        };
        Flowable.create((FlowableOnSubscribe<String>) e -> {
            if (callBack != null)
                callBack.task();
            e.onComplete();
        }, BackpressureStrategy.BUFFER).subscribe(subscriber);
    }

    public Disposable doTaskJust(List<List<String>> lists, final ListCallBack callBack) {
        return Observable.just(lists).subscribeOn(Schedulers.io()).map(lists1 -> {
            List<List<String>> results = new ArrayList<>();
            callBack.task(results);
            return results;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack::onComplete, Throwable::printStackTrace);
    }

    public interface CallBack {
        void task();


        void onComplete();


        void onError(Throwable t);
    }

    public interface ListCallBack {

        void task(List<List<String>> results);

        void onComplete(List<List<String>> list);

    }

    public void dispose() {
        compositeDisposable.dispose();
    }
}
