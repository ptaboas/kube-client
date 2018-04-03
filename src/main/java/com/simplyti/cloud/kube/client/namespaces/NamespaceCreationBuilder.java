package com.simplyti.cloud.kube.client.namespaces;

import java.util.Map;

import com.google.common.collect.Maps;
import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.CreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;

public class NamespaceCreationBuilder implements CreationBuilder<Namespace> {
	
	private static final TypeLiteral<Namespace> TYPE = new TypeLiteral<Namespace>() {};
	
	public static final String KIND = "Namespace";
	public static final String API = "v1";

	private final InternalClient client;
	
	private String name;
	private Map<String,String> labels;
	private Map<String,String> annotations;
	
	public NamespaceCreationBuilder(InternalClient client){
		this.client=client;
	}

	@Override
	public CreationBuilder<Namespace> withName(String name) {
		this.name=name;
		return this;
	}

	@Override
	public Future<Namespace> create() {
		Namespace namespace = new Namespace(KIND, API, Metadata.builder().name(name)
				.labels(labels)
				.annotations(annotations).build());
		return client.sendRequest(new KubernetesApiRequest(HttpMethod.POST, "/api/v1/namespaces",namespace),TYPE);
	}

	@Override
	public CreationBuilder<Namespace> addLabel(String name,String value) {
		if(labels==null){
			labels=Maps.newHashMap();
		}
		labels.put(name, value);
		return this;
	}

	@Override
	public CreationBuilder<Namespace> addAnnotation(String name, String value) {
		if(annotations==null){
			annotations=Maps.newHashMap();
		}
		annotations.put(name, value);
		return this;
	}

}
