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

import io.grpc.stub.StreamObserver;

import java.util.function.Function;

/**
 * Transcoding stream observer.
 */
public class TranscodingStreamObserver<T, U> implements StreamObserver<T> {
    private final StreamObserver<U> handler;
    private final Function<T, U> transcoder;

    public TranscodingStreamObserver(StreamObserver<U> handler, Function<T, U> transcoder) {
        this.handler = handler;
        this.transcoder = transcoder;
    }

    @Override
    public void onNext(T value) {
        handler.onNext(transcoder.apply(value));
    }

    @Override
    public void onCompleted() {
        handler.onCompleted();
    }

    @Override
    public void onError(Throwable error) {
        handler.onError(error);
    }
}
