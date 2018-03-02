package com.simplyti.cloud.kube.client;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.observe.Observable;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;
import com.simplyti.cloud.kube.client.reqs.KubernetesWatchApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;

public class AbstractKubeApi<T extends KubernetesResource> {
	
	protected final InternalClient client;
	protected final String resourceName;
	protected final TypeLiteral<T> resourceClass;
	protected final TypeLiteral<ResourceList<T>> resourceListClass;
	protected final TypeLiteral<Event<T>> eventsClass;

	@SuppressWarnings("unchecked")
	public AbstractKubeApi(InternalClient client, String resourceName){
		this.client=client;
		this.resourceName=resourceName;
		Type resourceType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.resourceClass = TypeLiteral.create(resourceType);
		this.resourceListClass =  TypeLiteral.create(new ParameterizedTypeImpl(ResourceList.class, resourceType)); //new TypeLiteral<ResourceList<T>>(new ParameterizedTypeImpl(ResourceList.class, resourceType));
		this.eventsClass =  TypeLiteral.create(new ParameterizedTypeImpl(Event.class, resourceType)); //new TypeLiteral<Event<T>>(new ParameterizedTypeImpl(Event.class, resourceType));
	}
	
	public Future<ResourceList<T>> list() {
		return sendRequest(new KubernetesApiRequest(HttpMethod.GET, 
				"/api/v1/"+resourceName,null),resourceListClass);
	}
	
	public Observable<T> watch() {
		return watch(null);
	}
	
	public Observable<T> watch(String version) {
		return watch(version, newVersion->new KubernetesWatchApiRequest(
				"/api/v1/"+resourceName,
				newVersion));
	}
	
	protected <O> Future<O> sendRequest(KubernetesApiRequest apiRequest,TypeLiteral<O> tpyeLit) {
		return client.sendRequest(apiRequest,tpyeLit);
	}
	
	protected Observable<T> watch(String version,Function<String,KubernetesWatchApiRequest> reqSupplier) {
		return client.observe(version,reqSupplier,eventsClass);
	}

}
