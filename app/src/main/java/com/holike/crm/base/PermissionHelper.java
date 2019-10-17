package com.holike.crm.base;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


/*权限回调处理*/
final class PermissionHelper {

    static void convert(Object target, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,
                        @Nullable OnRequestPermissionsCallback requestCallback) {
        boolean allGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        if (allGranted) {
            if (requestCallback != null) {
                requestCallback.onGranted(requestCode, permissions, grantResults);
            }
        } else {
            Fragment fragment = null;
            Activity activity = null;
            if (target instanceof Fragment) {
                fragment = (Fragment) target;
            } else if (target instanceof Activity) {
                activity = (Activity) target;
            }
            if (fragment == null && activity == null) {
                if (requestCallback != null)
                    requestCallback.onDenied(requestCode, permissions, false);
            } else {
                boolean isProhibit = false;
                if (fragment != null) {
                    for (String permission : permissions) {
                        //可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                        if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                            isProhibit = true;
                            break;
                        }
                    }
                } else {
                    for (String permission : permissions) {
                        //可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                            isProhibit = true;
                            break;
                        }
                    }
                }
                if (requestCallback != null) {
                    requestCallback.onDenied(requestCode, permissions, isProhibit);
                }
            }
        }
    }
}
