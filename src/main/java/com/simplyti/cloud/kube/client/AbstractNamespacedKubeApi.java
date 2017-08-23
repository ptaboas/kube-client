package com.simplyti.cloud.kube.client;

import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.observe.Observable;

import io.netty.util.concurrent.Future;

public abstract class AbstractNamespacedKubeApi<T extends KubernetesResource, B extends CreationBuilder<T>, U extends Updater<T>> implements NamespacedKubeApi<T, B, U>{

	protected final String namespace;
	private final ResourceSupplier<T,B,U> resourceSupplier;

	public AbstractNamespacedKubeApi(String namespace, ResourceSupplier<T,B,U> resourceSupplier) {
		this.namespace=namespace;
		this.resourceSupplier=resourceSupplier;
	}

	@Override
	public Future<ResourceList<T>> list() {
		return resourceSupplier.list(namespace);
	}

	@Override
	public Future<T> get(String name) {
		return resourceSupplier.get(namespace, name);
	}

	@Override
	public Future<Void> delete(String name) {
		return resourceSupplier.delete(namespace,name);
	}
	
	@Override
	public Observable<T> watch() {
		return watch(null);
	}

	@Override
	public Observable<T> watch(String version) {
		return resourceSupplier.watch(version,namespace);
	}

	@Override
	public B builder() {
		return resourceSupplier.builder(namespace);
	}
	
	@Override
	public U update(String name) {
		return resourceSupplier.updateBuilder(namespace,name);
	}

}
