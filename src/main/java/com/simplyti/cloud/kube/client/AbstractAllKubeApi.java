package com.simplyti.cloud.kube.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.domain.Status;
import com.simplyti.cloud.kube.client.observe.Observable;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;
import com.simplyti.cloud.kube.client.reqs.KubernetesWatchApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;

public abstract class AbstractAllKubeApi<T extends KubernetesResource, B extends CreationBuilder<T>, U extends Updater<T>, N extends NamespacedKubeApi<T, B, U>> extends AbstractKubeApi<T> implements KubeApi<T>, ResourceSupplier<T,B,U>{
	
	private static final TypeReference<Status> STATUS_TYPE = new TypeReference<Status>() {};
	
	public AbstractAllKubeApi(InternalClient client,String resourceName){
		super(client,resourceName);
	}
	
	@Override
	public Future<T> get(String namespace, String name) {
		return sendRequest(new KubernetesApiRequest(HttpMethod.GET, 
				"/api/v1/namespaces/"+namespace+"/"+resourceName+"/"+name,null,
				resourceClass));
	}
	
	@Override
	public Future<ResourceList<T>> list(String namespace) {
		return sendRequest(new KubernetesApiRequest(HttpMethod.GET, 
				"/api/v1/namespaces/"+namespace+"/"+resourceName,null,
				resourceListClass));
	}
	
	@Override
	public Future<Void> delete(String namespace, String name) {
		return sendRequest(new KubernetesApiRequest(HttpMethod.DELETE, 
				"/api/v1/namespaces/"+namespace+"/"+resourceName+"/"+name,null,
				STATUS_TYPE));
	}
	
	@Override
	public Observable<T> watch(String version, String namespace) {
		return watch(version, newVersion->new KubernetesWatchApiRequest(
				"/api/v1/namespaces/"+namespace+"/"+resourceName,
				newVersion,eventsClass));
	}
	
}
