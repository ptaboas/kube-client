package com.simplyti.cloud.kube.client.reqs;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.simplyti.cloud.kube.client.domain.Service;

import io.netty.handler.codec.http.HttpMethod;

public class CreateServiceRequest extends KubernetesApiRequest {

	public CreateServiceRequest(String namespace, String name, ImmutableSet<Integer> ports, Map<String, String> selector) {
		super(HttpMethod.POST, "/api/v1/namespaces/"+namespace+"/services",serviceResource(namespace,name,ports,selector),Service.class);
	}

	private static Object serviceResource(String namespace, String name, ImmutableSet<Integer> ports,
			Map<String, String> selector) {
		return ImmutableMap.builder()
			.put("kind", "Service")
			.put("apiVersion", "v1")
			.put("metadata", ImmutableMap.builder()
					.put("name", name)
					.build())
			.put("spec", ImmutableMap.builder()
					.put("ports", ports.stream().map(port->Collections.singletonMap("port", port)).collect(Collectors.toSet()))
					.put("selector", selector)
					.build())
			.build();
	}

}
