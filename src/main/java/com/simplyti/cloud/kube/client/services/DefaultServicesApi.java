package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Service;

public class DefaultServicesApi extends AbstractAllKubeApi<Service,DefaultServiceCreationBuilder,ServiceUpdater, NamespacedServicesApi> implements ServicesApi {

	private static final String API = "v1";
	
	public DefaultServicesApi(InternalClient client) {
		super(client,API,"services");
	}

	@Override
	public DefaultServiceCreationBuilder builder(String namespace) {
		return new DefaultServiceCreationBuilder(client,API,namespace,resourceName);
	}

	@Override
	public NamespacedServicesApi namespace(String namespace) {
		return new DefaultNamespacedServicesApi(namespace,this);
	}

	@Override
	public ServiceUpdater updateBuilder(String namespace, String name) {
		return new ServiceUpdater(client,API,namespace,name,resourceName,resourceClass,this);
	}

}
