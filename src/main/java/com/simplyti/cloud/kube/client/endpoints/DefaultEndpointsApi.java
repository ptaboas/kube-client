package com.simplyti.cloud.kube.client.endpoints;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Endpoint;

public class DefaultEndpointsApi extends AbstractAllKubeApi<Endpoint,EndpointCreationBuilder,EndpointUpdater,NamespacedEndpointsApi> implements EndpointsApi {

	private static final String API = "v1";

	public DefaultEndpointsApi(InternalClient client) {
		super(client,API,"endpoints");
	}

	@Override
	public EndpointCreationBuilder builder(String namespace) {
		return new EndpointCreationBuilder(client,API,namespace,"endpoints");
	}
	
	public NamespacedEndpointsApi namespace(String namespace) {
		return new DefaultNamespacedEndpointsApi(namespace,this);
	}

	@Override
	public EndpointUpdater updateBuilder(String namespace, String name) {
		return new EndpointUpdater(client,API,namespace,name,resourceName,resourceClass,this);
	}

}
