package com.simplyti.cloud.kube.client;

import java.util.Collection;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Promise;

public abstract class AbstractUpdater<T extends KubernetesResource,U extends Updater<T>> extends ResourceUpdater<T,U> implements Updater<T>  {
	
	private final InternalClient client;
	private final String namespace;
	private final String name;
	private final String resourceName;
	private final TypeLiteral<T> resourceClass;
	
	public AbstractUpdater(InternalClient client, 
			String namespace,String name,
			String resourceName,TypeLiteral<T> resourceClass,
			ResourceSupplier<T, ?,?> supplier){
		super(client.nextExecutor(),supplier.get(namespace, name));
		this.client=client;
		this.namespace=namespace;
		this.name=name;
		this.resourceName=resourceName;
		this.resourceClass=resourceClass;
	}
	
	@Override
	public void update(Promise<T> promise,Collection<JsonPatch> patches) {
		client.sendRequest(promise,new KubernetesApiRequest(HttpMethod.PATCH,
				"/api/v1/namespaces/"+namespace+"/"+resourceName+"/"+name,patches),resourceClass);
	}
	
}
