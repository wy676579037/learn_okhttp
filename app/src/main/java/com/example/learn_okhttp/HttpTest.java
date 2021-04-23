package com.example.learn_okhttp;

import android.util.Log;

import java.io.IOException;

import learn.okhttp3.OkHttpClient;
import learn.okhttp3.Request;
import learn.okhttp3.Response;
import learn.okhttp3.ResponseBody;

public class HttpTest {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        final String result = response.body().string();
        System.out.println(result);
    }
}
