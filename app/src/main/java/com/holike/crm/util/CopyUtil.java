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
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", content);
        myClipboard.setPrimaryClip(myClip);
    }
}
