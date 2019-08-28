package com.holike.crm.customView;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.holike.crm.R;

import java.lang.reflect.Field;

/**
 * 自定义Toast 兼容各个系统
 */
public class CompatToast extends Toast {
    public static class Gravity {
        public static final int LOW = 0;
        public static final int CENTER = 1;
        public static final int HIGH = 2;

        private Gravity() {
        }
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    public CompatToast(Context context) {
        super(context);
        Class<Toast> clazz = Toast.class;
        try {
            Field mTN = clazz.getDeclaredField("mTN");
            mTN.setAccessible(true);
            Object mObj = mTN.get(this);
            // 取消掉各个系统的默认toast弹出动画
            Field field = mObj.getClass().getDeclaredField("mParams");
            field.setAccessible(true);
            Object mParams = field.get(mObj);
            if (mParams == null) return;
            if (mParams instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                params.windowAnimations = R.style.CompatToast;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
