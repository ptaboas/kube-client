package com.simplyti.cloud.kube.client.reqs;


import com.simplyti.cloud.kube.client.domain.Status;

import io.netty.handler.codec.http.HttpMethod;

public class DeletePodRequest extends KubernetesApiRequest {

	public DeletePodRequest(String namespace,String name) {
		super(HttpMethod.DELETE, "/api/v1/namespaces/"+namespace+"/pods/"+name, null ,Status.class);
	}

}
