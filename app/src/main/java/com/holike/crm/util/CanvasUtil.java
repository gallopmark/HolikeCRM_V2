package com.holike.crm.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * Created by wqj on 2018/7/2.
 * 画布工具类
 */

public class CanvasUtil {

    /**
     * 获取字符串宽度
     *
     * @param text
     * @return
     */
    public static int getTextWidth(String text, Paint pFont) {
        Rect rect = new Rect();
        pFont.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    /**
     * 获取文字高度
     *
     * @param text
     * @return
     */
    public static int getTextHeight(String text, Paint pFont) {
        Rect rect = new Rect();
        pFont.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    //写文字
    public static void textCenter(String string, Canvas canvas, float x, float y, int textWidth, int textSize, int textColor) {
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.dp2px(textSize));
        StaticLayout staticLayout = new StaticLayout(string, textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1f, 0, false);
        canvas.save();
        canvas.translate(x, y);
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
