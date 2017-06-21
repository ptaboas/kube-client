package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.SecretList;

import io.netty.handler.codec.http.HttpMethod;

public class GetSecretsRequest extends KubernetesApiRequest {

	public GetSecretsRequest() {
		super(HttpMethod.GET, "/api/v1/secrets",null,SecretList.class);
	}


}
