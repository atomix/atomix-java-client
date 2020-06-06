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

import com.google.common.collect.Maps;
import io.atomix.api.primitive.PrimitiveId;
import io.atomix.client.DistributedPrimitive;
import io.atomix.client.PrimitiveCache;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Core primitive cache.
 */
public class PrimitiveCacheImpl implements PrimitiveCache {
    private final Map<PrimitiveId, CompletableFuture> primitives = Maps.newConcurrentMap();

    @Override
    @SuppressWarnings("unchecked")
    public <P extends DistributedPrimitive> CompletableFuture<P> getPrimitive(PrimitiveId primitiveId, Supplier<CompletableFuture<P>> supplier) {
        return primitives.computeIfAbsent(primitiveId, n -> supplier.get());
    }
}
