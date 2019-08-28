package com.holike.crm.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;

import com.holike.crm.util.GenericsUtils;

import java.util.Random;

/**
 * Created by wqj on 2017/10/17.
 * 所有presenter的抽象类
 */

public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {
    public static int REQUEST_CODE = new Random().nextInt(65536);
    public final static String DEATTACH_EXCEPTION = "view not attached";
    private V view;
    protected M model;

    @SuppressWarnings("unchecked")
    public void attach(@NonNull V t) {
        view = t;
        try {
            model = ((Class<M>) GenericsUtils.getSuperClassGenricType(getClass(), 1)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public M getModel() {
        return model;
    }

    public void deAttach() {
        if (model != null) {
            model.dispose();
        }
        view = null;
        onDestroy();
    }

    protected void onDestroy() {
    }

    public String isEmpty(String s) {
        return TextUtils.isEmpty(s) ? "-" : s;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    @Nullable
    public V getView() {
        return view;
//        if (view == null) {
//            throw new RuntimeException(DEATTACH_EXCEPTION);
//        } else {
//            return view;
//        }
    }

}
