package com.holike.crm.base;


import androidx.annotation.NonNull;

public interface OnRequestPermissionsCallback {
    void onGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit);
}
