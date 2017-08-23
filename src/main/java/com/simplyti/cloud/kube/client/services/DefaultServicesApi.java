package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Service;

public class DefaultServicesApi extends AbstractAllKubeApi<Service,DefaultServiceCreationBuilder,ServiceUpdater, NamespacedServicesApi> implements ServicesApi {

	public DefaultServicesApi(InternalClient client) {
		super(client,"services");
	}

	@Override
	public DefaultServiceCreationBuilder builder(String namespace) {
		return new DefaultServiceCreationBuilder(client,namespace,resourceName);
	}

	@Override
	public NamespacedServicesApi namespace(String namespace) {
		return new DefaultNamespacedServicesApi(namespace,this);
	}

	@Override
	public ServiceUpdater updateBuilder(String namespace, String name) {
		return new ServiceUpdater(client,namespace,name,resourceName,resourceClass,this);
	}

}
