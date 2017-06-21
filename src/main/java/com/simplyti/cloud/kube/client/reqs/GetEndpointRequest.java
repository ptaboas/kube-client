package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.EndpointList;

import io.netty.handler.codec.http.HttpMethod;

public class GetEndpointRequest extends KubernetesApiRequest {

	public GetEndpointRequest() {
		super(HttpMethod.GET, "/api/v1/endpoints",null,EndpointList.class);
	}

}
