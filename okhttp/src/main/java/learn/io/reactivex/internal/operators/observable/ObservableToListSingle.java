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

import learn.io.reactivex.internal.functions.ObjectHelper;
import java.util.*;
import java.util.concurrent.Callable;

import learn.io.reactivex.*;
import learn.io.reactivex.Observable;
import learn.io.reactivex.Observer;
import learn.io.reactivex.disposables.Disposable;
import learn.io.reactivex.exceptions.Exceptions;
import learn.io.reactivex.internal.disposables.*;
import learn.io.reactivex.internal.functions.Functions;
import learn.io.reactivex.internal.fuseable.FuseToObservable;
import learn.io.reactivex.plugins.RxJavaPlugins;

public final class ObservableToListSingle<T, U extends Collection<? super T>>
extends Single<U> implements FuseToObservable<U> {

    final ObservableSource<T> source;

    final Callable<U> collectionSupplier;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ObservableToListSingle(ObservableSource<T> source, final int defaultCapacityHint) {
        this.source = source;
        this.collectionSupplier = (Callable)Functions.createArrayList(defaultCapacityHint);
    }

    public ObservableToListSingle(ObservableSource<T> source, Callable<U> collectionSupplier) {
        this.source = source;
        this.collectionSupplier = collectionSupplier;
    }

    @Override
    public void subscribeActual(SingleObserver<? super U> t) {
        U coll;
        try {
            coll = ObjectHelper.requireNonNull(collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, t);
            return;
        }
        source.subscribe(new ToListObserver<T, U>(t, coll));
    }

    @Override
    public Observable<U> fuseToObservable() {
        return RxJavaPlugins.onAssembly(new ObservableToList<T, U>(source, collectionSupplier));
    }

    static final class ToListObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
        final SingleObserver<? super U> actual;

        U collection;

        Disposable s;

        ToListObserver(SingleObserver<? super U> actual, U collection) {
            this.actual = actual;
            this.collection = collection;
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
            collection.add(t);
        }

        @Override
        public void onError(Throwable t) {
            collection = null;
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            U c = collection;
            collection = null;
            actual.onSuccess(c);
        }
    }
}
