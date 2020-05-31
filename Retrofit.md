## Retrofit源码阅读

Retrofit 和 OkHttp是目前几乎所有做安卓开发的人网络请求标配，它们 都是由 Square 公司推出的网络请求库，并且 Retrofit 实际上是基于 OkHttp 实现的，它在 OkHttp 现有功能的基础上进行了封装，支持通过<u><font size=4 color=#FF69B4>注解</font></u>进行网络请求参数的配置，同时对数据返回后的解析、序列化进行了统一的包装，甚至在近期引入了对协程对支持。

简单对比下OkHttp跟Retrofit的使用

~~~java
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
~~~

两者的使用流程基本一致的，但是Retrofit底层实际是OkHttp做网络请求的，也就是说最后的调用流程是OKhttpTest那样的。

这个就是Retrofit的魅力所在，保持了原有基本一致的调用习惯，同时做了扩展让使用者更加舒服。

#### 我们来看下Retrofit的Call跟OkHttp代码里的Call有何区别：

~~~java
//Retrofit Call
public interface Call<T> extends Cloneable {

  /** The original HTTP request. */
  Request request();

  Response<T> execute() throws IOException;

  void enqueue(Callback<T> callback);

  void cancel();

  boolean isExecuted();

  boolean isCanceled();

  Call<T> clone();

}

//OkHttpCall
public interface Call extends Cloneable {
  /** Returns the original request that initiated this call. */
  Request request();

  Response execute() throws IOException;

  void enqueue(Callback responseCallback);

  void cancel();

  boolean isExecuted();

  boolean isCanceled();

  Call clone();
final class OkHttpCall<T> implements Call<T> {
  private final ServiceMethod<T, ?> serviceMethod;
  private final @Nullable Object[] args;

  private volatile boolean canceled;

  @GuardedBy("this")
  private @Nullable learn.okhttp3.Call rawCall;
  @GuardedBy("this")
  private @Nullable Throwable creationFailure; // Either a RuntimeException or IOException.
  @GuardedBy("this")
  private boolean executed;
  Timeout timeout();

  interface Factory {
    Call newCall(Request request);
  }
}
~~~

从接口上看两者的Call基本是一致的，我们再看下Retrofit的Call的实现类OKHttpCall

~~~java
final class OkHttpCall<T> implements Call<T> {
  private final ServiceMethod<T, ?> serviceMethod;
  private final @Nullable Object[] args;

  private volatile boolean canceled;

  @GuardedBy("this")
  private @Nullable learn.okhttp3.Call rawCall;
  @GuardedBy("this")
  private @Nullable Throwable creationFailure; // Either a RuntimeException or IOException.
  @GuardedBy("this")
  private boolean executed;
~~~

我们看到了什么，他有  private @Nullable learn.okhttp3.Call rawCall; OKhttp3里的call的对象，我们在看下excute方法，代码简单点

~~~java
@Override public Response<T> execute() throws IOException {
    learn.okhttp3.Call call;

    synchronized (this) {
      if (executed) throw new IllegalStateException("Already executed.");
      executed = true;

      if (creationFailure != null) {
        if (creationFailure instanceof IOException) {
          throw (IOException) creationFailure;
        } else {
          throw (RuntimeException) creationFailure;
        }
      }

      call = rawCall;
      if (call == null) {
        try {
          call = rawCall = createRawCall();
        } catch (IOException | RuntimeException e) {
          creationFailure = e;
          throw e;
        }
      }
    }

    if (canceled) {
      call.cancel();
    }

    return parseResponse(call.execute());
  }
~~~

return parseResponse(call.execute());就是调用OKHttp3的Call进行实现的

~~~java
  private learn.okhttp3.Call createRawCall() throws IOException {
    Request request = serviceMethod.toRequest(args);
    learn.okhttp3.Call call = serviceMethod.callFactory.newCall(request);
    if (call == null) {
      throw new NullPointerException("Call.Factory returned null.");
    }
    return call;
  }
~~~

我们看到了OKHttp3创建Call需要的request了。

~~~java
    Request request = serviceMethod.toRequest(args);
~~~

#### serviceMethod 这个又是什么东东？

