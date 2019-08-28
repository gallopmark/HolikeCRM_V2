package com.holike.crm.http;

import android.app.Activity;

import com.holike.crm.R;
import com.holike.crm.activity.main.BootingActivity;
import com.holike.crm.base.MyApplication;
import com.holike.crm.dialog.MaterialDialog;
import com.holike.crm.presenter.fragment.MinePresenter;
import com.holike.crm.util.LogCat;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gallop on 2019/7/29.
 * Copyright holike possess 2019.
 * http请求结果处理
 */
public class CallbackHelper {

   public static Disposable deliveryResult(Observable<String> observable, final RequestCallBack<?> callBack) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> onDeliverySuccess(s, callBack),
                        throwable -> onDeliveryFailure(throwable, callBack),
                        callBack::onFinished,
                        callBack::onStart);
    }

    public static <T> void onDeliverySuccess(String s, final RequestCallBack<T> callBack) {
        LogCat.i_response(s);
        try {
            switch (MyJsonParser.getCode(s)) {
                case 0:
//                    T t = MyJsonParser.parseJson(s, getClass());
                    callBack.onSuccess(MyJsonParser.parseJson(s, callBack.getClass()));
                    break;
                case 9:
                case 210521:
                    final Activity activity = MyApplication.getInstance().getCurrentActivity();
                    if (activity != null && !(activity instanceof BootingActivity)) {
                        new MaterialDialog.Builder(activity)
                                .title(R.string.re_login)
                                .message(R.string.login_expired)
                                .negativeButton(R.string.cancel, null)
                                .positiveButton(R.string.login_now, (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    MinePresenter.logout(activity);
                                }).show();
//                        new SimpleDialog(MyApplication.getInstance().getCurrentActivity()).setDate("重新登录", "您的登录信息已经过期，请重新登录", "取消", "马上登录").setListener(new SimpleDialog.ClickListener() {
//                            @Override
//                            public void left() {
//                            }
//
//                            @Override
//                            public void right() {
//                                MinePresenter.logout(MyApplication.getInstance().getCurrentActivity());
//                            }
//                        }).show();
                        callBack.onFailed(MyJsonParser.getMsg(s));
                    }
                    break;
                case MyJsonParser.DEFAULT_CODE:  //没有Code字段 但是有data字段或result字段
                    callBack.onSuccess(MyJsonParser.parseJson(s, callBack.getClass()));
                    break;
                default:
                    if (MyJsonParser.hasMsg(s)) {
                        callBack.onFailed(MyJsonParser.getMsg(s));
                    } else if (MyJsonParser.hasReason(s)) {
                        callBack.onFailed(MyJsonParser.getReason(s));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogCat.e("RequestCallBack", e);
            callBack.onFailed("数据处理异常");
        }
        callBack.onFinished();
    }

    public static <T> void onDeliveryFailure(Throwable throwable, final RequestCallBack<T> callBack) {
        LogCat.e(throwable);
        callBack.onFailed("网络请求出错");
        callBack.onFinished();
    }

    static Disposable processResult(Observable<String> observable, final RequestCallBack<String> callBack) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    LogCat.i_response(s);
                    callBack.onSuccess(s);
                }, throwable -> onDeliveryFailure(throwable, callBack),
                        callBack::onFinished, callBack::onStart);
    }
}
