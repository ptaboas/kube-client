package com.simplyti.cloud.kube.client.namespaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.simplyti.cloud.kube.client.CreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;

public class NamespaceCreationBuilder implements CreationBuilder<Namespace> {
	
	private static final TypeReference<Namespace> TYPE = new TypeReference<Namespace>() {};
	
	public static final String KIND = "Namespace";
	public static final String API = "v1";

	private final InternalClient client;
	
	private String name;
	
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
		Namespace namespace = new Namespace(KIND, API, Metadata.builder().name(name).build());
		return client.sendRequest(new KubernetesApiRequest(HttpMethod.POST, "/api/v1/namespaces",namespace,TYPE));
	}

}
