package com.holike.crm.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by gallop on 2019/7/22.
 * Copyright holike possess 2019.
 */
public class AppUtils {

    /*打开app设置页面*/
    public static void openSettings(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception ignored) {
        }
    }
}
