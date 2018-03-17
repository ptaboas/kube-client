package com.simplyti.cloud.kube.client;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.domain.Status;
import com.simplyti.cloud.kube.client.observe.Observable;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;
import com.simplyti.cloud.kube.client.reqs.KubernetesWatchApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;

public abstract class AbstractAllKubeApi<T extends KubernetesResource, B extends CreationBuilder<T>, U extends Updater<T>, N extends NamespacedKubeApi<T, B, U>> extends AbstractKubeApi<T> implements KubeApi<T>, ResourceSupplier<T,B,U>{
	
	private static final TypeLiteral<Status> STATUS_TYPE = new TypeLiteral<Status>() {};
	
	public AbstractAllKubeApi(InternalClient client, String api,String resourceName){
		this(client,true,api,resourceName);
	}
	
	public AbstractAllKubeApi(InternalClient client,boolean core, String api,String resourceName){
		super(client,core,api,resourceName);
	}
	
	@Override
	public Future<T> get(String namespace, String name) {
		return sendRequest(new KubernetesApiRequest(HttpMethod.GET, 
				resource(namespace)+"/"+name,null),resourceClass);
	}
	
	private String resource(String namespace) {
		return (core?"/api/":"/apis/")+api+"/namespaces/"+namespace+"/"+resourceName;
	}

	@Override
	public Future<ResourceList<T>> list(String namespace) {
		return sendRequest(new KubernetesApiRequest(HttpMethod.GET, 
				resource(namespace),null),resourceListClass);
	}
	
	@Override
	public Future<Status> delete(String namespace, String name) {
		return sendRequest(new KubernetesApiRequest(HttpMethod.DELETE, 
				resource(namespace)+"/"+name,null),STATUS_TYPE);
	}
	
	@Override
	public Observable<T> watch(String version, String namespace) {
		return watch(version, newVersion->new KubernetesWatchApiRequest(
				resource(namespace),
				newVersion));
	}
	
}
