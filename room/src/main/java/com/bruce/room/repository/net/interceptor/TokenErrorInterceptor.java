package com.bruce.room.repository.net.interceptor;

import android.annotation.SuppressLint;

import com.bruce.room.repository.bean.ResponseNoData;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import timber.log.Timber;

/**
 * token异常处理
 */
public class TokenErrorInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
//        Timber.d("拦截器 url: " + request.url());
        Response response = chain.proceed(request);
        isTokenFail(response);
        return response;
    }

    @SuppressLint("BinaryOperationInTimber")
    private boolean isTokenFail(Response response) {
        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.getBuffer();
            String json = buffer.clone().readString(StandardCharsets.UTF_8);
//            Timber.d("拦截器 json " + json);
            ResponseNoData responseNoData = new Gson().fromJson(json, ResponseNoData.class);
//            Timber.d("拦截器Response" + responseNoData.toString());

            if (responseNoData.code == 40013) {
                Timber.w("token失效");
//                clearToLogin();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

  /*  *//**
     * 避免频繁调用
     *//*
    private void clearToLogin() {
        BaseBaseActivity.clearActivityAndToLogin();
    }*/

}