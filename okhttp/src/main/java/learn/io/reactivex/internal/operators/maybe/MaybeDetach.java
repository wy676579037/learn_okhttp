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

package learn.io.reactivex.internal.operators.maybe;

import learn.io.reactivex.*;
import learn.io.reactivex.disposables.Disposable;
import learn.io.reactivex.internal.disposables.DisposableHelper;

/**
 * Breaks the references between the upstream and downstream when the Maybe terminates.
 *
 * @param <T> the value type
 */
public final class MaybeDetach<T> extends AbstractMaybeWithUpstream<T, T> {

    public MaybeDetach(MaybeSource<T> source) {
        super(source);
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super T> observer) {
        source.subscribe(new DetachMaybeObserver<T>(observer));
    }

    static final class DetachMaybeObserver<T> implements MaybeObserver<T>, Disposable {

        MaybeObserver<? super T> actual;

        Disposable d;

        DetachMaybeObserver(MaybeObserver<? super T> actual) {
            this.actual = actual;
        }

        @Override
        public void dispose() {
            actual = null;
            d.dispose();
            d = DisposableHelper.DISPOSED;
        }

        @Override
        public boolean isDisposed() {
            return d.isDisposed();
        }

        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.d, d)) {
                this.d = d;

                actual.onSubscribe(this);
            }
        }

        @Override
        public void onSuccess(T value) {
            d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = actual;
            if (a != null) {
                actual = null;
                a.onSuccess(value);
            }
        }

        @Override
        public void onError(Throwable e) {
            d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = actual;
            if (a != null) {
                actual = null;
                a.onError(e);
            }
        }

        @Override
        public void onComplete() {
            d = DisposableHelper.DISPOSED;
            MaybeObserver<? super T> a = actual;
            if (a != null) {
                actual = null;
                a.onComplete();
            }
        }
    }
}
