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
package io.atomix.client.map;

import com.google.common.util.concurrent.MoreExecutors;
import io.atomix.client.PrimitiveType;
import io.atomix.client.SyncPrimitive;
import io.atomix.client.collection.DistributedCollection;
import io.atomix.client.set.DistributedSet;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Distributed map.
 */
public interface DistributedMap<K, V> extends SyncPrimitive, Map<K, V> {
    @Override
    default PrimitiveType type() {
        return DistributedMapType.instance();
    }

    /**
     * Registers the specified listener to be notified whenever the map is updated.
     *
     * @param listener listener to notify about map events
     */
    default void addListener(MapEventListener<K, V> listener) {
        addListener(listener, MoreExecutors.directExecutor());
    }

    /**
     * Registers the specified listener to be notified whenever the map is updated.
     *
     * @param listener listener to notify about map events
     * @param executor executor to use for handling incoming map events
     */
    void addListener(MapEventListener<K, V> listener, Executor executor);

    /**
     * Unregisters the specified listener such that it will no longer
     * receive map change notifications.
     *
     * @param listener listener to unregister
     */
    void removeListener(MapEventListener<K, V> listener);

    @Override
    DistributedSet<K> keySet();

    @Override
    DistributedSet<Entry<K, V>> entrySet();

    @Override
    DistributedCollection<V> values();

    @Override
    AsyncDistributedMap<K, V> async();
}
