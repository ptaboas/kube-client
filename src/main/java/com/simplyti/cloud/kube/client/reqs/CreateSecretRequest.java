package com.simplyti.cloud.kube.client.reqs;

import java.util.Map;

import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Secret;

import io.netty.handler.codec.http.HttpMethod;

public class CreateSecretRequest extends KubernetesApiRequest {

	public CreateSecretRequest(String namespace,String name, Map<String,String> data) {
		super(HttpMethod.POST, "/api/v1/namespaces/"+namespace+"/secrets",secretResource(name,namespace,data),Secret.class);
	}
	
	private static Secret secretResource(String name,String namespace, Map<String,String> data) {
		return new Secret(Secret.KIND, Secret.API, Metadata.builder()
				.name(name)
				.namespace(namespace)
				.build(),
				data);
	}

}
