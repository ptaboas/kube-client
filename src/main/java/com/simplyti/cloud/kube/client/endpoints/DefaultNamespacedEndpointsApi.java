package com.simplyti.cloud.kube.client.endpoints;

import com.simplyti.cloud.kube.client.AbstractNamespacedKubeApi;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Endpoint;


public class DefaultNamespacedEndpointsApi extends AbstractNamespacedKubeApi<Endpoint, EndpointCreationBuilder,EndpointUpdater> implements NamespacedEndpointsApi {

	public DefaultNamespacedEndpointsApi(String namespace,
			ResourceSupplier<Endpoint, EndpointCreationBuilder, EndpointUpdater> resourceSupplier) {
		super(namespace, resourceSupplier);
	}


}
