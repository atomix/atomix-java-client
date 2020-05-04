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
package io.atomix.client.impl;

import com.google.common.base.MoreObjects;
import io.atomix.client.AsyncPrimitive;
import io.atomix.client.ManagedAsyncPrimitive;
import io.atomix.client.PrimitiveState;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for primitive delegates.
 */
public abstract class DelegatingAsyncPrimitive<T extends AsyncPrimitive> implements AsyncPrimitive, ManagedAsyncPrimitive<T> {
    private final T primitive;

    public DelegatingAsyncPrimitive(T primitive) {
        this.primitive = checkNotNull(primitive);
    }

    /**
     * Returns the delegate primitive.
     *
     * @return the underlying primitive
     */
    protected T delegate() {
        return primitive;
    }

    @Override
    public String name() {
        return primitive.name();
    }

    @Override
    public CompletableFuture<Void> delete() {
        return primitive.delete();
    }

    @Override
    public void addStateChangeListener(Consumer<PrimitiveState> listener) {
        primitive.addStateChangeListener(listener);
    }

    @Override
    public void removeStateChangeListener(Consumer<PrimitiveState> listener) {
        primitive.removeStateChangeListener(listener);
    }

    @Override
    public CompletableFuture<Void> close() {
        return primitive.close();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<T> connect() {
        return ((ManagedAsyncPrimitive) primitive).connect().thenApply(v -> this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
            .add("delegate", primitive)
            .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(primitive);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DelegatingAsyncPrimitive
            && primitive.equals(((DelegatingAsyncPrimitive) other).primitive);
    }
}
