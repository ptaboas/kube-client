package com.simplyti.cloud.kube.client.reqs;


import com.simplyti.cloud.kube.client.domain.Status;

import io.netty.handler.codec.http.HttpMethod;

public class DeleteNamespaceRequest extends KubernetesApiRequest {

	public DeleteNamespaceRequest(String name) {
		super(HttpMethod.DELETE, "/api/v1/namespaces/"+name, null ,Status.class);
	}

}
