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

import io.atomix.api.primitive.PrimitiveId;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.PrimitiveType;
import io.atomix.client.map.impl.DefaultDistributedMapBuilder;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Distributed map primitive type.
 */
public class DistributedMapType<K, V> implements PrimitiveType<DistributedMapBuilder<K, V>, DistributedMap<K, V>> {
    private static final DistributedMapType INSTANCE = new DistributedMapType();

    /**
     * Returns a new distributed map type.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a new distributed map type
     */
    @SuppressWarnings("unchecked")
    public static <K, V> DistributedMapType<K, V> instance() {
        return INSTANCE;
    }

    @Override
    public DistributedMapBuilder<K, V> newBuilder(PrimitiveId primitiveId, PrimitiveManagementService managementService) {
        return new DefaultDistributedMapBuilder<>(primitiveId, managementService);
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
