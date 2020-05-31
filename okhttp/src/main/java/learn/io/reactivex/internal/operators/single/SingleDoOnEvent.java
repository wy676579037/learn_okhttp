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

package learn.io.reactivex.internal.operators.single;

import learn.io.reactivex.Single;
import learn.io.reactivex.SingleObserver;
import learn.io.reactivex.SingleSource;
import learn.io.reactivex.disposables.Disposable;
import learn.io.reactivex.exceptions.CompositeException;
import learn.io.reactivex.exceptions.Exceptions;
import learn.io.reactivex.functions.BiConsumer;

public final class SingleDoOnEvent<T> extends Single<T> {
    final SingleSource<T> source;

    final BiConsumer<? super T, ? super Throwable> onEvent;

    public SingleDoOnEvent(SingleSource<T> source, BiConsumer<? super T, ? super Throwable> onEvent) {
        this.source = source;
        this.onEvent = onEvent;
    }

    @Override
    protected void subscribeActual(final SingleObserver<? super T> s) {

        source.subscribe(new DoOnEvent(s));
    }

    final class DoOnEvent implements SingleObserver<T> {
        private final SingleObserver<? super T> s;

        DoOnEvent(SingleObserver<? super T> s) {
            this.s = s;
        }

        @Override
        public void onSubscribe(Disposable d) {
            s.onSubscribe(d);
        }

        @Override
        public void onSuccess(T value) {
            try {
                onEvent.accept(value, null);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                s.onError(ex);
                return;
            }

            s.onSuccess(value);
        }

        @Override
        public void onError(Throwable e) {
            try {
                onEvent.accept(null, e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            s.onError(e);
        }
    }
}
