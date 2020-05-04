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
package io.atomix.client.set.impl;

import io.atomix.client.collection.impl.CachingAsyncDistributedCollection;
import io.atomix.client.set.AsyncDistributedSet;
import io.atomix.client.set.DistributedSet;

import java.time.Duration;

/**
 * Caching asynchronous distributed queue.
 */
public class CachingAsyncDistributedSet<E> extends CachingAsyncDistributedCollection<E> implements AsyncDistributedSet<E> {
    private final AsyncDistributedSet<E> backingSet;

    public CachingAsyncDistributedSet(AsyncDistributedSet<E> backingCollection, int cacheSize) {
        super(backingCollection, cacheSize);
        this.backingSet = backingCollection;
    }

    @Override
    public DistributedSet<E> sync(Duration operationTimeout) {
        return new BlockingDistributedSet<>(this, operationTimeout.toMillis());
    }
}
