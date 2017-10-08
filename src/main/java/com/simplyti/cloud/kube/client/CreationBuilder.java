package com.simplyti.cloud.kube.client;

import io.netty.util.concurrent.Future;

public interface CreationBuilder<T> {

	CreationBuilder<T> withName(String name);
	
	CreationBuilder<T> addLabel(String name,String value);

	Future<T> create();

}
