package com.example.learn_okhttp;

import androidx.appcompat.app.AppCompatActivity;
import learn.okhttp3.OkHttpClient;
import learn.okhttp3.Request;
import learn.okhttp3.Response;
import learn.okhttp3.ResponseBody;
import learn.retrofit2.Call;
import learn.retrofit2.Callback;
import learn.retrofit2.Retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView request_result_textview;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request_result_textview = findViewById(R.id.request_result);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.requestClick){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MainActivity.this.run("https://www.baidu.com");
                    } catch (IOException e) {
                        Log.e("wy",e+"");
                    }
                }
            }).start();
        }


        if (R.id.retrofitClick==view.getId()){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.wanandroid.com")
                    .build();

            GitHubService service = retrofit.create(GitHubService.class);

            Call<ResponseBody> call = service.hotkey();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, learn.retrofit2.Response<ResponseBody> response) {
                    try {
                        MainActivity.this.request_result_textview.setText(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }

    void run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        final String result = response.body().string();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.request_result_textview.setText(result);
            }
        });
    }
}