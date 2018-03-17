package com.simplyti.cloud.kube.client.ingresses;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.AbstractUpdater;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Ingress;

public class IngressUpdater extends AbstractUpdater<Ingress,IngressUpdater>{

	public IngressUpdater(InternalClient client, boolean core, String api, String namespace, String name, String resourceName,
			TypeLiteral<Ingress> resourceClass, ResourceSupplier<Ingress, ?, ?> supplier) {
		super(client, core,api,namespace, name, resourceName, resourceClass, supplier);
	}

}
