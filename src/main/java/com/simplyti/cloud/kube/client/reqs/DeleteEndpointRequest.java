package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.Status;

import io.netty.handler.codec.http.HttpMethod;

public class DeleteEndpointRequest extends KubernetesApiRequest {

	public DeleteEndpointRequest(String namespace, String name) {
		super(HttpMethod.DELETE, "/api/v1/namespaces/"+namespace+"/endpoints/"+name, null ,Status.class);
	}

}
