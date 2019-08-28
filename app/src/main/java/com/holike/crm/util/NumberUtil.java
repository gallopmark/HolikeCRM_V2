package com.holike.crm.util;

import java.text.DecimalFormat;

/**
 * 金额格式化
 */
public class NumberUtil {
    public static String format(String amount) throws NumberFormatException {
        if (amount == null || amount.equals("")) return "";
        DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00");
        return fmt.format(Double.parseDouble(amount));
    }

}
