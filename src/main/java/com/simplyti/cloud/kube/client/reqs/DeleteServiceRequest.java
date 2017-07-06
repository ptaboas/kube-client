package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.Status;

import io.netty.handler.codec.http.HttpMethod;

public class DeleteServiceRequest extends KubernetesApiRequest {

	public DeleteServiceRequest(String namespace, String name) {
		super(HttpMethod.DELETE, "/api/v1/namespaces/"+namespace+"/services/"+name, null ,Status.class);
	}

}
