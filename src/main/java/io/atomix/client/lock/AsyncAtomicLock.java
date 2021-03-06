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

import java.time.Duration;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

import io.atomix.client.AsyncPrimitive;
import io.atomix.client.DistributedPrimitive;
import io.atomix.client.PrimitiveType;

/**
 * Asynchronous lock primitive.
 */
public interface AsyncAtomicLock extends AsyncPrimitive {
    @Override
    default PrimitiveType type() {
        return AtomicLockType.instance();
    }

    /**
     * Acquires the lock, blocking until it's available.
     *
     * @return future to be completed once the lock has been acquired
     */
    CompletableFuture<Long> lock();

    /**
     * Attempts to acquire the lock.
     *
     * @return future to be completed with a boolean indicating whether the lock was acquired
     */
    CompletableFuture<OptionalLong> tryLock();

    /**
     * Attempts to acquire the lock for a specified amount of time.
     *
     * @param timeout the timeout after which to give up attempting to acquire the lock
     * @return future to be completed with a boolean indicating whether the lock was acquired
     */
    CompletableFuture<OptionalLong> tryLock(Duration timeout);

    /**
     * Unlocks the lock.
     *
     * @return future to be completed once the lock has been released
     */
    CompletableFuture<Void> unlock();

    /**
     * Unlocks the lock.
     *
     * @param version the lock version
     * @return future to be completed once the lock has been released
     */
    CompletableFuture<Boolean> unlock(long version);

    /**
     * Returns a boolean indicating whether the lock is locked.
     *
     * @return indicates whether the lock is locked
     */
    CompletableFuture<Boolean> isLocked();

    /**
     * Returns a boolean indicating whether the lock is locked with the given version.
     *
     * @param version the lock version
     * @return indicates whether the lock is locked
     */
    CompletableFuture<Boolean> isLocked(long version);

    @Override
    default AtomicLock sync() {
        return sync(Duration.ofMillis(DistributedPrimitive.DEFAULT_OPERATION_TIMEOUT_MILLIS));
    }

    @Override
    AtomicLock sync(Duration operationTimeout);
}
