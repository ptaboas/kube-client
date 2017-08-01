package com.simplyti.cloud.kube.client.reqs;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.simplyti.cloud.kube.client.domain.Address;
import com.simplyti.cloud.kube.client.domain.Endpoint;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Port;
import com.simplyti.cloud.kube.client.domain.Subset;

import io.netty.handler.codec.http.HttpMethod;

public class CreateEndpointRequest extends KubernetesApiRequest {

	public CreateEndpointRequest(String namespace,String name, Set<String> addresses, Set<Integer> ports) {
		super(HttpMethod.POST, "/api/v1/namespaces/"+namespace+"/endpoints",endpointResource(name,namespace,addresses,ports),Endpoint.class);
	}
	
	private static Endpoint endpointResource(String name,String namespace, Set<String> addresses, Set<Integer> ports) {
		return new Endpoint(Endpoint.KIND, Endpoint.API, Metadata.builder()
				.name(name)
				.namespace(namespace)
				.build(),
				Collections.singletonList(
						Subset.builder()
						.addresses(addresses.stream().map(ip->new Address(ip)).collect(Collectors.toList()))
						.ports(ports.stream().map(port->new Port(null,port,null)).collect(Collectors.toList()))
						.build()));
	}

}
