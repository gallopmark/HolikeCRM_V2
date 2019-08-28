package com.holike.crm.popupwindown;

import android.graphics.Rect;
import android.os.Build;
import androidx.core.widget.PopupWindowCompat;

import android.view.View;
import android.widget.PopupWindow;


public class PopupWindowUtils {

    public static void showPopupWindow(PopupWindow popupWindow, View parent, int xoff, int yoff,
                                       int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect visibleFrame = new Rect();
            parent.getGlobalVisibleRect(visibleFrame);
            int height = parent.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            popupWindow.setHeight(height);
            popupWindow.showAsDropDown(parent, xoff, yoff, gravity);
        } else {
            PopupWindowCompat.showAsDropDown(popupWindow, parent, 0, 0,gravity);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            int[] location = new int[2];
//            parent.getLocationInWindow(location);
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) { // 7.1 版本处理
//                int screenHeight = parent.getResources().getDisplayMetrics().heightPixels;
//                popupWindow.setHeight(screenHeight - location[1] - parent.getHeight());
//            }
//            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1] + parent.getHeight());
//        } else
//            PopupWindowCompat.showAsDropDown(popupWindow, parent, 0, 0, Gravity.TOP);
    }
}
