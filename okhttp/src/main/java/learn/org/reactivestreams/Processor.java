package learn.org.reactivestreams;

public abstract interface Processor<T, R>
  extends Subscriber<T>, Publisher<R>
{}


/* Location:              /Users/wangyong14/.gradle/caches/modules-2/files-2.1/org.reactivestreams/reactive-streams/1.0.2/323964c36556eb0e6209f65c1cef72b53b461ab8/reactive-streams-1.0.2.jar!/org/reactivestreams/Processor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */