package com.holike.crm.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 复制工具类
 */
public class CopyUtil {
    public static void copy(Context context, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("text", content);
            clipboardManager.setPrimaryClip(clipData);
        }
    }
}
