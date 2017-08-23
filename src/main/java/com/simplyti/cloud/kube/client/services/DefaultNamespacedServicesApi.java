package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.AbstractNamespacedKubeApi;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Service;

public class DefaultNamespacedServicesApi extends AbstractNamespacedKubeApi<Service, DefaultServiceCreationBuilder,ServiceUpdater > implements NamespacedServicesApi {

	public DefaultNamespacedServicesApi(String namespace,
			ResourceSupplier<Service, DefaultServiceCreationBuilder,ServiceUpdater> resourceSupplier) {
		super(namespace, resourceSupplier);
	}


}
