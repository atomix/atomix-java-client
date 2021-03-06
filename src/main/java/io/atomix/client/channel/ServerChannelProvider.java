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
package io.atomix.client.channel;

import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

/**
 * Server channel provider.
 */
public class ServerChannelProvider implements ChannelProvider {
    private final String host;
    private final int port;
    private final ChannelConfig config;

    public ServerChannelProvider(String host, int port, ChannelConfig config) {
        this.host = host;
        this.port = port;
        this.config = config;
    }

    @Override
    public ChannelFactory getFactory() {
        NettyChannelBuilder builder;
        if (config.isTlsEnabled()) {
            builder = NettyChannelBuilder.forAddress(host, port)
                .useTransportSecurity();
        } else {
            builder = NettyChannelBuilder.forAddress(host, port)
                .usePlaintext();
        }
        return builder::build;
    }
}
