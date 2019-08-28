package com.holike.crm.util;

import java.security.MessageDigest;

/**
 * Created by wqj on 2017/9/28.
 * MD5工具类
 */

public class MD5Util {

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * url转换成md5文件名
     *
     * @param url
     * @return
     */
    public static String urlToMD5(String url) {
        return MD5(url) + StringUtil.getFileType(url);
    }

    /**
     * url转换成md5缓存路径下的文件名
     *
     * @param url
     * @return
     */
    public static String urlToCachePath(String url) {
        return IOUtil.getCachePath() + "/" + MD5(url) + StringUtil.getFileType(url);
    }
}
