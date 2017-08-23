package com.simplyti.cloud.kube.client.reqs;


import com.fasterxml.jackson.core.type.TypeReference;

import io.netty.handler.codec.http.HttpMethod;

public class GetHealthRequest extends KubernetesApiRequest {
	
	private static final TypeReference<String> TYPE = new TypeReference<String>() {};

	public GetHealthRequest() {
		super(HttpMethod.GET, "/healthz",null,TYPE);
	}

}
