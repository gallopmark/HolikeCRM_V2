package com.holike.crm.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.holike.crm.base.MyApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;

/**
 * Created by wqj on 2017/11/13.
 * 文件操作帮助类
 */

public class IOUtil {


    /**
     * 创建文件夹
     *
     * @param name
     * @return
     */
    public static String getPath(String name) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = MyApplication.getInstance().getFilesDir();
            if (!file.exists()) {
                file.mkdir();
            }
            return file.getPath();
        } else {
            return null;
        }
    }

    /**
     * 获取应用的缓存路径
     *
     * @return
     */
    public static String getCachePath() {
        File cache = getExternalCacheDirectory(MyApplication.getInstance());
        if (cache.exists()) {
            return cache.getPath();
        } else {
            return null;
        }
    }

    /**
     * 获取SD卡缓存目录
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDirectory(Context context) {
        File appCacheDir = context.getExternalCacheDir();
        if (appCacheDir == null) {// 有些手机需要通过自定义目录
            appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/cache");
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            LogCat.i("getExternalDirectory", "getExternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }

    public static File getInternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        } else {
            appCacheDir = new File(context.getFilesDir(), type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            Log.e("getInternalDirectory", "getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }

    /**
     * 判断文件/夹是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存文件
     *
     * @param inPutStream
     * @param path
     * @param name
     */
    public static void saveFile(InputStream inPutStream, String path, String name) {
        try {
            File file = new File(path, "temp");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(inPutStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            fos.close();
            bis.close();
            inPutStream.close();
            file.renameTo(new File(path, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(byte[] data, String path, String name) {
        try {
            File file = new File(path, name);
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(data);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
//        if(kiloByte < 1) {
//            return size + "B";
//        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 保存一个对象到文件中
     *
     * @param object
     * @param path
     */
    public static void saveObject(Object object, String path) {
        if (null != object && null != path) {
            ObjectOutputStream oos = null;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(path));
                oos = new ObjectOutputStream(fos);
                oos.writeObject(object);
                oos.flush();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (oos != null) {
                    try {
                        oos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 从文件中读取对象
     *
     * @param path
     * @return
     */
    public static Object readObject(String path) {
        Object obj = null;
        if (null != path && path.length() > 0) {
            ObjectInputStream ois = null;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(new File(path));
                ois = new ObjectInputStream(fis);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }

            try {
                obj = ois.readObject();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return obj;
        }
        return obj;
    }
}
