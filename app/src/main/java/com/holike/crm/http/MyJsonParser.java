package com.holike.crm.http;

import androidx.annotation.NonNull;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.$Gson$Types;
import com.holike.crm.R;
import com.holike.crm.base.MyApplication;
import com.holike.crm.util.LogCat;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyJsonParser {
    static final int DEFAULT_CODE = -1234567890;

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @NonNull
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @NonNull
        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    public static boolean isEmpty(String json) {
        return TextUtils.isEmpty(json);
    }

    @NonNull
    public static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) superclass;
            if (parameterized.getActualTypeArguments().length == 0)
                return Object.class;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        } else {
            return Object.class;
        }
    }

    @NonNull
    public static JsonObject getAsJsonObject(String json) {
        try {
            return new JsonParser().parse(json).getAsJsonObject();
        } catch (Exception e) {
            return new JsonObject();
        }
    }

    public static boolean has(String json, String key) {
        if (isEmpty(json)) return false;
        try {
            JsonObject jsonObject = getAsJsonObject(json);
            return jsonObject.has(key);
        } catch (Exception e) {
            LogCat.e(e);
            return false;
        }
    }

    public static boolean hasCode(String json) {
        return has(json, "code");
    }

    /*接口返回结果 code字段 int类型*/
    public static int getCode(String json) {
        if (!hasCode(json)) return DEFAULT_CODE;
        try {
            return getAsJsonObject(json).get("code").getAsInt();
        } catch (Exception e) {
            LogCat.e(e);
            return DEFAULT_CODE;
        }
    }

    public static boolean hasMsg(String json) {
        return has(json, "msg");
    }

    private static JsonElement getMsgElement(String json) {
        return getAsJsonObject(json).get("msg");
    }

    /*接口返回结果 msg字段 String类型*/
    public static String getMsg(String json) {
        if (!hasMsg(json)) return "";
        try {
            JsonElement element = getMsgElement(json);
            String message = element.isJsonNull() ? "" : element.getAsString();
            if (TextUtils.equals(message, "NOT_AUTHORIED") || TextUtils.equals(message, MyApplication.getInstance().getString(R.string.noAuthority))) {
                return MyApplication.getInstance().getString(R.string.tips_nopermissions);
            }
            return message;
//            return getAsJsonObject(json).getInstance("msg").getAsString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    public static boolean hasResult(String json) {
        return has(json, "result");
    }

    private static JsonElement getResultElement(String json) {
        return getAsJsonObject(json).get("result");
    }

    /*接口返回结果 result字段*/
    public static String getResultAsString(String json) {
        if (!hasResult(json)) return "";
        try {
            JsonElement element = getResultElement(json);
            return element.isJsonNull() ? "" : element.getAsString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    /*接口返回结果 result字段*/
    public static String getResult(String json) {
        if (!hasResult(json)) return "";
        try {
            JsonElement element = getResultElement(json);
            return element.isJsonNull() ? "" : element.toString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean hasAtt(String json) {
        return has(json, "att");
    }

    private static JsonElement getAttElement(String json) {
        return getAsJsonObject(json).get("att");
    }

    public static String getAtt(String json) {
        if (!hasAtt(json)) return "";
        try {
            JsonElement element = getAttElement(json);
            return element.isJsonNull() ? "" : element.toString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    public static String getShowMessage(String json) {
        if (!TextUtils.isEmpty(getMsg(json))) return getMsg(json);
        if (!TextUtils.isEmpty(getReason(json))) return getReason(json);
        return "";
    }

    public static boolean hasData(String json) {
        return has(json, "data");
    }

    private static JsonElement getDateElement(String json) {
        return getAsJsonObject(json).get("data");
    }

    /*接口返回结果 data字段*/
    public static String getData(String json) {
        if (!hasData(json)) return "";
        try {
            JsonElement element = getDateElement(json);
            return element.isJsonNull() ? "" : element.toString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    public static String getDataAsString(String json) {
        if (!hasData(json)) return "";
        try {
            JsonElement element = getDateElement(json);
            return element.isJsonNull() ? "" : element.getAsString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    /*json result字段解析成实体类*/
    public static <T> T fromResult(String json, Class<T> clazz) {
        if (isEmpty(json) || !hasResult(json)) return null;
        try {
            return new Gson().fromJson(getResult(json), clazz);
        } catch (Exception e) {
            LogCat.e(e);
            return null;
        }
    }

    public static <T> T fromResultToClass(String json, Class<?> clazz) {
        if (!hasResult(json)) return null;
        try {
            return new Gson().fromJson(getResult(json), getSuperclassTypeParameter(clazz));
        } catch (Exception e) {
            LogCat.e(e);
            return null;
        }
    }

    /*json result字段解析成实体类*/
    public static <T> T fromData(String json, Class<T> clazz) {
        if (isEmpty(json) || !hasData(json)) return null;
        try {
            return new Gson().fromJson(getData(json), clazz);
        } catch (Exception e) {
            LogCat.e(e);
            return null;
        }
    }

    /*json字符串转成List集合*/
    public static <T> List<T> fromDataToList(String json, Class<T> clazz) {
        if (isEmpty(json) || !hasData(json)) return new ArrayList<>();
        try {
            Type type = new ParameterizedTypeImpl(clazz);
            return new Gson().fromJson(getData(json), type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /*json result字段解析成实体类*/
    public static <T> T fromDataToClass(String json, Class<?> clazz) {
        if (!hasData(json)) return null;
        try {
            return new Gson().fromJson(getData(json), getSuperclassTypeParameter(clazz));
        } catch (Exception e) {
            LogCat.e(e);
            return null;
        }
    }

    public static boolean hasReason(String json) {
        return has(json, "reason");
    }

    private static JsonElement getReasonElement(String json) {
        return getAsJsonObject(json).get("reason");
    }

    public static String getReason(String json) {
        if (!hasReason(json)) return "";
        try {
            JsonElement element = getReasonElement(json);
            String message = element.isJsonNull() ? "" : element.getAsString();
            if (TextUtils.equals(message, "NOT_AUTHORIED") || TextUtils.equals(message, MyApplication.getInstance().getString(R.string.noAuthority))) {
                return MyApplication.getInstance().getString(R.string.tips_nopermissions);
            }
            return message;
//            return getAsJsonObject(json).getInstance("reason").getAsString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    public static <T> T fromJson(String json, Class<T> subclass) {
        try {
            return new Gson().fromJson(json, subclass);
        } catch (Exception e) {
            return null;
        }
    }

    //将后台返回的result字段或者data字段转成list
    public static <T> List<T> parseJsonToList(String json, Class<T> cls) {
        if (TextUtils.isEmpty(json)) return new ArrayList<>();
        if (!TextUtils.isEmpty(getResult(json))) return fromJsonToList(getResult(json), cls);
        if (!TextUtils.isEmpty(getData(json))) return fromJsonToList(getData(json), cls);
        return new ArrayList<>();
    }

    /*解析后台数据成对应的类型*/
    @SuppressWarnings("unchecked")
    public static <T> T parseJson(String json, Class<?> subclass) {
        Type type = getSuperclassTypeParameter(subclass);
        if (type == String.class) {  //如果传入的是string则直接返回后台返回的完整的字符串
            return (T) json;
        } else {
            T t;
            if (hasResult(json)) {    //有result字段则解析result
                t = fromResultToClass(json, subclass);
            } else if (hasData(json)) {
                t = fromDataToClass(json, subclass);
            } else if (hasMsg(json)) {
                t = (T) getMsg(json);
            } else if (hasReason(json)) {
                t = (T) getReason(json);
            } else {
                t = (T) json;
            }
            return t;
        }
    }

    /*json字符串转实体类*/
    public static <T> T fromJsonToBean(String json, Class<T> clazz) {
        if (isEmpty(json)) return null;
        try {
            return new Gson().fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /*实体类转json字符串*/
    public static String fromBeanToJson(Object object) {
        if (object == null) return "{}";
        try {
            return new Gson().toJson(object);
        } catch (Exception e) {
            return "{}";
        }
    }

    /*json字符串转成List集合*/
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) return new ArrayList<>();
        try {
            Type type = new ParameterizedTypeImpl(clazz);
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /*获取json字符串中的某一字段*/
    public static String getAsString(String json, String memberName) {
        if (!has(json, memberName)) return "";
        try {
            return getAsJsonObject(json).get(memberName).getAsString();
        } catch (Exception e) {
            LogCat.e(e);
            return "";
        }
    }

    public static int getAsInt(String json, String memberName) {
        if (!has(json, memberName)) return -1;
        try {
            return getAsJsonObject(json).get(memberName).getAsInt();
        } catch (Exception e) {
            LogCat.e(e);
            return -1;
        }
    }

    public static long getAsLong(String json, String memberName) {
        if (!has(json, memberName)) return -1;
        try {
            return getAsJsonObject(json).get(memberName).getAsLong();
        } catch (Exception e) {
            LogCat.e(e);
            return -1;
        }
    }

    public static <T> List<T> getAsList(String json, String memberName, Class<T> clazz) {
        if (!has(json, memberName)) return new ArrayList<>();
        try {
            return fromJsonToList(getAsJsonObject(json).get(memberName).toString(), clazz);
        } catch (Exception e) {
            LogCat.e(e);
            return new ArrayList<>();
        }
    }

    public static String getMsgAsString(String json) {
        if (!has(json, "msg")) return "";
        try {
            JsonElement element = getMsgElement(json);
            return element.isJsonNull() ? "" : element.getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMoreInfoAsString(String json) {
        if (!has(json, "moreInfo")) return "";
        try {
            JsonElement element = getAsJsonObject(json).get("moreInfo");
            return element.isJsonNull() ? "" : element.getAsString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getErrorInfoAsString(String json) {
        if (!has(json, "errorInfo")) return "";
        try {
            JsonElement element = getAsJsonObject(json).get("errorInfo");
            return element.isJsonNull() ? "" : element.getAsString();
        } catch (Exception e) {
            return "";
        }
    }
}
