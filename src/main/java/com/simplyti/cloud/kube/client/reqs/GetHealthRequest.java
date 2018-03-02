package com.simplyti.cloud.kube.client.reqs;

import io.netty.handler.codec.http.HttpMethod;

public class GetHealthRequest extends KubernetesApiRequest {
	
	public GetHealthRequest() {
		super(HttpMethod.GET, "/healthz",null);
	}

}
