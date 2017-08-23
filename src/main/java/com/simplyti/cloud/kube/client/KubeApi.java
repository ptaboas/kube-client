package com.simplyti.cloud.kube.client;

import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.observe.Observable;

import io.netty.util.concurrent.Future;

public interface KubeApi<T extends KubernetesResource> {
	
	public Future<ResourceList<T>> list();
	
	public Observable<T> watch();
	
	public Observable<T> watch(String version);
	
}
