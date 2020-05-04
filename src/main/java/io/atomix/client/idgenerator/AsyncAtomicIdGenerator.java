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
package io.atomix.client.idgenerator;

import io.atomix.client.AsyncPrimitive;
import io.atomix.client.DistributedPrimitive;
import io.atomix.client.PrimitiveType;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * An async ID generator for generating globally unique numbers.
 */
public interface AsyncAtomicIdGenerator extends AsyncPrimitive {
    @Override
    default PrimitiveType type() {
        return AtomicIdGeneratorType.instance();
    }

    /**
     * Returns the next globally unique numeric ID.
     *
     * @return a future to be completed with the next globally unique identifier
     */
    CompletableFuture<Long> nextId();

    @Override
    default AtomicIdGenerator sync() {
        return sync(Duration.ofMillis(DistributedPrimitive.DEFAULT_OPERATION_TIMEOUT_MILLIS));
    }

    @Override
    AtomicIdGenerator sync(Duration operationTimeout);
}
