package com.simplyti.cloud.kube.client.reqs;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.simplyti.cloud.kube.client.domain.Endpoint;

import io.netty.handler.codec.http.HttpMethod;

public class CreateEndpointRequest extends KubernetesApiRequest {

	public CreateEndpointRequest(String namespaces,String name, Set<String> addresses, Set<Integer> ports) {
		super(HttpMethod.POST, "/api/v1/namespaces/"+namespaces+"/endpoints",endpointResource(name,addresses,ports),Endpoint.class);
	}
	
	private static Object endpointResource(String name, Set<String> addresses, Set<Integer> ports) {
		return ImmutableMap.builder()
			.put("kind", "Endpoints")
			.put("apiVersion", "v1")
			.put("metadata", Collections.singletonMap("name", name))
			.put("subsets",ImmutableSet.builder()
					.add(ImmutableMap.builder()
							.put("addresses", addresses.stream().map(ip->Collections.singletonMap("ip", ip)).collect(Collectors.toList()))
							.put("ports", ports.stream().map(port->Collections.singletonMap("port", port)).collect(Collectors.toList()))
							.build())
					.build())
			.build();
	}

}
