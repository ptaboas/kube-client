package com.simplyti.cloud.kube.client;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;

public abstract class AbstractCreationBuilder<T,B extends CreationBuilder<T>> implements CreationBuilder<T>{
	
	private final InternalClient client;
	private final TypeLiteral<T> resourceClass;
	private final String namespace;
	private final String resoueceName;
	
	private String name;
	private Map<String,String> labels;

	@SuppressWarnings("unchecked")
	public AbstractCreationBuilder(InternalClient client, String namespace, String resoueceName){
		this.namespace=namespace;
		this.resoueceName=resoueceName;
		this.client=client;
		Type resourceType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.resourceClass = TypeLiteral.create(resourceType);
	}

	@Override
	@SuppressWarnings("unchecked")
	public B withName(String name) {
		this.name=name;
		return (B) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public B addLabel(String name,String value) {
		if(labels==null){
			labels=Maps.newHashMap();
		}
		labels.put(name, value);
		return (B) this;
	}

	@Override
	public Future<T> create() {
		T resource = create(Metadata.builder().namespace(namespace).name(name).labels(labels).build());
		return client.sendRequest(new KubernetesApiRequest(HttpMethod.POST, 
				"/api/v1/namespaces/"+namespace+"/"+resoueceName,resource),resourceClass);
	}

	protected abstract T create(Metadata metadata);

}
