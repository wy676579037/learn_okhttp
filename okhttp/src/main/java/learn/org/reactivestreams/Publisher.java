package learn.org.reactivestreams;

public abstract interface Publisher<T>
{
  public abstract void subscribe(Subscriber<? super T> paramSubscriber);
}


/* Location:              /Users/wangyong14/.gradle/caches/modules-2/files-2.1/org.reactivestreams/reactive-streams/1.0.2/323964c36556eb0e6209f65c1cef72b53b461ab8/reactive-streams-1.0.2.jar!/org/reactivestreams/Publisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */