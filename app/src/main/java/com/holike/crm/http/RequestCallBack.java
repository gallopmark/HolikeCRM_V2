package com.holike.crm.http;

import io.reactivex.disposables.Disposable;

public abstract class RequestCallBack<T> {
//    private Disposable disposable;
//
//    @Override
//    public void onSubscribe(@NonNull Disposable d) {
//        disposable = d;
//        onStart(d);
//    }
//
//    public void cancel() {
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//    }
//
//    @Override
//    public void onComplete() {
//        HttpClient.getInstance().remove(this);
//        onFinished();
//    }
//
//    @Override
//    public void onError(@NonNull Throwable e) {
//        LogCat.e(e);
//        onFailed("网络请求出错");
//        HttpClient.getInstance().remove(this);
//        onFinished();
//    }
//
//    @Override
//    public void onNext(@NonNull String s) {
//        LogCat.i_response(s);
//        try {
//            switch (MyJsonParser.getCode(s)) {
//                case 0:
////                    T t = MyJsonParser.parseJson(s, getClass());
//                    onSuccess(MyJsonParser.parseJson(s, getClass()));
//                    break;
//                case 9:
//                case 210521:
//                    Activity activity = MyApplication.getInstance().getCurrentActivity();
//                    if (activity != null && !(activity instanceof BootingActivity)) {
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
//                        onFailed(MyJsonParser.getMsg(s));
//                    }
//                    break;
//                case -1234456789:
//                    onSuccess(MyJsonParser.fromJson(s, getClass()));
//                    break;
//                default:
//                    if (MyJsonParser.hasMsg(s)) {
//                        onFailed(MyJsonParser.getMsg(s));
//                    } else if (MyJsonParser.hasReason(s)) {
//                        onFailed(MyJsonParser.getReason(s));
//                    }
//                    break;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogCat.e("RequestCallBack", e);
//            onFailed("数据处理异常");
//        }
//        onFinished();
//    }

    public void onStart(Disposable d) {
    }

    public abstract void onFailed(String failReason);

    public abstract void onSuccess(T result);

    public void onFinished(){}
}
