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
package learn.io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;

import learn.io.reactivex.*;
import learn.io.reactivex.disposables.Disposable;
import learn.io.reactivex.functions.BiConsumer;
import learn.io.reactivex.internal.disposables.*;
import learn.io.reactivex.internal.functions.ObjectHelper;
import learn.io.reactivex.internal.fuseable.FuseToObservable;
import learn.io.reactivex.plugins.RxJavaPlugins;

public final class ObservableCollectSingle<T, U> extends Single<U> implements FuseToObservable<U> {

    final ObservableSource<T> source;

    final Callable<? extends U> initialSupplier;
    final BiConsumer<? super U, ? super T> collector;

    public ObservableCollectSingle(ObservableSource<T> source,
            Callable<? extends U> initialSupplier, BiConsumer<? super U, ? super T> collector) {
        this.source = source;
        this.initialSupplier = initialSupplier;
        this.collector = collector;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super U> t) {
        U u;
        try {
            u = ObjectHelper.requireNonNull(initialSupplier.call(), "The initialSupplier returned a null value");
        } catch (Throwable e) {
            EmptyDisposable.error(e, t);
            return;
        }

        source.subscribe(new CollectObserver<T, U>(t, u, collector));
    }

    @Override
    public Observable<U> fuseToObservable() {
        return RxJavaPlugins.onAssembly(new ObservableCollect<T, U>(source, initialSupplier, collector));
    }

    static final class CollectObserver<T, U> implements Observer<T>, Disposable {
        final SingleObserver<? super U> actual;
        final BiConsumer<? super U, ? super T> collector;
        final U u;

        Disposable s;

        boolean done;

        CollectObserver(SingleObserver<? super U> actual, U u, BiConsumer<? super U, ? super T> collector) {
            this.actual = actual;
            this.collector = collector;
            this.u = u;
        }

        @Override
        public void onSubscribe(Disposable s) {
            if (DisposableHelper.validate(this.s, s)) {
                this.s = s;
                actual.onSubscribe(this);
            }
        }


        @Override
        public void dispose() {
            s.dispose();
        }

        @Override
        public boolean isDisposed() {
            return s.isDisposed();
        }


        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            try {
                collector.accept(u, t);
            } catch (Throwable e) {
                s.dispose();
                onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                RxJavaPlugins.onError(t);
                return;
            }
            done = true;
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            done = true;
            actual.onSuccess(u);
        }
    }
}
