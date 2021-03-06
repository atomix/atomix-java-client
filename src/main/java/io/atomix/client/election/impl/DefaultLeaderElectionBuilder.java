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
package io.atomix.client.election.impl;

import java.util.concurrent.CompletableFuture;

import com.google.common.io.BaseEncoding;
import io.atomix.api.primitive.Name;
import io.atomix.client.PrimitiveManagementService;
import io.atomix.client.election.AsyncLeaderElection;
import io.atomix.client.election.LeaderElection;
import io.atomix.client.election.LeaderElectionBuilder;
import io.atomix.client.utils.serializer.Serializer;

/**
 * Default implementation of {@code LeaderElectorBuilder}.
 */
public class DefaultLeaderElectionBuilder<T> extends LeaderElectionBuilder<T> {
    public DefaultLeaderElectionBuilder(Name name, PrimitiveManagementService managementService) {
        super(name, managementService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<LeaderElection<T>> buildAsync() {
        return managementService.getPartitionService().getPartitionGroup(group)
            .thenCompose(group -> new DefaultAsyncLeaderElection(
                getName(),
                group.getPartition(partitioner.partition(getName().getName(), group.getPartitionIds())),
                managementService.getThreadFactory().createContext(),
                sessionTimeout)
                .connect()
                .thenApply(election -> {
                    Serializer serializer = serializer();
                    return new TranscodingAsyncLeaderElection<T, String>(
                        election,
                        id -> BaseEncoding.base16().encode(serializer.encode(id)),
                        string -> serializer.decode(BaseEncoding.base16().decode(string)));
                })
                .thenApply(AsyncLeaderElection::sync));
    }
}