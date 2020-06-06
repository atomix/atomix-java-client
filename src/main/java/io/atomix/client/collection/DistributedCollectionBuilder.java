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
package io.atomix.client.collection;

import io.atomix.api.primitive.PrimitiveId;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.cache.CachedPrimitiveBuilder;

/**
 * Distributed collection builder.
 */
public abstract class DistributedCollectionBuilder<
    B extends DistributedCollectionBuilder<B, P, E>,
    P extends DistributedCollection<E>, E>
    extends CachedPrimitiveBuilder<B, P> {
    protected DistributedCollectionBuilder(PrimitiveId primitiveId, PrimitiveManagementService managementService) {
        super(primitiveId, managementService);
    }
}
