package com.holike.crm.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * Created by gallop on 2019/7/30.
 * Copyright holike possess 2019.
 */
public class TextSpanHelper {
    private Context mContext;
    public static final int DEFAULT_FLAGS = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;

    private TextSpanHelper(Context context) {
        this.mContext = context;
    }

    public static TextSpanHelper from(Context context) {
        return new TextSpanHelper(context);
    }

    private String wrap(@StringRes int resId) {
        return resId == 0 ? "" : mContext.getString(resId);
    }

    private String wrap(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    public SpannableString obtain(@StringRes int resId, String content, Object... spans) {
        return obtain(wrap(resId), content, spans);
    }

    /*从origin字符串长度开始设置Span*/
    public SpannableString obtain(String origin, String content, Object... spans) {
        String source = wrap(origin);
        int start = source.length();
        source += TextUtils.isEmpty(content) ? "" : content;
        int end = source.length();
        SpannableString ss = new SpannableString(source);
        if (spans != null) {
            for (Object span : spans) {
                ss.setSpan(span, start, end, DEFAULT_FLAGS);
            }
        }
        return ss;
    }

    public SpannableString obtainGeneral(@StringRes int resId, String content, int start, int end, Object... spans) {
        return obtainGeneral(wrap(resId), content, start, end, spans);
    }

    /*自定义开始下标和结束下标*/
    public SpannableString obtainGeneral(String origin, String content, int start, int end, Object... spans) {
        String source = wrap(origin) + (TextUtils.isEmpty(content) ? "" : content);
        SpannableString ss = new SpannableString(source);
        if (spans != null) {
            for (Object span : spans) {
                ss.setSpan(span, start, end, DEFAULT_FLAGS);
            }
        }
        return ss;
    }

    public SpannableString obtainColorStyle(@StringRes int resId, String content, @ColorRes int colorId) {
        return obtainColorStyle(wrap(resId), content, colorId);
    }

    /*从origin字符串长度开始设置ColorSpan*/
    public SpannableString obtainColorStyle(String origin, String content, @ColorRes int colorId) {
        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(mContext, colorId));
        return obtain(origin, content, span);
    }

    public SpannableString obtainColorBoldStyle(@StringRes int resId, String content, @ColorRes int colorId) {
        return obtainColorBoldStyle(wrap(resId), content, colorId);
    }

    /*从origin字符串长度开始设置ColorSpan、StyleSpan，达到不同文字颜色和文字加粗的效果*/
    public SpannableString obtainColorBoldStyle(String origin, String content, @ColorRes int colorId) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, colorId));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        return obtain(origin, content, colorSpan, styleSpan);
    }

    /*纯颜色文字，下标从0开始到内容总长度*/
    public SpannableString generalStyle(@StringRes int resId, String content, @ColorRes int colorId) {
        return generalStyle(wrap(resId), content, colorId);
    }

    public SpannableString generalStyle(String origin, String content, @ColorRes int colorId) {
        int end = (wrap(origin) + wrap(content)).length();
        return obtainGeneral(origin, content, 0, end, new ForegroundColorSpan(ContextCompat.getColor(mContext, colorId)));
    }

    /*纯颜色加粗文字，下标从0开始到内容总长度*/
    public SpannableString generalBoldStyle(String origin, String content, @ColorRes int colorId) {
        int end = (wrap(origin) + wrap(content)).length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, colorId));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        return obtainGeneral(origin, content, 0, end, colorSpan, styleSpan);
    }

    public SpannableString generalTypefaceStyle(String origin, String content, @ColorRes int colorId, int style) {
        int end = (wrap(origin) + wrap(content)).length();
        return obtainGeneral(origin, content, 0, end, getColorSpan(mContext, colorId), getStyleSpan(style));
    }

    public static ForegroundColorSpan getColorSpan(@NonNull Context context, @ColorRes int colorId) {
        return new ForegroundColorSpan(ContextCompat.getColor(context, colorId));
    }

    public static StyleSpan getStyleSpan(int style) {
        return new StyleSpan(style);
    }

    public static StyleSpan getBoldStyleSpan() {
        return getStyleSpan(Typeface.BOLD);
    }

    public static SpannableString getSquareMeter() {
        SpannableString m2 = new SpannableString("m2");
        m2.setSpan(new RelativeSizeSpan(0.8f), 1, 2, DEFAULT_FLAGS);
        m2.setSpan(new SuperscriptSpan(), 1, 2, DEFAULT_FLAGS);
        return m2;
    }

    public static SpannableString getSquareMeter(String source) {
        SpannableString m2 = new SpannableString(source);
        int start = source.indexOf("2");
        int end = source.indexOf("2") + 1;
        m2.setSpan(new RelativeSizeSpan(0.8f), start, end, DEFAULT_FLAGS);
        m2.setSpan(new SuperscriptSpan(), start, end, DEFAULT_FLAGS);
        return m2;
    }

}