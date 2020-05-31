package learn.org.reactivestreams;

public abstract interface Subscriber<T>
{
  public abstract void onSubscribe(Subscription paramSubscription);
  
  public abstract void onNext(T paramT);
  
  public abstract void onError(Throwable paramThrowable);
  
  public abstract void onComplete();
}


/* Location:              /Users/wangyong14/.gradle/caches/modules-2/files-2.1/org.reactivestreams/reactive-streams/1.0.2/323964c36556eb0e6209f65c1cef72b53b461ab8/reactive-streams-1.0.2.jar!/org/reactivestreams/Subscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */