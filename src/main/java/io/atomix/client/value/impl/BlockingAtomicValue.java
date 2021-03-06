/*
 * Copyright 2019-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.client.value.impl;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.base.Throwables;
import io.atomix.client.PrimitiveException;
import io.atomix.client.Synchronous;
import io.atomix.client.Versioned;
import io.atomix.client.value.AsyncAtomicValue;
import io.atomix.client.value.AtomicValue;
import io.atomix.client.value.AtomicValueEventListener;

/**
 * Default implementation for a {@code AtomicValue} backed by a {@link AsyncAtomicValue}.
 *
 * @param <V> value type
 */
public class BlockingAtomicValue<V> extends Synchronous<AsyncAtomicValue<V>> implements AtomicValue<V> {

    private final AsyncAtomicValue<V> asyncValue;
    private final long operationTimeoutMillis;

    public BlockingAtomicValue(AsyncAtomicValue<V> asyncValue, long operationTimeoutMillis) {
        super(asyncValue);
        this.asyncValue = asyncValue;
        this.operationTimeoutMillis = operationTimeoutMillis;
    }

    @Override
    public Optional<Versioned<V>> compareAndSet(V expect, V update) {
        return complete(asyncValue.compareAndSet(expect, update));
    }

    @Override
    public Optional<Versioned<V>> compareAndSet(long version, V value) {
        return complete(asyncValue.compareAndSet(version, value));
    }

    @Override
    public Versioned<V> get() {
        return complete(asyncValue.get());
    }

    @Override
    public Versioned<V> getAndSet(V value) {
        return complete(asyncValue.getAndSet(value));
    }

    @Override
    public Versioned<V> set(V value) {
        return complete(asyncValue.set(value));
    }

    @Override
    public void addListener(AtomicValueEventListener<V> listener) {
        complete(asyncValue.addListener(listener));
    }

    @Override
    public void removeListener(AtomicValueEventListener<V> listener) {
        complete(asyncValue.removeListener(listener));
    }

    @Override
    public AsyncAtomicValue<V> async() {
        return asyncValue;
    }

    private <T> T complete(CompletableFuture<T> future) {
        try {
            return future.get(operationTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PrimitiveException.Interrupted();
        } catch (TimeoutException e) {
            throw new PrimitiveException.Timeout();
        } catch (ExecutionException e) {
            Throwable cause = Throwables.getRootCause(e);
            if (cause instanceof PrimitiveException) {
                throw (PrimitiveException) cause;
            } else {
                throw new PrimitiveException(cause);
            }
        }
    }
}
