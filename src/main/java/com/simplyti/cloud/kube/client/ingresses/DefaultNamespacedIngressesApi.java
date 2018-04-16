package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.AbstractNamespacedKubeApi;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Ingress;

public class DefaultNamespacedIngressesApi extends AbstractNamespacedKubeApi<Ingress, DefaultIngressCreationBuilder,IngressUpdater> implements NamespacedIngressesApi {

	public DefaultNamespacedIngressesApi(String namespace,
			ResourceSupplier<Ingress, DefaultIngressCreationBuilder, IngressUpdater> resourceSupplier) {
		super(namespace, resourceSupplier);
	}

}
