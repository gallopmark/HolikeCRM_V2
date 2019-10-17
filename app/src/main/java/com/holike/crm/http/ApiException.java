package com.holike.crm.http;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

public class ApiException extends Exception {

    private String message;

    private ApiException(Throwable throwable) {
        super(throwable);
        this.message = throwable.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    static ApiException handleException(Throwable e) {
        ApiException ex = new ApiException(e);
        if (e instanceof HttpException) { //404或405、500等错误码
            ex.message = "请求失败，请稍后再试！";
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex.message = "连接超时，请检查您的网络状态，稍后重试！";
            return ex;
        } else if (e instanceof ConnectException) {
            ex.message = "连接异常，请检查您的网络状态，稍后重试！";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex.message = "连接超时，请检查您的网络状态，稍后重试！";
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex.message = "连接异常，请检查您的网络状态，稍后重试！";
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof JsonSerializer
                || e instanceof NotSerializableException
                || e instanceof ParseException) {
            ex.message = "数据解析错误";
            return ex;
        } else {
            ex.message = "请求失败，请稍后再试！";
            return ex;
        }
    }
}
