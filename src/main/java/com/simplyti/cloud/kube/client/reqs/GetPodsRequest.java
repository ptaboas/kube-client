package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.PodList;

import io.netty.handler.codec.http.HttpMethod;

public class GetPodsRequest extends KubernetesApiRequest {

	public GetPodsRequest(String namespace) {
		super(HttpMethod.GET, "/api/v1/namespaces/"+namespace+"/pods",null,PodList.class);
	}
	
	public GetPodsRequest() {
		super(HttpMethod.GET, "/api/v1/pods",null,PodList.class);
	}

}
