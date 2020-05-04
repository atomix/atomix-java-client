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
package io.atomix.client.counter.impl;

import io.atomix.client.PrimitiveException;
import io.atomix.client.Synchronous;
import io.atomix.client.counter.AsyncDistributedCounter;
import io.atomix.client.counter.DistributedCounter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Default implementation for a {@code DistributedCounter} backed by a {@link AsyncDistributedCounter}.
 */
public class BlockingDistributedCounter extends Synchronous<AsyncDistributedCounter> implements DistributedCounter {

    private final AsyncDistributedCounter asyncCounter;
    private final long operationTimeoutMillis;

    public BlockingDistributedCounter(AsyncDistributedCounter asyncCounter, long operationTimeoutMillis) {
        super(asyncCounter);
        this.asyncCounter = asyncCounter;
        this.operationTimeoutMillis = operationTimeoutMillis;
    }

    @Override
    public boolean compareAndSet(long expectedValue, long updateValue) {
        return complete(asyncCounter.compareAndSet(expectedValue, updateValue));
    }

    @Override
    public void set(long value) {
        complete(asyncCounter.set(value));
    }

    @Override
    public long incrementAndGet() {
        return complete(asyncCounter.incrementAndGet());
    }

    @Override
    public long decrementAndGet() {
        return complete(asyncCounter.decrementAndGet());
    }

    @Override
    public long getAndIncrement() {
        return complete(asyncCounter.getAndIncrement());
    }

    @Override
    public long getAndDecrement() {
        return complete(asyncCounter.getAndDecrement());
    }

    @Override
    public long getAndAdd(long delta) {
        return complete(asyncCounter.getAndAdd(delta));
    }

    @Override
    public long addAndGet(long delta) {
        return complete(asyncCounter.addAndGet(delta));
    }

    @Override
    public long get() {
        return complete(asyncCounter.get());
    }

    @Override
    public AsyncDistributedCounter async() {
        return asyncCounter;
    }

    private <T> T complete(CompletableFuture<T> future) {
        try {
            return future.get(operationTimeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PrimitiveException.Interrupted();
        } catch (TimeoutException e) {
            throw new PrimitiveException.ConcurrentModification();
        } catch (ExecutionException e) {
            throw new PrimitiveException(e.getCause());
        }
    }
}
