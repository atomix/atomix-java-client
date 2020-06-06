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
package io.atomix.client.lock;

import io.atomix.api.primitive.PrimitiveId;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.PrimitiveType;
import io.atomix.client.lock.impl.DefaultAtomicLockBuilder;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Atomic lock primitive type.
 */
public class AtomicLockType implements PrimitiveType<AtomicLockBuilder, AtomicLock> {
    private static final AtomicLockType INSTANCE = new AtomicLockType();

    /**
     * Returns a new distributed lock type.
     *
     * @return a new distributed lock type
     */
    public static AtomicLockType instance() {
        return INSTANCE;
    }

    @Override
    public AtomicLockBuilder newBuilder(PrimitiveId primitiveId, PrimitiveManagementService managementService) {
        return new DefaultAtomicLockBuilder(primitiveId, managementService);
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
