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
            MainActivity.this.okHttpTest();
        }

        if (R.id.retrofitClick==view.getId()){
            retrofitTest();
        }

    }

    void retrofitTest(){

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://www.wanandroid.com")
                .build();

        RetrofitTestService service = retrofit.create(RetrofitTestService.class);
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

    void okHttpTest(){

        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/hotkey/json")
                .build();

        learn.okhttp3.Call call = client.newCall(request);

        call.enqueue(new learn.okhttp3.Callback() {
            @Override
            public void onFailure(learn.okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(learn.okhttp3.Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.request_result_textview.setText(responseStr);
                    }
                });

            }
        });
    }
}