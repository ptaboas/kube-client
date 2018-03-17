package com.simplyti.cloud.kube.client.endpoints;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.AbstractUpdater;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.JsonPatch;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Address;
import com.simplyti.cloud.kube.client.domain.Endpoint;

public class EndpointUpdater extends AbstractUpdater<Endpoint,EndpointUpdater> {

	public EndpointUpdater(InternalClient client, String api, String namespace, String name, String resourceName,
			TypeLiteral<Endpoint> resourceClass,ResourceSupplier<Endpoint, ?,?> supplier) {
		super(client, api, namespace, name, resourceName, resourceClass,supplier);
	}

	public EndpointAddressesUpdater addAddress() {
		return new EndpointAddressesUpdater(this);
	}
	
	public EndpointUpdater addAddress(int index, Address address) {
		addPatch(JsonPatch.add("/subsets/"+index+"/addresses/-", address));
		return this;
	}
	
}
