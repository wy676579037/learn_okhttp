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

package learn.io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;

import learn.io.reactivex.SingleObserver;
import learn.io.reactivex.disposables.Disposable;
import learn.io.reactivex.exceptions.*;
import learn.io.reactivex.functions.BiConsumer;
import learn.io.reactivex.internal.disposables.*;
import learn.io.reactivex.plugins.RxJavaPlugins;

public final class BiConsumerSingleObserver<T>
extends AtomicReference<Disposable>
implements SingleObserver<T>, Disposable {


    private static final long serialVersionUID = 4943102778943297569L;
    final BiConsumer<? super T, ? super Throwable> onCallback;

    public BiConsumerSingleObserver(BiConsumer<? super T, ? super Throwable> onCallback) {
        this.onCallback = onCallback;
    }

    @Override
    public void onError(Throwable e) {
        try {
            lazySet(DisposableHelper.DISPOSED);
            onCallback.accept(null, e);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(new CompositeException(e, ex));
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        DisposableHelper.setOnce(this, d);
    }

    @Override
    public void onSuccess(T value) {
        try {
            lazySet(DisposableHelper.DISPOSED);
            onCallback.accept(value, null);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            RxJavaPlugins.onError(ex);
        }
    }

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }
}
