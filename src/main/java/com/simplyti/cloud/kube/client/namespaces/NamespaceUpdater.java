package com.simplyti.cloud.kube.client.namespaces;

import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.JsonPatch;
import com.simplyti.cloud.kube.client.ResourceUpdater;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public class NamespaceUpdater extends ResourceUpdater<Namespace,NamespaceUpdater>{

	private final InternalClient client;
	private final String name;
	private final TypeReference<Namespace> resourceClass;

	public NamespaceUpdater(InternalClient client, String name, Future<Namespace> currentFuture,
			TypeReference<Namespace> resourceClass) {
		super(client.nextExecutor(), currentFuture);
		this.client=client;
		this.name=name;
		this.resourceClass=resourceClass;
	}

	@Override
	protected void update(Promise<Namespace> promise,Collection<JsonPatch> patches) {
		client.sendRequest(promise,new KubernetesApiRequest(HttpMethod.PATCH,
				"/api/v1/namespaces/"+name,patches,resourceClass));
	}

}
