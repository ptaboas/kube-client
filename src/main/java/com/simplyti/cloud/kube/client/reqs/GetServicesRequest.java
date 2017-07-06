package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.ServiceList;

import io.netty.handler.codec.http.HttpMethod;

public class GetServicesRequest extends KubernetesApiRequest {

	public GetServicesRequest() {
		super(HttpMethod.GET, "/api/v1/services",null,ServiceList.class);
	}
	
	public GetServicesRequest(String namespace) {
		super(HttpMethod.GET, "/api/v1/namespaces/"+namespace+"/services",null,ServiceList.class);
	}

}
