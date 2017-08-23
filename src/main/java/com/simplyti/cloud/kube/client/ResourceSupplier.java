package com.simplyti.cloud.kube.client;

import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.observe.Observable;

import io.netty.util.concurrent.Future;

public interface ResourceSupplier<T extends KubernetesResource, B extends CreationBuilder<T>, U extends Updater<T>> {

	Future<ResourceList<T>> list(String namespace);

	Future<T> get(String namespace, String name);

	Future<Void> delete(String namespace, String name);

	Observable<T> watch(String version, String namespace);

	B builder(String namespace);

	U updateBuilder(String namespace, String name);

}
