package com.simplyti.cloud.kube.client.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.simplyti.cloud.kube.client.AbstractUpdater;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.JsonPatch;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Address;
import com.simplyti.cloud.kube.client.domain.Endpoint;

public class EndpointUpdater extends AbstractUpdater<Endpoint,EndpointUpdater> {

	public EndpointUpdater(InternalClient client, String namespace, String name, String resourceName,
			TypeReference<Endpoint> resourceClass,ResourceSupplier<Endpoint, ?,?> supplier) {
		super(client, namespace, name, resourceName, resourceClass,supplier);
	}

	public EndpointAddressesUpdater addAddress() {
		return new EndpointAddressesUpdater(this);
	}
	
	public EndpointUpdater addAddress(int index, Address address) {
		addPatch(JsonPatch.add("/subsets/"+index+"/addresses/-", address));
		return this;
	}
	
	

}