package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.Pod;

import io.netty.handler.codec.http.HttpMethod;

public class GetPodRequest extends KubernetesApiRequest {

	public GetPodRequest(String namespace, String name) {
		super(HttpMethod.GET, "/api/v1/namespaces/"+namespace+"/pods/"+name,null,Pod.class);
	}

}
