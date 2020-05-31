package com.example.learn_okhttp;

import learn.okhttp3.ResponseBody;
import learn.retrofit2.Call;
import learn.retrofit2.http.GET;


//https://www.wanandroid.com
public interface GitHubService {
    @GET("/hotkey/json")
    Call<ResponseBody> hotkey();
}