package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.NamespaceList;

import io.netty.handler.codec.http.HttpMethod;

public class GetNamespacesRequest extends KubernetesApiRequest {

	public GetNamespacesRequest() {
		super(HttpMethod.GET, "/api/v1/namespaces",null,NamespaceList.class);
	}


}
