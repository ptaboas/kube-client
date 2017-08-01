package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.Service;

import io.netty.handler.codec.http.HttpMethod;

public class GetServiceRequest extends KubernetesApiRequest {

	public GetServiceRequest(String namespace,String name) {
		super(HttpMethod.GET, "/api/v1/namespaces/"+namespace+"/services/"+name,null,Service.class);
	}

}
