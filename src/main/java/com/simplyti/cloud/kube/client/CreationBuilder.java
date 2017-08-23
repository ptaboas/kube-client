package com.simplyti.cloud.kube.client;

import io.netty.util.concurrent.Future;

public interface CreationBuilder<T> {

	CreationBuilder<T> withName(String name);

	Future<T> create();

}
