package com.holike.crm.util;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartUtils {

    public static MultipartBody pathsToMultipartBody(String fileKey, List<String> filePaths) {
        List<File> files = new ArrayList<>();
        for (String filePath : filePaths) {
            files.add(new File(filePath));
        }
        return filesToMultipartBody(fileKey, files);
    }

    public static MultipartBody filesToMultipartBody(String fileKey, List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        int i = 0;
        for (File file : files) {
            try {
                RequestBody requestBody = RequestBody.create(MediaType.parse(FileTypeUtils.getMimeType(file)), file);
                String name = TextUtils.isEmpty(fileKey) ? "file" + i : fileKey;
                builder.addFormDataPart(name, file.getName(), requestBody);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    public static List<MultipartBody.Part> pathsToMultipartBodyParts(String fileKey, List<String> paths) {
        List<File> files = new ArrayList<>();
        for (String filePath : paths) {
            files.add(new File(filePath));
        }
        return filesToMultipartBodyParts(fileKey, files);
    }

    public static List<MultipartBody.Part> pathsToMultipartBodyPartsByMD5(String fileKey, List<String> paths) {
        List<MultipartBody.Part> parts = new ArrayList<>();
//        int i = 0;
        for (String filePath : paths) {
            try {
                File file = new File(filePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse(FileTypeUtils.getMimeType(file)), file);
                final String key = MD5Util.MD5(String.valueOf(System.currentTimeMillis()) + UUID.randomUUID().toString()) + ".jpg";
//                String name = TextUtils.isEmpty(fileKey) ? "file" + i : fileKey + i;
                MultipartBody.Part part = MultipartBody.Part.createFormData(fileKey, key, requestBody);
                parts.add(part);
//                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parts;
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(String fileKey, List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        int i = 0;
        for (File file : files) {
            try {
                RequestBody requestBody = RequestBody.create(MediaType.parse(FileTypeUtils.getMimeType(file)), file);
                String name = TextUtils.isEmpty(fileKey) ? "file" + i : fileKey + i;
                MultipartBody.Part part = MultipartBody.Part.createFormData(name, file.getName(), requestBody);
                parts.add(part);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parts;
    }
}
