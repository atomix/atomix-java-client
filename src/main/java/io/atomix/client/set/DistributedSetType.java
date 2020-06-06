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
package io.atomix.client.set;

import io.atomix.api.primitive.PrimitiveId;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.PrimitiveType;
import io.atomix.client.set.impl.DefaultDistributedSetBuilder;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Distributed set primitive type.
 */
public class DistributedSetType<E> implements PrimitiveType<DistributedSetBuilder<E>, DistributedSet<E>> {
    private static final DistributedSetType INSTANCE = new DistributedSetType();

    /**
     * Returns a new distributed set type.
     *
     * @param <E> the set element type
     * @return a new distributed set type
     */
    @SuppressWarnings("unchecked")
    public static <E> DistributedSetType<E> instance() {
        return INSTANCE;
    }

    @Override
    public DistributedSetBuilder<E> newBuilder(PrimitiveId primitiveId, PrimitiveManagementService managementService) {
        return new DefaultDistributedSetBuilder<>(primitiveId, managementService);
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
