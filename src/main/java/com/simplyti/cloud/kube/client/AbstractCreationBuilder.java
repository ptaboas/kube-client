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
	
	private final boolean core;
	private final InternalClient client;
	private final TypeLiteral<T> resourceClass;
	private final String api;
	private final String namespace;
	private final String resoueceName;
	
	private String name;
	private Map<String,String> labels;
	private Map<String,String> annotations;
	
	public AbstractCreationBuilder(InternalClient client, String api, String namespace ,String resoueceName){
		this(client,true,api,namespace,resoueceName);
	}

	@SuppressWarnings("unchecked")
	public AbstractCreationBuilder(InternalClient client, boolean core, String api, String namespace ,String resoueceName){
		this.core=core;
		this.api=api;
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
	@SuppressWarnings("unchecked")
	public B addAnnotation(String name,String value) {
		if(annotations==null){
			annotations=Maps.newHashMap();
		}
		annotations.put(name, value);
		return (B) this;
	}

	@Override
	public Future<T> create() {
		T resource = create(Metadata.builder().namespace(namespace).name(name)
				.labels(labels)
				.annotations(annotations).build());
		return client.sendRequest(new KubernetesApiRequest(HttpMethod.POST, 
				(core?"/api/":"/apis/")+api+"/namespaces/"+namespace+"/"+resoueceName,resource),resourceClass);
	}
	
	public String api() {
		return api;
	}

	protected abstract T create(Metadata metadata);

}
