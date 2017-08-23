package com.simplyti.cloud.kube.client;

import com.simplyti.cloud.kube.client.domain.KubernetesResource;

import io.netty.util.concurrent.Future;

public interface NamespacedKubeApi<T extends KubernetesResource, B extends CreationBuilder<T>, U extends Updater<T>> extends KubeApi<T>{
	
	public Future<T> get(String name);
	
	public Future<Void> delete(String name);
	
	public B builder();
	
	public U update(String name);

}
