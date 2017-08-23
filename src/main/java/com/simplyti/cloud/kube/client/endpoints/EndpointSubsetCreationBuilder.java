package com.simplyti.cloud.kube.client.endpoints;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.domain.Address;
import com.simplyti.cloud.kube.client.domain.Port;
import com.simplyti.cloud.kube.client.domain.Subset;

public class EndpointSubsetCreationBuilder {

	private final EndpointCreationBuilder builder;
	
	private final List<Address> addresses = new ArrayList<>();
	private final List<Port> ports = new ArrayList<>();

	public EndpointSubsetCreationBuilder(EndpointCreationBuilder builder) {
		this.builder=builder;
	}

	public EndpointSubsetCreationBuilder withAddress(String ip) {
		addresses.add(new Address(ip));
		return this;
	}

	public EndpointSubsetCreationBuilder withPort(int port) {
		ports.add(new Port(null, port, null));
		return this;
	}

	public EndpointCreationBuilder create() {
		return builder.withSubset(Subset.builder().addresses(addresses).ports(ports).build());
	}

}
