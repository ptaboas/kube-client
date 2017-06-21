package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.ServiceAccount;

import io.netty.handler.codec.http.HttpMethod;

public class GetServiceAccount extends KubernetesApiRequest {

	public GetServiceAccount(String namespace, String name) {
		super(HttpMethod.GET, "/api/v1/namespaces/"+namespace+"/serviceaccounts/"+name,null,ServiceAccount.class);
	}


}
