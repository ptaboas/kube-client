package com.simplyti.cloud.kube.client.namespaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.simplyti.cloud.kube.client.AbstractKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.domain.Status;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;

public class DefaultNamespacesApi extends AbstractKubeApi<Namespace> implements NamespacesApi{
	
	private static final TypeReference<Status> STATUS_TYPE = new TypeReference<Status>() {};

	public DefaultNamespacesApi(InternalClient client) {
		super(client,"namespaces");
	}

	@Override
	public Future<Namespace> get(String name) {
		return this.client.sendRequest(
				new KubernetesApiRequest(HttpMethod.GET, 
				"/api/v1/namespaces/"+name,null,
				resourceClass));
	}

	@Override
	public Future<Void> delete(String name) {
		return this.client.sendRequest(
				new KubernetesApiRequest(HttpMethod.DELETE, 
				"/api/v1/namespaces/"+name,null,
				STATUS_TYPE));
	}

	@Override
	public NamespaceCreationBuilder builder() {
		return new NamespaceCreationBuilder(client);
	}


	@Override
	public NamespaceUpdater update(String name) {
		return new NamespaceUpdater(client,name,get(name),resourceClass);
	}

}
