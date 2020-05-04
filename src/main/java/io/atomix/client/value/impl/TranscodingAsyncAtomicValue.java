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

import com.google.common.collect.Maps;
import io.atomix.client.Versioned;
import io.atomix.client.impl.DelegatingAsyncPrimitive;
import io.atomix.client.value.AsyncAtomicValue;
import io.atomix.client.value.AtomicValue;
import io.atomix.client.value.AtomicValueEvent;
import io.atomix.client.value.AtomicValueEventListener;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Transcoding async atomic value.
 */
public class TranscodingAsyncAtomicValue<V1, V2> extends DelegatingAsyncPrimitive implements AsyncAtomicValue<V1> {

    private final AsyncAtomicValue<V2> backingValue;
    private final Function<V1, V2> valueEncoder;
    private final Function<V2, V1> valueDecoder;
    private final Map<AtomicValueEventListener<V1>, InternalAtomicValueEventListener> listeners = Maps.newIdentityHashMap();

    public TranscodingAsyncAtomicValue(AsyncAtomicValue<V2> backingValue, Function<V1, V2> valueEncoder, Function<V2, V1> valueDecoder) {
        super(backingValue);
        this.backingValue = backingValue;
        this.valueEncoder = v -> v != null ? valueEncoder.apply(v) : null;
        this.valueDecoder = v -> v != null ? valueDecoder.apply(v) : null;
    }

    @Override
    public CompletableFuture<Optional<Versioned<V1>>> compareAndSet(V1 expect, V1 update) {
        return backingValue.compareAndSet(valueEncoder.apply(expect), valueEncoder.apply(update))
            .thenApply(result -> result.map(v -> v != null ? v.map(valueDecoder) : null));
    }

    @Override
    public CompletableFuture<Optional<Versioned<V1>>> compareAndSet(long version, V1 value) {
        return backingValue.compareAndSet(version, valueEncoder.apply(value))
            .thenApply(result -> result.map(v -> v != null ? v.map(valueDecoder) : null));
    }

    @Override
    public CompletableFuture<Versioned<V1>> get() {
        return backingValue.get().thenApply(v -> v != null ? v.map(valueDecoder) : null);
    }

    @Override
    public CompletableFuture<Versioned<V1>> getAndSet(V1 value) {
        return backingValue.getAndSet(valueEncoder.apply(value)).thenApply(v -> v != null ? v.map(valueDecoder) : null);
    }

    @Override
    public CompletableFuture<Versioned<V1>> set(V1 value) {
        return backingValue.set(valueEncoder.apply(value)).thenApply(v -> v != null ? v.map(valueDecoder) : null);
    }

    @Override
    public CompletableFuture<Void> addListener(AtomicValueEventListener<V1> listener) {
        synchronized (listeners) {
            InternalAtomicValueEventListener internalListener =
                listeners.computeIfAbsent(listener, k -> new InternalAtomicValueEventListener(listener));
            return backingValue.addListener(internalListener);
        }
    }

    @Override
    public CompletableFuture<Void> removeListener(AtomicValueEventListener<V1> listener) {
        synchronized (listeners) {
            InternalAtomicValueEventListener internalListener = listeners.remove(listener);
            if (internalListener != null) {
                return backingValue.removeListener(internalListener);
            } else {
                return CompletableFuture.completedFuture(null);
            }
        }
    }

    @Override
    public AtomicValue<V1> sync(Duration operationTimeout) {
        return new BlockingAtomicValue<>(this, operationTimeout.toMillis());
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("backingValue", backingValue)
            .toString();
    }

    private class InternalAtomicValueEventListener implements AtomicValueEventListener<V2> {
        private final AtomicValueEventListener<V1> listener;

        InternalAtomicValueEventListener(AtomicValueEventListener<V1> listener) {
            this.listener = listener;
        }

        @Override
        public void event(AtomicValueEvent<V2> event) {
            listener.event(new AtomicValueEvent<>(
                AtomicValueEvent.Type.UPDATE,
                event.newValue() != null ? event.newValue().map(valueDecoder) : null,
                event.oldValue() != null ? event.oldValue().map(valueDecoder) : null));
        }
    }
}
