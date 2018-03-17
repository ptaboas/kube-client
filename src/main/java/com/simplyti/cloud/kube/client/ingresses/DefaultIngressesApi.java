package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Ingress;

public class DefaultIngressesApi extends AbstractAllKubeApi<Ingress,IngressCreationBuilder,IngressUpdater,NamespacedIngressesApi> implements IngressesApi {

	private static final String API = "extensions/v1beta1";

	public DefaultIngressesApi(InternalClient client) {
		super(client,false,API,"ingresses");
	}

	@Override
	public IngressCreationBuilder builder(String namespace) {
		return new IngressCreationBuilder(client,API,namespace,"ingresses");
	}

	@Override
	public IngressUpdater updateBuilder(String namespace, String name) {
		return new IngressUpdater(client,false,API,namespace,name,resourceName,resourceClass,this);
	}

	@Override
	public NamespacedIngressesApi namespace(String namespace) {
		return new DefaultNamespacedIngressesApi(namespace,this);
	}
}
