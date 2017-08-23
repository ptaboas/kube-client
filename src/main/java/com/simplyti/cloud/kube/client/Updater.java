package com.simplyti.cloud.kube.client;

import com.simplyti.cloud.kube.client.domain.KubernetesResource;

import io.netty.util.concurrent.Future;

public interface Updater<T extends KubernetesResource> {
	
	public Future<T> update();

}
