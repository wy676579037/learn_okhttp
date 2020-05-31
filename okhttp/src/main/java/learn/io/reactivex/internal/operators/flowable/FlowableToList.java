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

import java.util.Collection;
import java.util.concurrent.Callable;

import learn.io.reactivex.*;
import learn.io.reactivex.exceptions.Exceptions;
import learn.io.reactivex.internal.functions.ObjectHelper;
import learn.io.reactivex.internal.subscriptions.*;
import learn.org.reactivestreams.Subscriber;
import learn.org.reactivestreams.Subscription;

public final class FlowableToList<T, U extends Collection<? super T>> extends AbstractFlowableWithUpstream<T, U> {
    final Callable<U> collectionSupplier;

    public FlowableToList(Flowable<T> source, Callable<U> collectionSupplier) {
        super(source);
        this.collectionSupplier = collectionSupplier;
    }

    @Override
    protected void subscribeActual(Subscriber<? super U> s) {
        U coll;
        try {
            coll = ObjectHelper.requireNonNull(collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
            return;
        }
        source.subscribe(new ToListSubscriber<T, U>(s, coll));
    }


    static final class ToListSubscriber<T, U extends Collection<? super T>>
    extends DeferredScalarSubscription<U>
    implements FlowableSubscriber<T>, Subscription {


        private static final long serialVersionUID = -8134157938864266736L;
        Subscription s;

        ToListSubscriber(Subscriber<? super U> actual, U collection) {
            super(actual);
            this.value = collection;
        }

        @Override
        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.s, s)) {
                this.s = s;
                actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        @Override
        public void onNext(T t) {
            U v = value;
            if (v != null) {
                v.add(t);
            }
        }

        @Override
        public void onError(Throwable t) {
            value = null;
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            complete(value);
        }

        @Override
        public void cancel() {
            super.cancel();
            s.cancel();
        }
    }
}
