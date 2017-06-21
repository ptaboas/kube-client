package com.simplyti.cloud.kube.client.reqs;


import com.google.common.collect.ImmutableMap;
import com.simplyti.cloud.kube.client.domain.Namespace;

import io.netty.handler.codec.http.HttpMethod;

public class CreateNamespaceRequest extends KubernetesApiRequest {

	public CreateNamespaceRequest(String name) {
		super(HttpMethod.POST, "/api/v1/namespaces",namespaceResource(name),Namespace.class);
	}
	
	private static Object namespaceResource(String name) {
		return ImmutableMap.builder()
			.put("kind", "Namespace")
			.put("apiVersion", "v1")
			.put("metadata", ImmutableMap.builder()
					.put("name", name)
					.build())
			.build();
	}

}
