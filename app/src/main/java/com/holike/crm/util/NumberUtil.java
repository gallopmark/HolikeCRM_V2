package com.holike.crm.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 金额格式化
 */
public class NumberUtil {
    public static String format(String amount) throws NumberFormatException {
        if (amount == null || amount.equals("")) return "";
        DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00");
        return fmt.format(Double.parseDouble(amount));
    }

    @Deprecated
    public static String formatDouble(double value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

    public static double doubleValue(String source) {
        return doubleValue(source, 2);
    }

    public static double doubleValue(String source, int newScale) {
        try {
            return new BigDecimal(source).setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            return 0.00;
        }
    }

    public static double doubleValue(double source) {
        return doubleValue(source, 2);
    }

    public static double doubleValue(double source, int newScale) {
        return new BigDecimal(Double.toString(source)).setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String decimals(CharSequence source) {
        if (TextUtils.isEmpty(source)) return "0.00";
        double value = ParseUtils.parseDouble(source.toString());
        return decimals(value);
    }

    /*double类型数据 保留两位小数*/
    public static String decimals(double value) {
        if (value > 0) {
            try {
                DecimalFormat df = new DecimalFormat("##,###,###,###,###,###,###,###,###,##0.00");
                return df.format(value);
            } catch (Exception e) {
                return "0.00";
            }
        }
        return "0.00";
    }
}
