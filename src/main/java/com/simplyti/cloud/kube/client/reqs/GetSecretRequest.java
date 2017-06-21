package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.Secret;

import io.netty.handler.codec.http.HttpMethod;

public class GetSecretRequest extends KubernetesApiRequest {

	public GetSecretRequest(String namespace, String name) {
		super(HttpMethod.GET, "/api/v1/namespaces/"+namespace+"/secrets/"+name,null,Secret.class);
	}
}
