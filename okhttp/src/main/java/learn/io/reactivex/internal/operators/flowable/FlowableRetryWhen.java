/**
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package learn.io.reactivex.internal.operators.flowable;

import learn.io.reactivex.Flowable;
import learn.io.reactivex.exceptions.Exceptions;
import learn.io.reactivex.functions.Function;
import learn.io.reactivex.internal.functions.ObjectHelper;
import learn.io.reactivex.internal.operators.flowable.FlowableRepeatWhen.*;
import learn.io.reactivex.internal.subscriptions.EmptySubscription;
import learn.io.reactivex.processors.*;
import learn.io.reactivex.subscribers.SerializedSubscriber;
import learn.org.reactivestreams.Publisher;
import learn.org.reactivestreams.Subscriber;
import learn.org.reactivestreams.Subscription;

public final class FlowableRetryWhen<T> extends AbstractFlowableWithUpstream<T, T> {
    final Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler;

    public FlowableRetryWhen(Flowable<T> source,
            Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
        super(source);
        this.handler = handler;
    }

    @Override
    public void subscribeActual(Subscriber<? super T> s) {
        SerializedSubscriber<T> z = new SerializedSubscriber<T>(s);

        FlowableProcessor<Throwable> processor = UnicastProcessor.<Throwable>create(8).toSerialized();

        Publisher<?> when;

        try {
            when = ObjectHelper.requireNonNull(handler.apply(processor), "handler returned a null Publisher");
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptySubscription.error(ex, s);
            return;
        }

        WhenReceiver<T, Throwable> receiver = new WhenReceiver<T, Throwable>(source);

        RetryWhenSubscriber<T> subscriber = new RetryWhenSubscriber<T>(z, processor, receiver);

        receiver.subscriber = subscriber;

        s.onSubscribe(subscriber);

        when.subscribe(receiver);

        receiver.onNext(0);
    }

    static final class RetryWhenSubscriber<T> extends WhenSourceSubscriber<T, Throwable> {


        private static final long serialVersionUID = -2680129890138081029L;

        RetryWhenSubscriber(Subscriber<? super T> actual, FlowableProcessor<Throwable> processor,
                Subscription receiver) {
            super(actual, processor, receiver);
        }

        @Override
        public void onError(Throwable t) {
            again(t);
        }

        @Override
        public void onComplete() {
            receiver.cancel();
            actual.onComplete();
        }
    }

}
